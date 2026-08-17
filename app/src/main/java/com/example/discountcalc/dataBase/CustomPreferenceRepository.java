package com.example.discountcalc.dataBase;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.discountcalc.DAO.CustomPreferenceTableDAO;
import com.example.discountcalc.params.CustomPreferenceTable;
import com.example.discountcalc.params.DiscountRateTable;

import java.util.ArrayList;
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
    public long insertPreference(String newSaveName,int newOrderIndex){
        CustomPreferenceTable newTable=CustomPreferenceTable.createPreferenceParam(newSaveName,newOrderIndex);
        return dao.insertPreference(newTable);
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

    /// 保存名&割引率リスト完全新規保存
    public long insertPreferenceWithRate(String newSaveName,List<DiscountRateTable> rateList){
        if (newSaveName == null || rateList == null) return 0;
        // 設定名を保存し、lowIdを取得
        long lowId=insertPreference(newSaveName, getNextPreferenceOrderIndex());
        if (lowId != -1) {
            // 新しいリストを作成し、lowIdをセット(紐付けるため)
            // rateId,per,orderIndexは引数のものを使用
            List<DiscountRateTable> list = new ArrayList<>();
            for (var rtb : rateList) {
                list.add(new DiscountRateTable(
                        rtb.rateId(),
                        lowId,
                        rtb.per(),
                        rtb.orderIndex()
                ));
            }
            // 割引率リストを新規保存
            insertRates(list);
        }
        return lowId;
    }

    /// 割引率リスト更新処理
    public void updateRates(List<DiscountRateTable> toDelete, List<DiscountRateTable> toInsert,List<DiscountRateTable> toUpdate) {

        dao.applyRatesChange(toDelete, toInsert, toUpdate);
    }


    /// 新規保存名追加時、次に使うOrderIndexの値を取得
    private int getNextPreferenceOrderIndex(){
        Integer maxOrderIndex=dao.getPreferenceMaxOrderIndex();
        int newOrderIndex;

        //
        if (maxOrderIndex == null) {
            newOrderIndex = 0;
        } else {
            newOrderIndex = maxOrderIndex + 1;
        }
        return newOrderIndex;
    }
}