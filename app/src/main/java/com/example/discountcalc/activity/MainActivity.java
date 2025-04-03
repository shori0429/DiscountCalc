package com.example.discountcalc.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import com.example.discountcalc.dataBase.CustomConfigDataStoreSingleton;
import com.example.discountcalc.dataBase.DataStoreHelper;
import com.example.discountcalc.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity{

    // 設定ファイル名保存キー("xxx".preferences_pb)
    private static final String SETTING_DATA = "setting_data";
    private ActivityMainBinding binding;

    // データストア
    RxDataStore<Preferences> datastoreRX;
    // データストアのインスタンス取得用
    CustomConfigDataStoreSingleton dataStoreSingleton;

    // データストアヘルパー取得用
    private DataStoreHelper dataStoreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // データストアシングルトン取得
        getDataStoreInstance();
        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // DataStore取得
    private void getDataStoreInstance() {
        dataStoreSingleton = CustomConfigDataStoreSingleton.getInstance();
        // データストアのSingletonが存在してなければ、新たにデータストアを作成
        if (dataStoreSingleton.getDatastore() == null) {
            datastoreRX = new RxPreferenceDataStoreBuilder(this, SETTING_DATA).build();
        } else {
            // データストア取得
            datastoreRX = dataStoreSingleton.getDatastore();
        }
        // Singletonのデータストアにセット
        dataStoreSingleton.setDataStore(datastoreRX);
    }

}