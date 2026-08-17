package com.example.discountcalc.params;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// ForeignKey:外部キー
// entity:親テーブルクラス指定、parentColumns:親テーブルの参照カラム childColumns:自テーブルで外部キーを適用させるカラム
// onDelete:親カラムが削除されたとき　CASCADE:親カラムの削除と共に削除される
@Entity(tableName = "discount_rate_table",
        foreignKeys = @ForeignKey(
        entity = CustomPreferenceTable.class,
        parentColumns = "preference_id",
        childColumns = "preference_id",
        onDelete = ForeignKey.CASCADE),
        // preference_idとperのセット重複禁止
        indices = {@Index(value = {"preference_id","per"}, unique = true)})
// 割引率テーブル
public record DiscountRateTable(@PrimaryKey(autoGenerate = true)
                                @ColumnInfo(name = "rate_id") long rateId,

                                @ColumnInfo(name ="preference_id") long preferenceId, // FK
                                @ColumnInfo(name = "per") int per,
                                @ColumnInfo(name = "order_index") int orderIndex) {

    public static DiscountRateTable createDiscountRateTable(int per, int orderIndex) {
        return new DiscountRateTable(0,0, per, orderIndex);
    }

}
