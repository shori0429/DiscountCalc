package com.example.discountcalc.Params;

// カスタム割引率設定に使用するデータクラス
public class CustomPreferenceData {

    // カスタム割引率の要素名
    private int discountElement;

    // 割引率
    private int discountPer;

    public CustomPreferenceData(){
        discountElement =0;
        discountPer=0;
    }

    public CustomPreferenceData(int elementName,int per){
        this.discountElement =elementName;
        this.discountPer=per;
    }

    public int getDiscountElement() {
        return discountElement;
    }

    // 自然数を返す
    public int getDiscountElementNaturalNumber(){
        return discountElement+1;
    }
    public void setDiscountElement(int discountElement) {
        this.discountElement = discountElement;
    }


    public int getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(int discountPer) {
        this.discountPer = discountPer;
    }
}
