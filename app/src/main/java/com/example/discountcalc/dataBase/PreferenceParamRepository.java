package com.example.discountcalc.dataBase;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.discountcalc.DAO.PreferenceParamDAO;
import com.example.discountcalc.params.PreferenceParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

// TODO:Singletonで実装したほうがいい気はする。
public class PreferenceParamRepository {
    private final PreferenceParamDAO preferenceParamDAO;
    private final LiveData<List<PreferenceParam>> preferenceParamList;

    int result;

    // WordRepositoryをユニットテストするには、Application依存関係を削除する必要があることに注意

    // DAOにデータベースの読み取り/書き込みメソッドがすべて含まれているため、
    // リポジトリコンストラクタには、データベース全体でなくDAOが渡される。
    public PreferenceParamRepository(Application application){
        AppDataBase db = AppDataBase.getDatabase(application);
        preferenceParamDAO=db.preferenceParamDAO();
        preferenceParamList= preferenceParamDAO.getAll();
        Log.i("database", Objects.requireNonNull(db.getOpenHelper().getDatabaseName()));
    }

    public LiveData<List<PreferenceParam>> PreferenceParamList(){
        return preferenceParamList;
    }

    // Roomは全てのクエリを別スレッドで実行する。

    // これを非UIスレッド上で呼び出さないと、アプリが例外をスローする。
    // Roomは、メイン・スレッドで長時間実行する操作を行わず、UIをブロックしないようにする。
    public void insert(List<PreferenceParam> params){
        // メインスレッドで実行しないよう、ExecutorServiceで作成したAppDataBaseをバックグラウンドスレッドで挿入を実行
        AppDataBase.databaseWriteExecutor.execute(()->{
            preferenceParamDAO.insert(params);
        });
    }

    public void upsert(List<PreferenceParam> param){
        AppDataBase.databaseWriteExecutor.execute(()->{
            try {
                preferenceParamDAO.upsert(param);
                //Log.i("upsert","saveName:"+param.saveName()+" per:"+param.per());
            }catch(Exception e){
                Log.e("database", Objects.requireNonNull(e.getMessage()));
            }
        });
    }

    public void upsertAll(List<PreferenceParam> params){
        AppDataBase.databaseWriteExecutor.execute(()->{
            try{
                preferenceParamDAO.upsertAll(params);
            }catch(Exception e){
                Log.e("database", Objects.requireNonNull(e.getMessage()));
            }
        });
        Log.i("database","do_upsert");
    }

    public void update(List<PreferenceParam> param){
        AppDataBase.databaseWriteExecutor.execute(()->{
            preferenceParamDAO.update(param);
        });
    }

    public void delete(PreferenceParam param){
        AppDataBase.databaseWriteExecutor.execute(()->{
         preferenceParamDAO.delete(param);
        });
    }

    public void deleteForSaveName(String saveName){
        AppDataBase.databaseWriteExecutor.execute(()->{
            result= preferenceParamDAO.deleteForSaveName(saveName);
        });
    }

    public void deleteAll(){
        AppDataBase.databaseWriteExecutor.execute(()->{
           result= preferenceParamDAO.deleteAll();
        });
    }

}
