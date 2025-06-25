package com.example.discountcalc.viewModels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.discountcalc.DAO.PreferenceParamDAO;
import com.example.discountcalc.dataBase.AppDataBase;
import com.example.discountcalc.dataBase.PreferenceParamRepository;
import com.example.discountcalc.params.CustomPreferenceData;
import com.example.discountcalc.params.PreferenceParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomPreferenceListViewModel extends AndroidViewModel {

    private PreferenceParamRepository dataRepository;
    AppDataBase dataBase;
    private final MutableLiveData<List<CustomPreferenceData>> preferenceDataList;

    private List<PreferenceParam> allSaveDataList;

    public CustomPreferenceListViewModel(Application application){
        super(application);
        dataRepository=new PreferenceParamRepository(application);
        preferenceDataList =new MutableLiveData<>(new ArrayList<>(0));
        allSaveDataList=new ArrayList<>(0);
        getAllDAO();
    }

    public LiveData<List<CustomPreferenceData>> CustomPreferenceList(){
        return preferenceDataList;
    }

    public List<String> SaveNameList(){
        return SaveNameColumnsList();
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


    public void addPreferenceData(int per,String saveName){
        List<CustomPreferenceData> currentList=CustomPreferenceList().getValue();
        CustomPreferenceData data=new CustomPreferenceData(listSize()+1,per,saveName);
        currentList.add(data);
        preferenceDataList.setValue(currentList);

    }

    public void addDefaultPreferenceData(){
        List<CustomPreferenceData> currentList=CustomPreferenceList().getValue();
        CustomPreferenceData data=new CustomPreferenceData(listSize()+1,0,"");
        currentList.add(data);
        preferenceDataList.setValue(currentList);

    }

    public void updatePreferenceData(int index,CustomPreferenceData newData){
        List<CustomPreferenceData> currentList=new ArrayList<>(Objects.requireNonNull(preferenceDataList.getValue()));
        if(index>=0&&index<currentList.size()){
            currentList.get(index).getDiscountNo().setValue(currentList.get(index).getDiscountNo().getValue());
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
        Objects.requireNonNull(preferenceDataList.getValue()).get(index).setDiscountNo(value);
    }

    public boolean saveDataBase(@NonNull String saveName){
        if(preferenceDataList.getValue()!=null) {
            List<PreferenceParam> params=new ArrayList<>();
            for (var data : preferenceDataList.getValue()) {
                params.add(PreferenceParam.createPreferenceParam(saveName, data.DiscountPer()));
            }
            dataRepository.upsertAll(params);
        }
        return false;
    }

    public boolean updateDAO() {
        if(preferenceDataList.getValue()!=null) {
            // データベース取得
            PreferenceParamDAO preferenceParamDAO = dataBase.preferenceParamDAO();
            //TODO　保存処理を書く


            Log.i("database", dataBase.preferenceParamDAO().getAll().stream().toString());
        return true;
        }
        return false;
    }

    public boolean existingCheckDAO(String name){
        List<PreferenceParam> params;
        params= dataRepository.getSave(name);
        Log.i("existingCheck",""+params.size());
        return params != null;
    }

    public void getAllDAO(){
        List<PreferenceParam> params;
        params= dataRepository.getAll();
        if(params!=null) {
            Log.i("repository","connectSuccess");
            Log.i("getAllDAO",params.size()+"");
            allSaveDataList=params;
            params.forEach(t->{
                Log.i("repository",t.toString());
            });
        }else{
            Log.i("repository","notConnect");
        }
    }

    public void deleteAllDAO(){
        dataRepository.deleteAll();
    }

    private List<String> SaveNameColumnsList(){
        return dataRepository.getSaveNameColumnsList();
    }

}
