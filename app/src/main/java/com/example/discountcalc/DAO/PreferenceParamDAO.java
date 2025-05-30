package com.example.discountcalc.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import com.example.discountcalc.params.PreferenceParam;

import java.util.List;

@Dao
public interface PreferenceParamDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(List<PreferenceParam> params);

    @Upsert
    public void upsert(PreferenceParam param);


    @Upsert
    public void upsertAll(List<PreferenceParam> params);

    @Update
    public int update(PreferenceParam param);

    @Delete int delete(PreferenceParam param);

    @Delete int deleteAll(List<PreferenceParam> params);

    @Query("SELECT * FROM custom_preference_table")
    LiveData<List<PreferenceParam>> getAll();

}
