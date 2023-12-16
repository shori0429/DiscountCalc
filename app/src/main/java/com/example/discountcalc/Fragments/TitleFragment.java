package com.example.discountcalc.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.discountcalc.R;
import com.example.discountcalc.databinding.CalcTitleBinding;

public class TitleFragment extends Fragment {

    private CalcTitleBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calc_title, container, false);
    }

    public void onViewCreated(@NonNull View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }
}
