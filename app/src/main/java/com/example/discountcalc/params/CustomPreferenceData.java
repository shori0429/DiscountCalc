package com.example.discountcalc.params;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

// カスタム割引率設定に使用するデータクラス

public class CustomPreferenceData{

    // カスタム割引率の要素名
    private final MutableLiveData<Integer> discountElement=new MutableLiveData<>(0);

    // 割引率
    private final MutableLiveData<Integer> discountPer=new MutableLiveData<>(0);


    public  CustomPreferenceData(){
        discountElement.setValue(0);
        discountPer.setValue(0);
    }

    public CustomPreferenceData(int element,int per){
        discountElement.setValue(element);
        discountPer.setValue(per);
    }


    public void setDiscountElement(int value){
            discountElement.setValue(value);
    }

    public MutableLiveData<Integer> getDiscountElement() {
        return discountElement;
    }

    public int DiscountElement(){
        if(discountElement.getValue()!=null)return discountElement.getValue();
        else return -1;
    }


    public void setDiscountPer(int value){
        discountPer.setValue(value);
    }

    public MutableLiveData<Integer> getDiscountPer() {
        return discountPer;
    }

    public int DiscountPer(){
        if(discountPer.getValue()!=null) return discountPer.getValue();
        else return -1;
    }


    // 自然数を返す
    public MutableLiveData<Integer> getDiscountElementNaturalNumber(){
        return discountElement;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj==null)return false;
        if(obj instanceof CustomPreferenceData){
            CustomPreferenceData data=(CustomPreferenceData) obj;
            if(this.discountElement!=data.discountElement){
                Log.i("CustomPreferenceData_equal","discountElementFalse"+this.discountElement.getValue()+":"+data.discountElement.getValue());
                return false;
            }
            if(this.discountPer!=data.discountPer){
                Log.i("CustomPreferenceData_equal","discountPerFalse"+this.discountPer.getValue()+":"+data.discountPer.getValue());
                return false;
            }
            return true;
        }

        return false;
    }
}
