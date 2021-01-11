package com.example.foodbank.ui.categories;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.R;

import java.util.Vector;

public class CategoriesFragment extends Fragment implements CategoriesAdapter.OnItemClickListener, CategoriesAdapter.OnItemLongClickListener {

    // Recycler View
    public Vector<String> categoriesList = new Vector<>();
    private CategoriesViewModel categoriesViewModel;
    private RecyclerView recyclerView;
    private CategoriesAdapter adapter;
    private GridLayoutManager gridLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        categoriesViewModel =
                new ViewModelProvider(this).get(CategoriesViewModel.class);
        View root = inflater.inflate(R.layout.a2_fragment_categories, container, false);
        final TextView textView = root.findViewById(R.id.text_categories);
        categoriesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        String[] hardCodedItems = {"HELLO", "WORLD", "THIS", "IS", "ME"};

        recyclerView = root.findViewById(R.id.categories_rv);
        // Initialize each note from the db to the notesList
        for (int i = 0; i < hardCodedItems.length; i++) {
            categoriesList.add(hardCodedItems[i]);
            System.out.println("item is "  + hardCodedItems[i]);
        }

        // Get Recycler from activity_main and set parameters
        recyclerView.setHasFixedSize(true);

        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CategoriesAdapter(categoriesList, this, this);
        recyclerView.setAdapter(adapter);


        return root;
    }

    @Override
    public void itemClicked(View v, int pos, String value) {
    }

    @Override
    public boolean itemLongClicked(View v, int pos, String value) {
        return false;
    }
}