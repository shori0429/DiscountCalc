package com.example.discountcalc.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.discountcalc.CustomAdapters.CustomPreferenceAdapter;
import com.example.discountcalc.Params.CustomPreferenceData;
import com.example.discountcalc.databinding.CustomDiscountPreferenceFragmentBinding;

import java.util.ArrayList;

public class CustomDiscountPreferenceFragment extends Fragment {

    CustomDiscountPreferenceFragmentBinding customDiscountPrefenreceFragmentBinding;
    RecyclerView recyclerView;
    CustomPreferenceAdapter customPreferenceAdapter;
    ArrayList<CustomPreferenceData> preferenceDataSet;

    View view;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewInitialize();
    }

    private void recyclerViewInitialize() {
        recyclerView=customDiscountPrefenreceFragmentBinding.PreferenceList;
        customPreferenceAdapter=new CustomPreferenceAdapter(preferenceDataSet);
        LinearLayoutManager llm=new LinearLayoutManager(view.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(customPreferenceAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        customDiscountPrefenreceFragmentBinding=CustomDiscountPreferenceFragmentBinding.inflate(inflater,container,false);
        view=customDiscountPrefenreceFragmentBinding.getRoot();

        return view;
    }


}