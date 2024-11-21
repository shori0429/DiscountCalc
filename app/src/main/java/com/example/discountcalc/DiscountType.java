package com.example.discountcalc;

public enum DiscountType {
    None(-1),
    Custom(0),
    Const(1);

    private final int value;
    DiscountType(int value){
        this.value=value;
    }

    public int getValue(){return value;}
}