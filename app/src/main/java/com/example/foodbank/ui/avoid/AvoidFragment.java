package com.example.foodbank.ui.avoid;

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

public class AvoidFragment extends Fragment {

    private AvoidViewModel avoidViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        avoidViewModel =
                new ViewModelProvider(this).get(AvoidViewModel.class);
        View root = inflater.inflate(R.layout.a7_fragment_avoid, container, false);
        final TextView textView = root.findViewById(R.id.text_avoid);
        avoidViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}