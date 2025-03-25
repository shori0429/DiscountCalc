package com.example.discountcalc.params;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

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

    public LiveData<String> discountElementString = Transformations.map(discountElement,String::valueOf);


    public void setDiscountPer(int value){
        discountPer.setValue(value);
    }

    public MutableLiveData<Integer> getDiscountPer() {
        return discountPer;
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
            if(this.discountElement!=data.discountElement)return false;
            if(this.discountPer!=data.discountPer)return false;
            return true;
        }

        return false;
    }
}
