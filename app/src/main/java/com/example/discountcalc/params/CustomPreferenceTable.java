package com.example.discountcalc.params;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// 設定名テーブル
@Entity(tableName = "preference_table")
public record CustomPreferenceTable(@PrimaryKey(autoGenerate = true) long preferenceId,
                                    @ColumnInfo(name = "save_name") String saveName,
                                    @ColumnInfo(name = "orderIndex") int orderIndex) {
    public static CustomPreferenceTable createPreferenceParam(String saveName, int orderIndex) {
        return new CustomPreferenceTable(0, saveName, orderIndex);
    }

}
