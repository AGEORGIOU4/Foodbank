package com.example.foodbank.ui.products;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodbank.Product;
import com.example.foodbank.R;
import com.example.foodbank.db.ProductsRoomDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Vector;

public class ViewProductActivity extends AppCompatActivity {

    private Product myProduct;
    private Vector<Product> productsList = new Vector<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // Remove Action Bar
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {}

        setContentView(R.layout.j0_activity_view_product);

        View root = findViewById(R.id.root);

        productsList.addAll(getAllProductsSortedByTitle());

        String barcode;

        Intent intent = getIntent();
        if (!intent.getStringExtra("product_barcode_products").equals("") || intent.getStringExtra("product_barcode_products") != null) {
            barcode = intent.getStringExtra("product_barcode_products");
            System.out.println("Barcode is: " + barcode);

            // Check from the db which product is clicked
            findClickedProduct(productsList, barcode);
            initializeValuesOnCard(root);
        }
    }

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
        // Initialize values on card
        textView_viewProductBarcode.setText("Barcode: " + myProduct.getBarcode());

        textView_viewProductTitle.setText(myProduct.getTitle());

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
    }

    public void findClickedProduct(Vector<Product> productsList, String barcode) {
        for (int i = 0; i < productsList.size(); i++) {
            if (productsList.get(i).getBarcode().equals(barcode)) {
                myProduct = productsList.get(i);
            }
        }
    }

    List<Product> getAllProductsSortedByTitle() {
        return ProductsRoomDatabase.getDatabase(this).productsDao().getProductsSortedByTitle();
    }
}
