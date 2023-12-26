package com.example.discountcalc.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.discountcalc.R;
import com.example.discountcalc.databinding.CalcTitleBinding;

public class TitleFragment extends Fragment {

    private CalcTitleBinding calcTitleBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        calcTitleBinding = CalcTitleBinding.inflate(inflater, container, false);
        return calcTitleBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = calcTitleBinding.getRoot();
        setClickListeners(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        calcTitleBinding = null;
    }


    private void setClickListeners(View view) {
        String text = "Button Click";
        calcTitleBinding.configButton.setOnClickListener(v -> {
            Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT).show();
            Log.i("test", text);
            NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = null;
            if (navHostFragment != null) {
                navController = navHostFragment.getNavController();
            }

            NavDirections action = TitleFragmentDirections.actionTitleFragmentToSettingsFragment();
            if (navController != null) {
                navController.navigate(action);
            }
        });
    }
}
