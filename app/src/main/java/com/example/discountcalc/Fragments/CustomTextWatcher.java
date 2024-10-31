package com.example.discountcalc.Fragments;

import android.text.Editable;
import android.text.TextWatcher;

public interface CustomTextWatcher extends TextWatcher {
    // 文字列が修正される直前に呼び出されるメソッド
    @Override
    default void beforeTextChanged(CharSequence s, int start, int count, int after){}

    // 文字一つを入力したときに呼び出される
    @Override
    default void onTextChanged(CharSequence s, int start, int before, int count){}

    // 最後にこのメソッドが呼び出される
    @Override
    default void afterTextChanged(Editable s){}
}
