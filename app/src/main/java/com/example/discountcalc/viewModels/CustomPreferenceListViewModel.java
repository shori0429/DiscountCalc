package com.example.discountcalc.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.discountcalc.dataBase.PreferenceParamRepository;
import com.example.discountcalc.params.PreferenceParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomPreferenceListViewModel extends AndroidViewModel {

    private PreferenceParamRepository dataRepository;

    // 現状保持している1リスト。変動有
    private final MutableLiveData<List<PreferenceParam>> preferenceParamList;

    // リポジトリから取得した「全リスト」を保持。リポジトリアクセス時以外変動無
    private final MutableLiveData<List<PreferenceParam>> allPreferenceParamList;
    // リポジトリのLiveData追跡用のフィールド


    public CustomPreferenceListViewModel(Application application){
        super(application);
        dataRepository=new PreferenceParamRepository(application);
        preferenceParamList=new MutableLiveData<>();
        allPreferenceParamList=new MutableLiveData<>();
        dataRepository.PreferenceParamList().observeForever(allPreferenceParamList::setValue);
        // TODO AllPreferenceListが更新された時の処理を書く
    }

    public void usePreferenceParamList(String name){
        preferenceParamList.setValue(getUsePreferenceParamList(allPreferenceParamList.getValue(),name));
    }

    // 全データから使用する保存名データを抽出してlivedataにセット
    private List<PreferenceParam> getUsePreferenceParamList(List<PreferenceParam> allData,String name) {
        List<PreferenceParam> dataList = new ArrayList<>();
        if (allData != null) {
            dataList = allData.stream().filter(param -> param.saveName().equals(name))
                    .collect(Collectors.toList());
        }
        // 一致データが存在していなかったら初期値を1個セット。
        if (dataList.size() == 0) dataList.add(PreferenceParam.createDefaultParam());
        return dataList;
    }

    public LiveData<List<PreferenceParam>> PreferenceParamList(){
        return preferenceParamList;
    }

    public LiveData<List<PreferenceParam>> AllPreferenceParamList(){return allPreferenceParamList;}

    // 全リストから使用する保存名のデータリストを現在のリストにセット
    public void commitPreferenceParamList(String saveName){
        if(preferenceParamList.getValue()==null||saveName==null)return;
        List<PreferenceParam> currentList=new ArrayList<>(0);
        //List<PreferenceParam> repoParamList=repoPreferenceParamList.getValue();

        // 保存名でフィルター
        allPreferenceParamList.getValue().stream().filter(v->v.saveName().equals(saveName))
                .forEach(v->currentList.add(new PreferenceParam(v.uid(),v.orderIndex(),v.per(),saveName)));

        preferenceParamList.postValue(currentList);
    }


    public PreferenceParam getPreferenceParamData(int index){
        return Objects.requireNonNull(preferenceParamList.getValue()).get(index);
    }

    public int preferenceParamListSize(){
        return Objects.requireNonNull(preferenceParamList.getValue()).size();
    }

    public void addPreferenceData(int per,String saveName){
//        if(preferenceParamList.getValue()==null) preferenceParamList.setValue(repoPreferenceParamList.getValue());
        List<PreferenceParam> currentList=new ArrayList<>(Objects.requireNonNull(preferenceParamList.getValue()));
//        currentList.add(new PreferenceParam(Math.incrementExact(currentList.size()),per,saveName));
        preferenceParamList.setValue(currentList);
    }

    public void addDefaultPreferenceData(){
//        if(preferenceParamList.getValue()==null) preferenceParamList.setValue(repoPreferenceParamList.getValue());
        List<PreferenceParam> currentList=new ArrayList<>(Objects.requireNonNull(preferenceParamList.getValue()));
        currentList.add(new PreferenceParam(0,Math.incrementExact(currentList.size()),0,""));
        preferenceParamList.setValue(currentList);
    }

    // 現在リストのposition番目にある要素をnewValueの値にアップデート
    public void updatePreferenceData(int position, PreferenceParam newValue){
        if(newValue==null)return;
        try{
            List<PreferenceParam> currentList=Objects.requireNonNull(preferenceParamList.getValue());
            currentList.set(position,newValue);
            preferenceParamList.setValue(currentList);
        }catch (NullPointerException e) {
            throw new RuntimeException(e);
        }

    }

    // リストの最後の要素を削除
    public void removePreferenceLastData(){
        if(preferenceParamList.getValue().size()>1) {
            List<PreferenceParam> dataList=preferenceParamList.getValue();
            dataList.remove(dataList.size()-1);
            preferenceParamList.setValue(dataList);
        }
    }

    // リストの全要素削除
    public void allRemovePreferenceData(){
            ArrayList<PreferenceParam> dataList=new ArrayList<>();
            dataList.add(PreferenceParam.createDefaultParam());
            preferenceParamList.postValue(dataList);
    }

    public boolean saveNewData(@NonNull String saveName){
        if(preferenceParamList.getValue()!=null) {
            List<PreferenceParam> params=new ArrayList<>();
            for (var data : preferenceParamList.getValue()) {
                params.add(new PreferenceParam(data.uid(),data.orderIndex(),data.per(),saveName));
            }
            dataRepository.upsert(params);
            return true;
        }
        return false;
    }

    public int deleteSaveData(String name){
        if(name==null)return -1;
        dataRepository.deleteForSaveName(name);
        return 0;
    }

    public boolean saveUpsertData(@NonNull String saveName){
        if(preferenceParamList.getValue()!=null){
            List<PreferenceParam> params=new ArrayList<>();
            for (var data:preferenceParamList.getValue()) {
                params.add(new PreferenceParam(data.uid(),data.orderIndex(),data.per(),saveName));
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
        return getSaveNameList().stream().anyMatch(name::equals);
    }

    // リポジトリのデータから保存名のリストを重複を取り除いて抽出
    public List<String> getSaveNameList(){
        return Objects.requireNonNull(dataRepository.PreferenceParamList().getValue()).stream()
                .map(PreferenceParam::saveName)
                .distinct()
                .collect(Collectors.toList());
    }

}
