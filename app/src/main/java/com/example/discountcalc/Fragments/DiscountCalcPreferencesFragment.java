package com.example.discountcalc.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.discountcalc.Params.DiscountType;
import com.example.discountcalc.R;

public class DiscountCalcPreferencesFragment extends PreferenceFragmentCompat {
    // 全体の設定データ
    SharedPreferences sharedPreferences;
    // 使用する設定データのPreference
    ListPreference usingCustomPreference;
    // カスタム割引率設定移行のPreference
    Preference customDiscountPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.discount_preferences_toppage, rootKey);
        getPreferences();
        // 「使用する設定データ」の初期パラメータに応じて、カスタム割引率を設定するページに移行する項目を表示・非表示させる
        customPreferenceSetting(DiscountType.valueOf(usingCustomPreference.getValue()),customDiscountPreference);
        setOnChangeListener();
    }

    // 設定データ取得
    private boolean getPreferences(){
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(requireContext());
        usingCustomPreference = findPreference(getString(R.string.using_setting));
        customDiscountPreference=findPreference(getString(R.string.custom_discount_preference));
        // どれか一つでも取得できなければfalseが返される
        return sharedPreferences != null && usingCustomPreference != null && customDiscountPreference != null;
    }

    public void setOnChangeListener() {
        // 使用する設定データの変更リスナー
        usingCustomPreference.setOnPreferenceChangeListener((preference, newValue) -> customPreferenceSetting(DiscountType.valueOf(newValue.toString()), customDiscountPreference));

        // カスタム割引率設定の項目クリックリスナー
        customDiscountPreference.setOnPreferenceClickListener(c -> {

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

}