package com.example.discountcalc.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.discountcalc.params.PreferenceParam;

import java.util.List;

@Dao
public interface PreferenceParamDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertPreferenceParam(PreferenceParam... params);

    @Update
    public int updatePreferenceParam(PreferenceParam... params);

    @Delete int deletePreferenceParams(PreferenceParam... params);

    @Query("SELECT save_title FROM custom_preference_table")
    public List<PreferenceParam> loadPreferenceParam();

}
