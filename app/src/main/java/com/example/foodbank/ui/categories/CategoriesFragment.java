package com.example.foodbank.ui.categories;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class CategoriesFragment extends Fragment implements CategoriesAdapter.OnItemClickListener, CategoriesAdapter.OnItemLongClickListener {
    // Activity states for switching layouts
    private static final int INITIAL_STATE = 2001;
    private static final int ERROR_STATE = 2002;
    private static final String SERVICE_URL = "https://world.openfoodfacts.org/categories.json";
    String tagsResponse = "";

    private final Vector<Category> categoriesList = new Vector<>();

    private ProgressDialog progressDialog;

    // Recycler view
    private RecyclerView recyclerView_categories;
    private CategoriesAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.c1_fragment_categories, container, false);

        // Initialize each products from the db to the productsList
        categoriesList.addAll(getAllCategoriesSortedByProducts());

        // Recycler View implementation
        recyclerView_categories = root.findViewById(R.id.recyclerView_categories);
        recyclerView_categories.setHasFixedSize(true);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView_categories.setLayoutManager(gridLayoutManager);
        adapter = new CategoriesAdapter(categoriesList, this, this);
        recyclerView_categories.setAdapter(adapter);


        SearchView searchView_categories = root.findViewById(R.id.searchView_categories);

        // Try to get response again
        Button button_categories_tryAgain = root.findViewById(R.id.button_categories_tryAgain);
        button_categories_tryAgain.setOnClickListener(v -> getResponse());

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        getResponse();
    }

    /*-------------------------------RESPONSE-----------------------------------*/
    private void handleResponse(final String response) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            tagsResponse = jsonObject.getString("tags");
            Category[] categoryArray = new Gson().fromJson(tagsResponse, Category[].class);
            this.categoriesList.clear();
            this.categoriesList.addAll(Arrays.asList(categoryArray));
            adapter.notifyDataSetChanged();

            progressDialog.dismiss();
            switchLayout(INITIAL_STATE);
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
            switchLayout(ERROR_STATE);
        }
    }

    public void getResponse() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        // Set up progress bar before call
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMax(100);
        progressDialog.setMessage("Loading....");
        progressDialog.setTitle("Fetching data from world.openfoodfacts.org");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Show it
        progressDialog.show();

        final StringRequest request = new StringRequest(
                Request.Method.GET,
                SERVICE_URL,
                this::handleResponse, // method reference, equivalent to: response -> handleResponse(response)
                this::handleError); // method reference, equivalent to: error -> handleError(error)

        requestQueue.add(request);
    }

    private void handleError(VolleyError volleyError) {
        Snackbar.make(recyclerView_categories, "Something went wrong. Please check your connection.", BaseTransientBottomBar.LENGTH_LONG).show();
        progressDialog.dismiss();
        switchLayout(ERROR_STATE);
    }

    /*-------------------------------DATABASE-----------------------------------*/
    List<Category> getAllCategoriesSortedByProducts() {
        return CategoriesRoomDatabase.getDatabase(getContext()).categoriesDao().getCategoriesSortedByProducts();
    }

    /*-------------------------------------------------------------------------*/
    @Override
    public void itemClicked(View v, int pos, String value) {
        System.out.println("You clicked item in position " + pos + " with value " + value);
    }

    @Override
    public boolean itemLongClicked(View v, int pos, String value) {
        return false;
    }

    private void switchLayout(int state) {
        // Layout elements
        FrameLayout frameLayout_categories = requireView().findViewById(R.id.frameLayout_categories);
        recyclerView_categories = requireView().findViewById(R.id.recyclerView_categories);

        switch (state) {
            case INITIAL_STATE:
                frameLayout_categories.setVisibility(View.INVISIBLE);
                recyclerView_categories.setVisibility(View.VISIBLE);
                break;
            case ERROR_STATE:
                frameLayout_categories.setVisibility(View.VISIBLE);
                recyclerView_categories.setVisibility(View.INVISIBLE);
                break;
        }
    }



}