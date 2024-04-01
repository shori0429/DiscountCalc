package com.example.discountcalc.Params;

public class DiscountData {

    // 割引率
    private int discount;
    // 割引額
    private int discountPrice;
    // 価格
    private int price;

    public DiscountData(){
        discount=0;
        discountPrice=0;
        price=0;
    }
    public DiscountData(int discount,int discountPrice,int price){
        this.discount=discount;
        this.discountPrice=discountPrice;
        this.price=price;
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
}
