package com.example.discountcalc.DataBase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.fragment.app.Fragment;

import java.util.Map;

import io.reactivex.rxjava3.core.Single;

public class DataStoreHelper {
    Fragment fragment;
    RxDataStore<Preferences>dataStoreRx;
    Preferences pref_error=new Preferences() {
        @Override
        public <T> boolean contains(@NonNull Key<T> key) {
            return false;
        }

        @Nullable
        @Override
        public <T> T get(@NonNull Key<T> key) {
            return null;
        }

        @NonNull
        @Override
        public Map<Key<?>, Object> asMap() {
            return null;
        }
    };

    public DataStoreHelper(Fragment fragment,RxDataStore<Preferences>dataStoreRx){
        this.fragment=fragment;
        this.dataStoreRx=dataStoreRx;
    }

    // 値の保存
    public boolean putStringValue(String key,String value){
        boolean returnValue;
        // DataStoreは通常のStringをキーとして受け付けないので、String型のPreferences.Keyを作成する必要がある。
        // 対応する値もString型に
        Preferences.Key<String> PREF_KEY= PreferencesKeys.stringKey(key);
        Single<Preferences> updateResult= dataStoreRx.updateDataAsync(prefsIn ->{
            MutablePreferences mutablePreferences=prefsIn.toMutablePreferences();
            mutablePreferences.set(PREF_KEY,value);
            return Single.just(mutablePreferences);
        //値の保存や再取得エラーをキャッチするための処理
        } ).onErrorReturnItem(pref_error);
        returnValue=updateResult.blockingGet()!=pref_error;
        return returnValue;
    }
    public String getStringValue(String key){
        Preferences.Key<String> PREF_KEY= PreferencesKeys.stringKey(key);
        Single<String>value=dataStoreRx.data().firstOrError().map(prefs->prefs.get(PREF_KEY)).onErrorReturnItem("null");
        return value.blockingGet();
    }
    public boolean putBoolValue(String key,boolean value){
        boolean returnValue;
        Preferences.Key<Boolean> PREF_KEY= PreferencesKeys.booleanKey(key);
        Single<Preferences> updateResult= dataStoreRx.updateDataAsync(prefsIn ->{
            MutablePreferences mutablePreferences=prefsIn.toMutablePreferences();
            mutablePreferences.set(PREF_KEY,value);
            return Single.just(mutablePreferences);
        } ).onErrorReturnItem(pref_error);
        returnValue=updateResult.blockingGet()!=pref_error;
        return returnValue;
    }
    public boolean getBoolValue(String key){
        Preferences.Key<Boolean> PREF_KEY= PreferencesKeys.booleanKey(key);
        Single<Boolean>value=dataStoreRx.data().firstOrError().map(prefs->prefs.get(PREF_KEY)).onErrorReturnItem(false);
        return value.blockingGet();
    }

    public boolean putIntegerValue(String key,int integer){
        boolean returnValue;
        // DataStoreは通常のStringをキーとして受け付けないので、Integer型のPreferences.Keyを作成する必要がある。
        // 対応する値もString型に
        Preferences.Key<Integer> PREF_KEY= PreferencesKeys.intKey(key);
        Single<Preferences> updateResult= dataStoreRx.updateDataAsync(prefsIn ->{
            MutablePreferences mutablePreferences=prefsIn.toMutablePreferences();
            mutablePreferences.set(PREF_KEY,integer);
            return Single.just(mutablePreferences);
            //値の保存や再取得エラーをキャッチするための処理
        } ).onErrorReturnItem(pref_error);
        returnValue=updateResult.blockingGet()!=pref_error;
        return returnValue;
    }

    public int getIntValue(String key){
        Preferences.Key<Integer> PREF_KEY= PreferencesKeys.intKey(key);
        Single<Integer>value=dataStoreRx.data().firstOrError().map(prefs->prefs.get(PREF_KEY)).onErrorReturnItem(-1);
        return value.blockingGet();
    }
}
