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
    public void insertPreferenceParam(PreferenceParam... params);

    @Upsert
    public void upsertPreferenceParam(PreferenceParam param);

    @Update
    public int updatePreferenceParam(PreferenceParam param);

    @Delete int deletePreferenceParams(PreferenceParam param);

    @Query("SELECT * FROM custom_preference_table")
    LiveData<List<PreferenceParam>> getAll();

}
