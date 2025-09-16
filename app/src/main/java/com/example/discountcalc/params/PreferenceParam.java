package com.example.discountcalc.params;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "custom_preference_table")
public record PreferenceParam (@PrimaryKey(autoGenerate = true) int uid,
                               @ColumnInfo(name = "per") int per,
                               @ColumnInfo(name = "save_name") String saveName){
    // id指定をしないことでデータベースのuidをオートインクリメントさせる。
    public static PreferenceParam createPreferenceParam(int per,String saveName){
        return new PreferenceParam(0,per,saveName);
    }

    public static PreferenceParam createDefaultParam(){
        return new PreferenceParam(1, 0,"");
    }

    @NonNull
    @Override
    public String toString() {
        return "uid:"+uid+", saveName:"+saveName+",per:"+per;
    }
}
