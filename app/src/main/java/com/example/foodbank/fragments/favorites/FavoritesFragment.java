package com.example.foodbank.fragments.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.R;
import com.example.foodbank.db.ProductsDao;
import com.example.foodbank.db.ProductsRoomDatabase;
import com.example.foodbank.main_activities.EditProductActivity;
import com.example.foodbank.classes.Product;
import com.example.foodbank.adapters.MyProductsAdapter;
import com.example.foodbank.main_activities.ViewProductActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;

public class FavoritesFragment extends Fragment implements MyProductsAdapter.OnItemClickListener, MyProductsAdapter.OnItemLongClickListener,
        MyProductsAdapter.OnActionBarMenuClickListener, MyProductsAdapter.OnStarClickListener,
        AdapterView.OnItemSelectedListener {

    // Recycler View
    private final Vector<Product> productsList = new Vector<>();
    private MyProductsAdapter adapter;
    RecyclerView recyclerView;

    // Layout
    AppCompatSpinner spinner_productsOptions;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_products, container, false);

        // Initialize each products from the db to the productsList
        productsList.clear();
        productsList.addAll(getFavoriteProductsSortedByTimestamp());

        setRecyclerView(root);

        setSpinner(root);

        return root;
    }

    @Override
    public void onResume() {
        // Initialize each products from the db to the productsList
        productsList.clear();
        productsList.addAll(getFavoriteProductsSortedByTimestamp());
        adapter.notifyDataSetChanged();

        super.onResume();
    }

    /*--------------------------------LAYOUT------------------------------------*/
    public void setRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView_products);
        // Item helper for swipe events
        new ItemTouchHelper((itemTouchHelperCallback)).attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyProductsAdapter(productsList, this, this, this, this);
        recyclerView.setAdapter(adapter);
    }

    /*-------------------------------DATABASE-----------------------------------*/
    List<Product> getProductsFavorites() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getFavoriteProducts();
    }

    List<Product> getFavoriteProductsSortedByTimestamp() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getFavoriteProductsSortedByTimestamp();
    }

    List<Product> getFavoriteProductsSortedByTitle() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getFavoriteProductsSortedByTitle();
    }

    List<Product> getFavoriteProductsSortedByNutriscore() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getFavoriteProductsSortedByNutriscore();
    }

    List<Product> getFavoriteProductsSortedByEcoscore() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getFavoriteProductsSortedByEcoscore();
    }

    List<Product> getFavoriteProductsSortedByNovaGroup() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getFavoriteProductsSortedByNovaGroup();
    }

    void insert(Product product) {
        ProductsRoomDatabase.getDatabase(getContext()).productsDao().insert(product);
    }

    void update(Product product) {
        ProductsRoomDatabase.getDatabase(getContext()).productsDao().update(product);
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

    /*-------------------------------SPINNER-----------------------------------*/
    public void setSpinner(View view) {
        spinner_productsOptions = view.findViewById(R.id.spinner_productsOptions);
        String[] spinnerOptions = {"Date Added", "Title", "Nutriscore", "Ecoscore", "Novagroup"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerOptions);
        spinner_productsOptions.setAdapter(spinnerAdapter);
        spinner_productsOptions.setOnItemSelectedListener(this);
    }

    // Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedOption = parent.getItemAtPosition(position).toString();
        // Set recycler view adapter for each selection
        switch (selectedOption) {
            case "Date Added":
                productsList.clear();
                productsList.addAll(getFavoriteProductsSortedByTimestamp());
                adapter.notifyDataSetChanged();
                break;
            case "Title":
                productsList.clear();
                productsList.addAll(getFavoriteProductsSortedByTitle());
                adapter.notifyDataSetChanged();
                break;
            case "Nutriscore":
                productsList.clear();
                productsList.addAll(getFavoriteProductsSortedByNutriscore());
                adapter.notifyDataSetChanged();
                break;
            case "Ecoscore":
                productsList.clear();
                productsList.addAll(getFavoriteProductsSortedByEcoscore());
                adapter.notifyDataSetChanged();
                break;
            case "Novagroup":
                productsList.clear();
                productsList.addAll(getFavoriteProductsSortedByNovaGroup());
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    /*-----------------------------INTERFACES----------------------------------*/
    // Delete Item on Swipe
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAdapterPosition();

            productsList.get(pos).setStarred(false);
            Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
            update(productsList.get(pos));
            productsList.clear();
            productsList.addAll(getProductsFavorites());
            adapter.notifyDataSetChanged();
        }
    };

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
        inflater.inflate(R.menu.product_card_menu, popup.getMenu());
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

    // Handle checkbox(star) clicks
    @Override
    public void itemClicked(View v, int pos, boolean checked) {
        boolean checkStar = productsList.get(pos).isStarred();

        if (pos >= 0) {
            checkStar = !checkStar;
            Toast.makeText(requireContext(), "Removed from favorites!", Toast.LENGTH_SHORT).show();

            productsList.get(pos).setStarred(checkStar);
            update(productsList.get(pos));
            productsList.clear();
            productsList.addAll(getProductsFavorites());
            adapter.notifyDataSetChanged();
        }
    }
}