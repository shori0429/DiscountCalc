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
import com.example.discountcalc.params.EditableDiscountRate;
import com.example.discountcalc.viewModels.CustomPreferenceListViewModel;

import java.util.List;

public class CustomPreferenceListAdapter
        extends ListAdapter<EditableDiscountRate, CustomPreferenceListAdapter.CustomPreferenceListViewHolder> {

    private int mainTextSize;

    CustomPreferenceOneLineBinding binding;

    CustomPreferenceListViewModel customPreferenceListViewModel;

    public static class CustomPreferenceListViewHolder extends RecyclerView.ViewHolder implements LifecycleOwner, CustomTextWatcher {
        private final LifecycleRegistry lifecycle = new LifecycleRegistry(this);
        private final CustomPreferenceOneLineBinding binding;

        private final ViewDataBinding viewDataBinding;

        //
        public CustomPreferenceListViewHolder(CustomPreferenceOneLineBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            viewDataBinding = DataBindingUtil.bind(binding.getRoot());
        }

        public ViewDataBinding getViewDataBinding() {
            return viewDataBinding;
        }


        void bind(EditableDiscountRate eRateTable, CustomPreferenceListViewModel viewModel) {
            //
            binding.setPosition(eRateTable.orderIndex());
            binding.setPer(eRateTable.per());
            binding.customPreferenceOneLineNum.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) return;
                // アイテムからフォーカス外れた時に処理
                if (!hasFocus) {
                    //　アダプターに対する相対位置を取得
                    int adapterPosition = getBindingAdapterPosition();
                    if (adapterPosition == RecyclerView.NO_POSITION ||
                            viewModel.getWorkingList().get(adapterPosition) == null) return;

                    // 変更前の割引率情報を保持
                    EditableDiscountRate beforeRate = viewModel.getWorkingList().get(adapterPosition);

                    String columnText = binding.customPreferenceOneLineNum.getText().toString();

                    if (columnText.isEmpty()) {
                        binding.customPreferenceOneLineNum.setText(String.valueOf(eRateTable.per()));
                        return;
                    }
                    try {
                        // 値が変わってなければここで終了
                        int newPer = Integer.parseInt(columnText);
                        if (newPer == eRateTable.per()) return;

                    } catch (NumberFormatException ex) {
                        binding.customPreferenceOneLineNum.setText(eRateTable.per());
                        Log.e("CustomPreferenceListAdapter", "Failed to parse preference value", ex);
                    }
                }
            });
            binding.executePendingBindings();
        }

        public void changeTextSize(int textSize) {
            if (textSize > 0) {
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

    public CustomPreferenceListAdapter(CustomPreferenceListViewModel viewModel) {
        super(DIFF_CALLBACK);
        customPreferenceListViewModel = viewModel;
    }

    @NonNull
    @Override
    public CustomPreferenceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        binding = CustomPreferenceOneLineBinding.inflate(inflater, parent, false);
        return new CustomPreferenceListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomPreferenceListViewHolder holder, int position) {
        EditableDiscountRate data = getItem(position);
        // 各Viewに関連付け+購読
        holder.bind(data, customPreferenceListViewModel);

        // テキストサイズ変更
        holder.changeTextSize(mainTextSize);

        // テキストを右寄りに
        holder.alignmentText(Gravity.END);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomPreferenceListViewHolder holder, int position, @NonNull List<Object> payloads) {
        EditableDiscountRate data = getItem(position);
        if (payloads.isEmpty()) {
            holder.bind(data, customPreferenceListViewModel);
        } else {
            // DiffUtilで算出した差分を適用させる。
            for (Object obj : payloads) {
                Bundle payload = (Bundle) obj;
                int per = 0;
                if (payload.containsKey("per")) {
                    per = payload.getInt("per");
                }
                holder.bind(data, customPreferenceListViewModel);
                holder.getViewDataBinding().executePendingBindings();
            }
        }
        // テキストサイズ変更
        holder.changeTextSize(mainTextSize);

        holder.alignmentText(Gravity.END);
    }

    public void setTextSizes(int textSize) {
        this.mainTextSize = textSize;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void submitList(@Nullable List<EditableDiscountRate> list) {
        super.submitList(list);
    }


    private static final DiffUtil.ItemCallback<EditableDiscountRate> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<EditableDiscountRate>() {

                @Override
                public boolean areItemsTheSame(@NonNull EditableDiscountRate oldItem, @NonNull EditableDiscountRate newItem) {
                    return oldItem.localId() == newItem.localId();

                }

                @Override
                public boolean areContentsTheSame(@NonNull EditableDiscountRate oldItem, @NonNull EditableDiscountRate newItem) {
                    return oldItem.equals(newItem);
                }

                @Nullable
                @Override
                public Object getChangePayload(@NonNull EditableDiscountRate oldItem, @NonNull EditableDiscountRate newItem) {
                    Bundle diff = new Bundle();
                    if (newItem.per() != oldItem.per()) {
                        diff.putInt("per", newItem.per());
                    }
                    if (diff.isEmpty()) {
                        return null;
                    }
                    return diff;
                }
            };
}
