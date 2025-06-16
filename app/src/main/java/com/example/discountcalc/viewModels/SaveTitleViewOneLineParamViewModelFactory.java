package com.example.discountcalc.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SaveTitleViewOneLineParamViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private Object[] mParams;

    public SaveTitleViewOneLineParamViewModelFactory (Application application,Object... params){
        mApplication=application;
        mParams=params;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(SaveTitleViewOneLineParamViewModel.class.isAssignableFrom(modelClass)){
            return (T) new SaveTitleViewOneLineParamViewModel(mApplication);
        }
        return super.create(modelClass);
    }
}
