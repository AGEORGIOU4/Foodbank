package com.example.foodbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodbank.classes.ProductInCategory;
import com.example.foodbank.adapters.ProductsInCategoryAdapter;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Vector;

public class ProductsInCategoryActivity extends AppCompatActivity implements ProductsInCategoryAdapter.OnItemClickListener {
    // Activity states for switching layouts
    private static final int INITIAL_STATE = 2001;
    private static final int ERROR_STATE = 2002;

    // Layout
    View root;
    TextView textView_showPage;
    private ProgressDialog progressDialog;

    // Recycler view
    private final Vector<ProductInCategory> productsList = new Vector<>();
    private RecyclerView recyclerView_viewCategoryProducts;
    private ProductsInCategoryAdapter adapter;

    // Response
    private String productsResponse = "";
    private String numberOfProductsInPage = "";
    private int totalCategoryProducts = 0;

    // Pages control
    int totalPages = 0;
    private int selectedPage = 1;

    // Passed attributes
    private String categoryId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get extras from categories fragment
        categoryId = getIntent().getStringExtra("selected_item_id");
        categoryName = getIntent().getStringExtra("selected_item_name");
        totalCategoryProducts = getIntent().getIntExtra("selected_item_total_products", 0);

        setActionBar();

        setContentView(R.layout.c2_activity_products_in_category);

        // Fetch all products using API call
        getResponse();

        setRecyclerView();

        root = findViewById(R.id.root);

        // Pages indication
        textView_showPage = root.findViewById(R.id.textView_showPage);

        recyclerView_viewCategoryProducts = findViewById(R.id.recyclerView_viewCategoryProducts);

        // Try again on no connection
        tryAgain(root);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*-----------------------------------LAYOUT---------------------------------------*/
    public void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && categoryName != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(categoryName);
            actionBar.setBackgroundDrawable(getDrawable(R.drawable.action_bar_bc));
        }
    }

    private void switchLayout(int state) {
        // Layout elements
        FrameLayout frameLayout_productsInCategory = findViewById(R.id.frameLayout_productsInCategory);

        switch (state) {
            case INITIAL_STATE:
                frameLayout_productsInCategory.setVisibility(View.INVISIBLE);
                recyclerView_viewCategoryProducts.setVisibility(View.VISIBLE);
                break;
            case ERROR_STATE:
                frameLayout_productsInCategory.setVisibility(View.VISIBLE);
                recyclerView_viewCategoryProducts.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void setRecyclerView() {
        recyclerView_viewCategoryProducts = findViewById(R.id.recyclerView_viewCategoryProducts);
        recyclerView_viewCategoryProducts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_viewCategoryProducts.setLayoutManager(linearLayoutManager);
        adapter = new ProductsInCategoryAdapter(this, productsList, this);
        recyclerView_viewCategoryProducts.setAdapter(adapter);
    }

    /*----------------------------------RESPONSE--------------------------------------*/
    public String getFormedUrl() {
        return "https://world.openfoodfacts.org/category/" + categoryId + ".json?page_size=100&page=" + selectedPage;
    }

    public void getResponse() {
        // Set up progress bar before call
        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Loading....");
        progressDialog.setTitle("Fetching data from world.openfoodfacts.org");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final StringRequest request = new StringRequest(
                Request.Method.GET,
                getFormedUrl(),
                this::handleResponse, // method reference, equivalent to: response -> handleResponse(response)
                this::handleError); // method reference, equivalent to: error -> handleError(error)
        requestQueue.add(request);
    }

    private void handleResponse(final String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            productsResponse = jsonObject.getString("products");
            numberOfProductsInPage = jsonObject.getString("page_count");

            // Populate an array with the all the fetched products
            ProductInCategory[] productArray = new Gson().fromJson(productsResponse, ProductInCategory[].class);
            this.productsList.clear();
            this.productsList.addAll(Arrays.asList(productArray));

            // Recycler View (this must be implemented here so search view will work)
            setRecyclerView();
            // Search
            searchItem(root);

            // Pages handler
            totalPages = (totalCategoryProducts / 100) + 1;
            textView_showPage.setText(selectedPage + "/" + totalPages + " page");

            switchLayout(INITIAL_STATE);

            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();

            switchLayout(ERROR_STATE);

            progressDialog.dismiss();
        }
    }

    private void handleError(VolleyError volleyError) {
        Snackbar.make(recyclerView_viewCategoryProducts, "Something went wrong. Please check your connection.", BaseTransientBottomBar.LENGTH_LONG).show();

        switchLayout(ERROR_STATE);

        progressDialog.dismiss();
    }

    /*---------------------------------PAGES-----------------------------------*/
    public void previousPage(View view) {
        if (selectedPage > 1) {
            selectedPage--;
            getResponse();
        }
    }

    public void nextPage(View view) {
        if (Integer.parseInt(numberOfProductsInPage) == 100) {
            selectedPage++;
            getResponse();
        }
    }

    /*--------------------------------SEARCH-----------------------------------*/
    public void searchItem(View view) {
        SearchView searchView = view.findViewById(R.id.searchView_productsInCategories);
        searchView.setQuery("", true);
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

    /*-----------------------------INTERFACES----------------------------------*/
    @Override
    public void itemClicked(View v, int pos, String code) {
        Intent intent = new Intent(this, ViewProductActivity.class);
        intent.putExtra("extra_products_code", code);
        startActivity(intent);
    }

    // Back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*-------------------------------------------------------------------------*/
    public void tryAgain(View view) {
        Button button_categories_tryAgain = view.findViewById(R.id.button_tryAgain);
        button_categories_tryAgain.setOnClickListener(v -> {
            getResponse();
        });
    }

}