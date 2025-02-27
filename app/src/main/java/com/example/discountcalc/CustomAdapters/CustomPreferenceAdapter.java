package com.example.discountcalc.CustomAdapters;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discountcalc.Params.CustomPreferenceData;
import com.example.discountcalc.R;
import com.example.discountcalc.databinding.CustomPreferenceOneLineBinding;

import java.util.ArrayList;
import java.util.Locale;

public class CustomPreferenceAdapter extends RecyclerView.Adapter<CustomPreferenceAdapter.CustomPreferenceViewHolder> {

    private ArrayList<CustomPreferenceData> localData;
    private View.OnClickListener onClickListener;
    // 文字サイズ
    private int mainTextSize;

    public static class CustomPreferenceViewHolder extends RecyclerView.ViewHolder {
        private final TextView listDiscountTitleTextView;
        private final EditText listDiscountNumEditView;
        private final LinearLayout linearLayout;

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
            listDiscountTitleTextView = binding.customPreferenceOneLineTitle;
            listDiscountNumEditView = binding.customPreferenceOneLineNum;
            linearLayout=binding.customPreferenceOneLineNumContainer;
        }

        public TextView getListDiscountTitleTextView() {
            return listDiscountTitleTextView;
        }

        public TextView getListDiscountNumEditView() {
            return listDiscountNumEditView;
        }

    }

    public CustomPreferenceAdapter(ArrayList<CustomPreferenceData> dataset){
        localData=dataset;
    }
    @NonNull
    @Override
    public CustomPreferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // リスト項目のUIを定義する新しいビューを作成する。
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        CustomPreferenceOneLineBinding binding=CustomPreferenceOneLineBinding.inflate(inflater,parent,false);
//        View inflate = LayoutInflater.from(parent.getContext())
//               .inflate(R.layout.custom_preference_one_line, parent, false);
        return new CustomPreferenceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomPreferenceViewHolder holder, int position) {
        // この位置のデータセットから要素を取得し、ビューの内容をその要素で置き換える
        // 要素番号を1~表示させたいので、NaturalNumberで数値を取得(TODO:もっと綺麗な実装方法があるとは思う)
        int per=localData.get(position).getDiscountPer();
        holder.listDiscountTitleTextView.setText(String.format(Locale.getDefault(), "%d", localData.get(position).getDiscountElementNaturalNumber()));
        // 0%なら入力を空にしてhintを表示させる
        if(per==0){
            holder.listDiscountNumEditView.setText("");
        }else {
            holder.listDiscountNumEditView.setText(String.format(Locale.getDefault(), "%d", localData.get(position).getDiscountPer()));
        }

        if(mainTextSize>0) {
            // 文字サイズ設定
            holder.listDiscountTitleTextView.setTextSize(mainTextSize);
            holder.listDiscountNumEditView.setTextSize(mainTextSize);
        }

        // 文字のGravityを変更(右寄せ)
        holder.listDiscountTitleTextView.setGravity(Gravity.END);
        holder.listDiscountNumEditView.setGravity(Gravity.END);
        holder.linearLayout.setOnClickListener(l->{
            Log.i("RecyclerViewClickEvent",holder.listDiscountTitleTextView.getText()+" : "+holder.listDiscountNumEditView.getText());
        });
    }

    @Override
    public int getItemCount() {
        return localData.size();
    }



    public void updateItems(ArrayList<CustomPreferenceData> data){
        localData=data;
        // localDataのサイズ分の変更をobserverに通知
        notifyItemRangeChanged(0,getItemCount());
    }
    public void setTextSizes(int textSize){
        this.mainTextSize =textSize;
        notifyDataSetChanged();
    }
}
