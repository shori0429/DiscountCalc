package com.example.discountcalc;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.discountcalc.Fragments.SettingsFragment;
import com.example.discountcalc.Fragments.TitleFragment;
import com.example.discountcalc.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements TitleFragment.OnClickListener{

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

        //使用するフラグメント宣言
        TitleFragment titleFragment=new TitleFragment();

        //フラグメントを扱うためのManagerとTransactionを取得
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_title_container,titleFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onClick() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_title_container, SettingsFragment.class,null)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }
}