package com.example.discountcalc.customAdapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discountcalc.databinding.SavedataListviewOnelineBinding;
import com.example.discountcalc.params.CustomPreferenceTable;
import com.example.discountcalc.viewModels.CustomPreferenceListViewModel;

import java.util.List;

/// 保存された設定名をリスト表示する為のアダプタクラス
public class SaveDataListViewAdapter
        extends ListAdapter<String, SaveDataListViewAdapter.SaveDataListViewHolder>{

    private SavedataListviewOnelineBinding binding;

    // 何番目の読込ボタンが押されたかを記録するフィールド
    private MutableLiveData<String> loadButtonPushPosition;

    private CustomPreferenceListViewModel customPreferenceListViewModel;

    public static class SaveDataListViewHolder extends RecyclerView.ViewHolder{

        private List<CustomPreferenceTable> preferenceParamList;

        private final SavedataListviewOnelineBinding binding;

        public SaveDataListViewHolder(SavedataListviewOnelineBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bind(String param,CustomPreferenceListViewModel viewModel){
            binding.saveTitle.setText(param);
            binding.buttonDelete.setOnClickListener(v->{
                //saviewModel.deleteSaveData(param);
            });
            binding.executePendingBindings();
        }
    }


    public SaveDataListViewAdapter(CustomPreferenceListViewModel viewModel){
        super(DIFF_CALLBACK);
        loadButtonPushPosition =new MutableLiveData<>();
        if(viewModel==null)return;
        customPreferenceListViewModel=viewModel;

    }
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

        holder.bind(data,customPreferenceListViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull SaveDataListViewHolder holder, int position, @NonNull List<Object> payloads) {
        String data=getItem(position);

        if(payloads.isEmpty()){
            holder.bind(data,customPreferenceListViewModel);
        }else{
            for(Object payload:payloads){
                if("saveTitle".equals(payload)){
                    holder.bind(data,customPreferenceListViewModel);
                }
            }
        }

        holder.bind(data,customPreferenceListViewModel);
        holder.binding.buttonLoad.setOnClickListener(v->{
            loadButtonPushPosition.setValue(data);
        });
    }

    @Override
    public void submitList(@Nullable List<String> list) {
        super.submitList(list);
    }

    public LiveData<String> PushPosition(){
        return loadButtonPushPosition;
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
