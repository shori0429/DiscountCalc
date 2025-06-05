package com.example.discountcalc.params;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "custom_preference_table")
public record PreferenceParam (@PrimaryKey(autoGenerate = true) int uid,
                               @ColumnInfo(name = "save_name") String saveName,
                               @ColumnInfo(name = "per") int per){

    @NonNull
    @Override
    public String toString() {
        return "uid:"+uid+", saveName:"+saveName+",per:"+per;
    }
}
