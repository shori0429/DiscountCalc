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

    private final MutableLiveData<List<CustomPreferenceData>> preferenceDataList;

    private CustomPreferenceListViewModel(){
        preferenceDataList =new MutableLiveData<>(new ArrayList<>(0));
    }

    public LiveData<List<CustomPreferenceData>> CustomPreferenceList(){
        return preferenceDataList;
    }

    public CustomPreferenceData getCustomPreferenceData(int index){
        return Objects.requireNonNull(preferenceDataList.getValue()).get(index);
    }

    public int listSize(){
        return Objects.requireNonNull(preferenceDataList.getValue()).size();
    }

    public void setPreferenceDataList(List<CustomPreferenceData> datas){
        this.preferenceDataList.setValue(datas);
    }


    public void addPreferenceData(int per){
        preferenceDataList.getValue().add(new CustomPreferenceData(listSize(),per));
    }

    public void addDefaultPreferenceData(){
        CustomPreferenceData data=new CustomPreferenceData(listSize(),0);
        preferenceDataList.getValue().add(data);
    }

    public void updatePreferenceData(int index,CustomPreferenceData newData){
        List<CustomPreferenceData> currentList=new ArrayList<>(Objects.requireNonNull(preferenceDataList.getValue()));
        if(index>=0&&index<currentList.size()){
            currentList.get(index).getDiscountElement().setValue(currentList.get(index).getDiscountElement().getValue());
            currentList.get(index).getDiscountPer().setValue(currentList.get(index).getDiscountPer().getValue());
            preferenceDataList.setValue(currentList);
        }

    }

    // 指定したインデックスのデータを更新
    public void updatePreferenceDataAll(List<CustomPreferenceData> newData) {
        if (newData != null) {
            preferenceDataList.setValue(newData);
        }
    }

    public void removePreferenceData(int index){
        if(0<index&&index<preferenceDataList.getValue().size()){
            preferenceDataList.getValue().remove(index);
        }
    }

    public void changeTextView(Context context, int index,int value){
        Objects.requireNonNull(preferenceDataList.getValue()).get(index).setDiscountElement(value);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }


}
