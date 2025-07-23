package com.example.discountcalc.params;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "custom_preference_table")
public record PreferenceParam (@PrimaryKey(autoGenerate = true) int uid,
                               @ColumnInfo(name = "save_name") String saveName,
                               @ColumnInfo(name = "per") int per){
    // id指定をしないことでデータベースのuidをオートインクリメントさせる。
    public static PreferenceParam createPreferenceParam(String saveName, int per){
        return new PreferenceParam(0,saveName,per);
    }

    public static PreferenceParam createDefaultParam(){
        return new PreferenceParam(0,"",0);
    }

    @NonNull
    @Override
    public String toString() {
        return "uid:"+uid+", saveName:"+saveName+",per:"+per;
    }
}
