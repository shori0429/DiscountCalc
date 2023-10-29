package com.example.discountcalc.CalculationPack;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DiscountCalcTest {

    @Test
    public void discountFunctionUnderTwodigitZero() {
        int result=new DiscountCalc().discountFunction(3500,20);
        Assertions.assertEquals(result, 2800);
    }

    @Test
    public void discountFunctionUnderOnedigitZero(){
        int result=new DiscountCalc().discountFunction(3510,30);
        Assertions.assertEquals(result, 2457);
    }

    @Test
    public void discountFunction(){
        int result=new DiscountCalc().discountFunction(7777,3);
        Assertions.assertEquals(result, 7543);
    }
    @Test
    public void discount() {

    }
}