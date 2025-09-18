package com.example.discountcalc.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.discountcalc.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity{
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
    }


    @Override
    protected void onStart() {
        super.onStart();

        // 空白箇所タップでフォーカスを外す(子フラグメントのeditText用)
        binding.getRoot().setOnClickListener(v->{
            InputMethodManager imm=(InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            v.clearFocus();
        });
    }

}