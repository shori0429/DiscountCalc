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
import android.widget.Button;
import android.widget.TextView;

import com.example.discountcalc.CustomAdapters.CustomPreferenceAdapter;
import com.example.discountcalc.DataBase.CustomConfigDataStoreSingleton;
import com.example.discountcalc.DataBase.DataStoreHelper;
import com.example.discountcalc.Params.CustomPreferenceData;
import com.example.discountcalc.databinding.CustomDiscountPreferenceFragmentBinding;

import java.util.ArrayList;

public class CustomDiscountPreferenceFragment extends Fragment {

    // 要素数保存用のキー
    final String saveCountKey =DISCOUNT_TYPE_CUSTOM_KEY+DISCOUNT_CUSTOM_SAVE_COUNT_KEY;

    // 割引率保存用のキー
    final String saveDiscountKey=DISCOUNT_TYPE_CUSTOM_KEY+DISCOUNT_KEY;

    // 何番目の要素かを保存するキー
    final String saveDiscountElementKey=DISCOUNT_TYPE_CUSTOM_KEY+DISCOUNT_ELEMENT_KEY;

    CustomDiscountPreferenceFragmentBinding customDiscountPreferenceFragmentBinding;

    // リスト表示に使用するデータ
    RecyclerView recyclerView;
    CustomPreferenceAdapter customPreferenceAdapter;

    ArrayList<CustomPreferenceData> preferenceDataSet;


    int elementMax;



    // データストア関連
    private DataStoreHelper dataStoreHelper;
    private CustomConfigDataStoreSingleton dataStoreSingleton;

    // ビュー関係
    View view;
    TextView elementNumberViewText;
    Button elementAddButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingElements();
        // データストアインスタンス取得
        getDataStoreInstance();
        // データストアヘルパー取得
        dataStoreHelperInitialize(dataStoreSingleton.getDatastore());
        loadCustomPreferenceDatas();
        recyclerViewInitialize();
        setOnClickListeners();
    }

    // Viewの要素をバインディング
    private void bindingElements() {
        elementNumberViewText=customDiscountPreferenceFragmentBinding.PreferenceVolume;
        elementAddButton=customDiscountPreferenceFragmentBinding.AddElementButton;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        customDiscountPreferenceFragmentBinding =CustomDiscountPreferenceFragmentBinding.inflate(inflater,container,false);
        view= customDiscountPreferenceFragmentBinding.getRoot();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveDataStore();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveDataStore();
    }

    public void setOnClickListeners(){
        elementAddButton.setOnClickListener(b->{
            addPreferenceDataElement();
        });
    }

    // リサイクルビューの初期化関数
    private void recyclerViewInitialize() {
        recyclerView= customDiscountPreferenceFragmentBinding.PreferenceList;
        customPreferenceAdapter=new CustomPreferenceAdapter(preferenceDataSet);
        LinearLayoutManager llm=new LinearLayoutManager(view.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(customPreferenceAdapter);
    }

    // データストアからカスタムの割引率設定に関するデータを取得
    private void loadCustomPreferenceDatas(){
        loadCustomDiscountSaveCount();
    }

    // カスタム割引率の要素数に関するデータ取得
    private void loadCustomDiscountSaveCount() {
        elementMax = dataStoreHelper.getIntValue(saveCountKey);

        if(elementMax <=-1){
          elementMax =0;
        }

        // 要素数分初期化
        preferenceDataSet=new ArrayList<>();
        for (int i = 0; i < elementMax; i++) {
            preferenceDataSet.add(i,new CustomPreferenceData());
        }
        String lSize=String.valueOf(preferenceDataSet.size());
        elementNumberViewText.setText(lSize);
    }

    // データストアに保存
    private boolean saveDataStore(){
        // 要素数の保存
        dataStoreHelper.putIntegerValue(saveCountKey, preferenceDataSet.size());

        // TODO クラスごと保存できるようにしたい。Protobufを使ったデータ処理を実装できれば良
        // 要素内の各データ保存
        for(int i = 0; i< preferenceDataSet.size(); i++) {
            dataStoreHelper.putIntegerValue(saveDiscountKey+i,preferenceDataSet.get(i).getDiscountPer());
            dataStoreHelper.putIntegerValue(saveDiscountElementKey+i,preferenceDataSet.get(i).getDiscountElementName());
        }
        return true;
    }

    // 要素数追加
    private void addPreferenceDataElement(){
        preferenceDataSet.add(new CustomPreferenceData());
        String lSize=String.valueOf(preferenceDataSet.size());
        elementNumberViewText.setText(lSize);
    }

    private boolean removePreferenceDataElement(int removeElementNumber){
        preferenceDataSet.remove(removeElementNumber);
        return true;
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