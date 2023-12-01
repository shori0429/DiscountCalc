package com.example.discountcalc;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.discountcalc.databinding.ConfigPresetBinding;


public class ConfigPreset extends AppCompatActivity {
    private ConfigPresetBinding configPresetBinding;

    private Boolean[] enables;

    private Button[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configPresetBinding=ConfigPresetBinding.inflate(getLayoutInflater());

        init();


        setContentView(configPresetBinding.getRoot());
    }

    private void init(){
        enables=new Boolean[enumPresets.PRESET_MAX];
        for(int i=0;i<enables.length;i++){

        }
        setOnclickListeners();
    }

    private void setOnclickListeners(){
//        //ボタンのフラグ反転
//        //3%
//        configPresetBinding.Per3Button.setOnClickListener(v-> enables[enumPresets.PER3]= !enables[enumPresets.PER3]);
//        //5%
//        configPresetBinding.Per5Button.setOnClickListener(v-> enables[enumPresets.PER5]= !enables[enumPresets.PER5]);
//        //10%
//        configPresetBinding.Per10Button.setOnClickListener(v-> enables[enumPresets.PER10]= !enables[enumPresets.PER10]);
//        //20%
//        configPresetBinding.Per20Button.setOnClickListener(v-> enables[enumPresets.PER20]= !enables[enumPresets.PER20]);
//        //25%
//        configPresetBinding.Per25Button.setOnClickListener(v-> enables[enumPresets.PER25]= !enables[enumPresets.PER25]);
//        //30%
//        configPresetBinding.Per30Button.setOnClickListener(v-> enables[enumPresets.PER30]= !enables[enumPresets.PER30]);
//        //40%
//        configPresetBinding.Per40Button.setOnClickListener(v-> enables[enumPresets.PER40]= !enables[enumPresets.PER40]);
//        //50%
//        configPresetBinding.Per50Button.setOnClickListener(v-> enables[enumPresets.PER50]= !enables[enumPresets.PER50]);
//        //80%
//        configPresetBinding.Per80Button.setOnClickListener(v-> enables[enumPresets.PER80]= !enables[enumPresets.PER80]);
    }

    private void setButtonText(){
        for(int i=0;i<enables.length;i++){

        }
    }

}
