package com.example.discountcalc.Fragments;

import static com.example.discountcalc.DataBase.DataStoreKey.*;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.discountcalc.CustomAdapters.CustomPreferenceAdapter;
import com.example.discountcalc.DataBase.CustomConfigDataStoreSingleton;
import com.example.discountcalc.DataBase.DataStoreHelper;
import com.example.discountcalc.Params.CustomPreferenceData;
import com.example.discountcalc.databinding.CustomDiscountPreferenceFragmentBinding;

import java.util.ArrayList;

public class CustomDiscountPreferenceFragment extends Fragment {
    final String saveCountKey =DISCOUNT_TYPE_CUSTOM_KEY+DISCOUNT_CUSTOM_SAVE_COUNT_KEY;
    final String saveDiscountKey=DISCOUNT_TYPE_CUSTOM_KEY+DISCOUNT_KEY;

    final String saveDiscountElementKey=DISCOUNT_TYPE_CUSTOM_KEY+DISCOUNT_ELEMENT_KEY;

    CustomDiscountPreferenceFragmentBinding customDiscountPreferenceFragmentBinding;

    RecyclerView recyclerView;
    CustomPreferenceAdapter customPreferenceAdapter;

    //
    ArrayList<CustomPreferenceData> preferenceDataSet;

    int dataMaxSize;

    // データストアヘルパー
    private DataStoreHelper dataStoreHelper;
    // データストアシングルトン
    private CustomConfigDataStoreSingleton dataStoreSingleton;

    View view;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // データストアインスタンス取得
        getDataStoreInstance();
        // データストアヘルパー取得
        dataStoreHelperInitialize(dataStoreSingleton.getDatastore());
        recyclerViewInitialize();
    }

    private void recyclerViewInitialize() {
        recyclerView= customDiscountPreferenceFragmentBinding.PreferenceList;
        customPreferenceAdapter=new CustomPreferenceAdapter(preferenceDataSet);
        LinearLayoutManager llm=new LinearLayoutManager(view.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(customPreferenceAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        customDiscountPreferenceFragmentBinding =CustomDiscountPreferenceFragmentBinding.inflate(inflater,container,false);
        view= customDiscountPreferenceFragmentBinding.getRoot();

        return view;
    }

    // データストアからカスタム割引率設定に関するデータを取得
    private void getCustomPreferenceDatas(){
        final String discountKey=DISCOUNT_CUSTOM_SAVE_COUNT_KEY+DISCOUNT_KEY;
        getCustomDiscountSaveCount();
    }

    // カスタム割引率の要素数に関するデータ取得
    private void getCustomDiscountSaveCount() {
        dataMaxSize = dataStoreHelper.getIntValue(saveCountKey);
        if(dataMaxSize<=-1){
          dataMaxSize=0;
        }

        preferenceDataSet=new ArrayList<>();
        for (int i = 0; i < dataMaxSize; i++) {
            preferenceDataSet.set(i,new CustomPreferenceData());
        }
    }

    private boolean saveDataStore(){
        for(int i=0;i<dataMaxSize;i++) {
            dataStoreHelper.putIntegerValue(saveDiscountKey+i,preferenceDataSet.get(i).getDiscountPer());
            dataStoreHelper.putIntegerValue(saveDiscountElementKey+i,preferenceDataSet.get(i).getDiscountElementName());
        }

    }

    private void dataStoreHelperInitialize(RxDataStore<Preferences> dataStore){
        if(dataStoreHelper==null){
            dataStoreHelper=new DataStoreHelper(this.getParentFragment(),dataStore);
        }
    }

    private void getDataStoreInstance(){
        dataStoreSingleton=CustomConfigDataStoreSingleton.getInstance();
    }

}