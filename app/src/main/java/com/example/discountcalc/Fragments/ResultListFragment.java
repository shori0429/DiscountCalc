package com.example.discountcalc.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.discountcalc.CalculationPack.DiscountCalc;
import com.example.discountcalc.CustomAdapters.ResultLayoutAdapter;
import com.example.discountcalc.DataBase.CustomConfigDataStoreSingleton;
import com.example.discountcalc.DataBase.DataStoreHelper;
import com.example.discountcalc.Params.DiscountData;
import com.example.discountcalc.R;
import com.example.discountcalc.ViewModels.DiscountCalcViewModel;
import com.example.discountcalc.databinding.ResultPriceListBinding;

import java.util.ArrayList;


@SuppressWarnings("FieldCanBeLocal")
public class ResultListFragment extends Fragment {
    // データ保存に使うキー達

    // 設定ファイル名保存キー("xxx".preferences_pb)
    private static final String TAG_STORE_NAME = "custom_setting_data";
    //　割引率キー(x%)
    private static final String DISCOUNT_KEY = "discount_key";
    // 必要かわからん
    private static final String CONFIG_ENUM_KEY = "config_enum_key";
    // 表示数保存キー
    private static final String VIEWCOUNT = "view_count";

    // デフォルトの表示数
    private final int DEFAULT_VIEWCOUNT = 10;

    // 価格
    private int price;
    private View view;

    private RecyclerView recyclerView;
    private ResultLayoutAdapter resultLayoutAdapter;
    InputMethodManager inputMethodManager;

    DiscountCalcViewModel discountCalcViewModel;

    // データストア
    RxDataStore<Preferences> datastoreRX;
    // データストアのインスタンス取得用
    CustomConfigDataStoreSingleton dataStoreSingleton;

    // データストアヘルパー取得用
    private DataStoreHelper dataStoreHelper;

    private ResultPriceListBinding resultPriceListBinding;

    ArrayList<DiscountData> configDataList;

    ArrayList<Integer> discountPers;

    ArrayList<Integer> configEnums;

    int viewCount;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the ViewModel.
        Log.i("ResultListFragment","Called ViewModelProvider.get");
        discountCalcViewModel=new ViewModelProvider(requireActivity()).get(DiscountCalcViewModel.class);

        final Observer<Integer> priceObserver= integer -> {
            price=integer;
            calcDiscounts();
            resultLayoutAdapter.updateItem(configDataList);
        };
        discountCalcViewModel.getPrice().observe(getViewLifecycleOwner(),priceObserver);

        // データストアインスタンス取得
        getDataStoreInstance();
        // データストアヘルパー取得
        dataStoreHelperInitialize(dataStoreSingleton.getDatastore());

        //　表示数取得
        getViewCountData();
        // 保存データ取得
        loadDataStore();

        // 計算
        calcDiscounts();

        recyclerView = resultPriceListBinding.resultPriceList;
        resultLayoutAdapter= new ResultLayoutAdapter(configDataList);
        LinearLayoutManager llm = new LinearLayoutManager(resultPriceListBinding.getRoot().getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(resultLayoutAdapter);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        resultPriceListBinding = ResultPriceListBinding.inflate(inflater, container, false);
        view = resultPriceListBinding.getRoot();
        Log.i("resultFragment", getParentFragmentManager().toString());

        return view;
    }

    // 表示数取得
    private void getViewCountData() {
        int count = dataStoreHelper.getIntValue(VIEWCOUNT);
        // データが存在しない場合デフォルト値をviewCountとし、その値も保存する
        if (count == -1) {
            viewCount = DEFAULT_VIEWCOUNT;
            dataStoreHelper.putIntegerValue(VIEWCOUNT, viewCount);
        } else {
            viewCount = count;
            Log.i("viewCount", Integer.toString(count));
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(requireContext());
            viewCount=sharedPreferences.getInt("viewCount",-1);
            Log.i("viewCount", "Result-sharedPreferences:"+count);
        }

        // リスト初期化
        configDataList = new ArrayList<>();
        for(int i=0;i<viewCount;i++){
            configDataList.add(i,new DiscountData());
        }
    }


    // DataStore取得
    private void getDataStoreInstance() {
        dataStoreSingleton = CustomConfigDataStoreSingleton.getInstance();
        // データストアのSingletonが存在してなければ、新たにデータストアを作成
        if (dataStoreSingleton.getDatastore() == null) {
            datastoreRX = new RxPreferenceDataStoreBuilder(view.getContext(), TAG_STORE_NAME).build();
            createDiscountPreferenceData();
        } else {
            datastoreRX = dataStoreSingleton.getDatastore();
        }
        // Singletonのデータストアにセット
        dataStoreSingleton.setDataStore(datastoreRX);

    }

    // DataStoreのヘルパー取得
    private void dataStoreHelperInitialize(RxDataStore<Preferences> dataStore) {
        if (dataStoreHelper == null)
            dataStoreHelper = new DataStoreHelper(this.getParentFragment(), dataStore);
    }

    // DataStoreに割引データの設定を保存
    private void saveDataStore() {
        for(int i=0;i<viewCount;i++){

            dataStoreHelper.putIntegerValue(DISCOUNT_KEY + i, discountPers.get(i));
        }
    }

    // DataStoreから割引データの設定取得
    private void loadDataStore() {
        dataStoreHelperInitialize(dataStoreSingleton.getDatastore());
        discountPers=new ArrayList<>();
        configEnums=new ArrayList<>();
        for (int i = 0; i < viewCount; i++) {
            String PREF_KEY = DISCOUNT_KEY + i;
            //ロード処理
            discountPers.add(i,dataStoreHelper.getIntValue(PREF_KEY));
            configEnums.add(i,dataStoreHelper.getIntValue(CONFIG_ENUM_KEY + i));
        }
    }

    // 計算処理
    private void calcDiscounts(){
        for (int i = 0; i < viewCount; i++) {
            int discountPer = discountPers.get(i);
            int discountPrice = DiscountCalc.discountCalculationIntPercentage(price, discountPer);
            int afterPrice = price - discountPrice;
            //int enumKey = configEnums.get(i);
            DiscountData data=new DiscountData(discountPer,discountPrice,afterPrice,0);
            configDataList.set(i,data);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 一時中断で保存しておく
        saveDataStore();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Fragmentが削除された際に保存しておく
        saveDataStore();
    }

    private void createDiscountPreferenceData(){
        int[] discountData=getResources().getIntArray(R.array.PresetDiscounts);
        if(discountPers==null) {
            discountPers = new ArrayList<>();
        }
        for(int i=0;i<discountData.length;i++){
            discountPers.add(i,discountData[i]);
        }
    }

    private void setClickListener(View view) {
        view.setOnClickListener(v -> {
            // キーボードを隠す
            view.setOnClickListener(c -> {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            });
        });
    }

}