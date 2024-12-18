package com.example.discountcalc.Fragments;

import android.os.Bundle;
import android.util.Log;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.discountcalc.Params.DiscountType;
import com.example.discountcalc.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        setOnChangeListener();
    }

    public void setOnChangeListener() {
        ListPreference listPreference = findPreference(getString(R.string.using_setting));
        Preference customPreference = findPreference(getString(R.string.custom_discount_preference));
        if (listPreference == null || customPreference == null) return;
        listPreference.setOnPreferenceChangeListener((preference, newValue) -> customPreferenceSetting(DiscountType.valueOf(newValue.toString()), customPreference));

        Preference preference=findPreference(getString(R.string.custom_discount_preference));
        if(preference==null)return;
        preference.setOnPreferenceClickListener(c -> {

            return true;
        });
    }


    private boolean customPreferenceSetting(DiscountType type,Preference preference){
        if(type==DiscountType.Custom){
            preference.setVisible(true);
            Log.i("Preference_ListPreference","true");
            return true;
        }
        if(preference.isVisible()){
            preference.setVisible(false);
            return true;
        }
        Log.i("Preference_ListPreference",type.toString());
        return false;
    }

}