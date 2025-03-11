package com.example.discountcalc.Params;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

// カスタム割引率設定に使用するデータクラス
public class CustomPreferenceData {

    // カスタム割引率の要素名
    private final MutableLiveData<Integer> discountElement=new MutableLiveData<>(0);

    // 割引率
    private final MutableLiveData<Integer> discountPer=new MutableLiveData<>(0);

    public  CustomPreferenceData(){
        discountElement.postValue(0);
        discountPer.postValue(0);
    }

    public CustomPreferenceData(int element,int per){
        discountElement.postValue(element);
        discountPer.postValue(per);
    }


    public LiveData<Integer> getDiscountElement() {
        return discountElement;
    }

    // 自然数を返す
    public MutableLiveData<Integer> getDiscountElementNaturalNumber(){
        return discountElement;
    }


    public MutableLiveData<Integer> getDiscountPer() {
        return discountPer;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj==null)return false;
        if(obj instanceof CustomPreferenceData){
            CustomPreferenceData data=(CustomPreferenceData) obj;
            if(this.discountElement!=data.discountElement)return false;
            if(this.discountPer!=data.discountPer)return false;
            return true;
        }

        return false;
    }
}
