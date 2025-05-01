package com.example.discountcalc.dataBase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.discountcalc.DAO.PreferenceParamDAO;
import com.example.discountcalc.params.PreferenceParam;

import java.util.List;

public class PreferenceParamRepository {
    private PreferenceParamDAO preferenceParamDAO;
    private LiveData<List<PreferenceParam>> preferenceParamList;

    // WordRepositoryをユニットテストするには、Application依存関係を削除する必要があることに注意

    // DAOにデータベースの読み取り/書き込みメソッドがすべて含まれているため、
    // リポジトリコンストラクタには、データベース全体でなくDAOが渡される。
    public PreferenceParamRepository(Application application){
        AppDataBase db = AppDataBase.getDatabase(application);
        preferenceParamDAO=db.preferenceParamDAO();
        preferenceParamList=preferenceParamDAO.getAll();
    }

    // Roomは全てのクエリを別スレッドで実行する。
    public LiveData<List<PreferenceParam>> getAllPreferenceParam(){
        return preferenceParamList;
    }

    // これを非UIスレッド上で呼び出さないと、アプリが例外をスローする。
    // Roomは、メイン・スレッドで長時間実行する操作を行わず、UIをブロックしないようにする。
    public void insert(PreferenceParam param){
        // メインスレッドで実行しないよう、ExecutorServiceで作成したAppDataBaseをバックグラウンドスレッドで挿入を実行
        AppDataBase.databaseWriteExecutor.execute(()->{
            preferenceParamDAO.insertPreferenceParam(param);
        });
    }
}
