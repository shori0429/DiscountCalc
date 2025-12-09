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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discountcalc.BR;
import com.example.discountcalc.databinding.CustomPreferenceOneLineBinding;
import com.example.discountcalc.fragments.CustomTextWatcher;
import com.example.discountcalc.params.PreferenceParam;
import com.example.discountcalc.viewModels.CustomPreferenceListViewModel;

import java.util.List;

public class CustomPreferenceListAdapter
        extends ListAdapter<PreferenceParam, CustomPreferenceListAdapter.CustomPreferenceListViewHolder> {

    private int mainTextSize;

    CustomPreferenceOneLineBinding binding;

    CustomPreferenceListViewModel customPreferenceListViewModel;

    public static class CustomPreferenceListViewHolder extends RecyclerView.ViewHolder implements LifecycleOwner,CustomTextWatcher {
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


        void bind(int position,PreferenceParam preferenceParam,CustomPreferenceListViewModel viewModel){
            binding.setPosition(position);
            binding.setPreferenceParam(preferenceParam);
            binding.setViewModel(viewModel);
            binding.customPreferenceOneLineNum.setOnFocusChangeListener((v,hasFocus)->{
                if(!hasFocus){
                    int newPer= Integer.parseInt(binding.customPreferenceOneLineNum.getText().toString());
                    int beforePer=viewModel.getPreferenceParamData(position).per();
                    // 値が変わってなければここで終了
                    if(newPer==beforePer)return;

                    PreferenceParam newPreferenceData=new PreferenceParam(viewModel.getPreferenceParamData(position).uid(),
                            newPer,
                            viewModel.getPreferenceParamData(position).saveName());
                    viewModel.updatePreferenceData(position,newPreferenceData);
                }
            });
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
    
    public CustomPreferenceListAdapter(CustomPreferenceListViewModel viewModel){
        super(DIFF_CALLBACK);
        customPreferenceListViewModel=viewModel;
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
        PreferenceParam data=getItem(position);
        // 各Viewに関連付け+購読
        holder.bind(position,data,customPreferenceListViewModel);

        // テキストサイズ変更
        holder.changeTextSize(mainTextSize);

        // テキストを右寄りに
        holder.alignmentText(Gravity.END);

    }

    @Override
    public void onBindViewHolder(@NonNull CustomPreferenceListViewHolder holder, int position, @NonNull List<Object> payloads) {
        PreferenceParam data = getItem(position);
        if(payloads.isEmpty()){
            holder.bind(position,data,customPreferenceListViewModel);
        }else{
            // DiffUtilで算出した差分を適用させる。
            for (Object obj:payloads){
                Bundle payload=(Bundle) obj;
                if(payload.containsKey("per")){
                    data=new PreferenceParam(data.uid(), payload.getInt("per"),"");
                    holder.getViewDataBinding().setVariable(BR.preferenceParam, data);
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
    public void submitList(@Nullable List<PreferenceParam> list) {
        super.submitList(list);
    }


    private static final DiffUtil.ItemCallback<PreferenceParam> DIFF_CALLBACK=
            new DiffUtil.ItemCallback<PreferenceParam>() {

                @Override
                public boolean areItemsTheSame(@NonNull PreferenceParam oldItem, @NonNull PreferenceParam newItem) {
                    boolean bool= oldItem.uid()== newItem.uid();
                    return bool;

                }

                @Override
                public boolean areContentsTheSame(@NonNull PreferenceParam oldItem, @NonNull PreferenceParam newItem) {
                    boolean bool = oldItem.equals(newItem);
                    return bool;
                }

                @Nullable
                @Override
                public Object getChangePayload(@NonNull PreferenceParam oldItem, @NonNull PreferenceParam newItem) {
                    Bundle diff=new Bundle();
                    if(newItem.uid()!=oldItem.uid()){
                        diff.putInt("uid",newItem.uid());
                    }
                    if(newItem.per()!=oldItem.per()){
                        diff.putInt("per",newItem.per());
                    }
                    if(!newItem.saveName().equals(oldItem.saveName())){
                        diff.putString("saveName", newItem.saveName());
                    }
                    if(diff.size()==0){
                        return null;
                    }
                    return diff;
                }
            };
}
