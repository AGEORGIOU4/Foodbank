package com.example.foodbank.ui.products;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodbank.R;
import com.example.foodbank.db.ProductsRoomDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Vector;

public class ViewProductActivity extends AppCompatActivity {
    // Activity states for switching layouts
    private static final int INITIAL_STATE = 2001;
    private static final int ERROR_STATE = 2002;

    // Layout elements
    private final Vector<Product> productsList = new Vector<>();

    // Response
    private RequestQueue mQueue;

    // Product attributes
    private String barcode;
    private String title;
    private String nutriScore;
    private String novaGroup;
    private String ecoScore;
    private String ingredients;
    private String nutriments;
    private String vegan;
    private String vegetarian;
    private String categoriesImported;
    private String imageUrl;

    private Product myProduct = new Product("", "", "", "", "", "",
            "", "", "", "", false, System.currentTimeMillis(), "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove Action Bar
        try { this.getSupportActionBar().hide(); } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // Implement an HTTP request using Volley library
        this.mQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.d3_activity_view_product);
        View root = findViewById(R.id.root);

        // Get barcode from clicked item
        getExtraData(root);
        findClickedProduct(root);

        // Try again on no connection
        tryAgain(root);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void findClickedProduct(View view) {
        // Check if the product belongs to user's database (also offline)
        productsList.clear();
        productsList.addAll(getAllProductsSortedByTitle());
        if (checkIfProductIsOnDatabase()) {
            initializeValuesOnCard(view);
        } else {
            jsonParse(view);
        }
    }

    /*--------------------------------LAYOUT------------------------------------*/
    public void getExtraData(View view) {
        Intent intent = getIntent();
        if (!intent.getStringExtra("extra_products_code").equals("") ||
                intent.getStringExtra("extra_products_code") != null) {
            barcode = intent.getStringExtra("extra_products_code");
        }
    }

    private void switchLayout(int state) {
        // Layout elements
        FrameLayout frameLayout_productsInCategory = findViewById(R.id.frameLayout_productsInCategory);
        ScrollView scrollView_viewProduct = findViewById(R.id.scrollView_viewProduct);

        switch (state) {
            case INITIAL_STATE:
                frameLayout_productsInCategory.setVisibility(View.INVISIBLE);
                break;
            case ERROR_STATE:
                frameLayout_productsInCategory.setVisibility(View.VISIBLE);
                scrollView_viewProduct.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /*-------------------------------RESPONSE-----------------------------------*/
    private void jsonParse(View view) {
        String mainURL = "https://world.openfoodfacts.org/api/v0/product/";
        String apiURL = mainURL + barcode;

        // The user's input is concatenated with the main URL and the program makes a request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiURL, null, response -> {
            try {
                JSONObject productObject = response.getJSONObject("product");
                // Check and get the available data
                if (productObject.has("product_name"))
                    title = productObject.getString("product_name");
                else
                    title = "unknown";
                if (productObject.has("nutriscore_grade"))
                    nutriScore = productObject.getString("nutriscore_grade");
                else
                    nutriments = "unknown";
                if (productObject.has("nova_group"))
                    novaGroup = productObject.getString("nova_group");
                else
                    novaGroup = "unknown";
                if (productObject.has("ecoscore_grade"))
                    ecoScore = productObject.getString("ecoscore_grade");
                else
                    ecoScore = "unknown";
                if (productObject.has("ingredients_text"))
                    ingredients = productObject.getString("ingredients_text");
                else
                    ingredients = "unknown";

                if (productObject.has("nutriments")) {
                    nutriments = productObject.getString("nutriments");
                } else
                    nutriments = "unknown";
                if (productObject.has("vegan"))
                    vegan = productObject.getString("vegan");
                else
                    vegan = "unknown";
                if (productObject.has("vegetarian"))
                    vegetarian = productObject.getString("vegetarian");
                else
                    vegetarian = "unknown";
                if (productObject.has("categories_imported"))
                    categoriesImported = productObject.getString("categories_imported");
                else
                    categoriesImported = "unknown";

                // Set default image if not found
                if (productObject.has("image_front_small_url")) {
                    imageUrl = productObject.getString("image_front_small_url");
                } else {
                    imageUrl = "https://static.wixstatic.com/media/cd859f_11e62a8757e0440188f90ddc11af8230~mv2.png";
                }

                Product tmpProduct = new Product(barcode, title, nutriScore, novaGroup, ecoScore,
                        ingredients, nutriments, vegan, vegetarian, categoriesImported, false,
                        System.currentTimeMillis(), imageUrl);

                // Check if the product is not already included on the Database and add it
                insert(tmpProduct);
                findClickedProduct(view);
                switchLayout(INITIAL_STATE);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong. Please check your connection", Toast.LENGTH_SHORT).show();
                switchLayout(ERROR_STATE);
            }
        }, error -> {
            // If during the request or response an error is occurred, a Snackbar message will pop up
            Toast.makeText(this, "Something went wrong. Please check your connection", Toast.LENGTH_SHORT).show();
            switchLayout(ERROR_STATE);
        });
        mQueue.add(request);
    }

    /*-------------------------------DATABASE-----------------------------------*/
    List<Product> getAllProductsSortedByTitle() {
        return ProductsRoomDatabase.getDatabase(this).productsDao().getProductsSortedByTitle();
    }

    public boolean checkIfProductIsOnDatabase() {
        // Check if the product belongs to user's database
        for (int i = 0; i < productsList.size(); i++) {
            if (productsList.get(i).getBarcode().equals(barcode)) {
                myProduct = productsList.get(i);
                return true;
            }
        }
        return false;
    }

    // Insert product on products db
    void insert(Product product) {
        ProductsRoomDatabase.getDatabase(this).productsDao().insert(product);
    }

    /*--------------------------------------------------------------------------*/
    public void initializeValuesOnCard(View view) {
        ImageView imageView_viewProductImage = view.findViewById(R.id.imageView_viewProductImage);
        TextView textView_viewProductTitle = view.findViewById(R.id.textView_viewProductTitle);
        ImageView imageView_viewProductNutriScore = view.findViewById(R.id.imageView_viewProductNutriScore);
        ImageView imageView_viewNovaGroup = view.findViewById(R.id.imageView_viewNovaGroup);
        ImageView imageView_viewProductEcoScore = view.findViewById(R.id.imageView_viewProductEcoScore);
        TextView textView_viewProductIngredients = view.findViewById(R.id.textView_viewProductIngredients);
        TextView textView_viewProductNutriments = view.findViewById(R.id.textView_viewProductNutriments);
        TextView textView_viewProductVegan = view.findViewById(R.id.textView_viewProductVegan);
        TextView textView_viewProductVegetarian = view.findViewById(R.id.textView_viewProductVegetarian);
        TextView textView_viewProductCategoriesImported = view.findViewById(R.id.textView_viewProductCategoriesImported);
        TextView textView_viewProductBarcode = view.findViewById(R.id.textView_viewProductBarcode);

        ScrollView scrollView_viewProduct = view.findViewById(R.id.scrollView_viewProduct);

        // Initialize values on card
        textView_viewProductBarcode.setText("Barcode: " + myProduct.getBarcode());

        textView_viewProductTitle.setText(myProduct.getTitle());

        try {
            switch (myProduct.getNutriScore()) {
                case "1":
                case "A":
                case "a":
                    imageView_viewProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_a);
                    break;
                case "2":
                case "B":
                case "b":
                    imageView_viewProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_b);
                    break;
                case "3":
                case "C":
                case "c":
                    imageView_viewProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_c);
                    break;
                case "4":
                case "D":
                case "d":
                    imageView_viewProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_d);
                    break;
                case "5":
                case "E":
                case "e":
                    imageView_viewProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_e);
                    break;
                default:
                    imageView_viewProductNutriScore.setImageResource(R.drawable.d_img_nutriscore_unknown);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            switch (myProduct.getEcoScore()) {
                case "1":
                case "A":
                case "a":
                    imageView_viewProductEcoScore.setImageResource(R.drawable.d_img_ecoscore_a);
                    break;
                case "2":
                case "B":
                case "b":
                    imageView_viewProductEcoScore.setImageResource(R.drawable.d_img_ecoscore_b);
                    break;
                case "3":
                case "C":
                case "c":
                    imageView_viewProductEcoScore.setImageResource(R.drawable.d_img_ecoscore_c);
                    break;
                case "4":
                case "D":
                case "d":
                    imageView_viewProductEcoScore.setImageResource(R.drawable.d_img_ecoscore_d);
                    break;
                default:
                    imageView_viewProductEcoScore.setImageResource(R.drawable.d_img_ecoscore_unknown);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            switch (myProduct.getNovaGroup()) {
                case "1":
                case "A":
                case "a":
                    imageView_viewNovaGroup.setImageResource(R.drawable.d_img_novagroup_1);
                    break;
                case "2":
                case "B":
                case "b":
                    imageView_viewNovaGroup.setImageResource(R.drawable.d_img_novagroup_2);
                    break;
                case "3":
                case "C":
                case "c":
                    imageView_viewNovaGroup.setImageResource(R.drawable.d_img_novagroup_3);
                    break;
                case "4":
                case "D":
                case "d":
                    imageView_viewNovaGroup.setImageResource(R.drawable.d_img_novagroup_4);
                    break;
                default:
                    imageView_viewNovaGroup.setImageResource(R.drawable.d_img_novagroup_unknown);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!myProduct.getIngredients().equals("")) {
            textView_viewProductIngredients.setText("Ingredients: " + myProduct.getIngredients());
        } else {
            textView_viewProductIngredients.setText("Ingredients: unknown");
        }
        if (!myProduct.getNutriments().equals("")) {
            textView_viewProductNutriments.setText("Nutriments: " + myProduct.getNutriments());
        } else {
            textView_viewProductNutriments.setText("Nutriments: unknown");
        }
        if (!myProduct.getVegan().equals("")) {
            textView_viewProductVegan.setText("Vegan: " + myProduct.getVegan());
        } else {
            textView_viewProductVegan.setText("Vegan: unknown");
        }
        if (!myProduct.getVegetarian().equals("")) {
            textView_viewProductVegetarian.setText("Vegetarian: " + myProduct.getVegetarian());
        } else {
            textView_viewProductVegetarian.setText("Vegetarian: unknown");
        }
        if (!myProduct.getCategoriesImported().equals("")) {
            textView_viewProductCategoriesImported.setText("Categories: " + myProduct.getCategoriesImported());
        } else {
            textView_viewProductCategoriesImported.setText("Categories: unknown");
        }

        try {
            Picasso.get().load(myProduct.getImageUrl()).resize(66, 75).centerCrop().into(imageView_viewProductImage);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Oops, something is wrong with the photo " + myProduct.getImageUrl(), Toast.LENGTH_SHORT).show();
        }

        // Set card as visible when data is loaded
        scrollView_viewProduct.setVisibility(View.VISIBLE);
    }

    public void tryAgain(View view) {
        Button button_categories_tryAgain = view.findViewById(R.id.button_tryAgain);
        button_categories_tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });
    }
}
