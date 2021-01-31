package com.example.foodbank.main_activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.foodbank.R;
import com.example.foodbank.classes.Product;
import com.example.foodbank.classes.Settings;
import com.example.foodbank.db.ProductsRoomDatabase;
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
    private String passedBarcode = "";

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
        getExtras();
        loadSettings();
        super.onResume();
    }

    /*-------------------------------PASSED DATA---------------------------------*/
    private void getExtras() {
        Intent intent = getIntent();
        passedBarcode = intent.getStringExtra("extra_products_code");
        Product tmpProduct = getProduct(passedBarcode).get(0);

        // Set input field values
        if(tmpProduct.getTitle() == null || tmpProduct.getTitle().equals(""))
            edited_Title = "Unknown";
        else
            edited_Title = tmpProduct.getTitle();

        if (tmpProduct.getNutriScore() == null || tmpProduct.getNutriScore().toUpperCase().equals("UNKNOWN"))
            edited_NutriScore = "";
        else
            edited_NutriScore = tmpProduct.getNutriScore().toUpperCase();

        if (tmpProduct.getEcoScore() ==  null  || tmpProduct.getEcoScore().toUpperCase().equals("UNKNOWN"))
            edited_EcoScore = "";
        else
            edited_EcoScore = tmpProduct.getEcoScore().toUpperCase();

        if (tmpProduct.getNovaGroup() == null  || tmpProduct.getNovaGroup().toUpperCase().equals("UNKNOWN".toUpperCase()))
            edited_NovaGroup = "";
        else
            edited_NovaGroup = tmpProduct.getNovaGroup().toUpperCase();

        edited_Starred = tmpProduct.isStarred();


        editText_Title.setText(edited_Title);
        editText_NutriScore.setText(edited_NutriScore);
        editText_EcoScore.setText(edited_EcoScore);
        editText_NovaGroup.setText(edited_NovaGroup);
        checkBox_editStarred.setChecked(edited_Starred);
    }

    /*-------------------------------DATABASE-----------------------------------*/
    List<Product> getProduct(String product_code) {
        return ProductsRoomDatabase.getDatabase(this).productsDao().getProduct(product_code);
    }

    void update(Product product) {
        ProductsRoomDatabase.getDatabase(this).productsDao().update(product);
    }

    public void updateProduct() {
        edited_Title = editText_Title.getText().toString();
        edited_NutriScore = editText_NutriScore.getText().toString();
        edited_EcoScore = editText_EcoScore.getText().toString();
        edited_NovaGroup = editText_NovaGroup.getText().toString();
        edited_Starred = checkBox_editStarred.isChecked();

        Product tmpProduct = getProduct(passedBarcode).get(0);

        if (!edited_Title.equals("") && !edited_NutriScore.equals("") && !edited_NutriScore.equals("") && !edited_EcoScore.equals("")
                && !edited_NovaGroup.equals("")) {
            // Check position of which list the edited product comes from
            tmpProduct.setTitle(edited_Title);
            tmpProduct.setNutriScore(edited_NutriScore);
            tmpProduct.setEcoScore(edited_EcoScore);
            tmpProduct.setNovaGroup(edited_NovaGroup);

            update(tmpProduct);

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