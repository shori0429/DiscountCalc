package com.example.discountcalc.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.room.Upsert;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.discountcalc.params.PreferenceParam;
import com.example.discountcalc.params.SQLiteTableInfo;

import java.util.List;

/*
* バッググラウンド実行を行う実装メソッドをリポジトリクラスに書く
* */
@Dao
public interface PreferenceParamDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<PreferenceParam> params);

    @Upsert
    void upsert(PreferenceParam param);


    @Upsert
    void upsertAll(List<PreferenceParam> params);

    @Update
    int update(PreferenceParam param);

    @Delete int delete(PreferenceParam param);

    @Query("DELETE FROM custom_preference_table where save_name=:saveName")
    int deleteForSaveName(String saveName);
    @Query("DELETE FROM custom_preference_table")
    int deleteAll();

    @Query("SELECT * FROM custom_preference_table")
    List<PreferenceParam> getAll();

    @Query("SELECT uid,save_name,per FROM custom_preference_table WHERE save_name LIKE :saveName")
    List<PreferenceParam> getSave(String saveName);

    @RawQuery
    List<SQLiteTableInfo> getSaveNameColumnsList(SupportSQLiteQuery query);
}
