package com.example.discountcalc.CustomAdapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discountcalc.Params.CustomPreferenceData;
import com.example.discountcalc.R;

import java.util.ArrayList;
import java.util.Locale;

public class CustomPreferenceAdapter extends RecyclerView.Adapter<CustomPreferenceAdapter.CustomPreferenceViewHolder> {

    private ArrayList<CustomPreferenceData> localData;

    public static class CustomPreferenceViewHolder extends RecyclerView.ViewHolder {
        private final TextView listDiscountTitleTextView;
        private final TextView listDiscountNumTextView;

        public CustomPreferenceViewHolder(View view) {
            super(view);
            listDiscountTitleTextView = view.findViewById(R.id.custom_preference_one_line_title);
            listDiscountNumTextView = view.findViewById(R.id.custom_preference_one_line_num);
        }

        public TextView getListDiscountTitleTextView() {
            return listDiscountTitleTextView;
        }

        public TextView getListDiscountNumTextView() {
            return listDiscountNumTextView;
        }
    }

    public CustomPreferenceAdapter(ArrayList<CustomPreferenceData> dataset){
        localData=dataset;
    }
    @NonNull
    @Override
    public CustomPreferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // リスト項目のUIを定義する新しいビューを作成する。
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_preference_one_line, parent, false);
        return new CustomPreferenceViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomPreferenceViewHolder holder, int position) {
        // この位置のデータセットから要素を取得し、ビューの内容をその要素で置き換える
        holder.listDiscountTitleTextView.setText(String.format(Locale.getDefault(), "%d", localData.get(position).getDiscountElementName()));
        holder.listDiscountNumTextView.setText(String.format(Locale.getDefault(), "%d%%", localData.get(position).getDiscountPer()));
        // 文字のGravityを変更(右寄せ)
        holder.listDiscountTitleTextView.setGravity(Gravity.END);
        holder.listDiscountNumTextView.setGravity(Gravity.END);

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
}
