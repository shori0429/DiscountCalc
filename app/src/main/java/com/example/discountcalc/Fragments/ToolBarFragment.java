package com.example.discountcalc.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.discountcalc.R;
import com.example.discountcalc.databinding.FragmentOriginalToolbarBinding;

public class ToolBarFragment extends Fragment implements ToolBarCustomViewDelegate {

    private FragmentOriginalToolbarBinding binding;

    boolean isHideLeftButton=false;
    boolean isHideRightButton=false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentOriginalToolbarBinding.inflate(inflater,container,false);

        setCustomToolBar(inflater);

        return binding.getRoot();
    }

    private void setCustomToolBar(LayoutInflater inflater){
//        CustomToolBar customToolBar=new CustomToolBar(inflater.getContext());
//        customToolBar.delegate=this;

        String title=getString(R.string.toolBarTitle);
//        customToolBar.configure(title,false,false);
        binding.ActionTitle.setText(title);

        setLeftButton(isHideLeftButton);

        setRightButton(isHideRightButton);

//        // カスタムツールバーを挿入するコンテナを指定
//        LinearLayoutCompat layoutCompat=binding.layoutCustomToolbar;
//
//        // ツールバーの表示をコンテナに合わせる
//        customToolBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
//
//        layoutCompat.addView(customToolBar);
    }

    public void setLeftButton(boolean isVisible){
        // 左ボタン
        // ボタンの表示/非表示設定
        if(isVisible) {
            binding.ActionLeftButton.setVisibility(View.INVISIBLE);
        }else{
            binding.ActionLeftButton.setVisibility(View.VISIBLE);
        }
        binding.ActionLeftButton.setOnClickListener(b->{
            onClickedLeftButton();
        });
        //onClickedLeftButton();
    }

    public void setRightButton(boolean isVisible){
        // 右ボタン
        // ボタン表示/非表示設定
        if(isVisible) {
            binding.ActionRightButton.setVisibility(View.INVISIBLE);
        }else{
            binding.ActionRightButton.setVisibility(View.VISIBLE);
        }
        binding.ActionRightButton.setOnClickListener(b->{
            onClickedRightButton();
        });
    }

    @Override
    public void onClickedLeftButton() {
        Log.i("ToolBarOnClicked","onClickedLeftButton");
    }

    @Override
    public void onClickedRightButton() {
        Log.i("ToolBarOnClicked","onClickedRightButton");
        setNavGraphDestination();
    }

    // navigationGraphのDestination遷移を実装
    private void setNavGraphDestination(){
        NavHostFragment navHostFragment=(NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.host_fragment);
        assert navHostFragment != null:"null navHostFragment. TitleFragment.java line:89";
        NavController navHostController=navHostFragment.getNavController();
        NavDirections navDirections=TitleFragmentDirections.actionTitleFragmentToSettingsFragment();
        navHostController.navigate(navDirections);
    }

}
