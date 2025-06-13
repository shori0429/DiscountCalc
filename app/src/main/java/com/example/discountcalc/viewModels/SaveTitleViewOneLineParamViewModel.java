package com.example.discountcalc.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.discountcalc.dataBase.PreferenceParamRepository;

public class SaveTitleViewOneLineParamViewModel extends AndroidViewModel {
    private MutableLiveData<String> loadSaveTitleColumn;

    PreferenceParamRepository repository;

    public SaveTitleViewOneLineParamViewModel(@NonNull Application application) {
        super(application);
        repository=new PreferenceParamRepository(application);
        loadSaveTitleColumn=new MutableLiveData<>("");
    }

    public void setLoadSaveTitleColumn(String loadSaveTitleColumn) {
        repository.getSave(loadSaveTitleColumn);
        this.loadSaveTitleColumn.setValue(loadSaveTitleColumn);
    }
}
