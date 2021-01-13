package com.example.foodbank.ui.addProduct;

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

import com.example.foodbank.R;

public class AddProductFragment extends Fragment {

    private AddProductViewModel addProductViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addProductViewModel =
                new ViewModelProvider(this).get(AddProductViewModel.class);
        View root = inflater.inflate(R.layout.a4_fragment_add_product, container, false);
        addProductViewModel.getText().observe(getViewLifecycleOwner(), s -> { });
        return root;
    }
}