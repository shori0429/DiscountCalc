package com.example.discountcalc.Params;

// カスタム割引率設定に使用するデータクラス
public class CustomPreferenceData {

    // カスタム割引率の要素名
    private int discountElementName;

    // 割引率
    private int discountPer;

    public CustomPreferenceData(){
        discountElementName=0;
        discountPer=0;
    }

    public CustomPreferenceData(int elementName,int per){
        this.discountElementName=elementName;
        this.discountPer=per;
    }

    public int getDiscountElementName() {
        return discountElementName;
    }

    public void setDiscountElementName(int discountElementName) {
        this.discountElementName = discountElementName;
    }

    public int getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(int discountPer) {
        this.discountPer = discountPer;
    }
}
