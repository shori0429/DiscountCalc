package com.example.discountcalc.dataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.discountcalc.DAO.CustomPreferenceTableDAO;
import com.example.discountcalc.DAO.PreferenceParamDAO;
import com.example.discountcalc.params.CustomPreferenceTable;
import com.example.discountcalc.params.DiscountRateTable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {CustomPreferenceTable.class,DiscountRateTable.class},version = 2)
public abstract class AppDataBase extends RoomDatabase {
    public abstract PreferenceParamDAO preferenceParamDAO();

    public abstract CustomPreferenceTableDAO customPreferenceTableDAO();
    public abstract DiscountRateTable discountRateTable();

    private static volatile AppDataBase instance;
    private static final int NUMBER_OF_THREADS=4;
    static final ExecutorService databaseWriteExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDataBase getDatabase(final Context context){
        if(instance==null){
            synchronized (AppDataBase.class){
                if(instance==null){
                    // 第三引数は端末内に保存されるファイル名になる
                    instance= Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class,"discount-calc_database")
                            .build();
                }
            }
        }
        return instance;
    }

}
