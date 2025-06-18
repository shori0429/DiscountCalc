package com.example.discountcalc.dataBase;

import android.app.Application;
import android.util.Log;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.discountcalc.DAO.PreferenceParamDAO;
import com.example.discountcalc.params.PreferenceParam;
import com.example.discountcalc.params.SQLiteTableInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO:Singletonで実装したほうがいい気はする。
public class PreferenceParamRepository {
    private PreferenceParamDAO preferenceParamDAO;
    private List<PreferenceParam> preferenceParamList;

    private List<SQLiteTableInfo> sqLiteTableInfoList;

    private int result;

    // WordRepositoryをユニットテストするには、Application依存関係を削除する必要があることに注意

    // DAOにデータベースの読み取り/書き込みメソッドがすべて含まれているため、
    // リポジトリコンストラクタには、データベース全体でなくDAOが渡される。
    public PreferenceParamRepository(Application application){
        AppDataBase db = AppDataBase.getDatabase(application);
        preferenceParamDAO=db.preferenceParamDAO();
        preferenceParamList=new ArrayList<>();
        sqLiteTableInfoList =new ArrayList<>();
        Log.i("database", Objects.requireNonNull(db.getOpenHelper().getDatabaseName()));
    }

    // Roomは全てのクエリを別スレッドで実行する。
    public List<PreferenceParam> getAll(){
        AppDataBase.databaseWriteExecutor.execute(()->{
            preferenceParamList = preferenceParamDAO.getAll();
        });
        return preferenceParamList;
    }

    // これを非UIスレッド上で呼び出さないと、アプリが例外をスローする。
    // Roomは、メイン・スレッドで長時間実行する操作を行わず、UIをブロックしないようにする。
    public void insert(List<PreferenceParam> params){
        // メインスレッドで実行しないよう、ExecutorServiceで作成したAppDataBaseをバックグラウンドスレッドで挿入を実行
        AppDataBase.databaseWriteExecutor.execute(()->{
            preferenceParamDAO.insert(params);
        });
    }

    public void upsert(PreferenceParam param){
        AppDataBase.databaseWriteExecutor.execute(()->{
            try {
                preferenceParamDAO.upsert(param);
                //Log.i("upsert","saveName:"+param.saveName()+" per:"+param.per());
            }catch(Exception e){
                Log.e("database",e.getMessage());
            }
        });
    }

    public void upsertAll(List<PreferenceParam> params){
        AppDataBase.databaseWriteExecutor.execute(()->{
            try{
                preferenceParamDAO.upsertAll(params);
            }catch(Exception e){
                Log.e("database",e.getMessage());
            }
        });
        Log.i("database","do_upsert");
    }

    public void update(PreferenceParam param){
        AppDataBase.databaseWriteExecutor.execute(()->{
            preferenceParamDAO.update(param);
        });
    }

    public int delete(PreferenceParam param){
        AppDataBase.databaseWriteExecutor.execute(()->{
        result= preferenceParamDAO.delete(param);
        });
        return result;
    }

    public int deleteForSaveName(String saveName){
        AppDataBase.databaseWriteExecutor.execute(()->{
            result= preferenceParamDAO.deleteForSaveName(saveName);
        });
        return result;
    }

    public int deleteAll(){
        AppDataBase.databaseWriteExecutor.execute(()->{
           result= preferenceParamDAO.deleteAll();
        });
        return result;
    }

    public List<PreferenceParam> getSave(String getName){
        AppDataBase.databaseWriteExecutor.execute(()->{
            preferenceParamList=preferenceParamDAO.getSave(getName);
        });
        return preferenceParamList;
    }

    public List<String> getSaveNameColumnsList(){
        AppDataBase.databaseWriteExecutor.execute(()->{
            SimpleSQLiteQuery query=new SimpleSQLiteQuery("PRAGMA table_info(custom_preference_table)");
            sqLiteTableInfoList =preferenceParamDAO.getSaveNameColumnsList(query);
        });
        List<String> saveNameList=new ArrayList<>();
        for(SQLiteTableInfo info: sqLiteTableInfoList){
            saveNameList.add(info.name());
        }
        return saveNameList;
    }
}
