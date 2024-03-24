package com.example.discountcalc.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.discountcalc.CalculationPack.DiscountCalc;
import com.example.discountcalc.CustomAdapters.ResultLayoutAdapter;
import com.example.discountcalc.DataBase.CustomConfigDataStoreSingleton;
import com.example.discountcalc.DataBase.DataStoreHelper;
import com.example.discountcalc.Params.DiscountData;
import com.example.discountcalc.databinding.ResultPriceListBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


class PriceViewModel extends ViewModel {
    private MutableLiveData<TextView> priceText;

    public LiveData<TextView> getPriceText() {
        return priceText;
    }
}
@SuppressWarnings("FieldCanBeLocal")
public class ResultListFragment extends Fragment {
    // データ保存に使うキー達
    private static final String TAG_STORE_NAME = "custom_setting_data";
    private static final String DISCOUNT_KEY = "discount_key";
    private static final String CONFIG_ENUM_KEY = "config_enum_key";
    private static final String VIEWCOUNT = "view_count";

    // デフォルトの表示数
    private final int DEFAULT_VIEWCOUNT = 10;

    // 価格
    private int price;
    private View view;
    InputMethodManager inputMethodManager;

    // データストア
    RxDataStore<Preferences> datastoreRX;
    // データストアのインスタンス取得用
    CustomConfigDataStoreSingleton dataStoreSingleton;

    // データストアヘルパー取得用
    private DataStoreHelper dataStoreHelper;

    private ResultPriceListBinding resultPriceListBinding;

    ArrayList<DiscountData> configDataList;

    int viewCount;

    public ResultListFragment() {
        // Required empty public constructor

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        inputMethodManager=(InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        setClickListener(view);

        RecyclerView rv = resultPriceListBinding.resultPriceList;

        //ResultLayoutAdapter adapter = new ResultLayoutAdapter(this.configDataList);
        ResultLayoutAdapter adapter = new ResultLayoutAdapter(configDataList);
        LinearLayoutManager llm = new LinearLayoutManager(resultPriceListBinding.getRoot().getContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        resultPriceListBinding = ResultPriceListBinding.inflate(inflater, container, false);
        view = resultPriceListBinding.getRoot();
        Log.i("resultFragment", getParentFragmentManager().toString());

        // リスト初期化
        configDataList = new ArrayList<>();

        // データストアインスタンス取得
        getDataStoreInstance();
        // データストアヘルパー取得
        dataStoreHelperInitialize(dataStoreSingleton.getDatastore());

        //　表示数取得
        getViewCountData();
        // 保存データ取得
        loadDataStore();


//        // Get the ViewModel.
//        priceViewModel=new ViewModelProvider(this).get(PriceViewModel.class);
//        // Create the observer which updates the UI.
//        final Observer<TextView> priceObserver= textView -> price=Integer.parseInt(textView.getText().toString());
//        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
//        priceViewModel.getPriceText().observe(getViewLifecycleOwner(),priceObserver);

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
            SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(requireContext());
            viewCount=sharedPreferences.getInt("viewCount",-1);
            Log.i("viewCount", "Result-sharedPreferences:"+count);
        }
    }


    // DataStore取得
    private void getDataStoreInstance() {
        dataStoreSingleton = CustomConfigDataStoreSingleton.getInstance();
        // データストアのSingletonが存在してなければ、新たにデータストアを作成
        if (dataStoreSingleton.getDatastore() == null) {
            datastoreRX = new RxPreferenceDataStoreBuilder(view.getContext(), TAG_STORE_NAME + ".pb").build();
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
        final AtomicInteger number = new AtomicInteger(0);
        configDataList.forEach(d ->
        {
            dataStoreHelper.putIntegerValue(DISCOUNT_KEY + number, d.getDiscount());
            dataStoreHelper.putIntegerValue(CONFIG_ENUM_KEY + number, d.getConfigEnum());
            number.incrementAndGet();
        });
    }

    // DataStoreから割引データの設定取得
    private void loadDataStore() {
        dataStoreHelperInitialize(dataStoreSingleton.getDatastore());
        for (int i = 0; i < viewCount; i++) {
            //ロード処理
            String PREF_KEY = DISCOUNT_KEY + i;
            int discount = dataStoreHelper.getIntValue(PREF_KEY);
            int discountPrice = DiscountCalc.discountCalculationInTax(price, discount);
            int postPrice = discount - discountPrice;
            int enumKey = dataStoreHelper.getIntValue(CONFIG_ENUM_KEY + i);
            DiscountData data = new DiscountData(discount, discountPrice, postPrice, enumKey);
            configDataList.add(data);
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
        saveDataStore();
    }

    private List<DiscountData> getDiscountDataList() {
        configDataList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            DiscountData data = new DiscountData();
            data.setDiscount(i);
            data.setDiscountPrice(i * 10);
            data.setPrice(i * 100);

            configDataList.add(data);
        }
        return configDataList;
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