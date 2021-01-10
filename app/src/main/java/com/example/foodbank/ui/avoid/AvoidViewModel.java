package com.example.foodbank.ui.avoid;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AvoidViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AvoidViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is avoid fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}