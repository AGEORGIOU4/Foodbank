package com.example.foodbank.ui.healthy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HealthyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HealthyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is healthy fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}