package com.example.foodbank.ui.products;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.Product;
import com.example.foodbank.R;
import com.example.foodbank.db.ProductsDao;
import com.example.foodbank.db.ProductsRoomDatabase;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;

public class ProductsFragment extends Fragment implements ProductsAdapter.OnItemClickListener, ProductsAdapter.OnItemLongClickListener,
        ProductsAdapter.OnActionBarMenuClickListener {

    // Recycler View
    private Vector<Product> productsList = new Vector<>();
    private ProductsAdapter adapter;
    // Delete Item on Swipe
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // Create a temp note if user wants to undo

            int pos = viewHolder.getAdapterPosition();

            // Create a temp note if user wants to undo
            Product tmpProduct = productsList.get(pos);

            delete(productsList.get(pos));
            // Clear the list and update it
            Executors.newSingleThreadExecutor().execute(() -> {
                final ProductsDao myDAO = ProductsRoomDatabase.getDatabase(requireContext()).productsDao();
                productsList.clear();
                productsList.addAll(myDAO.getProductsSortedByTimestamp());
                requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
            });
            Snackbar snackbar = Snackbar.make(getView(), "You have deleted '" + tmpProduct.getTitle() + "'", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Undo
                            insert(tmpProduct);
                            Executors.newSingleThreadExecutor().execute(() -> {
                                final ProductsDao myDAO = ProductsRoomDatabase.getDatabase(requireContext()).productsDao();
                                productsList.clear();
                                productsList.addAll(myDAO.getProductsSortedByTimestamp());
                                requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                            });
                        }
                    });
            snackbar.show();
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.d1_fragment_products, container, false);

        // Initialize each products from the db to the productsList
        productsList.addAll(getAllProductsSortedByTimestamp());

        // Recycler View implementation
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView_products);
        // Item helper for swipe events
        new ItemTouchHelper((itemTouchHelperCallback)).attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ProductsAdapter(productsList, this::itemClicked, this::itemLongClicked, this::onPopupMenuClick);
        recyclerView.setAdapter(adapter);

        return root;
    }

    List<Product> getAllProductsSortedByTitle() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getProductsSortedByTitle();
    }

    List<Product> getAllProductsSortedByTimestamp() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getProductsSortedByTimestamp();
    }

    @Override
    public void itemClicked(View v, int pos, String value) {
        Intent intent = new Intent(getActivity(), ViewProductActivity.class);
        intent.putExtra("product_barcode_products", value);
        startActivity(intent);
    }

    @Override
    public boolean itemLongClicked(View v, int pos, String value) {
        return false;
    }

    @Override
    public void onPopupMenuClick(View view, int pos, String value) {
        Product listItem = productsList.get(pos);
        PopupMenu popup = new PopupMenu(requireContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_product_card, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            //do your things in each of the following cases
            if (item.getItemId() == R.id.menu_addToList) {
                Toast.makeText(requireContext(), "Add to list clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (item.getItemId() == R.id.menu_viewProduct) {
                Intent intent = new Intent(getActivity(), ViewProductActivity.class);
                intent.putExtra("product_barcode_products", value);
                startActivity(intent);
                return true;
            }
            return false;
        });
        popup.show();
    }

    // Insert product on products db
    void insert(Product product) {
        ProductsRoomDatabase.getDatabase(getContext()).productsDao().insert(product);
        Toast.makeText(getContext(), "New item added to your products", Toast.LENGTH_LONG).show();
    }


    void update(Product note) {
        ProductsRoomDatabase.getDatabase(requireContext()).productsDao().update(note);
    }

    void delete(final Product note) {
        ProductsRoomDatabase.getDatabase(requireContext()).productsDao().delete(note);
    }
}