package com.example.foodbank.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodbank.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {

    private CategoriesViewModel categoriesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        categoriesViewModel =
                new ViewModelProvider(this).get(CategoriesViewModel.class);
        View root = inflater.inflate(R.layout.a2_fragment_categories, container, false);
        GridView gridView = (GridView) root.findViewById(R.id.gridView_categories);

        String[] gridViewCategories = {"Breakfast Today, tomorrow hello world", "Lunch", "Dinner", "Afternoon"};

        List<String> values = new ArrayList<String>();
        for (int y = 0; y < gridViewCategories.length; y++) {
            values.add(gridViewCategories[y]);
        }

        gridView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.a2_gridcell, values));

        final TextView textView = root.findViewById(R.id.text_categories);
        categoriesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}