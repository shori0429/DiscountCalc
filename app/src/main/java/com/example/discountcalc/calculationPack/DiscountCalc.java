package com.example.discountcalc.calculationPack;

//割引計算クラス
public class DiscountCalc {
    /**
     * 税込み価格の割引
     * @param price 価格
     * @param ratio n%割引
     **/

    public static int discountCalculationPercentage(int price, double ratio){
        double discount=price*ratio;
        //計算結果の少数を切り捨てて返す
        return (int)Math.ceil(discount);
    }

    public static int discountCalculationIntPercentage(int price,double ratio){
        double discount=price*(ratio/100);
        //計算結果の少数を切り捨てて返す
        return (int)Math.ceil(discount);
    }
}
