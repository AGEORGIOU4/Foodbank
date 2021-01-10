package com.example.foodbank.ui.healthy;

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

public class HealthyFragment extends Fragment {

    private HealthyViewModel healthyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        healthyViewModel =
                new ViewModelProvider(this).get(HealthyViewModel.class);
        View root = inflater.inflate(R.layout.a6_fragment_healthy, container, false);
        final TextView textView = root.findViewById(R.id.text_healthy);
        healthyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}