package com.example.discountcalc.CustomAdapters;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.processors.FlowableProcessor;
import io.reactivex.rxjava3.processors.PublishProcessor;
import io.reactivex.rxjava3.schedulers.Schedulers;

// リサイクルビューのベースアダプター

public abstract class RecyclerBaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<Object> items=new ArrayList<>();
    private final FlowableProcessor<List<Object>> subject= PublishProcessor.<List<Object>>create().toSerialized();
    private final CompositeDisposable compositeDisposable=new CompositeDisposable();

    public RecyclerBaseAdapter(){
        compositeDisposable.add(
                subject.observeOn(Schedulers.computation())
                        .map(newList->new DiffUtil.Callback(){
                            private final List<Object> oldList=new ArrayList<>(items);

                            @Override
                            public int getOldListSize() {
                                return oldList.size();
                            }

                            @Override
                            public int getNewListSize(){
                                return newList.size();
                            }

                            @Override
                            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
                            }

                            @Override
                            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                                return RecyclerBaseAdapter.this.areContentsTheSame(oldList, newList, oldItemPosition, newItemPosition);
                            }
                        })
                        .map(DiffUtil::calculateDiff)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(diffResult -> {
                            diffResult.dispatchUpdatesTo(this);
                        },Throwable::printStackTrace)
        );
    }

    public abstract boolean areContentsTheSame(List<Object>oldList,List<Object>newList,int oldItemPosition,int newItemPosition);

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addAll(List<Object> newItems){
        items.addAll(newItems);
        subject.onNext(new ArrayList<>(items));
    }

    public void destroy(){
        compositeDisposable.clear();
    }
}
