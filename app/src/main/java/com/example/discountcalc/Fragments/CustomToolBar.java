package com.example.discountcalc.Fragments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.discountcalc.R;

interface ToolBarCustomViewDelegate{
    void onClickedLeftButton();
    void onClickedRightButton();
}

public class CustomToolBar extends LinearLayout {
    ToolBarCustomViewDelegate delegate=null;

    CustomToolBar(Context context){
        super(context);
        init(context,null,0);
    }
    CustomToolBar(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context,attrs,0);
    }

    CustomToolBar(Context context,AttributeSet attrs,int defStyle){
        super(context, attrs,defStyle);
        init(context,attrs,defStyle);
    }

    private void init(Context context,AttributeSet attrs,int defStyle){
        LayoutInflater.from(context).inflate(R.layout.fragment_original_toolbar,this,true);
    }

    public void configure(String titleText,Boolean isHideLeftButton,Boolean isHideRightButton){
        TextView centerText=findViewById(R.id.Action_Title);
        ImageButton leftButton=findViewById(R.id.Action_LeftButton);
        ImageButton rightButton=findViewById(R.id.Action_ButtonRight);

        // textに文字設定
        centerText.setText(titleText);

        // 左ボタン
        // ボタンの表示/非表示設定
        if(isHideLeftButton) {
            leftButton.setVisibility(View.INVISIBLE);
        }else{
            leftButton.setVisibility(View.VISIBLE);
        }

        // 右ボタン
        // ボタン表示/非表示設定
        if(isHideRightButton) {
            rightButton.setVisibility(View.INVISIBLE);
        }else{
            rightButton.setVisibility(View.VISIBLE);
        }

        // ボタンがクリックされた時のリスナー設定
        // 実際の処理は画面ごとのFragmentで設定
        leftButton.setOnClickListener(b->{
            delegate.onClickedLeftButton();
        });

        rightButton.setOnClickListener(b->{
            delegate.onClickedRightButton();
        });

    }
}
