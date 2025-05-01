package com.example.discountcalc.dataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.discountcalc.DAO.PreferenceParamDAO;
import com.example.discountcalc.params.PreferenceParam;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PreferenceParam.class},version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract PreferenceParamDAO preferenceParamDAO();

    private static volatile AppDataBase instance;
    private static final int NUMBER_OF_THREADS=4;
    static final ExecutorService databaseWriteExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AppDataBase getDatabase(final Context context){
        if(instance==null){
            synchronized (AppDataBase.class){
                if(instance==null){
                    instance= Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class,"custom_preference_table").build();
                }
            }
        }
        return instance;
    }
}
