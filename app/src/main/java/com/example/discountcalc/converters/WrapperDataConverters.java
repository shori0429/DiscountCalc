package com.example.discountcalc.converters;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class WrapperDataConverters {
    // EditTextにIntegerをセット
    public static void liveDataIntegerToEditText(EditText view,LiveData<Integer> value){
        if(!view.getText().toString().equals(String.valueOf(value))){
            view.setText(String.valueOf(value));
        }
    }

    public static LiveData<Integer> editTextToLiveDataInteger(EditText view){
        try{
            return new MutableLiveData<>(Integer.parseInt(view.getText().toString()));
        }catch (NumberFormatException e){
            return new MutableLiveData<>(-1);
        }
    }
//
//    // EditTextのテキストをIntegerに変換
//    @InverseMethod("LiveDataIntegerToEditText")
//    public static Integer getIntegerText(EditText view){
//        try{
//            return Integer.parseInt(view.getText().toString());
//        }catch (NumberFormatException e){
//            return -1;
//        }
//    }

    // 双方向バインディング用の変更リスナー
    @BindingAdapter("textAttrChanged")
    public static void setTextWatcher(EditText view, final InverseBindingListener listener){
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listener.onChange();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}
