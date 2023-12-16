package com.example.discountcalc;

import android.os.Bundle;
import android.widget.Toast;

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
        String text="Button Click";
        calcTitleBinding.configButton.setOnClickListener(v-> Toast.makeText(this,text,Toast.LENGTH_SHORT).show());
    }
}
