package com.example.discountcalc.Params;

public class DiscountData {

    // 割引率
    private int discount;
    // 割引額
    private int discountPrice;
    // 価格
    private int price;

    private int configEnum;

    public DiscountData(){
        discount=0;
        discountPrice=0;
        price=0;
        configEnum=-1;
    }
    public DiscountData(int discount,int discountPrice,int price,int configEnum){
        this.discount=discount;
        this.discountPrice=discountPrice;
        this.price=price;
        this.configEnum=configEnum;
    }
    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setConfigEnum(int configEnum) {
        this.configEnum = configEnum;
    }

    public int getConfigEnum() {
        return configEnum;
    }
}
