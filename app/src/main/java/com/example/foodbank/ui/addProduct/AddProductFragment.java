package com.example.foodbank.ui.addProduct;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodbank.Product;
import com.example.foodbank.R;
import com.example.foodbank.db.ProductsRoomDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddProductFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "test";

    RequestQueue mQueue;

    // Layout elements
    private EditText textInputEnterBarcode;
    private TextView textViewFetchedData;

    private String code = "";
    private String name = "";
    private String grade = "";
    private String nova_group = "";
    private String eco_score = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddProductViewModel addProductViewModel = new ViewModelProvider(this).get(AddProductViewModel.class);

        View root = inflater.inflate(R.layout.a4_fragment_add_product, container, false);
        addProductViewModel.getText().observe(getViewLifecycleOwner(), s -> { });

        // Layout elements
        Button buttonAddProduct = root.findViewById(R.id.buttonAddProduct);
        this.textViewFetchedData = root.findViewById(R.id.textViewFetchedData);

        // Implements an HTTP request using Volley library
        this.mQueue = Volley.newRequestQueue(requireContext());

        // Barcode input checks (not empty), and jsonParsing
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcodeHandler(root);
            }
        });

        return root;
    }

    public void barcodeHandler(View view) {
        this.textInputEnterBarcode = view.findViewById(R.id.textInputEnterBarcode);

        if (textInputEnterBarcode.getText().toString().matches("")) {
            Toast.makeText(getContext(), "Please enter barcode", Toast.LENGTH_SHORT).show();
        } else {
            jsonParse();
        }
    }

    private void jsonParse() {
        String mainURL = "https://world.openfoodfacts.org/api/v0/product/";
        String apiURL = mainURL + textInputEnterBarcode.getText().toString();

        // The user's input is concatenated with the main URL and the program makes a request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // if the response is successful we get useful data from the JSON file
                            JSONObject productObject = response.getJSONObject("product");
                            code = productObject.getString("code");
                            name = productObject.getString("product_name");
                            grade = productObject.getString("nutriscore_grade");
                            nova_group = productObject.getString("nova_group");
                            eco_score = productObject.getString("ecoscore_grade");
                            // String ingredients_text_en = productObject.getString("ingredients_text_en");


                            textViewFetchedData.append("Code: " + code.toUpperCase());
                            textViewFetchedData.append("\n");
                            textViewFetchedData.append("Name: " + name.toUpperCase());
                            textViewFetchedData.append("\n");
                            textViewFetchedData.append("Grade: " + grade.toUpperCase());
                            textViewFetchedData.append("\n");
                            textViewFetchedData.append("Novagroup: " + nova_group.toUpperCase());
                            textViewFetchedData.append("\n");
                            textViewFetchedData.append("Ecoscore: " + eco_score.toUpperCase());
                            textViewFetchedData.append("\n");
                            // data.append("Ingredients: " + ingredients_text_en);
                            textViewFetchedData.append("\n");
                            //  data.append("Nutrients: " + ingredients_text_en);

                            // Add the Product on the products list and DB
                            addProduct();
                            Toast.makeText(getContext(), "Added", Toast.LENGTH_LONG).show();

                            // Try on array
                            JSONArray traces_tags = productObject.getJSONArray("traces_tags");

                            for (int i = 0; i < traces_tags.length(); i++) {
                                JSONObject object = traces_tags.getJSONObject(i);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // If during the request or response an error is occurred, a Toast message will pop up
                Toast.makeText(getContext(), "Something went wrong. Please check your connection.", Toast.LENGTH_LONG).show();
            }
        });
        mQueue.add(request);
    }

    // Add the scanned product on products list
    public void addProduct() {
            Product testProduct = new Product(code, name, grade, Integer.parseInt(nova_group), "Ingredients", "Nutrients", false, System.currentTimeMillis());
            insert(testProduct);
    }

    // Insert product on products db
    void insert(Product product) {
        ProductsRoomDatabase.getDatabase(getContext()).productsDao().insert(product);
    }

    @Override
    public void onClick(View v) {

    }
}



