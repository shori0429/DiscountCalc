package com.example.discountcalc.CalculationPack;

public class DiscountCalc {

    private int tax;
    public DiscountCalc(){

    }
    public int discountFunction(int price,int ratio){
        // %計算にするため
        double ratioPer=ratio*0.01;

        double discount=price*ratioPer;
        //計算結果の少数を切り捨てて返す
        return (int)(price-discount);
    }


    public void setTax(int tax) {
        this.tax = tax;
    }

    public int getTax() {
        return tax;
    }
}
