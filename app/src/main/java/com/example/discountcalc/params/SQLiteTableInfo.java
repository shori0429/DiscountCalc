package com.example.discountcalc.params;

public record SQLiteTableInfo(
        int uid, //カラムid
        String name, // カラム名
        String type, // データ型
        int notNull, //NotNull制約
        String default_value, //デフォルト値
        int pk // 主キー(0 or 1)
){}
