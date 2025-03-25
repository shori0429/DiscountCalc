package com.example.discountcalc.customAdapters;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import kotlin.Pair;

// リサイクルビューのベースアダプター
// MUST CHECK:使用していないので別プロジェクトに退避した後に削除する等、要検討。
public abstract class RecyclerBaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<Object> items=new ArrayList<>();
    private final Subject<List<Object>> subject= PublishSubject.<List<Object>>create().toSerialized();
    private final CompositeDisposable compositeDisposable=new CompositeDisposable();

    public RecyclerBaseAdapter(){
        compositeDisposable.add(
                // 以下の処理を計算用スレッドで処理
                subject.observeOn(Schedulers.computation())
                        // ListをPair<List,ArrayList>に適用
                        .map((Function<List<Object>, Pair<List<Object>, List<Object>>>) list -> new Pair<>(list,new ArrayList<>()))
                        // 累積加算(oldPairは通常初期値が設定される)
                        .scan((oldPair, newPair) -> new Pair<>(oldPair.getFirst(),newPair.getFirst()))
                        // DiffUtil.Callbackに変換
                        .map((Function<Pair<List<Object>, List<Object>>, DiffUtil.Callback>) pair -> {
                            List<Object> oldList=pair.getFirst();
                            List<Object> newList= pair.getSecond();

                            return new DiffUtil.Callback(){

                                @Override
                                public int getOldListSize() {
                                    return oldList.size();
                                }

                                @Override
                                public int getNewListSize() {
                                    return oldList.size();
                                }

                                @Override
                                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                                    return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
                                }

                                @Override
                                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                                    return RecyclerBaseAdapter.this.areContentsTheSame(oldList, newList, oldItemPosition, newItemPosition);
                                }
                            };
                        })
                        // リストの差分計算
                        .map(DiffUtil::calculateDiff)
                        // 処理の結果をUIスレッドで受け取る
                        .observeOn(AndroidSchedulers.mainThread())
                        // 差分結果を受け取り。、RecyclerViewに変更を通知
                        .subscribe(diffResult -> {
                            diffResult.dispatchUpdatesTo(this);
                        },Throwable::printStackTrace));
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
