package com.example.discountcalc.viewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.discountcalc.params.CustomPreferenceData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomPreferenceListViewModel extends ViewModel {

    private final MutableLiveData<List<CustomPreferenceData>> preferenceDatas;

    private CustomPreferenceListViewModel(){
        preferenceDatas=new MutableLiveData<>(new ArrayList<>(1));
    }

    public LiveData<List<CustomPreferenceData>> getCustomPreferenceDatas(){
        return preferenceDatas;
    }

    public CustomPreferenceData getCustomPreferenceData(int index){
        return Objects.requireNonNull(preferenceDatas.getValue()).get(index);
    }

    public void setPreferenceDatas(List<CustomPreferenceData> datas){
        this.preferenceDatas.setValue(datas);
    }


    public void addPreferenceData(CustomPreferenceData newData){
        if(newData!=null){
            preferenceDatas.getValue().add(newData);
        }
    }

    // 指定したインデックスのデータを更新
    public void updatePreferenceDataAll(ArrayList<CustomPreferenceData> newData) {
        if (newData != null) {
            preferenceDatas.postValue(newData);
        }
    }

    public void changeTextView(Context context, int index,int value){
        Objects.requireNonNull(preferenceDatas.getValue()).get(index).setDiscountElement(value);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
