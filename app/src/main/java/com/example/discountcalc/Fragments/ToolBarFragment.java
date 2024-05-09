package com.example.discountcalc.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.example.discountcalc.R;
import com.example.discountcalc.databinding.FragmentOriginalToolbarBinding;

public class ToolBarFragment extends Fragment implements ToolBarCustomViewDelegate {

    private FragmentOriginalToolbarBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentOriginalToolbarBinding.inflate(inflater,container,false);
        setCustomToolBar();

        return binding.getRoot();

    }

    private void setCustomToolBar(){
        CustomToolBar customToolBar=new CustomToolBar(this.requireContext());
        customToolBar.delegate=this;

        String title=getString(R.string.title_tool_bar);
        customToolBar.configure(title,false,false);

        // カスタムツールバーを挿入するコンテナを指定
        LinearLayoutCompat layoutCompat=binding.layoutCustomToolbar;

        // ツールバーの表示をコンテナに合わせる
        customToolBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

        // カスタムツールバーを表示する
        layoutCompat.addView(customToolBar);
    }

    @Override
    public void onClickedLeftButton() {
        Log.i("ToolBarOnClicked","onClickedLeftButton");
    }

    @Override
    public void onClickedRightButton() {
        Log.i("ToolBarOnClicked","onClickedRightButton");

    }
}
