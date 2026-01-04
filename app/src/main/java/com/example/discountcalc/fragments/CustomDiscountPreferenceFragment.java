package com.example.discountcalc.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.discountcalc.customAdapters.CustomPreferenceListAdapter;
import com.example.discountcalc.customAdapters.SaveDataListViewAdapter;
import com.example.discountcalc.dataBase.AppDataBase;
import com.example.discountcalc.databinding.CustomDiscountPreferenceFragmentBinding;
import com.example.discountcalc.R;
import com.example.discountcalc.params.PreferenceParam;
import com.example.discountcalc.viewModels.SaveTitleViewOneLineParamViewModel;
import com.example.discountcalc.viewModels.CustomPreferenceListViewModel;
import com.example.discountcalc.viewModels.CustomPreferenceListViewModelFactory;
import com.example.discountcalc.viewModels.SaveTitleViewOneLineParamViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomDiscountPreferenceFragment extends Fragment {

    CustomDiscountPreferenceFragmentBinding customDiscountPreferenceFragmentBinding;

    SharedPreferences sharedPreferences;

    // 設定データ表示に使用するデータ
    RecyclerView customPreferenceListView;
    CustomPreferenceListAdapter customPreferenceListAdapter;
    CustomPreferenceListViewModel customPreferenceViewModel;

    SaveTitleViewOneLineParamViewModel saveTitleViewOneLineParamViewModel;

    // データベースに保存されている設定名
    RecyclerView saveDataListView;
    SaveDataListViewAdapter saveDataListViewAdapter;

    int elementMax;

    private AppDataBase dataBase;

    // adapterが持つ、押された読込ボタンの名前情報を購読するフィールド
    private MutableLiveData<String> useSaveDataNameLiveData;

    // ビュー関係
    View view;
    TextView elementNumberViewText;
    Button elementAddButton;
    Button changeTextSize;

    Button saveButton;

    Button clearButton;

    Button allClearButton;

    TextView saveTitle;
    int textSize=R.dimen.normal_size;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        useSaveDataNameLiveData=new MutableLiveData<>();
        useSaveDataNameLiveData.observe(getViewLifecycleOwner(),useName->{
            List<PreferenceParam> dataList=loadCustomPreferenceList(useName);
            if(dataList.size()==0) {
                // ロード先が存在しなければ1個の空要素だけを作成。
                dataList.add(new PreferenceParam(0,Math.incrementExact(dataList.size()),0,""));
            }
            saveTitle.setText(useName);
            // 現在リストの更新
            customPreferenceViewModel.commitPreferenceParamList(useName);
        });
        bindingElements();
        viewModelInitialize();
        recyclerViewInitialize();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        customDiscountPreferenceFragmentBinding =CustomDiscountPreferenceFragmentBinding.inflate(inflater,container,false);
        view= customDiscountPreferenceFragmentBinding.getRoot();
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(view.getContext());

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
        clearButton=customDiscountPreferenceFragmentBinding.clearElementButton;
        allClearButton=customDiscountPreferenceFragmentBinding.allClearElementButton;
        saveTitle=customDiscountPreferenceFragmentBinding.saveTitle;
    }
    private void viewModelInitialize() {
        customPreferenceViewModel =new ViewModelProvider(this, new CustomPreferenceListViewModelFactory(requireActivity().getApplication()))
                .get(CustomPreferenceListViewModel.class);
        saveTitleViewOneLineParamViewModel=new ViewModelProvider(this,new SaveTitleViewOneLineParamViewModelFactory(requireActivity().getApplication()))
                .get(SaveTitleViewOneLineParamViewModel.class);

        // 全リストの初回変更通知時に一度だけ処理をする。
        // observe内で購読を解除するためにObserverをnewしている。(ラムダだとthisがFragmentになる)
        customPreferenceViewModel.AllPreferenceParamList().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(List<PreferenceParam> allParams) {
                if (allParams.size() > 0) {
                    customPreferenceViewModel.usePreferenceParamList(useSaveDataNameLiveData.getValue());
                    useSaveDataNameLiveData.setValue(sharedPreferences.getString(getString(R.string.using_custom_preference), null));
                }
                if (allParams.size() == 0) {
                    customPreferenceViewModel.usePreferenceParamList("");
                }
                // 購読を解除。
                customPreferenceViewModel.AllPreferenceParamList().removeObserver(this);
            }
        });

        // ViewModel内の現在リストの更新を購読。
        customPreferenceViewModel.PreferenceParamList().observe(getViewLifecycleOwner(),preferenceParams -> {
            updateUI(preferenceParams);
            setOnClickListeners();
        });

        // ViewModel内の全リストの更新を購読
        customPreferenceViewModel.AllPreferenceParamList().observe(getViewLifecycleOwner(),allParams->{
            updateUI(customPreferenceViewModel.PreferenceParamList().getValue());
        });
    }

    private void updateUI(List<PreferenceParam> params) {
        Log.i("updateUI","updateUI");

        customPreferenceListAdapter.submitList(new ArrayList<>(params));
        customPreferenceListView.setHasFixedSize(customPreferenceListAdapter.getItemCount() >= 10);
        elementNumberViewText.setText("" + params.size());
        saveDataListViewAdapter.submitList(new ArrayList<>(customPreferenceViewModel.getSaveNameList()));
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
            b.setEnabled(false);
            saveCustomPreferenceDataToRepository();
            saveDataListViewAdapter.submitList(new ArrayList<>(customPreferenceViewModel.getSaveNameList()));
            // 1秒後にボタン再使用可能に
            Handler handler=new Handler();
            handler.postDelayed(() ->{
                Log.d("button","recasting");
                b.setEnabled(true);
            },1000);
            Log.i("button","available");
        });

        // 一番下の要素を削除
        clearButton.setOnClickListener(b->{
            b.setEnabled(false);
            customPreferenceViewModel.removePreferenceLastData();
            Handler handler=new Handler();
            handler.postDelayed(()->{
                b.setEnabled(true);
            },100);

        });

        allClearButton.setOnClickListener(b->{
            b.setEnabled(false);
            customPreferenceViewModel.allRemovePreferenceData();
            saveTitle.setText("");
            Handler handler=new Handler();
            handler.postDelayed(()->{
                b.setEnabled(true);
            },100);
        });

    }

    // リサイクルビューの初期化関数
    private void recyclerViewInitialize() {
        customPreferenceListView = customDiscountPreferenceFragmentBinding.PreferenceList;
        customPreferenceListAdapter=new CustomPreferenceListAdapter(customPreferenceViewModel);

        LinearLayoutManager llm=new LinearLayoutManager(view.getContext());
        customPreferenceListView.setLayoutManager(llm);
        customPreferenceListView.setAdapter(customPreferenceListAdapter);


        saveDataListView=customDiscountPreferenceFragmentBinding.saveDataList;
        saveDataListViewAdapter=new SaveDataListViewAdapter(customPreferenceViewModel);
        LinearLayoutManager llm2=new LinearLayoutManager(view.getContext());
        saveDataListView.setLayoutManager(llm2);
        saveDataListView.setAdapter(saveDataListViewAdapter);
        saveDataListViewAdapter.PushPosition().observe(getViewLifecycleOwner(),v->{
            // できれば保存前のデータが削除されることを確認するダイアログを出したい。
            if(customPreferenceViewModel.existingCheckDAO(v)){
                useSaveDataNameLiveData.setValue(v);
                saveTitle.setText(v);
            }
        });
    }

    // 指定された名前の保存されているカスタムの割引率設定に関するデータを取得
    private List<PreferenceParam> loadCustomPreferenceList(String name) {
        if(name==null)return new ArrayList<>();
        List<PreferenceParam> dataList = new ArrayList<>();

        //TODO viewModelのメソッド使用に変更する
        // 引数と一致する保存名のデータをセット
        if (customPreferenceViewModel.existingCheckDAO(name)) {
            List<PreferenceParam> params = customPreferenceViewModel.AllPreferenceParamList().getValue();
            for(int i=0;i<params.size();i++){
                if(Objects.equals(params.get(i).saveName(), name)){
                    // orderIndexの順に並び替える
                    dataList.add(new PreferenceParam(params.get(i).uid(), params.get(i).orderIndex(),params.get(i).per(), params.get(i).saveName()));
                }
            }
        }
        return dataList;
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

    // 設定データをリポジトリに保存
    private boolean saveCustomPreferenceDataToRepository(){
        // 現在の要素数の保存
        //dataStoreHelper.putIntegerValue(saveCountKey, customPreferenceViewModel.listSize());

        String title=saveTitle.getText().toString();
        if(title.equals("")){
            //TODO:入力無しは未入力ダイアログ出して保存しないほうがいいかも。
            Toast.makeText(getContext(),"未入力",Toast.LENGTH_SHORT).show();
            return false;
        }
        Log.i("saveId","saveID : "+title);
        if(!customPreferenceViewModel.existingCheckDAO(title)){
            if(customPreferenceViewModel.saveNewData(title)) {
                Toast.makeText(getContext(), title + "の名前で保存しました。", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "保存できませんでした。", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), "その名前は既に使用されています。", Toast.LENGTH_SHORT).show();
            // TODO:上書き確認を表示するフラグメントを作成して、表示する処理を作成する。
            // はいで上書き、いいえでキャンセル
            Log.e("saveDataBase",title+" is ExistingSaveName. SaveCanceled.");
        }

       return true;
    }

    // 要素数追加
    private void addPreferenceDataElement(){
        // コンストラクタで実体生成→List.size()の順で呼ばれる
        //CustomPreferenceData newData=new CustomPreferenceData(customPreferenceViewModel.listSize(),0);
        //customPreferenceViewModel.addPreferenceData(newData);

        customPreferenceViewModel.addDefaultPreferenceData();
        // 表示数が10未満の時、リサイクルビューのサイズ変更を固定にする。
        customPreferenceListView.setHasFixedSize(customPreferenceViewModel.preferenceParamListSize() >= 10);
    }


}