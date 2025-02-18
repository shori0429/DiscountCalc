package com.example.discountcalc.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CustomPreferenceViewModel extends ViewModel {


    // カスタム割引率の要素名
    private MutableLiveData<Integer> discountElement;

    // 割引率
    private MutableLiveData<Integer> discountPer;

    public CustomPreferenceViewModel(){
        discountElement=new MutableLiveData<>(0);
        discountPer=new MutableLiveData<>(0);
    }

    public MutableLiveData<Integer> getDiscountElement() {
        return discountElement;
    }

    public void setDiscountElement(MutableLiveData<Integer> discountElement) {
        this.discountElement = discountElement;
    }

    public MutableLiveData<Integer> getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(MutableLiveData<Integer> discountPer) {
        this.discountPer = discountPer;
    }
}
