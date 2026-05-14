package com.example.discountcalc.customAdapters;

import android.graphics.Point;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discountcalc.params.DiscountData;
import com.example.discountcalc.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * 1行分のデータを1行分のViewに設定して生成するクラス
 **/
public class ResultLayoutAdapter extends RecyclerView.Adapter<ResultLayoutAdapter.ResultViewHolder> {

    private ArrayList<DiscountData> localData;

    // resultPriceListViewのid,layout_width,Layout_heightを格納
    private HashMap<Integer,Point> adapterLayoutSize;

    /**
     * 1行分のViewの参照を保持するホルダークラス
     **/
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
    public ResultLayoutAdapter(ArrayList<DiscountData> dataset)
    {
        localData=dataset;
    }


    /// resultLabelのid,layout_width,Layout_heightを格納
    public ResultLayoutAdapter(ArrayList<DiscountData>dataset, HashMap<Integer,Point>layoutSize){
        localData=dataset;
        adapterLayoutSize=layoutSize;

    }

    // 新しい1行分のビューを作成(レイアウトマネージャーによって呼び出される)
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
        holder.discountTextview.setText(String.format(Locale.getDefault(),"%d%%",localData.get(position).getDiscountPer()));
        holder.discountPriceTextview.setText(String.format(Locale.getDefault(),"%,d円",localData.get(position).getDiscountPrice()));
        holder.priceTextview.setText(String.format(Locale.getDefault(),"%,d円",localData.get(position).getAfterPrice()));
        // 文字のGravityを変更(右寄せ)
        holder.discountTextview.setGravity(Gravity.END);
        holder.discountPriceTextview.setGravity(Gravity.END);
        holder.priceTextview.setGravity(Gravity.END);

        // ヘッダーのサイズに合わせる
        if(adapterLayoutSize !=null){
            applySize(holder.discountTextview,adapterLayoutSize.get(R.id.discountLabel));
            applySize(holder.discountPriceTextview,adapterLayoutSize.get(holder.discountPriceTextview.getId()));
            applySize(holder.priceTextview,adapterLayoutSize.get(holder.priceTextview.getId()));
        }

    }
    // データセットのサイズを返す (レイアウトマネージャによって呼び出される)
    @Override
    public int getItemCount() {
        return localData.size();
    }

    //
    public void updateItem(ArrayList<DiscountData> data){
        localData=data;
        // localDataのサイズ分の変更をobserverに通知
        notifyItemRangeChanged(0,getItemCount());
    }

    private void applySize(TextView textView,Point size){
        if(size == null)return;
        ViewGroup.LayoutParams lp=textView.getLayoutParams();
        lp.width = size.x;
        lp.height=size.y;
        textView.setLayoutParams(lp);
    }


}
