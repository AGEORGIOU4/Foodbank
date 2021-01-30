package com.example.foodbank;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.foodbank.classes.Settings;
import com.example.foodbank.db.ProductsRoomDatabase;
import com.example.foodbank.classes.Product;
import com.example.foodbank.db.SettingsRoomDatabase;

import java.util.List;


public class EditProductActivity extends AppCompatActivity {
    EditText editText_Title;
    EditText editText_NutriScore;
    EditText editText_EcoScore;
    EditText editText_NovaGroup;
    CheckBox checkBox_editStarred;
    String edited_Title;
    String edited_NutriScore;
    String edited_EcoScore;
    String edited_NovaGroup;
    boolean edited_Starred;
    int itemPosition = 0;

    Button button_submitEdit;

    private int optionsController = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        editText_Title = findViewById(R.id.editText_Title);
        editText_NutriScore = findViewById(R.id.editText_NutriScore);
        editText_EcoScore = findViewById(R.id.editText_EcoScore);
        editText_NovaGroup = findViewById(R.id.editText_NovaGroup);
        checkBox_editStarred = findViewById(R.id.checkBox_editStarred);

        getExtras();

        button_submitEdit = findViewById(R.id.button_submitEdit);
        button_submitEdit.setOnClickListener(v -> updateProduct());

    }

    @Override
    protected void onResume() {
        loadSettings();
        super.onResume();
    }

    /*-------------------------------PASSED DATA---------------------------------*/
    private void getExtras() {
        Intent intent = getIntent();
        edited_Title = intent.getStringExtra("clicked_item_title");

        if (intent.getStringExtra("clicked_item_nutri_score").toUpperCase().equals("UNKNOWN"))
            edited_NutriScore = "";
        else
            edited_NutriScore = intent.getStringExtra("clicked_item_nutri_score").toUpperCase();

        if (intent.getStringExtra("clicked_item_eco_score").toUpperCase().equals("UNKNOWN"))
            edited_EcoScore = "";
        else
            edited_EcoScore = intent.getStringExtra("clicked_item_eco_score").toUpperCase();

        if (intent.getStringExtra("clicked_item_nova_group").toUpperCase().equals("UNKNOWN"))
            edited_NovaGroup = "";
        else
            edited_NovaGroup = intent.getStringExtra("clicked_item_nova_group").toUpperCase();

        edited_Starred = intent.getBooleanExtra("clicked_item_starred", false);

        editText_Title.setText(edited_Title);
        editText_NutriScore.setText(edited_NutriScore);
        editText_EcoScore.setText(edited_EcoScore);
        editText_NovaGroup.setText(edited_NovaGroup);
        checkBox_editStarred.setChecked(edited_Starred);

        itemPosition = intent.getIntExtra("clicked_item_position", 0);

        optionsController = intent.getIntExtra("extra_sort_controller", 1);
        System.out.println("controller "  + optionsController);
    }

    /*-------------------------------DATABASE-----------------------------------*/
    List<Product> getAllProductsSortedByTimestamp() {
        return ProductsRoomDatabase.getDatabase(this).productsDao().getProductsSortedByTimestamp();
    }

    List<Product> getProductsSortedByTitle() {
        return ProductsRoomDatabase.getDatabase(this).productsDao().getProductsSortedByTitle();
    }

    List<Product> getProductsSortedByNutriscore() {
        return ProductsRoomDatabase.getDatabase(this).productsDao().getProductsSortedByNutriscore();
    }

    List<Product> getProductsSortedByEcoscore() {
        return ProductsRoomDatabase.getDatabase(this).productsDao().getProductsSortedByEcoscore();
    }

    List<Product> getProductsSortedByNovaGroup() {
        return ProductsRoomDatabase.getDatabase(this).productsDao().getProductsSortedByNovaGroup();
    }

    List<Product> getProductsFavorites() {
        return ProductsRoomDatabase.getDatabase(this).productsDao().getProductsFavorites();
    }

    void update(Product product) {
        ProductsRoomDatabase.getDatabase(this).productsDao().update(product);
    }

    void insert(Product product) {
        ProductsRoomDatabase.getDatabase(this).productsDao().insert(product);
    }

    void delete(final Product product) {
        ProductsRoomDatabase.getDatabase(this).productsDao().delete(product);
    }

    public void updateProduct() {
        edited_Title = editText_Title.getText().toString();
        edited_NutriScore = editText_NutriScore.getText().toString();
        edited_EcoScore = editText_EcoScore.getText().toString();
        edited_NovaGroup = editText_NovaGroup.getText().toString();
        edited_Starred = checkBox_editStarred.isChecked();

        if (!edited_Title.equals("") && !edited_NutriScore.equals("") && !edited_NutriScore.equals("") && !edited_EcoScore.equals("")
                && !edited_NovaGroup.equals("")) {
            // Check position of which list the edited product comes from
            Product currentProduct;
            switch (optionsController) {
                case 1:
                    currentProduct = getAllProductsSortedByTimestamp().get(itemPosition);
                    delete(getAllProductsSortedByTimestamp().get(itemPosition));
                    break;
                case 2:
                    currentProduct = (getProductsSortedByTitle()).get(itemPosition);
                    delete(getProductsSortedByTitle().get(itemPosition));
                    break;
                case 3:
                    currentProduct = getProductsSortedByNutriscore().get(itemPosition);
                    delete(getProductsSortedByNutriscore().get(itemPosition));
                    break;
                case 4:
                    currentProduct = getProductsSortedByEcoscore().get(itemPosition);
                    delete(getProductsSortedByEcoscore().get(itemPosition));
                    break;
                case 5:
                    currentProduct = getProductsSortedByNovaGroup().get(itemPosition);
                    delete(getProductsSortedByNovaGroup().get(itemPosition));
                    break;
                case 6:
                    currentProduct = getProductsFavorites().get(itemPosition);
                    delete(getProductsFavorites().get(itemPosition));
                    break;
                default:
                    currentProduct = getAllProductsSortedByTimestamp().get(itemPosition);
                    break;
            }
            Product tmpProduct = new Product(currentProduct.getBarcode(), edited_Title, edited_NutriScore, edited_NovaGroup,
                    edited_EcoScore, currentProduct.getIngredients(), currentProduct.getNutriments(),
                    currentProduct.getVegan(), currentProduct.getVegetarian(), currentProduct.getCategories(), edited_Starred,
                    currentProduct.getTimestamp(), currentProduct.getImageUrl());

            insert(tmpProduct);
            finish();
        } else {
            Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    /*-------------------------------SETTINGS-----------------------------------*/
    List<Settings> getSettings() {
        return SettingsRoomDatabase.getDatabase(this).settingsDao().getSettings();
    }

    public void loadSettings() {
        List<Settings> settings = getSettings();
        boolean theme = settings.get(0).isDarkMode();
        System.out.println("theme is " + theme);

        if (theme) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);
        } else {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);
        }
    }

}