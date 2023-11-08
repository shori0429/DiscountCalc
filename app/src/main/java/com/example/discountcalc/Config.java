package com.example.discountcalc;

public class Config {
    private boolean[] customDiscounts;
    private boolean[] constDiscounts;

    Config(){

    }

    public void DiscountInit(DiscountType type,int num){
        switch(type){
            case Const:
                constDiscounts= new boolean[num];
                break;
            case Custom:
                customDiscounts= new boolean[num];
                break;
            default:
                break;
        }
    }

    public void setBooleans(DiscountType type,boolean[] flags){
        switch(type){
            case Const:
                System.arraycopy(flags, 0, constDiscounts, 0, constDiscounts.length);
                break;
            case Custom:
                System.arraycopy(flags, 0, customDiscounts, 0, customDiscounts.length);
                break;
            default:
                break;
        }
    }
}