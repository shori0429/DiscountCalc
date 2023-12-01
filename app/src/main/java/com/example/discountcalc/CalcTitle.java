package com.example.discountcalc;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.discountcalc.databinding.CalcTitleBinding;

public class CalcTitle extends AppCompatActivity {
    private CalcTitleBinding calcTitleBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calcTitleBinding=CalcTitleBinding.inflate(getLayoutInflater());

        setContentView(calcTitleBinding.getRoot());
        setOnclickListeners();
    }

    private void setOnclickListeners(){
        calcTitleBinding.configButton.setOnClickListener(v->{

        });
    }
}
