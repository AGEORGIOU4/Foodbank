package com.example.foodbank.ui.products;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodbank.R;
import com.example.foodbank.db.ProductsRoomDatabase;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d4_activity_edit_product);

        editText_Title = findViewById(R.id.editText_Title);
        editText_NutriScore = findViewById(R.id.editText_NutriScore);
        editText_EcoScore = findViewById(R.id.editText_EcoScore);
        editText_NovaGroup = findViewById(R.id.editText_NovaGroup);
        checkBox_editStarred = findViewById(R.id.checkBox_editStarred);

        getExtras();

        button_submitEdit = findViewById(R.id.button_submitEdit);
        button_submitEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });

    }

    private void getExtras() {
        Intent intent = getIntent();
        edited_Title = intent.getStringExtra("clicked_item_title");

        if (intent.getStringExtra("clicked_item_nutri_score").toUpperCase().equals("UNKNOWN"))
            edited_NutriScore = "-";
        else
            edited_NutriScore = intent.getStringExtra("clicked_item_nutri_score").toUpperCase();

        if (intent.getStringExtra("clicked_item_eco_score").toUpperCase().equals("UNKNOWN"))
            edited_EcoScore = "-";
        else
            edited_EcoScore = intent.getStringExtra("clicked_item_eco_score").toUpperCase();

        if (intent.getStringExtra("clicked_item_nova_group").toUpperCase().equals("UNKNOWN"))
            edited_NovaGroup = "-";
        else
        edited_NovaGroup = intent.getStringExtra("clicked_item_nova_group").toUpperCase();

        edited_Starred = intent.getBooleanExtra("clicked_item_starred", false);

        editText_Title.setText(edited_Title);
        editText_NutriScore.setText(edited_NutriScore);
        editText_EcoScore.setText(edited_EcoScore);
        editText_NovaGroup.setText(edited_NovaGroup);
        checkBox_editStarred.setChecked(edited_Starred);

        itemPosition = intent.getIntExtra("clicked_item_position", 0);
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

        if(!edited_NutriScore.equals("") && !edited_NutriScore.equals("") && !edited_EcoScore.equals("")
        && !edited_NovaGroup.equals("")) {
            Product currentProduct = getAllProductsSortedByTimestamp().get(itemPosition);
            // Update product
            currentProduct.setTitle(edited_Title);
            currentProduct.setNutriScore(edited_NutriScore);
            currentProduct.setEcoScore(edited_EcoScore);
            currentProduct.setNovaGroup(edited_NovaGroup);
            currentProduct.setStarred(edited_Starred);
            update(currentProduct);

            finish();
        } else {
            Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show();
        }

    }

    List<Product> getAllProductsSortedByTimestamp() {
        return ProductsRoomDatabase.getDatabase(this).productsDao().getProductsSortedByTimestamp();
    }

}