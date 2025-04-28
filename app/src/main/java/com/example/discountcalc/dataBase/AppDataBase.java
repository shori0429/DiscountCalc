package com.example.discountcalc.dataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.discountcalc.DAO.PreferenceParamDAO;
import com.example.discountcalc.params.PreferenceParam;

@Database(entities = {PreferenceParam.class},version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract PreferenceParamDAO preferenceParamDAO();
}
