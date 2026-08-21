package com.example.discountcalc.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SeekBarPreference;

import com.example.discountcalc.params.DiscountType;
import com.example.discountcalc.R;
import com.example.discountcalc.viewModels.CustomPreferenceListViewModel;
import com.example.discountcalc.viewModels.CustomPreferenceListViewModelFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DiscountCalcPreferencesFragment extends PreferenceFragmentCompat {
    // 全体の設定データ
    SharedPreferences sharedPreferences;
    // 使用する設定データのPreference
    ListPreference usingCustomPreference;
    // データベースに保存された設定データのリスト
    ListPreference usingSaveCustomPreference;

    // カスタム割引率設定移行のPreference
    Preference customDiscountPreference;

    CustomPreferenceListViewModel customPreferenceListViewModel;

    SeekBarPreference viewCountSeekBar;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.discount_preferences_toppage, rootKey);
        getPreferences();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModelInitialize();
        // 「使用する設定データ」の初期パラメータに応じて、カスタム割引率を設定するページに移行する項目を表示・非表示させる
        customPreferenceSetting(DiscountType.valueOf(usingCustomPreference.getValue()),customDiscountPreference);
        setOnChangeListener();
    }

    private void viewModelInitialize() {
        customPreferenceListViewModel=new ViewModelProvider(this,new CustomPreferenceListViewModelFactory(requireActivity().getApplication()))
                .get(CustomPreferenceListViewModel.class);

        // TODO ViewModelから全設定リスト名を取得する
        // 保存済みのSaveNameをリスト取得
        // リストサイズ分のCharSequence配列を作成
        List<String> saveNameList = customPreferenceListViewModel.getSaveNameList();



        int listSize = 0;
        if(saveNameList!=null) {
            saveNameList.size();
        }

        // CharSequence[]に保存名のリストをセット
        CharSequence entries[] = new CharSequence[listSize];
        for (int i = 0; i < listSize; i++) {
            entries[i] = saveNameList.get(i);
        }

        // ListPreferenceにエンティティリストをセット
        // 実際に保存されるエンティティリストをセット
        usingSaveCustomPreference.setEntries(entries);
        usingSaveCustomPreference.setEntryValues(entries);
        // 初期化時に使用する設定データがカスタム設定になっているなら表示させておく
        customPreferenceSetting(DiscountType.valueOf(usingCustomPreference.getValue()), usingSaveCustomPreference);
//        });
    }

    // 設定データ取得
    private boolean getPreferences(){
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(requireContext());
        usingCustomPreference = findPreference(getString(R.string.using_setting));
        usingSaveCustomPreference=findPreference(getString(R.string.using_custom_preference));
        customDiscountPreference=findPreference(getString(R.string.custom_discount_preference));
        viewCountSeekBar=findPreference(getString(R.string.view_count));
        // どれか一つでも取得できなければfalseが返される
        return sharedPreferences != null && usingCustomPreference != null && customDiscountPreference != null;
    }

    public void setOnChangeListener() {
        // 使用する設定データの変更リスナー
        usingCustomPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            customPreferenceSetting(DiscountType.valueOf(newValue.toString()), customDiscountPreference);
            //
            if(usingSaveCustomPreference.getEntries()!=null){
                customPreferenceSetting(DiscountType.valueOf(newValue.toString()),usingSaveCustomPreference);
            }
            return newValue!=DiscountType.None;
        });

        // カスタム割引率設定の項目クリックリスナー
        customDiscountPreference.setOnPreferenceClickListener(c -> {
            // Navigation設定
            setNavGraphDestination();
            return true;
        });

        // 表示数シークバーの変更リスナー
        usingSaveCustomPreference.setOnPreferenceChangeListener((preference,useName) -> {
            setViewMax(useName.toString());
            return true;
        });

    }


    private void customPreferenceSetting(DiscountType type, Preference preference){
        switch (type){
            case Preset ->{
                preference.setVisible(false);
                viewCountSeekBar.setMax(getResources().getIntArray(R.array.preset_discount_values).length);
            }
            case Custom -> {
                preference.setVisible(true);
                setViewMax(usingSaveCustomPreference.getValue());
            }
            default -> {}
        }
    }

    private void setViewMax(String useName) {
        customPreferenceListViewModel.selectPreference(useName);
        List<String> saveNameList=customPreferenceListViewModel.getSaveNameList();
        if(saveNameList!=null) {
            int viewMax = saveNameList.size();
            viewCountSeekBar.setMax(viewMax);
            // 前のシークバーの位置が、更新後のシークバーの最大値を超えている場合に値を更新し、戻った時のエラー回避
            if (viewCountSeekBar.getValue() > viewMax) viewCountSeekBar.setValue(viewMax);
        }else{
            // TODO 一旦シークバー非表示
            viewCountSeekBar.setVisible(false);
        }
    }

    private void setNavGraphDestination(){
        NavHostFragment navHostFragment=(NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.host_fragment);
        assert navHostFragment != null:"null navHostFragment. DiscountCalcPreferencesFragment.java line:68";
        NavController navHostController=navHostFragment.getNavController();
        NavDirections navDirections=DiscountCalcPreferencesFragmentDirections.actionSettingsFragmentToCustomDiscountPreferenceFragment();
        navHostController.navigate(navDirections);
    }
}