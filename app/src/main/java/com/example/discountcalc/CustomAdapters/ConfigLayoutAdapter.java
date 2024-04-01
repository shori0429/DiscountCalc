package com.example.discountcalc.CustomAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discountcalc.R;

public class ConfigLayoutAdapter extends RecyclerView.Adapter<ConfigLayoutAdapter.ViewHolder> {

    private SwitchCompat[] localDataSet;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //private final TextView textView;
        private final SwitchCompat switchCompat;
        public ViewHolder(View view){
            super(view);

            switchCompat=(SwitchCompat) view.findViewById(R.id.perEnableSwitch);
        }

        public SwitchCompat getSwitchCompat() {
            return switchCompat;
        }

        //public TextView getTextView() {
        //    return textView;
        //}

    }

    /*
     * アダプタのデータセットを初期化
     * RecycleViewで使用されるビューに入力するデータを含む
     */
    public ConfigLayoutAdapter(SwitchCompat[] dataSet){
        //localDataSet = dataSet;
        localDataSet=dataSet;
    }

    // 新しいビューを作成(レイアウトマネージャーによって呼び出される)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // リスト項目のUIを定義する新しいビューを作成する。
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.config_preset,viewGroup,false);
        return new ViewHolder(view);
    }

    // ビューの内容を置き換える(レイアウトマネージャーによって呼び出される)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // この位置のデータセットから要素を取得し、ビューの内容をその要素で置き換える
        //viewHolder.getSwitchCompat().setChecked(localDataSet[position]);
        //viewHolder.getTextView().setText(localDataSet[position]);
    }
    // データセットのサイズを返す (レイアウトマネージャによって呼び出される)
    @Override
    public int getItemCount() {
        return 0;
    }
}
