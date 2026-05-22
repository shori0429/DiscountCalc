package com.example.discountcalc.customAdapters;

import android.graphics.Point;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discountcalc.databinding.ResultOneCalcViewBinding;
import com.example.discountcalc.params.DiscountData;
import com.example.discountcalc.R;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * 1行分のデータを1行分のViewに設定して生成するクラス
 **/
public class ResultLayoutAdapter extends ListAdapter<DiscountData,ResultLayoutAdapter.ResultViewHolder> {


    // resultPriceListViewのid,layout_width,Layout_heightを格納
    private final HashMap<Integer, Point> adapterLayoutSize;

    ResultOneCalcViewBinding binding;

    /**
     * 1行分のViewの参照を保持するホルダークラス
     **/
    public static class ResultViewHolder extends RecyclerView.ViewHolder{
        private final TextView discountTextview;
        private final TextView discountPriceTextview;
        private final TextView priceTextview;

        //ビューホルダー
        public ResultViewHolder(ResultOneCalcViewBinding binding) {
            super(binding.getRoot());

            discountTextview = binding.discountLabel;
            discountPriceTextview = binding.discountPriceLabel;
            priceTextview = binding.priceLabel;
        }

    }

    /*
     * アダプタのデータセットを初期化
     * RecycleViewで使用されるビューに入力するデータを含む
     */
    /// resultLabelのid,layout_width,Layout_heightを格納
    public ResultLayoutAdapter(HashMap<Integer, Point> layoutSize) {
        super(DIFF_CALLBACK);
        adapterLayoutSize = layoutSize;
    }

    // 新しい1行分のビューを作成(レイアウトマネージャーによって呼び出される)
    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // リスト項目のUIを定義する新しいビューを作成する。
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        binding = ResultOneCalcViewBinding.inflate(inflate,parent,false);
        return new ResultViewHolder(binding);
    }

    // ビューの内容を置き換える(レイアウトマネージャーによって呼び出される)
    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        DiscountData data=getItem(position);
        // この位置のデータセットから要素を取得し、ビューの内容をその要素で置き換える
        holder.discountTextview.setText(String.format(Locale.getDefault(), "%d%%", data.getDiscountPer()));
        holder.discountPriceTextview.setText(String.format(Locale.getDefault(), "%,d円", data.getDiscountPrice()));
        holder.priceTextview.setText(String.format(Locale.getDefault(), "%,d円", data.getAfterPrice()));
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
    @Override

    }

    private void applySize(TextView textView,Point size){
        if(size == null)return;
        ViewGroup.LayoutParams lp=textView.getLayoutParams();
        lp.width = size.x;
        lp.height=size.y;
        textView.setLayoutParams(lp);
    }

    @Override
    protected DiscountData getItem(int position) {
        return super.getItem(position);
    }


}
