package com.example.discountcalc.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.discountcalc.params.CustomPreferenceData;

import java.util.ArrayList;
import java.util.List;

public class CustomPreferenceListViewModel extends ViewModel {

    private static final CustomPreferenceListViewModel instance=new CustomPreferenceListViewModel();
    private final MutableLiveData<List<CustomPreferenceData>> preferenceDatas;

    private CustomPreferenceListViewModel(){
        preferenceDatas=new MutableLiveData<>();
    }

    public static CustomPreferenceListViewModel getInstance(){
        return instance;
    }

    public LiveData<List<CustomPreferenceData>> getCustomPreferenceDatas(){
        return preferenceDatas;
    }

    public void postValue(List<CustomPreferenceData> value){
        preferenceDatas.postValue(value);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
