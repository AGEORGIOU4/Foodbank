package com.example.foodbank.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbank.Category;
import com.example.foodbank.R;
import com.example.foodbank.db.CategoriesRoomDatabase;

import java.util.List;
import java.util.Vector;

public class CategoriesFragment extends Fragment implements CategoriesAdapter.OnItemClickListener, CategoriesAdapter.OnItemLongClickListener {

    private final Vector<Category> categoriesList = new Vector<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.c1_fragment_categories, container, false);

        // Initialize each category from the db to the categoriesList
        categoriesList.addAll(getAllCategoriesSortedByTitle());

        // Recycler View implementation
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView_categories);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        CategoriesAdapter adapter = new CategoriesAdapter(categoriesList, this, this);
        recyclerView.setAdapter(adapter);

        return root;
    }

    List<Category> getAllCategoriesSortedByTitle() {
        return CategoriesRoomDatabase.getDatabase(getContext()).categoriesDao().getCategoriesSortedByTitle();
    }

    @Override
    public void itemClicked(View v, int pos, String value) {
        System.out.println("You clicked item in position " + pos + " with value " + value);
    }

    @Override
    public boolean itemLongClicked(View v, int pos, String value) {
        return false;
    }
}