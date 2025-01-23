package com.example.discountcalc.DataBase;

// データ保存に使うキー達
public class DataStoreKey {
    // プリセットの割引率設定用のキー(DISCOUNT_KEYとの併用想定)
    public static final String DISCOUNT_TYPE_PRESET_KEY="type_preset_";
    // カスタムの割引率設定用のキー(DISCOUNT_KEYとの併用想定)
    public static final String DISCOUNT_TYPE_CUSTOM_KEY="type_custom_";
    //　割引率キー(x%)
    public static final String DISCOUNT_KEY = "discount_key";
    //　
    public static final String DISCOUNT_CUSTOM_SAVE_COUNT_KEY="use_save_count_key";

    public static final String DISCOUNT_ELEMENT_KEY="element_key";


    // 必要かわからん
    public static final String CONFIG_ENUM_KEY = "config_enum_key";
    // 表示数保存キー
    public static final String VIEWCOUNT_KEY = "view_count_key";
    // 設定の種類
    public static final String CONFIG_TYPE_KEY = "config_type_key";
}
