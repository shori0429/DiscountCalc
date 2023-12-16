package com.example.discountcalc.CustomAdapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discountcalc.Params.DiscountData;
import com.example.discountcalc.R;

import java.util.List;
import java.util.Locale;

public class ResultLayoutAdapter extends RecyclerView.Adapter<ResultLayoutAdapter.ResultViewHolder> {


    private final List<DiscountData> localData;
    public static class ResultViewHolder extends RecyclerView.ViewHolder{
        private final TextView discountTextview;
        private final TextView discountPriceTextview;
        private final TextView priceTextview;


        //ビューホルダー
        public ResultViewHolder(View view){
            super(view);
            discountTextview =view.findViewById(R.id.discountLabel);
            discountPriceTextview =view.findViewById(R.id.discountPriceLabel);
            priceTextview =view.findViewById(R.id.priceLabel);
        }

        public TextView getDiscountTextview(){
            return  discountTextview;
        }

        public TextView getDiscountPriceTextview() {
            return discountPriceTextview;
        }

        public TextView getPriceTextview() {
            return priceTextview;
        }
    }

    /*
     * アダプタのデータセットを初期化
     * RecycleViewで使用されるビューに入力するデータを含む
     */
    public ResultLayoutAdapter(List<DiscountData> dataset){
        localData=dataset;
    }

    // 新しいビューを作成(レイアウトマネージャーによって呼び出される)
    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // リスト項目のUIを定義する新しいビューを作成する。
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_one_calc_view,parent,false);
        return new ResultViewHolder(inflate);
    }

    // ビューの内容を置き換える(レイアウトマネージャーによって呼び出される)
    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        // この位置のデータセットから要素を取得し、ビューの内容をその要素で置き換える
        holder.discountTextview.setText(String.format(Locale.getDefault(),"%d",localData.get(position).getDiscount()));
        holder.discountPriceTextview.setText(String.format(Locale.getDefault(),"%d",localData.get(position).getDiscountPrice()));
        holder.priceTextview.setText(String.format(Locale.getDefault(),"%d",localData.get(position).getPrice()));
        Log.i("info","Data:"+position);
    }
    // データセットのサイズを返す (レイアウトマネージャによって呼び出される)
    @Override
    public int getItemCount() {
        return localData.size();
    }



}
