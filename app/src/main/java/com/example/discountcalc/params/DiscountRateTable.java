package com.example.discountcalc.params;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// ForeignKey:外部キー
// entity:親テーブルクラス指定、parentColumns:親テーブルの参照カラム childColumns:自テーブルで外部キーを適用させるカラム
// onDelete:親カラムが削除されたとき　CASCADE:親カラムの削除と共に削除される
@Entity(foreignKeys = @ForeignKey(
        entity = CustomPreferenceTable.class,
        parentColumns = "preferenceId",
        childColumns = "rateId",
        onDelete = ForeignKey.CASCADE),
        // perの重複禁止
        indices = {@Index(value = "per", unique = true)})
// 割引率テーブル
public record DiscountRateTable(@PrimaryKey(autoGenerate = true) long rateId,
                                @ColumnInfo(name = "per") int per,
                                @ColumnInfo(name = "order_index") int orderIndex) {

    public static DiscountRateTable createDiscountRateTable(int per, int orderIndex) {
        return new DiscountRateTable(0, per, orderIndex);
    }

}
