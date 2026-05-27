package com.example.discountcalc.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.discountcalc.R;
import com.example.discountcalc.databinding.FragmentOriginalToolbarBinding;

import java.util.Objects;

public class ToolBarFragment extends Fragment implements ToolBarCustomViewDelegate {

    private FragmentOriginalToolbarBinding binding;

    NavHostFragment navHostFragment;
    NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentOriginalToolbarBinding.inflate(inflater,container,false);

        navHostFragment=(NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.host_fragment);
        navController=navHostFragment.getNavController();
        setCustomToolBar();
        assert navHostFragment != null:"null navHostFragment. TitleFragment.java line:89";
        return binding.getRoot();
    }

    private void setCustomToolBar(){

        // navGraphのフラグメントが切り替わった時のイベントリスナーを更新
        navController.addOnDestinationChangedListener((((nController, nDestination, bundle) -> {
            int id = Objects.requireNonNull(nController.getCurrentDestination()).getId();
            if (id == R.id.nav_titleFragment) {
                binding.ActionTitle.setText(R.string.toolBar_title);
                binding.ActionRightButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.setting_image,null));
                setLeftButton(false);
                setRightButton(true);
            } else if (id == R.id.nav_settingsFragment) {
                binding.ActionTitle.setText(R.string.toolBar_setting);
                setLeftButton(true);
                setRightButton(false);
            } else if (id == R.id.nav_customDiscountPreferenceFragment) {
                binding.ActionTitle.setText(R.string.toolBar_customPreferenceSetting);
                binding.ActionRightButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.home_image,null));
                setLeftButton(true);
                setRightButton(true);
            }
        })));


    }

    public void setLeftButton(boolean isVisible){
        // 左ボタン
        // ボタンの表示/非表示設定
        if(isVisible) {
            binding.ActionLeftButton.setVisibility(View.VISIBLE);
            binding.ActionLeftButton.setOnClickListener(b->{
                onClickedLeftButton();
            });
        }else{
            binding.ActionLeftButton.setVisibility(View.INVISIBLE);
        }
        //onClickedLeftButton();
    }

    public void setRightButton(boolean isVisible){
        // 右ボタン
        // ボタン表示/非表示設定
        if(isVisible) {
            binding.ActionRightButton.setVisibility(View.VISIBLE);
            binding.ActionRightButton.setOnClickListener(b->{
                onClickedRightButton();
            });
        }else{
            binding.ActionRightButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClickedLeftButton() {
        Log.i("ToolBarOnClicked","onClickedLeftButton");
        int id = Objects.requireNonNull(navController.getCurrentDestination()).getId();
            CharSequence label=navController.getCurrentDestination().getLabel();

            // タイトル画面時の処理
            if (id == R.id.nav_titleFragment) {
                Log.i("toolBarFragment", "current fragment:" + label);
            }
            // 設定トップ画面時の処理
            if (id == R.id.nav_settingsFragment
                    || id ==R.id.nav_customDiscountPreferenceFragment) {
                Log.i("toolBarFragment", "current fragment:" + label);
                navController.popBackStack();
            }
//            // カスタム割引率設定画面の処理
//            if (id == R.id.nav_customDiscountPreferenceFragment) {
//                Log.i("toolBarFragment", "current fragment:" + label);
//            }
    }

    @Override
    public void onClickedRightButton() {
        Log.i("ToolBarOnClicked","onClickedRightButton");
        if (navController.getCurrentDestination() != null) {
            int id =  navController.getCurrentDestination().getId();
            CharSequence label=navController.getCurrentDestination().getLabel();

            // タイトル画面時の処理
            if (id == R.id.nav_titleFragment) {
                navController.navigate(R.id.nav_settingsFragment);
                Log.i("toolBarFragment", "next:" + R.id.nav_settingsFragment);
            }
            // 設定トップ画面時の処理
            if (id == R.id.nav_settingsFragment) {

            }
            // カスタム割引率設定画面の処理
            if (id == R.id.nav_customDiscountPreferenceFragment) {
                navController.navigate(R.id.nav_titleFragment);
                Log.i("toolBarFragment", "Go To Home");
            }
        }
    }

    private void setNavGraphMovement(NavDirections nextFragment){
        NavController navHostController=navHostFragment.getNavController();
        if(nextFragment!=null) {
            navHostController.navigate(nextFragment);
        }

    }

}
