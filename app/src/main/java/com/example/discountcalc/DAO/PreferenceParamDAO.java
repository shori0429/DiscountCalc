package com.example.discountcalc.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.room.Upsert;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.discountcalc.dataBase.DataBaseStrings;
import com.example.discountcalc.params.PreferenceParam;
import com.example.discountcalc.params.SQLiteTableInfo;

import java.util.List;

/*
* バッググラウンド実行を行う実装メソッドをリポジトリクラスに書く
* */
@Dao
public interface PreferenceParamDAO {
    @Insert
    void insert(List<PreferenceParam> params);

    @Upsert
    void upsert(List<PreferenceParam> param);

    @Upsert
    void upsertAll(List<PreferenceParam> params);

    @Update
    int update(List<PreferenceParam> param);

    @Delete int delete(PreferenceParam param);

    @Query("DELETE FROM "+ DataBaseStrings.TableName +" where save_name=:saveName")
    int deleteForSaveName(String saveName);
    @Query("DELETE FROM "+DataBaseStrings.TableName)
    int deleteAll();

    @Query("SELECT * FROM "+DataBaseStrings.TableName)
    LiveData<List<PreferenceParam>> getAll();

    @Query("SELECT uid,order_index,per,save_name FROM "+DataBaseStrings.TableName+" WHERE save_name LIKE :saveName")
    LiveData<List<PreferenceParam>> getSave(String saveName);

    @Query("SELECT DISTINCT save_name FROM "+DataBaseStrings.TableName)
    LiveData<List<String>> getSaveNameColumnsList();

    @RawQuery
    List<SQLiteTableInfo> getTableInfoList(SupportSQLiteQuery query);
}
