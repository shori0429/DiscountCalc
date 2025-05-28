package com.example.discountcalc.customAdapters;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discountcalc.BR;
import com.example.discountcalc.databinding.CustomPreferenceOneLineBinding;
import com.example.discountcalc.params.CustomPreferenceData;
import com.example.discountcalc.viewModels.CustomPreferenceListViewModel;

import java.util.List;

public class CustomPreferenceListAdapter
        extends ListAdapter<CustomPreferenceData, CustomPreferenceListAdapter.CustomPreferenceListViewHolder> {

    private int mainTextSize;
    //private final AsyncListDiffer<CustomPreferenceData> aDiffer=new AsyncListDiffer<>(this,DIFF_CALLBACK);

    CustomPreferenceOneLineBinding binding;

    public static class CustomPreferenceListViewHolder extends RecyclerView.ViewHolder implements LifecycleOwner {
        private final LifecycleRegistry lifecycle=new LifecycleRegistry(this);
        private final CustomPreferenceOneLineBinding binding;

        private final ViewDataBinding viewDataBinding;

        //
        public CustomPreferenceListViewHolder(CustomPreferenceOneLineBinding binding){
            super(binding.getRoot());
            this.binding=binding;
            viewDataBinding= DataBindingUtil.bind(binding.getRoot());
        }

        public ViewDataBinding getViewDataBinding(){
            return viewDataBinding;
        }


        void bind(CustomPreferenceData preferenceData){
            binding.setPreferenceData(preferenceData);
            binding.executePendingBindings();
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


    public CustomPreferenceListAdapter(CustomPreferenceListViewModel viewModel,LifecycleOwner lifecycleOwner){
        super(DIFF_CALLBACK);
        viewModel.CustomPreferenceList().observe(lifecycleOwner, this::submitList);
    }

    @NonNull
    @Override
    public CustomPreferenceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        binding = CustomPreferenceOneLineBinding.inflate(inflater,parent,false);
        return new CustomPreferenceListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomPreferenceListViewHolder holder, int position) {
        CustomPreferenceData data=getItem(position);
        // 各Viewに関連付け+購読
        holder.bind(data);

        // テキストサイズ変更
        holder.changeTextSize(mainTextSize);

        // テキストを右寄りに
        holder.alignmentText(Gravity.END);

    }

    @Override
    public void onBindViewHolder(@NonNull CustomPreferenceListViewHolder holder, int position, @NonNull List<Object> payloads) {
        CustomPreferenceData data = getItem(position);
        if(payloads.isEmpty()){
            holder.bind(data);
        }else{
            for (Object payload:payloads){
                if("Per".equals(payload)){
                    holder.getViewDataBinding().setVariable(BR.preferenceData, data);
                    holder.getViewDataBinding().executePendingBindings();
                }
            }
        }
        // テキストサイズ変更
        holder.changeTextSize(mainTextSize);

        holder.alignmentText(Gravity.END);
    }

    public void setTextSizes(int textSize){
        this.mainTextSize=textSize;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void submitList(@Nullable List<CustomPreferenceData> list) {
        super.submitList(list);
    }



    private static final DiffUtil.ItemCallback<CustomPreferenceData> DIFF_CALLBACK=
            new DiffUtil.ItemCallback<CustomPreferenceData>() {

                @Override
                public boolean areItemsTheSame(@NonNull CustomPreferenceData oldItem, @NonNull CustomPreferenceData newItem) {
                    boolean bool= oldItem.DiscountNo()== newItem.DiscountNo();
                    Log.i("DiffUtil","areItemTheSame:"+bool);
                    return bool;

                }

                @Override
                public boolean areContentsTheSame(@NonNull CustomPreferenceData oldItem, @NonNull CustomPreferenceData newItem) {
                    boolean bool= oldItem.DiscountPer()== newItem.DiscountPer();
                    Log.i("DiffUtil","areContentsTheSame:"+bool);
                    return bool;
                }

                @Nullable
                @Override
                public Object getChangePayload(@NonNull CustomPreferenceData oldItem, @NonNull CustomPreferenceData newItem) {
                    Bundle diff=new Bundle();
                    if(newItem.DiscountNo()!=oldItem.DiscountNo()){
                        diff.putInt("no",newItem.DiscountNo());
                    }
                    if(newItem.DiscountPer()!=oldItem.DiscountPer()){
                        diff.putInt("per",newItem.DiscountPer());
                    }
                    if(diff.size()==0){
                        return null;
                    }
                    return diff;
                }
            };
}
