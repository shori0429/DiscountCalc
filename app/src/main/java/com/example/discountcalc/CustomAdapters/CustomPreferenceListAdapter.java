package com.example.discountcalc.CustomAdapters;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discountcalc.Params.CustomPreferenceData;
import com.example.discountcalc.ViewModels.CustomPreferenceListViewModel;
import com.example.discountcalc.databinding.CustomPreferenceOneLineBinding;

import java.util.ArrayList;
import java.util.List;

public class CustomPreferenceListAdapter extends RecyclerBaseAdapter {


    @Override
    public boolean areContentsTheSame(List<Object> oldList, List<Object> newList, int oldItemPosition, int newItemPosition) {
        return false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }
}
