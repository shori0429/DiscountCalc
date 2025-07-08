package com.example.discountcalc.viewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.discountcalc.dataBase.PreferenceParamRepository;
import com.example.discountcalc.params.CustomPreferenceData;
import com.example.discountcalc.params.PreferenceParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomPreferenceListViewModel extends AndroidViewModel {

    private PreferenceParamRepository dataRepository;
    //private final MutableLiveData<List<CustomPreferenceData>> preferenceDataList;
    private final MutableLiveData<List<PreferenceParam>> preferenceParamList;
    // リポジトリのLiveData追跡用のフィールド
    private final LiveData<List<PreferenceParam>> repoPreferenceParamList;


    public CustomPreferenceListViewModel(Application application){
        super(application);
        dataRepository=new PreferenceParamRepository(application);
        preferenceParamList=new MutableLiveData<>();
        repoPreferenceParamList= dataRepository.PreferenceParamList();
    }

    public LiveData<List<PreferenceParam>> PreferenceParamList(){
        return preferenceParamList;
    }

    public LiveData<List<CustomPreferenceData>> CustomPreferenceList(){
        return preferenceDataList;
    }

    public List<String> SaveNameList(){
        return SaveNameColumnsList();
    }

    public PreferenceParam getCustomPreferenceParam(int index){
        return Objects.requireNonNull(preferenceParamList.getValue()).get(index);
    }

    public int listSize(){
        return preferenceParamList.getValue().size();
    }

    public void addPreferenceData(int per,String saveName){
        if(preferenceParamList.getValue()==null) preferenceParamList.setValue(repoPreferenceParamList.getValue());
        List<PreferenceParam> currentList=new ArrayList<>(Objects.requireNonNull(preferenceParamList.getValue()));
        currentList.add(new PreferenceParam(listSize()+1,saveName,per));
        preferenceParamList.setValue(currentList);
    }

    public void addDefaultPreferenceData(){
        if(preferenceParamList.getValue()==null) preferenceParamList.setValue(repoPreferenceParamList.getValue());
        List<PreferenceParam> currentList=new ArrayList<>(Objects.requireNonNull(preferenceParamList.getValue()));
        currentList.add(new PreferenceParam(listSize()+1,"",0));
        preferenceParamList.setValue(currentList);
    }

    public void updatePreferenceData(int index,PreferenceParam newData){
        List<PreferenceParam> currentList=new ArrayList<>(Objects.requireNonNull(preferenceParamList.getValue()));
        if(index>=0&&index<currentList.size()){
            currentList.get(index).uid();
            currentList.get(index).per();
            preferenceParamList.setValue(currentList);
        }

    }

    // 指定したインデックスのデータを更新
    public void updatePreferenceDataAll(List<PreferenceParam> newData) {
        if (newData != null) {
            preferenceParamList.setValue(newData);
        }
    }

    public void removePreferenceData(int index){
        if(0<index&&index<preferenceParamList.getValue().size()){
            preferenceParamList.getValue().remove(index);
        }
    }

    public void changeTextView(Context context, int index,int value){
        Objects.requireNonNull(preferenceParamList.getValue()).get(index).uid();
    }

    public boolean saveNewData(@NonNull String saveName){
        if(preferenceParamList.getValue()!=null) {
            List<PreferenceParam> params=new ArrayList<>();
            for (var data : preferenceParamList.getValue()) {
                params.add(PreferenceParam.createPreferenceParam(saveName, data.uid()));
            }
            dataRepository.insert(params);
            return true;
        }
        return false;
    }

    public boolean SaveUpdateData(@NonNull String saveName){
        if(preferenceParamList.getValue()!=null){
            List<PreferenceParam> params=new ArrayList<>();
            for (var data : preferenceParamList.getValue()) {
                params.add(PreferenceParam.createPreferenceParam(saveName, data.per()));
            }
            dataRepository.update(params);
            return true;
        }
        return false;
    }

    public boolean updateDAO() {
        if(preferenceParamList.getValue()!=null) {
            // データベース取得
            //TODO　保存処理を書く

        return true;
        }
        return false;
    }

    //　引数の名前が既に保存されていないか確認
    public boolean existingCheckDAO(String name){
        return getSaveNameList().stream().noneMatch(name::equals);
    }

    // リポジトリのデータから保存名のリストを重複を取り除いて抽出
    public List<String> getSaveNameList(){
        return Objects.requireNonNull(repoPreferenceParamList.getValue()).stream()
                .map(PreferenceParam::saveName)
                .distinct()
                .collect(Collectors.toList());
    }

}
