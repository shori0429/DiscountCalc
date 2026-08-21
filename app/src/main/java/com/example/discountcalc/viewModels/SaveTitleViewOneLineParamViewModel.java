package com.example.discountcalc.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.discountcalc.dataBase.CustomPreferenceRepository;

public class SaveTitleViewOneLineParamViewModel extends AndroidViewModel {
    private final MutableLiveData<String> loadSaveTitleColumn;

    CustomPreferenceRepository repository;

    public SaveTitleViewOneLineParamViewModel(@NonNull Application application) {
        super(application);
        repository=new CustomPreferenceRepository(application);
        loadSaveTitleColumn=new MutableLiveData<>("");
    }

    public void setLoadSaveTitleColumn(String loadSaveTitleColumn) {
        this.loadSaveTitleColumn.setValue(loadSaveTitleColumn);
    }
}
