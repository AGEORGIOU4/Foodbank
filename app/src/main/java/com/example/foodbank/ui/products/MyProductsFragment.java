package com.example.foodbank.ui.products;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.R;
import com.example.foodbank.db.ProductsDao;
import com.example.foodbank.db.ProductsRoomDatabase;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;

public class MyProductsFragment extends Fragment implements MyProductsAdapter.OnItemClickListener, MyProductsAdapter.OnItemLongClickListener,
        MyProductsAdapter.OnActionBarMenuClickListener, MyProductsAdapter.OnStarClickListener,
        AdapterView.OnItemSelectedListener {

    // Recycler View
    RecyclerView recyclerView;
    private final Vector<Product> productsListDate = new Vector<>();
    private final Vector<Product> productsListTitle = new Vector<>();
    private final Vector<Product> productsListNutriScore = new Vector<>();
    private final Vector<Product> productsListEcoScore = new Vector<>();
    private final Vector<Product> productsListNovaGroup = new Vector<>();
    private final Vector<Product> productsListFavorites = new Vector<>();

    private MyProductsAdapter adapterDate;
    private MyProductsAdapter adapterTitle;
    private MyProductsAdapter adapterNutriScore;
    private MyProductsAdapter adapterEcoScore;
    private MyProductsAdapter adapterNovaGroup;
    private MyProductsAdapter adapterFavorites;

    // Layout
    AppCompatSpinner spinner_productsOptions;
    ArrayAdapter<String> spinnerAdapter;
    SearchView searchView;

    // Sort controller
    private int sortController = 1;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.d1_fragment_my_products, container, false);

        //Set recycler view and adapters
        recyclerView = root.findViewById(R.id.recyclerView_products);
        setRecyclerView(root);

        // Initialize each product from the db to each list
        setLists();

        // Search
        searchItem(root);

        setSpinner(root);

        return root;
    }

    @Override
    public void onResume() {
        hideKeyboard();
        setLists();
        setSpinnerSelection();

        super.onResume();
    }

    public void setLists() {
        productsListDate.clear();
        productsListDate.addAll(getAllProductsSortedByTimestamp());
        adapterDate.notifyDataSetChanged();

        productsListTitle.clear();
        productsListTitle.addAll(getProductsSortedByTitle());
        adapterTitle.notifyDataSetChanged();

        productsListNutriScore.clear();
        productsListNutriScore.addAll(getProductsSortedByNutriscore());
        adapterNutriScore.notifyDataSetChanged();

        productsListEcoScore.clear();
        productsListEcoScore.addAll(getProductsSortedByEcoscore());
        adapterEcoScore.notifyDataSetChanged();

        productsListNovaGroup.clear();
        productsListNovaGroup.addAll(getProductsSortedByNovaGroup());
        adapterNovaGroup.notifyDataSetChanged();

        productsListFavorites.clear();
        productsListFavorites.addAll(getProductsFavorites());
        adapterFavorites.notifyDataSetChanged();
    }

    /*--------------------------------LAYOUT------------------------------------*/
    public void setRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView_products);

        // Item helper for swipe events
        new ItemTouchHelper((itemTouchHelperCallback)).attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        // Set adapters for each sorting selection
        adapterDate = new MyProductsAdapter(productsListDate, this, this, this, this);
        adapterTitle = new MyProductsAdapter(productsListTitle, this, this, this, this);
        adapterNutriScore = new MyProductsAdapter(productsListNutriScore, this, this, this, this);
        adapterEcoScore = new MyProductsAdapter(productsListEcoScore, this, this, this, this);
        adapterNovaGroup = new MyProductsAdapter(productsListNovaGroup, this, this, this, this);
        adapterFavorites = new MyProductsAdapter(productsListFavorites, this, this, this, this);
    }

    /*-------------------------------DATABASE-----------------------------------*/
    List<Product> getProductsFavorites() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getProductsFavorites();
    }

    List<Product> getAllProductsSortedByTimestamp() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getProductsSortedByTimestamp();
    }

    List<Product> getProductsSortedByTitle() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getProductsSortedByTitle();
    }

    List<Product> getProductsSortedByNutriscore() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getProductsSortedByNutriscore();
    }

    List<Product> getProductsSortedByEcoscore() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getProductsSortedByEcoscore();
    }

    List<Product> getProductsSortedByNovaGroup() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getProductsSortedByNovaGroup();
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
        // Create a temp note if user wants to undo, check the selected list and delete item

        // Step 1 - Get item from selected list, delete it and set a tmpProduct for undo case
        Product tmpProduct;

        switch (sortController) {
            case 1:
                tmpProduct = productsListDate.get(pos);
                delete(productsListDate.get(pos));
                break;
            case 2:
                tmpProduct = productsListTitle.get(pos);
                delete(productsListTitle.get(pos));
                break;
            case 3:
                tmpProduct = productsListNutriScore.get(pos);
                delete(productsListNutriScore.get(pos));
                break;
            case 4:
                tmpProduct = productsListEcoScore.get(pos);
                delete(productsListEcoScore.get(pos));
                break;
            case 5:
                tmpProduct = productsListNovaGroup.get(pos);
                delete(productsListNovaGroup.get(pos));
                break;
            case 6:
                tmpProduct = productsListFavorites.get(pos);
                delete(productsListFavorites.get(pos));
                break;
            default:
                tmpProduct = productsListDate.get(pos);
                break;


        }
        // Clear the list and update it
        // Step 2 - Clear all lists
        Executors.newSingleThreadExecutor().execute(() -> {
            final ProductsDao myDAO = ProductsRoomDatabase.getDatabase(requireContext()).productsDao();

            productsListDate.clear();
            productsListTitle.clear();
            productsListNutriScore.clear();
            productsListDate.clear();
            productsListEcoScore.clear();
            productsListNovaGroup.clear();
            productsListFavorites.clear();

            // Step 3 -  Update each list content
            productsListDate.addAll(myDAO.getProductsSortedByTimestamp());
            productsListTitle.addAll(myDAO.getProductsSortedByTitle());
            productsListNutriScore.addAll(myDAO.getProductsSortedByNutriscore());
            productsListEcoScore.addAll(myDAO.getProductsSortedByEcoscore());
            productsListNovaGroup.addAll(myDAO.getProductsSortedByNovaGroup());
            productsListFavorites.addAll(myDAO.getProductsFavorites());

            // Step 4 -  Notify each adapter
            requireActivity().runOnUiThread(() -> adapterDate.notifyDataSetChanged());
            requireActivity().runOnUiThread(() -> adapterTitle.notifyDataSetChanged());
            requireActivity().runOnUiThread(() -> adapterNutriScore.notifyDataSetChanged());
            requireActivity().runOnUiThread(() -> adapterEcoScore.notifyDataSetChanged());
            requireActivity().runOnUiThread(() -> adapterNovaGroup.notifyDataSetChanged());
            requireActivity().runOnUiThread(() -> adapterFavorites.notifyDataSetChanged());

            assert getFragmentManager() != null;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(MyProductsFragment.this).attach(MyProductsFragment.this).commit();
        });
        Snackbar snackbar = Snackbar.make(getView(), "You have deleted '" + tmpProduct.getTitle() + "'", Snackbar.LENGTH_LONG)
                .setAction("UNDO", v -> {
                    // Step 1 - Replace (insert back the tmp product as the 'deleted')
                    insert(tmpProduct);

                    // Clear the list and update it
                    // Step 2 - Clear all lists
                    Executors.newSingleThreadExecutor().execute(() -> {
                        final ProductsDao myDAO = ProductsRoomDatabase.getDatabase(requireContext()).productsDao();

                        productsListDate.clear();
                        productsListTitle.clear();
                        productsListNutriScore.clear();
                        productsListDate.clear();
                        productsListEcoScore.clear();
                        productsListNovaGroup.clear();
                        productsListFavorites.clear();

                        // Step 3 -  Update each list content
                        productsListDate.addAll(myDAO.getProductsSortedByTimestamp());
                        productsListTitle.addAll(myDAO.getProductsSortedByTitle());
                        productsListNutriScore.addAll(myDAO.getProductsSortedByNutriscore());
                        productsListEcoScore.addAll(myDAO.getProductsSortedByEcoscore());
                        productsListNovaGroup.addAll(myDAO.getProductsSortedByNovaGroup());
                        productsListFavorites.addAll(myDAO.getProductsFavorites());

                        // Step 4 -  Notify each adapter
                        requireActivity().runOnUiThread(() -> adapterDate.notifyDataSetChanged());
                        requireActivity().runOnUiThread(() -> adapterTitle.notifyDataSetChanged());
                        requireActivity().runOnUiThread(() -> adapterNutriScore.notifyDataSetChanged());
                        requireActivity().runOnUiThread(() -> adapterEcoScore.notifyDataSetChanged());
                        requireActivity().runOnUiThread(() -> adapterNovaGroup.notifyDataSetChanged());
                        requireActivity().runOnUiThread(() -> adapterFavorites.notifyDataSetChanged());
                    });
                });
        snackbar.show();
    }

    /*-------------------------------SPINNER-----------------------------------*/
    public void setSpinner(View view) {
        spinner_productsOptions = view.findViewById(R.id.spinner_productsOptions);
        String[] spinnerOptions = {"Date Added", "Title", "Nutriscore", "Ecoscore", "Novagroup", "Favorites"};
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerOptions);
        spinner_productsOptions.setAdapter(spinnerAdapter);
        spinner_productsOptions.setOnItemSelectedListener(this);
    }

    public void setSpinnerSelection() {
        switch (sortController) {
            case 1:
                spinner_productsOptions.setSelection(spinnerAdapter.getPosition("Date Added"));
                break;
            case 2:
                spinner_productsOptions.setSelection(spinnerAdapter.getPosition("Title"));
                break;
            case 3:
                spinner_productsOptions.setSelection(spinnerAdapter.getPosition("Nutriscore"));
                break;
            case 4:
                spinner_productsOptions.setSelection(spinnerAdapter.getPosition("Ecoscore"));
                break;
            case 5:
                spinner_productsOptions.setSelection(spinnerAdapter.getPosition("Novagroup"));
                break;
            case 6:
                spinner_productsOptions.setSelection(spinnerAdapter.getPosition("Favorites"));
                break;
        }
    }

    // Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedOption = parent.getItemAtPosition(position).toString();
        // Set recycler view adapter for each selection (switch adapter so searching will also keep working when shifting lists)

        switch (selectedOption) {
            case "Date Added":
                sortController = 1;
                recyclerView.setAdapter(adapterDate);
                hideKeyboard();
                break;
            case "Title":
                sortController = 2;
                recyclerView.setAdapter(adapterTitle);
                hideKeyboard();
                break;
            case "Nutriscore":
                sortController = 3;
                recyclerView.setAdapter(adapterNutriScore);
                hideKeyboard();
                break;
            case "Ecoscore":
                sortController = 4;
                recyclerView.setAdapter(adapterEcoScore);
                hideKeyboard();
                break;
            case "Novagroup":
                sortController = 5;
                recyclerView.setAdapter(adapterNovaGroup);
                hideKeyboard();
                break;
            case "Favorites":
                sortController = 6;
                recyclerView.setAdapter(adapterFavorites);
                hideKeyboard();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /*--------------------------------SEARCH------------------------------------*/
    public void searchItem(View view) {
        searchView = view.findViewById(R.id.searchView_products);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterDate.getFilter().filter(newText);
                adapterTitle.getFilter().filter(newText);
                adapterNutriScore.getFilter().filter(newText);
                adapterEcoScore.getFilter().filter(newText);
                adapterNovaGroup.getFilter().filter(newText);
                adapterFavorites.getFilter().filter(newText);
                return false;
            }
        });
    }

    /*--------------------------------------------------------------------------*/
    // View Product
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
    public void onPopupMenuClick(View view, int pos, String code, final String title, String nutriScore,
                                 String ecoScore, String novaGroup, boolean isStarred) {
        Product listItem = productsListDate.get(pos);
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
                intent.putExtra("extra_sort_controller", sortController);

                // Check for nulls
                if (title != null) {
                    intent.putExtra("clicked_item_title", title);
                } else {
                    intent.putExtra("clicked_item_title", "Unknown");
                }

                if (nutriScore != null) {
                    intent.putExtra("clicked_item_nutri_score", nutriScore);
                } else {
                    intent.putExtra("clicked_item_nutri_score", "Unknown");
                }

                if (ecoScore != null) {
                    intent.putExtra("clicked_item_eco_score", ecoScore);
                } else {
                    intent.putExtra("clicked_item_eco_score", "Unknown");
                }

                if (novaGroup != null) {
                    intent.putExtra("clicked_item_nova_group", novaGroup);
                } else {
                    intent.putExtra("clicked_item_nova_group", "Unknown");
                }

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
        boolean checkStar = productsListDate.get(pos).isStarred();

        if (pos >= 0) {
            checkStar = !checkStar;
            productsListDate.get(pos).setStarred(checkStar);
            update(productsListDate.get(pos));
            if (checkStar)
                Toast.makeText(requireContext(), "Added to favorites!", Toast.LENGTH_SHORT).show();
        }
        setLists();
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

    public void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
    }
}