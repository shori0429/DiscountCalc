package com.example.discountcalc.CustomAdapters;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discountcalc.databinding.CustomPreferenceOneLineBinding;
import com.example.discountcalc.params.CustomPreferenceData;
import com.example.discountcalc.viewModels.CustomPreferenceListViewModel;

import java.util.ArrayList;
import java.util.List;

public class CustomPreferenceListAdapter extends ListAdapter<CustomPreferenceData, CustomPreferenceListAdapter.CustomPreferenceListViewHolder> {

    private List<CustomPreferenceData> preferenceDataList=new ArrayList<>(0);
    private int mainTextSize;
    private final AsyncListDiffer<CustomPreferenceData> aDiffer=new AsyncListDiffer<>(this,DIFF_CALLBACK);

    public static class CustomPreferenceListViewHolder extends RecyclerView.ViewHolder implements LifecycleOwner {
        private final LifecycleRegistry lifecycle=new LifecycleRegistry(this);
        private final CustomPreferenceOneLineBinding binding;


        //
        public CustomPreferenceListViewHolder(CustomPreferenceOneLineBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }

        // 各Viewに関連付け+購読
        void bind(CustomPreferenceData viewmodel){
            viewmodel.getDiscountElement().observe(this,t->binding.customPreferenceOneLineTitle.setText(String.valueOf(t+1)));
            viewmodel.getDiscountPer().observe(this,v->binding.customPreferenceOneLineNum.setText(String.valueOf(v)));
            Log.i("OneLineNum",binding.customPreferenceOneLineNum.getText().toString());
        }

        public void changeTextSize(int textSize){
            if(textSize>0) {
                // 文字サイズ設定
                binding.customPreferenceOneLineTitle.setTextSize(textSize);
                binding.customPreferenceOneLineNum.setTextSize(textSize);
            }
        }

        public void alignmentText(int gravity) {
            // 文字のGravityを変更(右寄せ)
            binding.customPreferenceOneLineTitle.setGravity(gravity);
            binding.customPreferenceOneLineNum.setGravity(gravity);
            binding.customPreferenceOneLineNumContainer.setOnClickListener(l -> {
                Log.i("RecyclerViewClickEvent", binding.customPreferenceOneLineTitle.getText() + " : " + binding.customPreferenceOneLineNum.getText());
            });
        }

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return lifecycle;
        }


    }

    public CustomPreferenceListAdapter(CustomPreferenceListViewModel viewModel){
        super(DIFF_CALLBACK);
    }

    public CustomPreferenceListAdapter(CustomPreferenceListViewModel viewModel,LifecycleOwner lifecycleOwner){
        super(DIFF_CALLBACK);
        viewModel.getCustomPreferenceDatas().observe(lifecycleOwner, data->{
            preferenceDataList=data;
        });
    }

    @NonNull
    @Override
    public CustomPreferenceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        CustomPreferenceOneLineBinding binding=CustomPreferenceOneLineBinding.inflate(inflater,parent,false);
        return new CustomPreferenceListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomPreferenceListViewHolder holder, int position) {
        CustomPreferenceData data=aDiffer.getCurrentList().get(position);
        holder.bind(data);

        // テキストサイズ変更
        holder.changeTextSize(mainTextSize);

        //
        holder.alignmentText(Gravity.END);

    }

    public void setTextSizes(int textSize){
        this.mainTextSize=textSize;
    }

    @Override
    public int getItemCount() {
        return aDiffer.getCurrentList().size();
    }

    @Override
    public void submitList(@Nullable List<CustomPreferenceData> list) {
        aDiffer.submitList(list);
    }

    private static final DiffUtil.ItemCallback<CustomPreferenceData> DIFF_CALLBACK=
            new DiffUtil.ItemCallback<CustomPreferenceData>() {
                @Override
                public boolean areItemsTheSame(@NonNull CustomPreferenceData oldItem, @NonNull CustomPreferenceData newItem) {
                    return oldItem.getDiscountPer()==newItem.getDiscountPer();

                }

                @Override
                public boolean areContentsTheSame(@NonNull CustomPreferenceData oldItem, @NonNull CustomPreferenceData newItem) {
                    return oldItem.equals(newItem);
                }
            };
}
