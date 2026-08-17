package com.example.discountcalc.dataBase;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.discountcalc.DAO.CustomPreferenceTableDAO;
import com.example.discountcalc.params.CustomPreferenceTable;
import com.example.discountcalc.params.DiscountRateTable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomPreferenceRepository {
    private final AppDataBase dataBase;
    private final CustomPreferenceTableDAO dao;

    // DAOにデータベースの読み取り/書き込みメソッドがすべて含まれているため、
    // リポジトリコンストラクタには、データベース全体でなくDAOが渡される。
    public CustomPreferenceRepository(Application application){

        dataBase = AppDataBase.getDatabase(application);
        dao=dataBase.customPreferenceTableDAO();
        Log.i("database", Objects.requireNonNull(dataBase.getOpenHelper().getDatabaseName()));
    }

    /// 保存済みのsave_nameに基づく割引率リストを取得。(外部キー:discount_id)を利用。ASCで昇順に
    /// "SELECT * FROM discount_rate_table " +
    ///             "WHERE preference_id = :preferenceId ORDER BY order_index ASC"
    public LiveData<List<DiscountRateTable>> getRatesByPreferenceId(long preferenceId) {
        return dao.getRatesByPreferenceId(preferenceId);
    }

    /// 全save_nameの一覧取得。ASCで昇順に
    /// "SELECT * FROM preference_table ORDER BY order_index ASC"
    public LiveData<List<CustomPreferenceTable>> getAllPreferences(){
        return dao.getAllPreferences();
    }

    /// 割引率の設定名を新規保存
    public long insertPreference(CustomPreferenceTable customPreference){
        return dao.insertPreference(customPreference);
    }

    /// 割引率リストを新規保存
    public List<Long> insertRates(List<DiscountRateTable> rateList){
        return dao.insertRates(rateList);
    }

    /// 割引率の設定名を上書き更新
    public void updatePreference(CustomPreferenceTable customPreference){
        dao.updatePreference(customPreference);
    }

    /// save_nameの重複確認
    /// "SELECT COUNT(*) FROM preference_table WHERE save_name = :saveName"
    public int countBySaveName(String saveName){
        return dao.countBySaveName(saveName);
    }

    public void deletePreference(String saveName){
        dao.deletePreference(saveName);
    }

    /// 既存の割引率リストを削除(上書き時に使用)
    /// "DELETE FROM discount_rate_table WHERE preference_id = :preferenceId"
    public void deleteRateByPreferenceId(long preferenceId){
        dao.deleteRateByPreferenceId(preferenceId);
    }

    ///
    public Map<CustomPreferenceTable,List<DiscountRateTable>> loadCustomPreferenceTableAndDiscountRateTable(){
        return dao.loadCustomPreferenceTableAndDiscountRateTable();
    }

}
