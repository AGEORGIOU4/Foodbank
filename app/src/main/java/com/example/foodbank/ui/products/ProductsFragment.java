package com.example.foodbank.ui.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.Product;
import com.example.foodbank.R;
import com.example.foodbank.db.ProductsRoomDatabase;

import java.util.List;
import java.util.Vector;

public class ProductsFragment extends Fragment implements ProductsAdapter.OnItemClickListener, ProductsAdapter.OnItemLongClickListener {

    private ProductsViewModel productsViewModel;

    // Recycler View
    public Vector<Product> productsList = new Vector<>();
    private RecyclerView recyclerView;
    private ProductsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        productsViewModel =
                new ViewModelProvider(this).get(ProductsViewModel.class);
        View root = inflater.inflate(R.layout.a3_fragment_products, container, false);
        final TextView textView = root.findViewById(R.id.text_products);
        productsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        recyclerView = root.findViewById(R.id.products_rv);

        // Initialize each note from the db to the notesList
        for (int i = 0; i <= getAllProductsSortedByTitle().size() - 1; i++) {
            productsList.add(getAllProductsSortedByTitle().get(i));
        }

        // Get Recycler from activity_main and set parameters
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ProductsAdapter(productsList, this, this);
        recyclerView.setAdapter(adapter);

        return root;
    }

    List<Product> getAllProductsSortedByTitle() {
        return ProductsRoomDatabase.getDatabase(getContext()).productsDao().getProductsSortedByTitle();
    }

    @Override
    public void itemClicked(View v, int pos, String value) {

    }

    @Override
    public boolean itemLongClicked(View v, int pos, String value) {
        return false;
    }
}