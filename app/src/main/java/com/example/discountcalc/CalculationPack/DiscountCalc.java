package com.example.discountcalc.CalculationPack;

//割引計算クラス
public class DiscountCalc {

    //税
    private int tax;
    public DiscountCalc(){

    }

    /**
     * @param price 価格
     * @param ratio n割引
     **/

    public int discountFunction(int price,int ratio){
        // %計算にするため
        double ratioPer=ratio*0.01;

        double discount=price*ratioPer;
        //計算結果の少数を切り捨てて返す
        return (int)(price-discount);
    }

    /**
     * @param price 価格
     * @param ratio n%引き
     **/
    public int discountFunction(int price,float ratio){
        double discount=price*ratio;
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
