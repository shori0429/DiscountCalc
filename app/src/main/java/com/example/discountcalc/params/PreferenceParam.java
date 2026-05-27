package com.example.discountcalc.params;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "custom_preference_table",
        indices = {@Index(value = {"per", "save_name"}, unique = true)})
public record PreferenceParam(@PrimaryKey(autoGenerate = true) int uid,
                              @ColumnInfo(name = "order_index") int orderIndex,
                              @ColumnInfo(name = "per") int per,
                              @ColumnInfo(name = "save_name") String saveName) {
    // id指定をしないことで保存時データベースのuidをオートインクリメントさせる。
    public static PreferenceParam createPreferenceParam(int orderIndex, int per, String saveName) {
        return new PreferenceParam(0,orderIndex, per, saveName);
    }

//    public static PreferenceParam createDefaultParam() {
//        return new PreferenceParam(0, 0,0, "");
//    }

    // デフォルト値追加
    // 追加したいリストのサイズを引数に
    public static PreferenceParam createDefaultParam(int size){
        return new PreferenceParam(0,Math.incrementExact(size),0,"");
    }

    @NonNull
    @Override
    public String toString() {
        return "uid:"+uid+", orderIndex:"+orderIndex+", saveName:"+saveName+",per:"+per;
    }
}
