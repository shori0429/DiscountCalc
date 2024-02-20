package com.example.discountcalc.DataBase;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava3.RxDataStore;

public class CustomConfigDataStoreSingleton {
    RxDataStore<Preferences>datastore;
    private static final CustomConfigDataStoreSingleton Instance=new CustomConfigDataStoreSingleton();
    public static CustomConfigDataStoreSingleton getInstance(){
        return Instance;
    }
    private CustomConfigDataStoreSingleton(){}

    public void setDataStore(RxDataStore<Preferences>datastore){
        this.datastore=datastore;
    }
    public RxDataStore<Preferences> getDatastore(){
        return datastore;
    }

}
