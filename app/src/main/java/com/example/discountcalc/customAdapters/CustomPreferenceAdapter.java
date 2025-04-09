package com.example.discountcalc.customAdapters;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discountcalc.databinding.CustomPreferenceOneLineBinding;
import com.example.discountcalc.params.CustomPreferenceData;
import com.example.discountcalc.viewModels.CustomPreferenceListViewModel;

import java.util.ArrayList;
import java.util.List;

public class CustomPreferenceAdapter extends RecyclerView.Adapter<CustomPreferenceAdapter.CustomPreferenceViewHolder> {

    private List<CustomPreferenceData> preferenceDataList;
    private View.OnClickListener onClickListener;
    // 文字サイズ
    private int mainTextSize;

    public static class CustomPreferenceViewHolder extends RecyclerView.ViewHolder implements LifecycleOwner{
        LifecycleRegistry lifecycle=new LifecycleRegistry(this);

        private final CustomPreferenceOneLineBinding binding;

//        public CustomPreferenceViewHolder(View view) {
//            super(view);
//            listDiscountTitleTextView = view.findViewById(R.id.custom_preference_one_line_title);
//            listDiscountNumEditView = view.findViewById(R.id.custom_preference_one_line_num);
//            linearLayout=view.findViewById(R.id.custom_preference_one_line_num_container);
//        }

        public CustomPreferenceViewHolder(CustomPreferenceOneLineBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        void bind(CustomPreferenceData viewModel){
            viewModel.getDiscountElement().observe(this,t->binding.customPreferenceOneLineTitle.setText(t+1));
            viewModel.getDiscountPer().observe(this, binding.customPreferenceOneLineNum::setText);
        }


        public void changeTextSize(int textSize){

            if(textSize>0) {
                // 文字サイズ設定
                binding.customPreferenceOneLineTitle.setTextSize(textSize);
                binding.customPreferenceOneLineNum.setTextSize(textSize);
            }
        }

        public void alignmentText(int gravity){
            // 文字のGravityを変更(右寄せ)
            binding.customPreferenceOneLineTitle.setGravity(gravity);
            binding.customPreferenceOneLineNum.setGravity(gravity);
            binding.customPreferenceOneLineNumContainer.setOnClickListener(l->{
                Log.i("RecyclerViewClickEvent",binding.customPreferenceOneLineTitle.getText()+" : "+binding.customPreferenceOneLineNum.getText());
            });
        }
        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return lifecycle;
        }


    }

    public CustomPreferenceAdapter(CustomPreferenceListViewModel dataset){
        preferenceDataList= dataset.CustomPreferenceList().getValue();
    }

    public CustomPreferenceAdapter(CustomPreferenceListViewModel viewModel, LifecycleOwner lifecycleOwner){
        preferenceDataList=new ArrayList<>();
        viewModel.CustomPreferenceList().observe(lifecycleOwner, data->{
            preferenceDataList=data;
            notifyDataSetChanged();
        });

    }
    @NonNull
    @Override
    public CustomPreferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // リスト項目のUIを定義する新しいビューを作成する。
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        CustomPreferenceOneLineBinding binding=CustomPreferenceOneLineBinding.inflate(inflater,parent,false);
        return new CustomPreferenceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomPreferenceViewHolder holder, int position) {
        holder.bind(preferenceDataList.get(position));
        // この位置のデータセットから要素を取得し、ビューの内容をその要素で置き換える
        // 要素番号を1~表示させたいので、NaturalNumberで数値を取得(TODO:もっと綺麗な実装方法があるとは思う)
        // 0%なら入力を空にしてhintを表示させる
        holder.changeTextSize(mainTextSize);

        holder.alignmentText(Gravity.END);
    }

    @Override
    public int getItemCount() {
        return preferenceDataList.size();
    }




    public void updateItems(ArrayList<CustomPreferenceData> data){
        // localDataのサイズ分の変更をobserverに通知
        notifyItemRangeChanged(0,getItemCount());
    }
    public void setTextSizes(int textSize){
        this.mainTextSize =textSize;
        notifyDataSetChanged();
    }
}
