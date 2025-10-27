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
import com.example.discountcalc.params.PreferenceParam;
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

        customPreferenceListViewModel.AllPreferenceParamList().observe(getViewLifecycleOwner(),allParams->{
            if(allParams.size()>0){
                customPreferenceListViewModel.AllPreferenceParamList().removeObservers(getViewLifecycleOwner());
                // 重複要素を一つにまとめたリストを作成
                List<String> loadSaveList = allParams.stream()
                        .map(PreferenceParam::saveName)
                        .distinct()
                        .collect(Collectors.toList());

                // CharSequence[]に保存名のリストをセット
                // TODO ストリーム使ってうまいこと作れそう
                int listSize = loadSaveList.size();
                CharSequence entries[] = new CharSequence[listSize];

                for (int i = 0; i < listSize; i++) {
                    entries[i] = loadSaveList.get(i);
                }

                usingSaveCustomPreference.setEntries(entries);
                usingSaveCustomPreference.setEntryValues(entries);
                // 初期化時に使用する設定データがカスタム設定になっているなら表示させておく
                customPreferenceSetting(DiscountType.valueOf(usingCustomPreference.getValue()),usingSaveCustomPreference);
            }
        });
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
    }


    private boolean customPreferenceSetting(DiscountType type,Preference preference){
        if(type==DiscountType.Custom) {
            preference.setVisible(true);
            return true;
        }else if(type==DiscountType.Preset){
            preference.setVisible(false);
            return true;
        }
        return false;
    }

    private void setNavGraphDestination(){
        NavHostFragment navHostFragment=(NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.host_fragment);
        assert navHostFragment != null:"null navHostFragment. DiscountCalcPreferencesFragment.java line:68";
        NavController navHostController=navHostFragment.getNavController();
        NavDirections navDirections=DiscountCalcPreferencesFragmentDirections.actionSettingsFragmentToCustomDiscountPreferenceFragment();
        navHostController.navigate(navDirections);
    }
}