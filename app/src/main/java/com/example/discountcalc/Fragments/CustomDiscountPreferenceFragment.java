package com.example.discountcalc.Fragments;

import static com.example.discountcalc.DataBase.DataStoreKey.*;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.example.discountcalc.R;
import com.example.discountcalc.ViewModels.CustomPreferenceListViewModel;
import com.example.discountcalc.ViewModels.DiscountCalcViewModel;
import com.example.discountcalc.databinding.CustomDiscountPreferenceFragmentBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomDiscountPreferenceFragment extends Fragment {

    // 要素数保存用のキー
    final String saveCountKey =DISCOUNT_TYPE_CUSTOM_KEY+DISCOUNT_CUSTOM_SAVE_COUNT_KEY;

    // 割引率保存用のキー
    final String saveDiscountPerKey =DISCOUNT_TYPE_CUSTOM_KEY+DISCOUNT_KEY;

    // 何番目の要素かを保存するキー
    final String saveDiscountElementKey=DISCOUNT_TYPE_CUSTOM_KEY+DISCOUNT_ELEMENT_KEY;

    CustomDiscountPreferenceFragmentBinding customDiscountPreferenceFragmentBinding;

    // リスト表示に使用するデータ
    RecyclerView recyclerView;
    CustomPreferenceAdapter customPreferenceAdapter;

    ArrayList<CustomPreferenceData> preferenceDataList;
    CustomPreferenceListViewModel customPreferenceListViewModel;

    int elementMax;

    DiscountCalcViewModel calcViewmodel;

    // データストア関連
    private DataStoreHelper dataStoreHelper;
    private CustomConfigDataStoreSingleton dataStoreSingleton;

    // ビュー関係
    View view;
    TextView elementNumberViewText;
    Button elementAddButton;
    Button changeTextSize;
    int textSize;

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
        changeTextSize=customDiscountPreferenceFragmentBinding.ChangeTextSizeButton;

        customPreferenceListViewModel=CustomPreferenceListViewModel.getInstance();
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
        //saveDataStore();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //saveDataStore();
    }

    public void setOnClickListeners(){
        elementAddButton.setOnClickListener(b->{
            addPreferenceDataElement();
        });
        changeTextSize.setOnClickListener(b->{
            // TODO:文字サイズ変更は仮実装なのでちゃんとまとめたり整理する
            if(textSize==0){
                textSize=(int)elementNumberViewText.getTextSize();
            }
            int small=(int)getResources().getDimension(R.dimen.small_size);
            int normal=(int)getResources().getDimension(R.dimen.normal_size);
            int large=(int)getResources().getDimension(R.dimen.large_size);
            if(textSize==small){
                elementNumberViewText.setTextSize(normal);
                customPreferenceAdapter.setTextSizes(normal);
                textSize=normal;

            }else if(textSize==normal){
                elementNumberViewText.setTextSize(large);
                customPreferenceAdapter.setTextSizes(large);
                textSize=large;
            }else if(textSize==large){
                elementNumberViewText.setTextSize(small);
                customPreferenceAdapter.setTextSizes(small);
                textSize=small;
            }
        });

    }

    // リサイクルビューの初期化関数
    private void recyclerViewInitialize() {
        recyclerView= customDiscountPreferenceFragmentBinding.PreferenceList;
        //customPreferenceAdapter=new CustomPreferenceAdapter(customPreferenceListViewModel);
        customPreferenceAdapter=new CustomPreferenceAdapter(customPreferenceListViewModel,this);


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
        // データリスト配列初期化
        InitializeCustomPreferenceDataSetArrayList(elementMax);

        for(int i=0;i<elementMax;i++){
            int per=dataStoreHelper.getIntValue(saveDiscountPerKey +i);
            int element=dataStoreHelper.getIntValue(saveDiscountElementKey+i);
            preferenceDataList.set(i,new CustomPreferenceData(element,per));
        }

        String lSize=String.valueOf(preferenceDataList.size());
        elementNumberViewText.setText(lSize);
    }

    private void InitializeCustomPreferenceDataSetArrayList(int max) {
        // 要素数分初期化
        preferenceDataList =new ArrayList<>();
        for (int i = 0; i < max; i++) {
            preferenceDataList.add(i,new CustomPreferenceData());
        }
        // ViewModelにデータ送信
        customPreferenceListViewModel.postValue(preferenceDataList);

    }

    // データストアに保存
    private boolean saveDataStore(){
        // 要素数の保存
        dataStoreHelper.putIntegerValue(saveCountKey, preferenceDataList.size());

        // TODO クラスごと保存できるようにしたい。Protobufを使ったデータ処理を実装できれば良
        // 要素内の各データ保存
        for(int i = 0; i< preferenceDataList.size(); i++) {
            int per=Optional.ofNullable(preferenceDataList.get(i).getDiscountPer().getValue()).orElse(0);
            dataStoreHelper.putIntegerValue(saveDiscountPerKey +i, per);
            // 今のリスト要素番号をセット
//            preferenceDataList.get(i).setDiscountElement(i);
//            dataStoreHelper.putIntegerValue(saveDiscountElementKey+i, preferenceDataList.get(i).getDiscountElement());
        }
        return true;
    }

    // 要素数追加
    private void addPreferenceDataElement(){

        // コンストラクタで実体生成→List.size()の順で呼ばれる
        preferenceDataList.add(new CustomPreferenceData( preferenceDataList.size(),0));

        // テキスト更新
        String lSize=String.valueOf(preferenceDataList.size());
        elementNumberViewText.setText(lSize);

        // 表示数が10未満の時、リサイクルビューのサイズ変更を許容する。
        if(preferenceDataList.size()<=10){
            recyclerView.setHasFixedSize(false);
        }
        //customPreferenceAdapter.updateItems(customPreferenceListViewModel);
        customPreferenceListViewModel.postValue(preferenceDataList);
        recyclerView.setHasFixedSize(true);
    }

    private boolean removePreferenceDataElement(int removeElementNumber){
        preferenceDataList.remove(removeElementNumber);
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