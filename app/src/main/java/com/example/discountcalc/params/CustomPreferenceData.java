package com.example.discountcalc.params;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

// カスタム割引率設定に使用するデータクラス

public class CustomPreferenceData{

    // 何番目か
    private final MutableLiveData<Integer> discountNo =new MutableLiveData<>(0);

    // 割引率
    private final MutableLiveData<Integer> discountPer=new MutableLiveData<>(0);

    // どのカスタム設定に所属しているか
    private String saveName="";


    public  CustomPreferenceData(){
        discountNo.setValue(0);
        discountPer.setValue(0);
    }

    public CustomPreferenceData(int no,int per,String saveName){
        discountNo.setValue(no);
        discountPer.setValue(per);
        this.saveName=saveName;
    }


    public void setDiscountNo(int value){
            discountNo.setValue(value);
    }

    public MutableLiveData<Integer> getDiscountNo() {
        return discountNo;
    }

    public int DiscountNo(){
        if(discountNo.getValue()!=null)return discountNo.getValue();
        else return -1;
    }


    public void setDiscountPer(int value){
        discountPer.setValue(value);
    }

    public MutableLiveData<Integer> getDiscountPer() {
        return discountPer;
    }

    public int DiscountPer(){
        if(discountPer.getValue()!=null) return discountPer.getValue();
        else return -1;
    }

    public String SaveName(){
        return saveName;
    }


    // 自然数を返す
    public MutableLiveData<Integer> getDiscountElementNaturalNumber(){
        return discountNo;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj==null)return false;
        if(obj instanceof CustomPreferenceData){
            CustomPreferenceData data=(CustomPreferenceData) obj;
            if(this.discountNo !=data.discountNo){
                Log.i("CustomPreferenceData_equal","discountNoFalse"+this.discountNo.getValue()+":"+data.discountNo.getValue());
                return false;
            }
            if(this.discountPer!=data.discountPer){
                Log.i("CustomPreferenceData_equal","discountPerFalse"+this.discountPer.getValue()+":"+data.discountPer.getValue());
                return false;
            }
            if(!saveName.equals(data.saveName)){
                Log.i("CustomPreferenceData_equal","discountSaveNameFalse["+this.saveName+"]:["+data.saveName+"]");
                return false;
            }
            return true;
        }

        return false;
    }
}
