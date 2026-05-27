package com.example.discountcalc.calculationPack;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

public class ConvertDisplayUnitsHelper {
    // dp→px変換
    public static int dpToPx(int dp,@NonNull Context context){
        DisplayMetrics displayMetrics=context.getResources().getDisplayMetrics();
        return (int)((dp*displayMetrics.density)+0.5);
    }

    // px→dp変換
    public static int pxToDp(int px,@NonNull Context context){
        DisplayMetrics displayMetrics=context.getResources().getDisplayMetrics();
        return (int)((px/displayMetrics.density)+0.5);
    }
}
