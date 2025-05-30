package com.example.discountcalc.params;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "custom_preference_table")
public record PreferenceParam (@PrimaryKey(autoGenerate = true) int uid,
                               @ColumnInfo(name = "save_name") String saveName,
                               int per){ }
