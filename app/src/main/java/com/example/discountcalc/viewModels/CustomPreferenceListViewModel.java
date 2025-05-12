package com.example.discountcalc.viewModels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.discountcalc.DAO.PreferenceParamDAO;
import com.example.discountcalc.dataBase.AppDataBase;
import com.example.discountcalc.dataBase.PreferenceParamRepository;
import com.example.discountcalc.params.CustomPreferenceData;
import com.example.discountcalc.params.PreferenceParam;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomPreferenceListViewModel extends AndroidViewModel {

    private PreferenceParamRepository dataRepository;
    AppDataBase dataBase;
    private final MutableLiveData<List<CustomPreferenceData>> preferenceDataList;

    public CustomPreferenceListViewModel(Application application){
        super(application);
        //dataRepository=new PreferenceParamRepository(application);

        // データベースのインスタンスの作成
        dataBase= Room.databaseBuilder(application.getApplicationContext(), AppDataBase.class,"sample_db").build();
        Log.i("database", Objects.requireNonNull(dataBase.getOpenHelper().getDatabaseName()));
        // データベース取得
        PreferenceParamDAO preferenceParamDAO= dataBase.preferenceParamDAO();
        LiveData<List<PreferenceParam>> preferenceParamList=preferenceParamDAO.getAll();

        preferenceDataList =new MutableLiveData<>(new ArrayList<>(0));
        if(preferenceParamList.getValue()!=null) {
            for (var v : preferenceParamList.getValue()) {
                addPreferenceData(v.per());
            }
        }
        else{
            addPreferenceData(10);
        }
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

    public boolean updateDAO() {
        if(preferenceDataList.getValue()!=null) {
            // データベース取得
            PreferenceParamDAO preferenceParamDAO = dataBase.preferenceParamDAO();
            //TODO　保存処理を書く


            Log.i("database", dataBase.preferenceParamDAO().getAll().getValue().stream().toString());
        return true;
        }
        return false;
    }
}
