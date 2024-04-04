package com.example.discountcalc.CalculationPack;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

public class ConvertDisplayUnitsHelper {
    public static int dpToPx(int dp,@NonNull Context context){
        DisplayMetrics displayMetrics=context.getResources().getDisplayMetrics();
        return (int)((dp*displayMetrics.density)+0.5);
    }
    public static int pxToDp(int px,@NonNull Context context){
        DisplayMetrics displayMetrics=context.getResources().getDisplayMetrics();
        return (int)((px/displayMetrics.density)+0.5);
    }
}
