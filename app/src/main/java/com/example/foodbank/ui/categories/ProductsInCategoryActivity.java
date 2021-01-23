package com.example.foodbank.ui.categories;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SearchView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodbank.R;
import com.example.foodbank.ui.products.ProductsAdapter;
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

    // Response
    String productsResponse = "";
    String categoryId;
    String categoryName;

    // Recycler view
    private RecyclerView recyclerView_viewCategoryProducts;
    private final Vector<ProductInCategory> productsList = new Vector<>();
    private ProductsInCategoryAdapter adapter;

    // Layout elements
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.c3_activity_view_category_products);

        View root = findViewById(R.id.root);

        // Get category id from categories fragment
        categoryId = getIntent().getStringExtra("selected_item_id");
        categoryName = getIntent().getStringExtra("selected_item_name");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && categoryName != null) {
            actionBar.setTitle(categoryName);
        }

        // Recycler View implementation
        setRecyclerView();

        getResponse();

        // Try to get response again
        tryAgainEvent(root);

        // Search
        searchItem(root);

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    /*-------------------------------RESPONSE-----------------------------------*/
    public String getFormedUrl() {
        return "https://world.openfoodfacts.org/category/" + categoryId + ".json?page_size=100";
    }

    private void handleResponse(final String response) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            productsResponse = jsonObject.getString("products");
            ProductInCategory[] productArray = new Gson().fromJson(productsResponse, ProductInCategory[].class);
            this.productsList.clear();
            this.productsList.addAll(Arrays.asList(productArray));
            adapter.notifyDataSetChanged();

            progressDialog.dismiss();
            switchLayout(INITIAL_STATE);
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
            switchLayout(INITIAL_STATE);
        }
    }

    private void handleError(VolleyError volleyError) {
        Snackbar.make(recyclerView_viewCategoryProducts, "Something went wrong. Please check your connection.", BaseTransientBottomBar.LENGTH_LONG).show();
        progressDialog.dismiss();
        switchLayout(ERROR_STATE);
    }

    public void getResponse() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Set up progress bar before call
        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Loading....");
        progressDialog.setTitle("Fetching data from world.openfoodfacts.org");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Show it
        progressDialog.show();

        final StringRequest request = new StringRequest(
                Request.Method.GET,
                getFormedUrl(),
                this::handleResponse, // method reference, equivalent to: response -> handleResponse(response)
                this::handleError); // method reference, equivalent to: error -> handleError(error)

        requestQueue.add(request);
    }

    /*---------------------------------RECYCLER VIEW-----------------------------------*/
    public void setRecyclerView() {
        recyclerView_viewCategoryProducts = findViewById(R.id.recyclerView_viewCategoryProducts);
        // Item helper for swipe events
        recyclerView_viewCategoryProducts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_viewCategoryProducts.setLayoutManager(linearLayoutManager);
        adapter = new ProductsInCategoryAdapter(this, productsList, this);
        recyclerView_viewCategoryProducts.setAdapter(adapter);
    }

    /*------------------------------------SEARCH---------------------------------------*/
    public void searchItem(View view) {
        SearchView searchView = view.findViewById(R.id.searchView_categoryProducts);
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

    /*----------------------------------INTERFACES-------------------------------------*/
    @Override
    public void itemClicked(View v, int pos, String value) {
    }

    public void tryAgainEvent(View view) {
        Button button_categories_tryAgain = view.findViewById(R.id.button_categories_tryAgain);
        button_categories_tryAgain.setOnClickListener(v -> getResponse());
    }

    /*-------------------------------------------------------------------------*/
    private void switchLayout(int state) {
        // Layout elements
        FrameLayout frameLayout_categories = findViewById(R.id.frameLayout_categories);
        recyclerView_viewCategoryProducts = findViewById(R.id.recyclerView_viewCategoryProducts);

        switch (state) {
            case INITIAL_STATE:
                frameLayout_categories.setVisibility(View.INVISIBLE);
                recyclerView_viewCategoryProducts.setVisibility(View.VISIBLE);
                break;
            case ERROR_STATE:
                frameLayout_categories.setVisibility(View.VISIBLE);
                recyclerView_viewCategoryProducts.setVisibility(View.INVISIBLE);
                break;
        }
    }
}