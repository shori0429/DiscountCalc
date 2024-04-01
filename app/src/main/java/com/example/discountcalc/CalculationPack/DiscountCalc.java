package com.example.discountcalc.CalculationPack;

//割引計算クラス
public class DiscountCalc {
    /**
     * 税込み価格の割引
     * @param price 価格
     * @param ratio n%割引
     **/

    public static int discountCalculationInTax(int price,int ratio){
        double discount=price*ratio;
        //計算結果の少数を切り捨てて返す
        return (int)(price-discount);
    }

}
