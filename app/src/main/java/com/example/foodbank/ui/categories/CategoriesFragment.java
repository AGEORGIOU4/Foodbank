package com.example.foodbank.ui.categories;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodbank.R;
import com.example.foodbank.db.CategoriesRoomDatabase;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class CategoriesFragment extends Fragment implements CategoriesAdapter.OnItemClickListener, AdapterView.OnItemSelectedListener {
    private static final String SERVICE_URL = "https://world.openfoodfacts.org/categories.json";
    // Activity states for switching layouts
    private static final int INITIAL_STATE = 2001;
    private static final int ERROR_STATE = 2002;
    // Recycler view
    private final Vector<Category> categoriesList = new Vector<>();
    private final Vector<Category> categoriesListMostPopular = new Vector<>();
    String tagsResponse = "";
    // Layout
    AppCompatSpinner spinner_categoriesOptions;
    private RecyclerView recyclerView_Categories;
    private CategoriesAdapter adapterAPI;
    private CategoriesAdapter adapterDB;
    private ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.c1_fragment_categories, container, false);
        recyclerView_Categories = root.findViewById(R.id.recyclerView_categories);

        // Fetch all categories using API call
        getResponse();

        // Recycler View
        setRecyclerView(root);

        // Search
        searchItem(root);

        // Try again when no connection or load from spinner
        setSpinner(root);
        tryAgain(root);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /*--------------------------------LAYOUT------------------------------------*/
    private void switchLayout(int state) {
        // Layout elements
        FrameLayout frameLayout_categories = requireView().findViewById(R.id.frameLayout_productsInCategory);
        recyclerView_Categories = requireView().findViewById(R.id.recyclerView_categories);

        switch (state) {
            case INITIAL_STATE:
                frameLayout_categories.setVisibility(View.INVISIBLE);
                recyclerView_Categories.setVisibility(View.VISIBLE);
                break;
            case ERROR_STATE:
                frameLayout_categories.setVisibility(View.VISIBLE);
                recyclerView_Categories.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void setRecyclerView(View view) {
        recyclerView_Categories = view.findViewById(R.id.recyclerView_categories);
        recyclerView_Categories.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_Categories.setLayoutManager(linearLayoutManager);
        adapterAPI = new CategoriesAdapter(categoriesList, this);
        adapterDB = new CategoriesAdapter(categoriesListMostPopular, this);
    }

    /*-------------------------------RESPONSE-----------------------------------*/
    public void getResponse() {
        // Set up progress bar before call
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMax(100);
        progressDialog.setMessage("Loading....");
        progressDialog.setTitle("Fetching data from world.openfoodfacts.org");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Show it
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        final StringRequest request = new StringRequest(
                Request.Method.GET,
                SERVICE_URL,
                this::handleResponse, // method reference, equivalent to: response -> handleResponse(response)
                this::handleError); // method reference, equivalent to: error -> handleError(error)
        requestQueue.add(request);
    }

    private void handleResponse(final String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            tagsResponse = jsonObject.getString("tags");
            progressDialog.dismiss();
            switchLayout(INITIAL_STATE);
        } catch (JSONException e) {
            // handle exception
            e.printStackTrace();
            progressDialog.dismiss();
            switchLayout(ERROR_STATE);
        }

        Category[] categoryArray = new Gson().fromJson(tagsResponse, Category[].class);
        this.categoriesList.clear();
        this.categoriesList.addAll(Arrays.asList(categoryArray));

        this.categoriesListMostPopular.clear();
        for (int i = 0; i < 20; i++) {
            this.categoriesListMostPopular.add(categoryArray[i]);
        }

        // Recycler View
        setRecyclerView(getView());

        adapterAPI.notifyDataSetChanged();
        recyclerView_Categories.setAdapter(adapterAPI);

        adapterDB.notifyDataSetChanged();

        Snackbar.make(getView(), categoryArray.length + " items loaded", Snackbar.LENGTH_SHORT).show();
        progressDialog.dismiss();

    }

    private void handleError(VolleyError volleyError) {
        Snackbar.make(recyclerView_Categories, "Something went wrong. Please check your connection.", BaseTransientBottomBar.LENGTH_LONG).show();
        progressDialog.dismiss();
        switchLayout(ERROR_STATE);
    }

    /*-------------------------------DATABASE-----------------------------------*/
    List<Category> getAllCategoriesSortedByProducts() {
        return CategoriesRoomDatabase.getDatabase(getContext()).categoriesDao().getCategoriesSortedByProducts();
    }

    List<Category> getAllCategoriesSortedByTitle() {
        return CategoriesRoomDatabase.getDatabase(getContext()).categoriesDao().getCategoriesSortedByTitle();
    }

    /*-------------------------------SPINNER-----------------------------------*/
    public void setSpinner(View view) {
        spinner_categoriesOptions = view.findViewById(R.id.spinner_categoriesOptions);
        String[] spinnerOptions = {"Show all", "Most popular"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerOptions);
        spinner_categoriesOptions.setAdapter(spinnerAdapter);
        spinner_categoriesOptions.setOnItemSelectedListener(this);
    }

    /*--------------------------------SEARCH-----------------------------------*/
    public void searchItem(View view) {
        SearchView searchView = view.findViewById(R.id.searchView_categories);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterAPI.getFilter().filter(newText);
                adapterDB.getFilter().filter(newText);
                return false;
            }
        });
    }

    // Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedOption = parent.getItemAtPosition(position).toString();
        // Set recycler view adapter for each selection
        if (selectedOption.equals("Show all")) {
            recyclerView_Categories.setAdapter(adapterAPI);

        } else if (selectedOption.equals("Most popular")) {
            recyclerView_Categories.setAdapter(adapterDB);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /*--------------------------------------------------------------------------*/
    public void tryAgain(View root) {
        // Try to get response again
        Button button_categories_tryAgain = root.findViewById(R.id.button_tryAgain);
        button_categories_tryAgain.setOnClickListener(v -> {
            getResponse();
        });
    }

    @Override
    public void itemClicked(View v, int pos, String id, String categoryName, int productNumber) {
        Intent intent = new Intent(getActivity(), ProductsInCategoryActivity.class);
        intent.putExtra("selected_item_id", id);
        intent.putExtra("selected_item_name", categoryName);
        intent.putExtra("selected_item_total_products", productNumber);
        startActivity(intent);
    }
}