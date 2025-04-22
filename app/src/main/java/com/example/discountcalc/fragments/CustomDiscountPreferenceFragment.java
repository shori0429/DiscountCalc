package com.example.discountcalc.fragments;

import static com.example.discountcalc.dataBase.DataStoreKey.*;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.discountcalc.customAdapters.CustomPreferenceListAdapter;
import com.example.discountcalc.dataBase.CustomConfigDataStoreSingleton;
import com.example.discountcalc.dataBase.DataStoreHelper;
import com.example.discountcalc.databinding.CustomDiscountPreferenceFragmentBinding;
import com.example.discountcalc.params.CustomPreferenceData;
import com.example.discountcalc.R;
import com.example.discountcalc.viewModels.CustomPreferenceListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomDiscountPreferenceFragment extends Fragment {

    // 要素数保存用のキー
    final String saveCountKey =DISCOUNT_TYPE_CUSTOM_KEY+DISCOUNT_CUSTOM_SAVE_COUNT_KEY;

    // 割引率保存用のキー
    final String saveDiscountPerKey =DISCOUNT_TYPE_CUSTOM_KEY+DISCOUNT_KEY;

    // 保存する設定名のキー
    final String saveDiscountId=DISCOUNT_TYPE_CUSTOM_KEY+DISCOUNT_CUSTOM_SAVE_NAME;

    CustomDiscountPreferenceFragmentBinding customDiscountPreferenceFragmentBinding;

    // リスト表示に使用するデータ
    RecyclerView recyclerView;
    CustomPreferenceListAdapter customPreferenceListAdapter;

    CustomPreferenceListViewModel customPreferenceViewModel;

    int elementMax;

    // データストア関連
    private DataStoreHelper dataStoreHelper;
    private CustomConfigDataStoreSingleton dataStoreSingleton;

    // ビュー関係
    View view;
    TextView elementNumberViewText;
    Button elementAddButton;
    Button changeTextSize;

    Button saveButton;

    TextView saveTitle;
    int textSize;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingElements();
        // データストアインスタンス取得
        getDataStoreInstance();
        // データストアヘルパー取得
        dataStoreHelperInitialize(dataStoreSingleton.getDatastore());

        viewModelInitialize();
        loadCustomPreferenceList();
        recyclerViewInitialize();
        setOnClickListeners();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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

    // Viewの要素をバインディング
    private void bindingElements() {
        elementNumberViewText=customDiscountPreferenceFragmentBinding.PreferenceVolume;
        elementAddButton=customDiscountPreferenceFragmentBinding.AddElementButton;
        changeTextSize=customDiscountPreferenceFragmentBinding.ChangeTextSizeButton;
        saveButton=customDiscountPreferenceFragmentBinding.SaveButton;
        saveTitle=customDiscountPreferenceFragmentBinding.saveTitle;
    }
    private void viewModelInitialize() {
        customPreferenceViewModel =new ViewModelProvider(requireActivity()).get(CustomPreferenceListViewModel.class);
        //customPreferenceViewModel.setPreferenceDataList();
        customPreferenceViewModel.CustomPreferenceList().observe(getViewLifecycleOwner(), this::updateUI);
    }

    private void updateUI(@NonNull List<CustomPreferenceData> dataList) {
        Log.i("updateUI","updateUI");
        customPreferenceListAdapter.submitList(dataList);
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
                customPreferenceListAdapter.setTextSizes(normal);
                textSize=normal;

            }else if(textSize==normal){
                elementNumberViewText.setTextSize(large);
                customPreferenceListAdapter.setTextSizes(large);
                textSize=large;
            }else if(textSize==large){
                elementNumberViewText.setTextSize(small);
                customPreferenceListAdapter.setTextSizes(small);
                textSize=small;
            }
        });

        saveButton.setOnClickListener(b->{
            saveDataStore();
        });

    }

    // リサイクルビューの初期化関数
    private void recyclerViewInitialize() {
        recyclerView= customDiscountPreferenceFragmentBinding.PreferenceList;
        customPreferenceListAdapter=new CustomPreferenceListAdapter(customPreferenceViewModel,this);

        LinearLayoutManager llm=new LinearLayoutManager(view.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(customPreferenceListAdapter);
    }

    // データストアからカスタムの割引率設定に関するデータを取得
    private void loadCustomPreferenceList(){
       int elementMax= loadCustomDiscountElementMax();

        ArrayList<CustomPreferenceData> dataList=new ArrayList<>();
        //MUST CHECK  なんか実装として変。要修正
        for(int i=0;i<elementMax;i++){
            int per=dataStoreHelper.getIntValue(saveDiscountPerKey +i);
            //int element=dataStoreHelper.getIntValue(saveDiscountElementKey+i);
            customPreferenceViewModel.addPreferenceData(per);
            //preferenceDataList.set(i,new CustomPreferenceData(element,per));
        }
        // viewModel更新
        //customPreferenceViewModel.updatePreferenceDataAll(dataList);

        String lSize=String.valueOf(customPreferenceViewModel.listSize());
        elementNumberViewText.setText(lSize);

    }

    // カスタム割引率の要素数に関するデータ取得
    private int loadCustomDiscountElementMax() {
        elementMax = dataStoreHelper.getIntValue(saveCountKey);
        Log.i("loadElementCount","elementCount : "+elementMax);

        if(elementMax <=-1){
          elementMax =10;
        }
        // 仮データ
//        elementMax=1;
        // データリスト配列初期化
        //InitializeCustomPreferenceDataSetArrayList(elementMax);
        return elementMax;
    }

    private void InitializeCustomPreferenceDataSetArrayList(int max) {
        // 要素数分初期化
        //preferenceDataList = new ArrayList<>();
        //for (int i = 0; i < max; i++) {
        //    preferenceDataList.add(i, new CustomPreferenceData());
        //}
    }
    // 仮データ作成
    private void provisionalPreferenceDataListInit(){
        for (int i = 0; i < 10; i++) {
            //preferenceDataList.set(i,new CustomPreferenceData(i,i*5));
        }
    }

    // データストアに保存
    private boolean saveDataStore(){
        // 現在の要素数の保存
        dataStoreHelper.putIntegerValue(saveCountKey, customPreferenceViewModel.listSize());

        String title=saveTitle.getText().toString();
        if(title.equals("")){
            title="Custom001";
        }
        // 現在のプリセット名を保存
        dataStoreHelper.putStringValue(saveDiscountId,title);

        // TODO クラスごと保存できるようにしたい。Protobufを使ったデータ処理を実装できれば良
        // 要素内の各データ保存
        for(int i = 0; i< customPreferenceViewModel.listSize(); i++) {
            // 入力されている割引率を保存
            int per=customPreferenceViewModel.getCustomPreferenceData(i).DiscountPer();
            dataStoreHelper.putIntegerValue(saveDiscountPerKey +i, per);
        }
        return true;
    }

    // 要素数追加
    private void addPreferenceDataElement(){
        // コンストラクタで実体生成→List.size()の順で呼ばれる
        //CustomPreferenceData newData=new CustomPreferenceData(customPreferenceViewModel.listSize(),0);
        //customPreferenceViewModel.addPreferenceData(newData);

        customPreferenceViewModel.addDefaultPreferenceData();
        // テキスト更新
        int listSize= customPreferenceViewModel.listSize();
        elementNumberViewText.setText(String.valueOf(listSize));
        // 表示数が10未満の時、リサイクルビューのサイズ変更を固定にする。
        recyclerView.setHasFixedSize(listSize > 10);

    }

    private boolean removePreferenceDataElement(int removeElementNumber){
        customPreferenceViewModel.removePreferenceData(removeElementNumber);
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