package com.example.foodbank.ui.addProduct;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
    private EditText textInput_enterBarcode;

    private String code = "";
    private String title = "";
    private String grade = "";
    private String novaGroup = "";
    private String ecoScore = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.e1_fragment_add_product, container, false);

        // Layout elements
        Button buttonAddProduct = root.findViewById(R.id.button_addProduct);
        TextView textView_fetchedData = root.findViewById(R.id.textView_fetchedData);

        // Implements an HTTP request using Volley library
        this.mQueue = Volley.newRequestQueue(requireContext());

        // Barcode input checks (not empty), and jsonParsing
        buttonAddProduct.setOnClickListener(view -> barcodeHandler(root));

        return root;
    }

    public void barcodeHandler(View view) {
        this.textInput_enterBarcode = view.findViewById(R.id.textInput_enterBarcode);

        if (textInput_enterBarcode.getText().toString().matches("")) {
            Toast.makeText(getContext(), "Please enter barcode", Toast.LENGTH_SHORT).show();
        } else {
            jsonParse();
        }
    }

    private void jsonParse() {
        hideKeyboard();

        String mainURL = "https://world.openfoodfacts.org/api/v0/product/";
        String apiURL = mainURL + textInput_enterBarcode.getText().toString();

        // The user's input is concatenated with the main URL and the program makes a request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiURL, null,
                response -> {
                    try {
                        // if the response is successful we get useful data from the JSON file
                        JSONObject productObject = response.getJSONObject("product");
                        code = productObject.getString("code").toUpperCase();
                        title = productObject.getString("product_name").toUpperCase();
                        grade = productObject.getString("nutriscore_grade").toUpperCase();
                        novaGroup = productObject.getString("nova_group").toUpperCase();
                        ecoScore = productObject.getString("ecoscore_grade").toUpperCase();
                        // String ingredients_text_en = productObject.getString("ingredients_text_en");
                        setProductCard(getView());

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
                }, error -> {
            // If during the request or response an error is occurred, a Toast message will pop up
            Toast.makeText(getContext(), "Something went wrong. Please check your connection.", Toast.LENGTH_LONG).show();
        });
        mQueue.add(request);
    }

    // Add the scanned product on products list
    public void addProduct() {
        Product testProduct = new Product(code, title, grade, novaGroup, ecoScore, "Ingredients", "Nutrients", false, System.currentTimeMillis());
        insert(testProduct);
    }

    // Insert product on products db
    void insert(Product product) {
        ProductsRoomDatabase.getDatabase(getContext()).productsDao().insert(product);
    }

    @Override
    public void onClick(View v) {

    }

    public void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    public void setProductCard(View view) {
        TextView textView_title = view.findViewById(R.id.textView_title);
        ImageView imageView_grade = view.findViewById(R.id.imageView_grade);
        ImageView imageView_ecoScore = view.findViewById(R.id.imageView_ecoScore);
        ImageView imageView_novaGroup = view.findViewById(R.id.imageView_novaGroup);
        CheckBox starredCheckBox = view.findViewById(R.id.starredCheckBox);

        CardView cardView_product = view.findViewById(R.id.cardView_product);

        textView_title.setText(title);

        switch (grade) {
            case "A":
                imageView_grade.setImageResource(R.drawable.d_img_nutriscore_a);
                break;
            case "B":
                imageView_grade.setImageResource(R.drawable.d_img_nutriscore_b);
                break;
            case "C":
                imageView_grade.setImageResource(R.drawable.d_img_nutriscore_c);
                break;
            case "D":
                imageView_grade.setImageResource(R.drawable.d_img_nutriscore_d);
                break;
            case "E":
                imageView_grade.setImageResource(R.drawable.d_img_nutriscore_e);
                break;
            default:
                imageView_grade.setImageResource(R.drawable.d_img_nutriscore_unknown);
                break;
        }

        switch (ecoScore) {
            case "1":
            case "A":
                imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_a);
                break;
            case "2":
            case "B":
                imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_b);
                break;
            case "3":
            case "C":
                imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_c);
                break;
            case "4":
            case "D":
                imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_d);
                break;
            default:
                imageView_ecoScore.setImageResource(R.drawable.d_img_ecoscore_unknown);
                break;
        }

        switch (novaGroup) {
            case "1":
            case "A":
                imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_1);
                break;
            case "2":
            case "B":
                imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_2);
                break;
            case "3":
            case "C":
                imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_3);
                break;
            case "4":
            case "D":
                imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_4);
                break;
            default:
                imageView_novaGroup.setImageResource(R.drawable.d_img_novagroup_unknown);
                break;
        }

        cardView_product.setVisibility(View.VISIBLE);
    }

}



