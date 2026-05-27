package com.example.discountcalc.params;

import androidx.annotation.Nullable;

public class DiscountData {

    // 割引率
    private int discountPer;
    // 割引額
    private int discountPrice;
    // 値引き後価格
    private int afterPrice;

    private int configEnum;

    public DiscountData(){
        discountPer=0;
        discountPrice=0;
        afterPrice=0;
        configEnum=0;
    }
    public DiscountData(int discountPer, int discountPrice, int afterPrice, int configEnum){
        this.discountPer = discountPer;
        this.discountPrice=discountPrice;
        this.afterPrice = afterPrice;
        this.configEnum=configEnum;
    }
    public int getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(int discountPer) {
        this.discountPer = discountPer;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getAfterPrice() {
        return afterPrice;
    }

    public void setAfterPrice(int afterPrice) {
        this.afterPrice = afterPrice;
    }

    public void setConfigEnum(int configEnum) {
        this.configEnum = configEnum;
    }

    public int getConfigEnum() {
        return configEnum;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        boolean equals = false;
        if (obj instanceof DiscountData data) {
            if (this.discountPer == data.discountPer &&
                    this.discountPrice == data.discountPrice &&
                    this.afterPrice == data.afterPrice) {
                equals = true;
            }
        }
        return equals;
    }
}
