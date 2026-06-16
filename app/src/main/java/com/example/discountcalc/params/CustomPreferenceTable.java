package com.example.discountcalc.params;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// 設定名テーブル
@Entity(tableName = "preference_table")
public record CustomPreferenceTable(@PrimaryKey(autoGenerate = true)
                                    @ColumnInfo(name = "preference_id")
                                    long preferenceId,
                                    @ColumnInfo(name = "save_name") String saveName,
                                    @ColumnInfo(name = "order_index") int orderIndex) {
    public static CustomPreferenceTable createPreferenceParam(String saveName, int orderIndex) {
        return new CustomPreferenceTable(0, saveName, orderIndex);
    }

}
