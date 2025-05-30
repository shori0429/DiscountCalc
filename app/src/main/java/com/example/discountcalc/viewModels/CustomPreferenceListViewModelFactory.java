package com.example.discountcalc.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

public class CustomPreferenceListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application mApplication;
    private Object[] mParams;

    public CustomPreferenceListViewModelFactory(Application application,Object... params){
        mApplication=application;
        mParams=params;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(CustomPreferenceListViewModel.class.isAssignableFrom(modelClass)){
            return (T) new CustomPreferenceListViewModel(mApplication);
        }
        return super.create(modelClass);
    }

}
