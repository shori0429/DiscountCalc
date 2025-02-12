package com.example.discountcalc.Fragments;

import static com.example.discountcalc.DataBase.DataStoreKey.*;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.Preferences;
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

import com.example.discountcalc.CalculationPack.ConvertDisplayUnitsHelper;
import com.example.discountcalc.CalculationPack.DiscountCalc;
import com.example.discountcalc.CustomAdapters.ResultLayoutAdapter;
import com.example.discountcalc.DataBase.CustomConfigDataStoreSingleton;
import com.example.discountcalc.DataBase.DataStoreHelper;
import com.example.discountcalc.Params.DiscountType;
import com.example.discountcalc.Params.DiscountData;
import com.example.discountcalc.R;
import com.example.discountcalc.ViewModels.DiscountCalcViewModel;
import com.example.discountcalc.databinding.ResultPriceListBinding;

import java.util.ArrayList;


@SuppressWarnings("FieldCanBeLocal")
public class ResultListFragment extends Fragment {

    // デフォルトの表示数
    private final int DEFAULT_VIEWCOUNT = 10;

    // 価格
    private int price=0;
    private View view;

    SharedPreferences preferences;

    private RecyclerView recyclerView;
    private ResultLayoutAdapter resultLayoutAdapter;

    DiscountCalcViewModel discountCalcViewModel;

    // データストアのインスタンス取得用
    CustomConfigDataStoreSingleton dataStoreSingleton;

    // データストアヘルパー取得用
    private DataStoreHelper dataStoreHelper;

    private ResultPriceListBinding resultPriceListBinding;

    ArrayList<DiscountData> configDataList;

    ArrayList<Integer> discountPers;

    ArrayList<Integer> configEnums;

    // 結果表示数
    int viewCount;

    // 計算タイプ
    DiscountType discountType;

    // 余白の適用フラグ
    boolean[] paddingFlags;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the ViewModel.
        Log.i("ResultListFragment", "Called ViewModelProvider.get");
        discountCalcViewModel = new ViewModelProvider(requireActivity()).get(DiscountCalcViewModel.class);

        LivedataInit();

        // データストアインスタンス取得
        getDataStoreInstance();
        // データストアヘルパー取得
        dataStoreHelperInitialize(dataStoreSingleton.getDatastore());

        //　表示数取得
        getViewCountData();
        // 初回起動時の場合、データストアから値取得しないように(初期値-1になる為)

        // 保存データ取得
        loadDataStore();

        if (discountType == DiscountType.None) {
            createDiscountPreferenceData();
        }

        // 計算
        calcDiscounts();

        paddingFlags = new boolean[4];
        paddingFlags[2] = true;

        recyclerView = resultPriceListBinding.resultPriceList;
        resultLayoutAdapter = new ResultLayoutAdapter(configDataList, ConvertDisplayUnitsHelper.dpToPx(30, requireContext()), paddingFlags);
        // 縦方向のLayoutManagerを作成
        LinearLayoutManager llm = new LinearLayoutManager(resultPriceListBinding.getRoot().getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(resultLayoutAdapter);

    }

    //入力価格データ購読設定
    private void LivedataInit() {
        // LiveData設定
        final Observer<Integer> priceObserver = integer -> {
            price = integer;
            calcDiscounts();
            resultLayoutAdapter.updateItem(configDataList);
        };
        discountCalcViewModel.getPrice().observe(getViewLifecycleOwner(), priceObserver);
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
        //
        final String viewcountKey = VIEWCOUNT_KEY;
        int count = dataStoreHelper.getIntValue(viewcountKey);

        // データが存在しない場合デフォルト値をviewCountとし、その値も保存する
        if (count <= -1) {
            viewCount = DEFAULT_VIEWCOUNT;
            if (!dataStoreHelper.putIntegerValue(viewcountKey, viewCount)) {
                Log.e("viewcount_put", viewcountKey + ":put_error");
            }
        } else {
            viewCount = count;
            Log.i("viewCount", Integer.toString(count));

            //
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            viewCount = sharedPreferences.getInt("viewCount", -1);
            Log.i("viewCount", "Result-sharedPreferences:" + count);
        }

        // リスト初期化
        configDataList = new ArrayList<>();
        for (int i = 0; i < viewCount; i++) {
            configDataList.add(i, new DiscountData());
        }
    }


    // DataStore取得(MainActivityで取得済み)
    private void getDataStoreInstance() {
        dataStoreSingleton = CustomConfigDataStoreSingleton.getInstance();
    }

    // DataStoreのヘルパー取得
    private void dataStoreHelperInitialize(RxDataStore<Preferences> dataStore) {
        if (dataStoreHelper == null)
            // 子フラグメントとして使用しているので親フラグメントのFragmentを引数に
            dataStoreHelper = new DataStoreHelper(this.getParentFragment(), dataStore);
    }

    // DataStoreに割引データの設定を保存
    private boolean saveDataStore() {
        if(PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean("isSavePreferences",false)){
            return false;
        }
        for (int i = 0; i < viewCount; i++) {
            final String discountKey = DISCOUNT_KEY + i;
            if (!dataStoreHelper.putIntegerValue(discountKey, discountPers.get(i))) {
                Log.e("viewCount_put", discountKey + ":put_error");
                return false;
            }
        }
        if (!dataStoreHelper.putIntegerValue(CONFIG_TYPE_KEY, discountType.getValue())) {
            Log.e("configType_put", discountType.toString());
            return false;
        } else {
            Log.i("configType_put", discountType.toString());
        }
        return true;
    }

    // DataStoreから割引データの設定取得
    private void loadDataStore() {
        discountPers = new ArrayList<>();
        configEnums = new ArrayList<>();

        for (int i = 0; i < viewCount; i++) {
            //ロード処理
            discountPers.add(i, dataStoreHelper.getIntValue(DISCOUNT_KEY + i));
            configEnums.add(i, dataStoreHelper.getIntValue(CONFIG_ENUM_KEY + i));
        }
        getPreferences();
    }

    // デフォルトの割引率設定を指定
    private void getPreferences() {
        // Preferences.xmlで保存された設定データを呼び出し、discountTypeにセット
        preferences=PreferenceManager.getDefaultSharedPreferences(this.requireContext());

        String useKey=getString(R.string.using_setting);
        // SharedPreferencesに保存された設定キー取得しセット。存在しない場合はNone
        String settingType=preferences.getString(useKey,DiscountType.None.name());
        discountType = DiscountType.valueOf(settingType);
    }

    // 計算処理
    private void calcDiscounts() {
        for (int i = 0; i < viewCount; i++) {
            // 割引率取得
            int discountPer = discountPers.get(i);
            // 割引額算出
            int discountPrice = DiscountCalc.discountCalculationIntPercentage(price, discountPer);
            // 割引後の価格算出
            int afterPrice = price - discountPrice;
            //int enumKey = configEnums.get(i);

            DiscountData data = new DiscountData(discountPer, discountPrice, afterPrice, 0);
            configDataList.set(i, data);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        boolean isSaveFlag=preferences.getBoolean(getString(R.string.is_save_parameters),false);
        // パラメータ保存をしない設定であれば保存処理を実行しない。
        if(!isSaveFlag) return;

        // 一時中断で保存しておく
        boolean isSave=saveDataStore();
        Log.i("settingSave",String.valueOf(isSave));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        boolean isSaveFlag=preferences.getBoolean(getString(R.string.is_save_parameters),false);
        // パラメータ保存をしない設定であれば保存処理を実行しない。
        if(!isSaveFlag) return;

        // Fragmentが削除された際に保存しておく
        boolean isSave=saveDataStore();
        Log.i("settingSave",String.valueOf(isSave));
    }

    // あらかじめ用意された割引率取得し、一覧データに使用する割引率を設定する。
    private void createDiscountPreferenceData() {
        // あらかじめ用意された割引率を取得
        int[] discountData = getResources().getIntArray(R.array.preset_discount_values);
        if (discountPers == null) {
            discountPers = new ArrayList<>();
        }
        for (int i = 0; i < discountData.length; i++) {
            discountPers.add(i, discountData[i]);
        }
        // 使用する割引率設定をプリセットに指定して保存しておく。
        discountType=DiscountType.Preset;

        // 使用する割引率設定を更新しておく。
        String saveKey=getString(R.string.using_setting);
        preferences.edit().putString(saveKey,discountType.toString()).apply();
    }

}