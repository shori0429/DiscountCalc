package com.example.discountcalc.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.discountcalc.params.CustomPreferenceData;

import java.util.ArrayList;
import java.util.List;

public class CustomPreferenceListViewModel extends ViewModel {

    private final MutableLiveData<List<CustomPreferenceData>> preferenceDatas;

    private CustomPreferenceListViewModel(){
        preferenceDatas=new MutableLiveData<>(new ArrayList<>(1));
    }

    public LiveData<List<CustomPreferenceData>> getCustomPreferenceDatas(){
        return preferenceDatas;
    }

    public void setPreferenceDatas(List<CustomPreferenceData> datas){
        this.preferenceDatas.setValue(new ArrayList<>(datas));
    }


    public void addPreferenceData(int index,CustomPreferenceData newData){
        List<CustomPreferenceData> currentList=preferenceDatas.getValue();
        if(currentList !=null && index >=0 && index< currentList.size()){
            currentList.set(index,newData);
            preferenceDatas.setValue(new ArrayList<>(currentList));
        }
    }

    // 指定したインデックスのデータを更新
    public void updatePreferenceDataAll(ArrayList<CustomPreferenceData> newData) {
        List<CustomPreferenceData> currentList = preferenceDatas.getValue();
        if (currentList != null) {
            currentList.clear();
            currentList.addAll(newData);
            preferenceDatas.setValue(new ArrayList<>(currentList)); // 更新を通知
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
