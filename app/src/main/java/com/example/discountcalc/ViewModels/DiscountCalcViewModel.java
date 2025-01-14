package com.example.discountcalc.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DiscountCalcViewModel extends ViewModel {
    // 価格
    private final MutableLiveData<Integer> price;
    // 表示数
    private final MutableLiveData<Integer> viewCount;
    //
    private final MutableLiveData<Integer> configEnum;

    private DiscountCalcViewModel(){

        price=new MutableLiveData<>(0);
        viewCount=new MutableLiveData<>(0);
        configEnum=new MutableLiveData<>(0);
        Log.i("DiscountCalcViewModel","DiscountCalcViewModel created:("+price+","+viewCount+","+configEnum+")");
    }

    public LiveData<Integer> getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price.setValue(price);
    }

    public LiveData<Integer> getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount.setValue(viewCount);
    }

    public LiveData<Integer> getConfigEnum() {
        return configEnum;
    }

    public void setConfigEnum(int configEnum) {
        this.configEnum.setValue(configEnum);
    }
}
