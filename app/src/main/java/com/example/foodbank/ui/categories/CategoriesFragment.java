package com.example.foodbank.ui.categories;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodbank.R;
import com.example.foodbank.db.CategoriesRoomDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class CategoriesFragment extends Fragment implements SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {
    private static final String SERVICE_URL = "https://world.openfoodfacts.org/categories.json";

    // Activity states for switching layouts
    private static final int INITIAL_STATE = 2001;
    private static final int ERROR_STATE = 2002;
    String tagsResponse = "";
    // Layout
    AppCompatSpinner spinner_categoriesOptions;

    // Simple list view
    private List<Category> categoriesList = new Vector<>();
    private List<Category> tmpCategoriesList = new Vector<>();
    private ProgressDialog progressDialog;
    private ListView listView_Categories;
    private ArrayAdapter<Category> arrayAdapter;

    // Control api calls
    private boolean isCalled = false;

    public boolean isCalled() { return isCalled; }

    public void setCalled(boolean called) { isCalled = called; }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.c1_fragment_categories, container, false);

        setCategoriesAdapter(root);

        // Try again when no connection or load from spinner
        AppCompatSpinner spinner_categoriesOptions = root.findViewById(R.id.spinner_categoriesOptions);
        setSpinner(root);
        tryAgain(root);

        // Search
        searchCategory(root);

        // Get API response
        //getResponse();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /*--------------------------------LAYOUT------------------------------------*/
    public void setCategoriesAdapter(View root) {
        // Simple list view implementation and on click listener actions
        this.listView_Categories = root.findViewById(R.id.listView_Categories);
        this.arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, categoriesList);
        listView_Categories.setAdapter(arrayAdapter);
        listView_Categories.setOnItemClickListener((parent, view, position, id) -> {
            Category selected = arrayAdapter.getItem(position);
            String categoryId = selected.getId();
            String categoryName = selected.getName();

            Intent intent = new Intent(getActivity(), ProductsInCategoryActivity.class);
            intent.putExtra("selected_item_id", categoryId);
            intent.putExtra("selected_item_name", categoryName);
            startActivity(intent);
        });
    }

    private void switchLayout(int state) {
        // Layout elements
        FrameLayout frameLayout_categories = requireView().findViewById(R.id.frameLayout_productsInCategory);
        listView_Categories = requireView().findViewById(R.id.listView_Categories);

        switch (state) {
            case INITIAL_STATE:
                frameLayout_categories.setVisibility(View.INVISIBLE);
                listView_Categories.setVisibility(View.VISIBLE);
                break;
            case ERROR_STATE:
                frameLayout_categories.setVisibility(View.VISIBLE);
                listView_Categories.setVisibility(View.INVISIBLE);
                break;
        }
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
            setCalled(true);
        } catch (JSONException e) {
            // handle exception
            e.printStackTrace();
            progressDialog.dismiss();
            switchLayout(ERROR_STATE);
        }
        Category[] categoryArray = new Gson().fromJson(tagsResponse, Category[].class);
        this.categoriesList.clear();
        this.categoriesList.addAll(Arrays.asList(categoryArray));
        this.tmpCategoriesList.addAll(Arrays.asList(categoryArray));
        arrayAdapter.notifyDataSetChanged();
        Snackbar.make(getView(), categoryArray.length + " items loaded", Snackbar.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    private void handleError(VolleyError volleyError) {
        Snackbar.make(listView_Categories, "Something went wrong. Please check your connection.", BaseTransientBottomBar.LENGTH_LONG).show();
        progressDialog.dismiss();
        switchLayout(ERROR_STATE);
    }

    /*-------------------------------DATABASE-----------------------------------*/
    List<Category> getAllCategoriesSortedByProducts() {
        return CategoriesRoomDatabase.getDatabase(getContext()).categoriesDao().getCategoriesSortedByProducts();
    }

    /*--------------------------------SEARCH------------------------------------*/
    public void searchCategory(View root) {
        SearchView searchView_categories = root.findViewById(R.id.searchView_categories);
        searchView_categories.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView_categories.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        arrayAdapter.getFilter().filter(newText);

        getView();
        spinner_categoriesOptions.setVisibility(View.INVISIBLE);

        return false;
    }

    /*--------------------------------------------------------------------------*/
    public void tryAgain(View root) {
        // Try to get response again
        Button button_categories_tryAgain = root.findViewById(R.id.button_tryAgain);
        button_categories_tryAgain.setOnClickListener(v -> {
            getResponse();
        });
    }

    /*-------------------------------SPINNER-----------------------------------*/
    public void setSpinner(View view) {
        spinner_categoriesOptions = view.findViewById(R.id.spinner_categoriesOptions);
        String[] spinnerOptions = {"Most popular", "Show all"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerOptions);
        spinner_categoriesOptions.setAdapter(spinnerAdapter);
        spinner_categoriesOptions.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedOption = parent.getItemAtPosition(position).toString();

        if (selectedOption.equals("Show all")) {
            if (!isCalled()) {
                getResponse();
            } else {
                categoriesList.clear();
                this.categoriesList.addAll(tmpCategoriesList);
                arrayAdapter.notifyDataSetChanged();
            }

        } else if (selectedOption.equals("Most popular")) {
            if (!isCalled()) {
                categoriesList.clear();
                categoriesList.addAll(getAllCategoriesSortedByProducts());
            } else {
                categoriesList.clear();
                for (int i = 0; i < 15; i++) {

                    categoriesList.add(tmpCategoriesList.get(i));
                }
            }
            arrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Initialize Categories from DB
        categoriesList.clear();
        categoriesList.addAll(getAllCategoriesSortedByProducts());
    }
}