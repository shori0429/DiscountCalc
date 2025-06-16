package com.example.discountcalc.customAdapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discountcalc.databinding.SavedataListviewOnelineBinding;
import com.example.discountcalc.params.PreferenceParam;

import java.util.List;

public class SaveDataListViewAdapter
        extends ListAdapter<String, SaveDataListViewAdapter.SaveDataListViewHolder>{

    private SavedataListviewOnelineBinding binding;

    public static class SaveDataListViewHolder extends RecyclerView.ViewHolder{

        private List<PreferenceParam> preferenceParamList;

        private final SavedataListviewOnelineBinding binding;

        public SaveDataListViewHolder(SavedataListviewOnelineBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bind(String param){
            binding.saveTitle.setText(param);
            binding.executePendingBindings();
        }
    }

    public SaveDataListViewAdapter(){super(DIFF_CALLBACK);}
    @NonNull
    @Override
    public SaveDataListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        binding=SavedataListviewOnelineBinding.inflate(inflater,parent,false);
        return new SaveDataListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SaveDataListViewHolder holder, int position) {
        String data=getItem(position);

        holder.bind(data);
    }

    @Override
    public void onBindViewHolder(@NonNull SaveDataListViewHolder holder, int position, @NonNull List<Object> payloads) {
        String data=getItem(position);

        if(payloads.isEmpty()){
            holder.bind(data);
        }else{
            for(Object payload:payloads){
                if("saveTitle".equals(payload)){
                    holder.bind(data);
                }
            }
        }

        holder.bind(data);
    }

    @Override
    public void submitList(@Nullable List<String> list) {
        super.submitList(list);
    }

    private static final DiffUtil.ItemCallback<String> DIFF_CALLBACK=
            new DiffUtil.ItemCallback<String>() {
                @Override
                public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }

                @Nullable
                @Override
                public Object getChangePayload(@NonNull String oldItem, @NonNull String newItem) {
                    Bundle diff=new Bundle();
                    if(oldItem.equals(newItem)){
                        diff.putString("saveName", newItem);
                    }
                    return diff;
                }
            };
}
