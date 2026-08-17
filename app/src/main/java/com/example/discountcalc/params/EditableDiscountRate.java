package com.example.discountcalc.params;

///　画面上で編集したデータを保持するクラス
public record EditableDiscountRate(
        long localId, // 編集画面内で項目を一意に識別
        long rateId, // DB保存済みのレコードID

        long preferenceId, // 親設定ID

        int per, // 割引率

        int orderIndex // 現在の並び順
) {}
