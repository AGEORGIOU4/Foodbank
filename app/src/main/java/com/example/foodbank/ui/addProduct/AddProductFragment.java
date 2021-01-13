package com.example.foodbank.ui.addProduct;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodbank.Product;
import com.example.foodbank.R;
import com.example.foodbank.db.ProductsRoomDatabase;

public class AddProductFragment extends Fragment implements View.OnClickListener {

    private AddProductViewModel addProductViewModel;

    private EditText add_Title;
    private CheckBox add_Starred;
    private Button insert_note_btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addProductViewModel =
                new ViewModelProvider(this).get(AddProductViewModel.class);
        View root = inflater.inflate(R.layout.a4_fragment_add_product, container, false);

        add_Title = root.findViewById(R.id.add_Title);
        add_Starred = root.findViewById(R.id.add_Starred);
        insert_note_btn = root.findViewById(R.id.insert_note_btn);

        insert_note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct(root);
            }
        });

        addProductViewModel.getText().observe(getViewLifecycleOwner(), s -> {
        });
        return root;
    }

    void insert(Product product) {
        ProductsRoomDatabase.getDatabase(getContext()).productsDao().insert(product);
    }

    // Add note
    public void addProduct(View view) {
        add_Title = view.findViewById(R.id.add_Title);
        add_Starred = view.findViewById(R.id.add_Starred);

        if (add_Title.getText().toString().matches("")) {
            Toast.makeText(getContext(), "Enter Title", Toast.LENGTH_SHORT).show();
        } else {
            Product testProduct = new Product("737628064502", add_Title.getText().toString(), "A", 4, "Salt", "Pepper", add_Starred.isChecked(), System.currentTimeMillis());
            insert(testProduct);
            Toast.makeText(getContext(), "Added", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {

    }
}



