package com.example.discountcalc.Params;

public enum DiscountType {
    None(-1),
    Custom(0),
    Const(1);

    private final int value;
    DiscountType(int value){
        this.value=value;
    }

    public int getValue(){return value;}
    public static DiscountType getType(int type){
        for(DiscountType t : values()){
            if(t.getValue()==type){
                return t;
            }
        }
        return null;
    }
}