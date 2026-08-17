package com.example.discountcalc.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.discountcalc.params.CustomPreferenceTable;
import com.example.discountcalc.params.DiscountRateTable;

import java.util.List;
import java.util.Map;

/*
* バッググラウンド実行を行う実装メソッドをリポジトリクラスに書く
* */
@Dao
// @Transactionのメソッドは
public interface CustomPreferenceTableDAO {

    // 保存済みのsave_nameに基づく割引率リストを取得。(外部キー:discount_id)を利用
    // ASCで昇順に
    @Query("SELECT * FROM discount_rate_table " +
            "WHERE preference_id = :preferenceId ORDER BY order_index ASC")
    LiveData<List<DiscountRateTable>> getRatesByPreferenceId(long preferenceId);

    // 全save_nameの一覧取得
    // ASCで昇順に
    @Query("SELECT * FROM preference_table ORDER BY order_index ASC")
    LiveData<List<CustomPreferenceTable>> getAllPreferences();

    /// preference_tableのorderIndexカラムの最大値を取得
    @Query("SELECT MAX(order_index) FROM preference_table")
    Integer getPreferenceMaxOrderIndex();

    // 割引率の設定名を新規保存
    @Insert
    long insertPreference(CustomPreferenceTable table);

    // 割引率リストを新規保存
    @Insert
    List<Long> insertRates(List<DiscountRateTable> table);

    // 割引率の設定名を上書き更新
    @Update
    void updatePreference(CustomPreferenceTable table);

    // save_nameの重複確認
    @Query("SELECT COUNT(*) FROM preference_table WHERE save_name = :saveName")
    int countBySaveName(String saveName);

    // save_nameの削除(CASCADEで割引率も自動削除)
    @Delete
    void deletePreference(String saveName);

    // 既存の割引率リストを削除(上書き時に使用)
    @Query("DELETE FROM discount_rate_table WHERE preference_id = :preferenceId")
    void deleteRateByPreferenceId(long preferenceId);

    //
    @Query("SELECT * FROM preference_table " +
            "JOIN discount_rate_table ON preference_table.preference_id = discount_rate_table.preference_id")
    Map<CustomPreferenceTable,List<DiscountRateTable>> loadCustomPreferenceTableAndDiscountRateTable();


    // 割引率リストの上書き
    @Update
    int updateRates(List<DiscountRateTable> toUpdate);

    // 割引率リストの削除(上書き時に使用)
    @Delete
    void deleteRateTableAll(List<DiscountRateTable> toDelete);

    @Transaction
    default void applyRatesChange(List<DiscountRateTable> toDelete,List<DiscountRateTable> toInsert,List<DiscountRateTable> toUpdate){
        deleteRateTableAll(toDelete);
        insertRates(toInsert);
        int updateCount = updateRates(toUpdate);
    }


}
