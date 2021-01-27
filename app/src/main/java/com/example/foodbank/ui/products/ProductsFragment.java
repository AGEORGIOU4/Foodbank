package com.example.foodbank.ui.products;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.R;
import com.example.foodbank.db.ProductsDao;
import com.example.foodbank.db.ProductsRoomDatabase;
import com.example.foodbank.ui.favorites.Favorites;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;

public class ProductsFragment extends Fragment implements ProductsAdapter.OnItemClickListener, ProductsAdapter.OnItemLongClickListener,
        ProductsAdapter.OnActionBarMenuClickListener {

    // Recycler View
    private final Vector<Product> productsList = new Vector<>();
    private ProductsAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.d1_fragment_products, container, false);
        // Initialize each products from the db to the productsList
        productsList.clear();
        productsList.addAll(getAllProductsSortedByTimestamp());

        setRecyclerView(root);

        // Search
        searchItem(root);
        return root;
    }

    @Override
    public void onResume() {
        // Initialize each products from the db to the productsList
        productsList.clear();
        productsList.addAll(getAllProductsSortedByTimestamp());
        adapter.notifyDataSetChanged();

        super.onResume();
    }

    /*--------------------------------LAYOUT------------------------------------*/
    public void setRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_products);
        // Item helper for swipe events
        new ItemTouchHelper((itemTouchHelperCallback)).attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ProductsAdapter(productsList, this, this, this);
        recyclerView.setAdapter(adapter);
    }

    /*-------------------------------DATABASE-----------------------------------*/
    List<Product> getAllProductsSortedByTitle() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getProductsSortedByTitle();
    }
    List<Product> getAllProductsSortedByTimestamp() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getProductsSortedByTimestamp();
    }
    void insert(Product product) {
        ProductsRoomDatabase.getDatabase(getContext()).productsDao().insert(product);
    }
    void delete(final Product product) {
        ProductsRoomDatabase.getDatabase(requireContext()).productsDao().delete(product);
    }
    void deleteItem(int pos) {
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
                .setAction("UNDO", v -> {
                    // Undo
                    insert(tmpProduct);
                    Executors.newSingleThreadExecutor().execute(() -> {
                        final ProductsDao myDAO = ProductsRoomDatabase.getDatabase(requireContext()).productsDao();
                        productsList.clear();
                        productsList.addAll(myDAO.getProductsSortedByTimestamp());
                        requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                    });
                });
        snackbar.show();
    }

    /*--------------------------------SEARCH------------------------------------*/
    public void searchItem(View view) {
    SearchView searchView = view.findViewById(R.id.searchView_products);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            adapter.getFilter().filter(newText);
            return false;
        }
    });
}

    /*--------------------------------------------------------------------------*/
    @Override
    public void itemClicked(View v, int pos, String value) {
        Intent intent = new Intent(getActivity(), ViewProductActivity.class);
        intent.putExtra("extra_products_code", value);
        startActivity(intent);
    }
    @Override
    public boolean itemLongClicked(View v, int pos, String value) {
        Snackbar.make(v, "Swipe to delete", Snackbar.LENGTH_SHORT).show();
        return true;
    }
    @Override
    public void onPopupMenuClick(View view, int pos, String code, String title, String nutriScore,
                                 String ecoScore, String novaGroup, boolean isStarred) {
        Product listItem = productsList.get(pos);
        PopupMenu popup = new PopupMenu(requireContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_product_card, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            //do your things in each of the following cases
            if (item.getItemId() == R.id.menu_viewProduct) {
                Intent intent = new Intent(getActivity(), ViewProductActivity.class);
                intent.putExtra("extra_products_code", code);
                startActivity(intent);
                return true;
            }
            if (item.getItemId() == R.id.menu_addToList) {
                Toast.makeText(requireContext(), "Add to list clicked item " + pos, Toast.LENGTH_SHORT).show();
                return true;
            }
            if (item.getItemId() == R.id.menu_editProduct) {
                Intent intent = new Intent(getActivity(), EditProductActivity.class);
                intent.putExtra("extra_products_code", code);
                intent.putExtra("clicked_item_title", title);
                intent.putExtra("clicked_item_nutri_score", nutriScore);
                intent.putExtra("clicked_item_eco_score", ecoScore);
                intent.putExtra("clicked_item_nova_group", novaGroup);
                intent.putExtra("clicked_item_starred", isStarred);
                intent.putExtra("clicked_item_position", pos);
                startActivity(intent);
                return true;
            }

            if (item.getItemId() == R.id.menu_deleteProduct) {
                deleteItem(pos);
                return true;
            }
            return false;
        });
        popup.show();
    }

    // Delete Item on Swipe
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return true;
        }
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAdapterPosition();
            deleteItem(pos);
        }
    };
}