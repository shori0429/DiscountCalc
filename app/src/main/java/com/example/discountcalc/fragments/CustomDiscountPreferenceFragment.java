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
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.discountcalc.DAO.PreferenceParamDAO;
import com.example.discountcalc.customAdapters.CustomPreferenceListAdapter;
import com.example.discountcalc.dataBase.AppDataBase;
import com.example.discountcalc.dataBase.CustomConfigDataStoreSingleton;
import com.example.discountcalc.dataBase.DataStoreHelper;
import com.example.discountcalc.databinding.CustomDiscountPreferenceFragmentBinding;
import com.example.discountcalc.params.CustomPreferenceData;
import com.example.discountcalc.R;
import com.example.discountcalc.params.PreferenceParam;
import com.example.discountcalc.viewModels.CustomPreferenceListViewModel;
import com.example.discountcalc.viewModels.CustomPreferenceListViewModelFactory;

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

    private AppDataBase dataBase;

    // ビュー関係
    View view;
    TextView elementNumberViewText;
    Button elementAddButton;
    Button changeTextSize;

    Button saveButton;

    TextView saveTitle;
    int textSize=R.dimen.normal_size;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingElements();
        viewModelInitialize();
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
        customPreferenceViewModel =new ViewModelProvider(this, new CustomPreferenceListViewModelFactory(requireActivity().getApplication()))
                .get(CustomPreferenceListViewModel.class);
        elementNumberViewText.setText(""+customPreferenceViewModel.listSize());
    }

    private void updateUI() {
        Log.i("updateUI","updateUI");
        customPreferenceListAdapter.submitList(new ArrayList<>(customPreferenceViewModel.CustomPreferenceList().getValue()));
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
        customPreferenceListAdapter=new CustomPreferenceListAdapter();

        LinearLayoutManager llm=new LinearLayoutManager(view.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(customPreferenceListAdapter);
    }

    // データストアからカスタムの割引率設定に関するデータを取得
    private void loadCustomPreferenceList(){
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
        //dataStoreHelper.putIntegerValue(saveCountKey, customPreferenceViewModel.listSize());

        String title=saveTitle.getText().toString();
        if(title.equals("")){
            //TODO:入力無しは未入力ダイアログ出して保存しないほうがいいかも。
            title="Custom001";
        }
        Log.i("saveId","saveID : "+title);
        if(!customPreferenceViewModel.existingCheckDAO(title)){
            customPreferenceViewModel.saveDataBase(title);
            Toast.makeText(getContext(),title+"の名前で保存しました。",Toast.LENGTH_SHORT).show();
        }else{
            // TODO:上書き確認を表示するフラグメントを作成して、表示する処理を作成する。
            // はいで上書き、いいえでキャンセル
            Log.e("saveDataBase",title+"is ExistingSaveName. SaveCanceled.");
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
        //elementNumberViewText.setText("viewmodel:"+listSize+"adapter"+customPreferenceListAdapter.getItemCount());
        //customPreferenceListAdapter.notifyItemInserted(listSize-1);
        // 表示数が10未満の時、リサイクルビューのサイズ変更を固定にする。
        recyclerView.setHasFixedSize(listSize >= 10);

        elementNumberViewText.setText(listSize+"");
        updateUI();
    }

    private boolean removePreferenceDataElement(int removeElementNumber){
        customPreferenceViewModel.removePreferenceData(removeElementNumber);
        return true;
    }


}