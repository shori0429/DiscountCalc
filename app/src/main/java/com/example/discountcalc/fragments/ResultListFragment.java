package com.example.discountcalc.fragments;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TableRow;

import com.example.discountcalc.calculationPack.DiscountCalc;
import com.example.discountcalc.customAdapters.ResultLayoutAdapter;
import com.example.discountcalc.params.DiscountType;
import com.example.discountcalc.params.DiscountData;
import com.example.discountcalc.R;
import com.example.discountcalc.viewModels.CustomPreferenceListViewModel;
import com.example.discountcalc.viewModels.CustomPreferenceListViewModelFactory;
import com.example.discountcalc.viewModels.DiscountCalcViewModel;
import com.example.discountcalc.databinding.ResultPriceListBinding;

import java.util.ArrayList;
import java.util.HashMap;


@SuppressWarnings("FieldCanBeLocal")
public class ResultListFragment extends Fragment {

    // デフォルトの表示数
    private final int DEFAULT_VIEWCOUNT = 10;
    // 表示数
    private int viewCount=0;

    // 価格
    private int beforeDiscountPrice =0;
    private View view;

    // 結果リストのラベルView(割引率、割引額、小計)
    private TableRow resultOneCalcLabelView;

    private ProgressBar progressBarView;


    SharedPreferences preferences;

    private RecyclerView recyclerView;
    private ResultLayoutAdapter resultLayoutAdapter;

    DiscountCalcViewModel discountCalcViewModel;

    // カスタム設定データのViewModel
    CustomPreferenceListViewModel customPreferenceListViewModel;

    private ResultPriceListBinding resultPriceListBinding;

    // 結果表示用のリスト
    ArrayList<DiscountData> resultDataList = new ArrayList<>();

    // 使用する割引率のリスト
    ArrayList<Integer> discountPerList = new ArrayList<>();

    // 計算タイプ
    DiscountType discountType;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        resultPriceListBinding = ResultPriceListBinding.inflate(inflater, container, false);
        view = resultPriceListBinding.getRoot();
        recyclerView = resultPriceListBinding.resultPriceList;
        resultOneCalcLabelView = resultPriceListBinding.resultLabel.resultLabelPackage;
        progressBarView = resultPriceListBinding.resultListProgress;


        Log.i("resultFragment", getParentFragmentManager().toString());

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the ViewModel.
        Log.i("ResultListFragment", "Called ViewModelProvider.get");

        // 読み込みUI表示
        progressBarView.setVisibility(View.VISIBLE);
        viewModelInitialize();
        livedataInit();

    }

    @Override
    public void onStart() {
        super.onStart();


        // 設定データ取得
        getPreferences();
        // 保存データ取得
        loadSettingData();

        recyclerInit();

        if (progressBarView != null) {
            progressBarView.setVisibility(View.GONE);
        }
    }

    private void recyclerInit() {

        // 描画完了後の通知を受け取って、その地点のHashMapを作成
        resultOneCalcLabelView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View checkView=resultOneCalcLabelView.findViewById(R.id.discountLabel);
                if(checkView != null && checkView.getWidth() >0){
                    // 1度でいいので破棄
                    resultOneCalcLabelView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        resultLayoutAdapter = new ResultLayoutAdapter(resultDataList, getOneCalcViewLayoutWidthAndHeight());

                    // 縦方向のLayoutManagerを作成
                    LinearLayoutManager llm = new LinearLayoutManager(resultPriceListBinding.getRoot().getContext());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(llm);
                    recyclerView.setAdapter(resultLayoutAdapter);

                    if(!resultDataList.isEmpty()){
                        resultLayoutAdapter.updateItem(resultDataList);
                    }
                }
            }
        });
    }


    private void viewModelInitialize() {
        // ActivityにこのFragmentが追加されているかチェック
        if (this.getActivity() == null) return;
        discountCalcViewModel = new ViewModelProvider(this).get(DiscountCalcViewModel.class);
        customPreferenceListViewModel = new ViewModelProvider(this, new CustomPreferenceListViewModelFactory(requireActivity().getApplication()))
                .get(CustomPreferenceListViewModel.class);
    }

    // ユーザー入力した数値が変更された時の処理
    private void livedataInit() {
        // LiveData設定
        final Observer<Integer> priceObserver = integer -> {
            // 入力した数値をbeforePriceに適用
            beforeDiscountPrice = integer;
            // 再計算
            refreshCalc();
        };
        discountCalcViewModel.getPrice().observe(getViewLifecycleOwner(), priceObserver);
    }


    private boolean saveDataStore() {
        return !PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean("isSavePreferences", false);
    }

    private void loadSettingData() {


        switch (discountType) {
            case None, Preset -> {
                createDiscountPreferenceData();
                refreshCalc();
            }
            case Custom -> {
                if (customPreferenceListViewModel.PreferenceParamList()==null) {
                    createDiscountPreferenceData();
                    break;
                }
                customPreferenceListViewModel.AllPreferenceParamList().observe(getViewLifecycleOwner(),params->{
                    customPreferenceListViewModel.usePreferenceParamList(preferences.getString(getString(R.string.using_custom_preference),""));
                });

                customPreferenceListViewModel.PreferenceParamList().observe(getViewLifecycleOwner(),param->{
                    // 割引率リストをクリアしておく
                    discountPerList.clear();
                    for (int i = 0; i < customPreferenceListViewModel.preferenceParamListSize(); i++) {
                        discountPerList.add(customPreferenceListViewModel.getPreferenceParamData(i).per());
                    }
                    refreshCalc();
                });
            }
        }
    }

    private void getPreferences() {
        // discount_preferences_toppage.xmlで保存された設定データを呼び出し、discountTypeにセット
        preferences=PreferenceManager.getDefaultSharedPreferences(this.requireContext());

        // 表示数取得
        viewCount=preferences.getInt(getString(R.string.view_count),DEFAULT_VIEWCOUNT);

        String useKey=getString(R.string.using_setting);
        // SharedPreferencesに保存された設定キー取得しセット。存在しない場合はNone
        String settingType=preferences.getString(useKey,DiscountType.None.name());
        discountType = DiscountType.valueOf(settingType);
    }

    // 計算処理
    private void calcDiscounts() {
        resultDataList.clear();
        // ローカル変数を使用することでviewCountを直接操作しなくても表示数調整できるように
        int displayViewCount=viewCount;
        // 表示数が利用する割引率のリストよりも大きければ、利用する割引率のリストに合わせる。OutOfBoundsの防止
        if (displayViewCount > discountPerList.size()) displayViewCount = discountPerList.size();
        for (int i = 0; i < displayViewCount; i++) {
            // 割引率取得
            int discountPer = discountPerList.get(i);
            // 割引額算出
            int discountPrice = DiscountCalc.discountCalculationIntPercentage(beforeDiscountPrice, discountPer);
            // 割引後の価格算出
            int afterPrice = beforeDiscountPrice - discountPrice;

            // 結果用のリストに追加
            DiscountData data = new DiscountData(discountPer, discountPrice, afterPrice, 0);
            resultDataList.add(data);
        }
        if (resultLayoutAdapter != null) {
            // アダプタに最新の計算結果をupdate
            resultLayoutAdapter.updateItem(resultDataList);
        }
    }

    // 再計算用メソッド
    private void refreshCalc(){
        if(discountPerList ==null || discountPerList.isEmpty()){
            return;
        }
        calcDiscounts();

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

        discountPerList.clear();
        for (int i = 0; i < discountData.length; i++) {
            discountPerList.add(discountData[i]);
        }
        // 使用する割引率設定をプリセットに指定して保存しておく。
        discountType=DiscountType.Preset;

        // 使用する割引率設定を更新しておく。
        String saveKey=getString(R.string.using_setting);
        preferences.edit().putString(saveKey,discountType.toString()).apply();
    }

    private HashMap<Integer, Point> getOneCalcViewLayoutWidthAndHeight() {
        final HashMap<Integer, Point> layoutSize = new HashMap<>();
        layoutSize.put(R.id.discountLabel, new Point(
                resultOneCalcLabelView.findViewById(R.id.discountLabel).getWidth(),
                resultOneCalcLabelView.findViewById(R.id.discountLabel).getHeight()
        ));
        layoutSize.put(R.id.discountPriceLabel, new Point(
                resultOneCalcLabelView.findViewById(R.id.discountPriceLabel).getWidth(),
                resultOneCalcLabelView.findViewById(R.id.discountPriceLabel).getHeight()
        ));
        layoutSize.put(R.id.priceLabel, new Point(
                resultOneCalcLabelView.findViewById(R.id.priceLabel).getWidth(),
                resultOneCalcLabelView.findViewById(R.id.priceLabel).getHeight()
        ));

        return layoutSize;
    }

}