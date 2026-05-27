package com.example.discountcalc.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.discountcalc.R;
import com.example.discountcalc.databinding.ActivityMainBinding;
import com.example.discountcalc.fragments.ToolBarFragment;

public class MainActivity extends AppCompatActivity{
    private ActivityMainBinding binding;

    ToolBarFragment toolBarFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        if(savedInstanceState == null){
            toolBarFragment=new ToolBarFragment();

            // ツールバーフラグメントを配置
            FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.Main_ToolBarView,toolBarFragment)
                    .commit();
        }else {
            toolBarFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.Main_ToolBarView);
        }
        // 空白箇所タップでフォーカスを外す(子フラグメントのeditText用)
        binding.getRoot().setOnClickListener(v->{
            InputMethodManager imm=(InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            v.clearFocus();
        });
    }

}