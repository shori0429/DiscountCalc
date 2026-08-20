package com.example.discountcalc.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.discountcalc.dataBase.CustomPreferenceRepository;
import com.example.discountcalc.params.CustomPreferenceTable;
import com.example.discountcalc.params.DiscountRateTable;
import com.example.discountcalc.params.EditableDiscountRate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CustomPreferenceListViewModel extends AndroidViewModel {

    /// ここからViewModel内もしくはRepository経由でのみ変更されるフィールド

    /// DAOへの中継リポジトリ
    private final CustomPreferenceRepository preferenceRepository;

    /// 親テーブルである設定データ名リストを保持するLiveData
    private final LiveData<List<CustomPreferenceTable>> preferenceTableLiveDataList;

    /// 使用する割引率リストを保持するLiveData
    private final LiveData<List<DiscountRateTable>> rateTableLiveDataList;

    /// 差分計算用にDB取得地点のスナップショット保持
    private List<DiscountRateTable> snapShot = Collections.emptyList();

    ///

    /// 使用する割引率リスト名を保持するLiveData
    private final MutableLiveData<String> selectTableName = new MutableLiveData<>();

    /// 画面上で編集されるモデルリストLiveData
    private final MediatorLiveData<List<EditableDiscountRate>> workingList = new MediatorLiveData<>();

    private long nextLocalId = -1;

    public CustomPreferenceListViewModel(Application application){
        super(application);
        preferenceRepository = new CustomPreferenceRepository(application);
        preferenceTableLiveDataList = preferenceRepository.getAllPreferences();
        // selectTableNameの変更で割引率リストがDBのものに自動同期されるように設定
        rateTableLiveDataList = Transformations.switchMap(selectTableName, this::fetchRateTableLiveDataList);

        // rateTableLiveDataListの確定状態が変化したら、編集用コピーをリセット
        workingList.addSource(rateTableLiveDataList, dbList -> {
            snapShot = dbList;
            workingList.setValue(new ArrayList<>());
        });
    }

    /// 使用する割引率設定名に応じた割引率リストを取得
    /// 外部からはselectPreference(String)を呼び出すようにする
    private LiveData<List<DiscountRateTable>> fetchRateTableLiveDataList(String saveName) {
        List<CustomPreferenceTable> currentList = preferenceTableLiveDataList.getValue();
        // 親テーブルが空だった場合、空のリストを返す
        if (currentList == null) {
            return new MutableLiveData<>(Collections.emptyList());
        }

        // 空リストか、DAOからsaveNameと一致する割引率リストが返される
        // saveNameはUnique制約があるため、判定はfindFirstのみ
        return currentList.stream()
                .filter(v -> v.saveName().equals(saveName))
                .findFirst()
                .map(v -> preferenceRepository.getRatesByPreferenceId(v.preferenceId()))
                .orElseGet(() -> new MutableLiveData<>(Collections.emptyList()));
    }

    /// Fragment側から使用する設定データ名を指定するときに使用する
    public void selectPreference(String saveName) {
        selectTableName.setValue(saveName);
    }

    /// 適用されている設定データ名のLiveDataを返す
    public LiveData<String> getSelectTableName() {
        return selectTableName;
    }

    /// 現在使用している設定データの割引率リストLiveDataを返す
    public LiveData<List<DiscountRateTable>> getRateTableLiveDataList() {
        return rateTableLiveDataList;
    }

    /// 編集中の割引率リストのLiveDataを返す
    public LiveData<List<EditableDiscountRate>> getWorkingListLiveData() {
        return workingList;
    }

    /// 編集中の割引率リストを返す。空ならnullが返される
    public List<EditableDiscountRate> getWorkingList() {
        return workingListOrNull();
    }


    /// 編集用リストのnullチェックとコピーを実行して返す
    private List<EditableDiscountRate> workingListOrNull() {
        List<EditableDiscountRate> current = workingList.getValue();
        if (current == null) return null;
        return new ArrayList<>(current);
    }

    /// 設定データ名リストを取得
    public List<String> getSaveNameList() {
        if (preferenceTableLiveDataList.getValue() != null) {
            return preferenceTableLiveDataList.getValue().stream()
                    .map(CustomPreferenceTable::saveName)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /// nextLocalIdのリセット
    private void resetNextLocalId(){
        nextLocalId=-1;
    }

    /// workingListにレコードを追加をするときに使用するメソッド。
    /// 未保存の値は-1,-2,-3...といった形で採番していく
    private long generateNextLocalId() {
        return nextLocalId--;
    }


    /// 編集用リストにレコード追加
    public boolean addPer(int per) {
        // 編集用のリストに分離
        List<EditableDiscountRate> updated = workingListOrNull();
        if (updated == null) return false;

        // rateIdとpreferenceIdは追加しない
        // localIdは新規追加なので負の値になる
        updated.add(
                new EditableDiscountRate(
                        generateNextLocalId(),
                        0,
                        0,
                        per,
                        updated.size())
        );

        workingList.setValue(updated);
        return true;
    }

    // TODO 編集用リスト要素更新
    public boolean updatePer(int localId, int newPer) {
        // 編集用リストコピーを取得
        List<EditableDiscountRate> current = workingListOrNull();
        if (current == null) return false;

        // 対象のlocalIdの要素があるかチェック
        int index = findRateIndex(localId, current);
        if (index == 1) {
            return false;
        }

        // per以外は変更前のものを引き継ぐ
        EditableDiscountRate oldItem = current.get(index);

        EditableDiscountRate newItem = new EditableDiscountRate(
                oldItem.localId(),
                oldItem.rateId(),
                oldItem.preferenceId(),
                newPer,
                oldItem.orderIndex()
        );

        // 更新処理
        List<EditableDiscountRate> updated = new ArrayList<>(current);
        updated.set(index, newItem);
        workingList.setValue(updated);

        return true;
    }

    // TODO 編集用リスト特定要素削除
    public boolean removeRate(long localId) {
        List<EditableDiscountRate> updated = workingListOrNull();
        if (updated == null) return false;

        // 対象のlocalIdの要素を削除
        boolean removed = updated.removeIf(item -> item.localId() == localId);
        if (!removed) return false;

        // 再構築
        reindex(updated);
        workingList.setValue(updated);

        return true;
    }

    /// 編集中リスト1行削除
    public boolean removeRateLast() {
        List<EditableDiscountRate> updated = workingListOrNull();
        if (updated == null) return false;
        // 最後尾を削除
        updated.remove(updated.size() - 1);
        return true;
    }

    // TODO 編集中リスト全削除
    public boolean removeRateAll() {
        List<EditableDiscountRate> woking = workingListOrNull();
        if (workingList == null) return false;

        // 空リストにして1行追加
        workingList.setValue(new ArrayList<>());
        resetNextLocalId();

        return true;
    }

    // TODO 保存済み割引率リスト削除

    // TODO データベースに保存
    public boolean commitWorkList(String saveName) {
        // nullチェック
        List<EditableDiscountRate> working = workingListOrNull();
        if (working == null) return false;

        //
        List<DiscountRateTable> insertList = new ArrayList<>();
        List<DiscountRateTable> updateList = new ArrayList<>();
        List<DiscountRateTable> deleteList = new ArrayList<>();

        // 保存名から親データを探す
        CustomPreferenceTable preferenceTable = findPreferenceBySaveName(saveName);

        // 既存データがないならそのまま追加して終了
        if (preferenceTable == null) {

            // Repositoryのinsertに追加データのリストを渡す
            long preferenceId=preferenceRepository.insertPreferenceWithRate(saveName,insertList);
            for (var data : working) {
                insertList.add(new DiscountRateTable(0, preferenceId, data.per(), data.orderIndex()));
            }
            // データベース保存呼び出し
            return true;
        }


        // 既存データ有
        long preferenceId = 0;
        preferenceId = preferenceTable.preferenceId();

        // workingとsnapShotを比較
        // 比較用のMapを作成
        Map<Long, DiscountRateTable> snapShotMap = snapShot.stream()
                .collect(Collectors.toMap(
                        DiscountRateTable::rateId,
                        Function.identity()));

        // workingに残っている1以上のrateIdSetを作成
        Set<Long> originalWorkingRateId = working.stream()
                .map(EditableDiscountRate::rateId)
                .filter(id -> id != 0)
                .collect(Collectors.toSet());

        // snapshotにはあるが、workingにはないrateIdを復活候補としてリスト抽出
        List<DiscountRateTable> reviveCandidateList = snapShot.stream()
                .filter(ss -> !originalWorkingRateId.contains(ss.rateId()))
                .collect(Collectors.toList());

        // rateIdの再利用判定後のリスト
        List<DiscountRateTable> resolvedWorkingList = createResolvedWorkingList(preferenceId,working,reviveCandidateList);

        // 更新、追加の判定
        for (var data : resolvedWorkingList) {
            // rateIdが0でなければ更新
            if (data.rateId() != 0) {
                if (snapShotMap.containsKey(data.rateId())) {
                    DiscountRateTable oldItem = snapShotMap.get(data.rateId());

                    if (oldItem.per() != data.per()
                            || oldItem.orderIndex() != data.orderIndex()) {
                        updateList.add(data);
                    }
                }else{
                    Log.e("CustomPreferenceListViewModel",
                            String.format("rate Id not found in snapShotMap : [saveName:%s, preferenceId:%d, rateId:%d, per:%d, orderIndex:%d]",
                            saveName,preferenceId,data.rateId(),data.per(),data.orderIndex()));
                    return false;
                }
            } else {
                // insertリストのrateIdは自動採番をしてもらう為に0を入れる
                insertList.add(new DiscountRateTable(0,data.preferenceId(),data.per(),data.orderIndex()));
            }
        }

        // resolvedWorkingListからrateIdが0でないもののみ抽出(削除判定用)
        Set<Long> rWorkIdSet =
                resolvedWorkingList.stream()
                        .map(DiscountRateTable::rateId)
                        .filter(l -> l != 0)
                        .collect(Collectors.toSet());

        // 削除判定
        deleteList = snapShot.stream()
                .filter(ss -> !rWorkIdSet.contains(ss.rateId()))
                .collect(Collectors.toList());


        preferenceRepository.updateRates(deleteList,insertList,updateList);
        return true;
    }

    private List<DiscountRateTable> createResolvedWorkingList(long preferenceId,List<EditableDiscountRate> eWorkingList,List<DiscountRateTable> reviveCandidateList) {
        List<DiscountRateTable> resolvedWorkingList = new ArrayList<>();
        for (var work : eWorkingList) {

            if (work.rateId() != 0) {
                resolvedWorkingList.add(new DiscountRateTable(work.rateId(), preferenceId, work.per(), work.orderIndex()));
                continue;
            }

            // 追加したカラムと復活候補カラムのperが一致している時は復活候補カラムのrateIdを再利用する
            // なければ新規追加扱い
            long revivedRateId = reviveCandidateList.stream()
                    .filter(ss -> ss.per() == work.per())
                    .findFirst()
                    .map(DiscountRateTable::rateId)
                    .orElse(0L);

            // rateIdを更新したリストを作成
            resolvedWorkingList.add(new DiscountRateTable(revivedRateId, preferenceId, work.per(), work.orderIndex()));
        }
        // データ漏れがあった場合はnullを返して異常であることを返す
        if(resolvedWorkingList.size() != eWorkingList.size()) return null;
        return resolvedWorkingList;
    }

    //　引数の名前が既に保存されていないか確認
    public boolean existingCheckDAO(String name) {
        return getSaveNameList().stream().anyMatch(name::equals);
    }

    // 保存名から親データを探す
    private CustomPreferenceTable findPreferenceBySaveName(String saveName) {
        List<CustomPreferenceTable> currentList = preferenceTableLiveDataList.getValue();

        if (currentList == null) {
            return null;
        }

        return currentList.stream()
                .filter(item -> item.saveName().equals(saveName))
                .findFirst()
                .orElse(null);
    }

    //　localIdと一致する要素を検索
    private int findRateIndex(int localId, List<EditableDiscountRate> targetList) {
        for (int i = 0; i < targetList.size(); i++) {
            if (targetList.get(i).localId() == localId) {
                return i;
            }
        }
        return -1;
    }

    // orderIndexの再構築
    private void reindex(List<EditableDiscountRate> list) {
        for (int i = 0; i < list.size(); i++) {
            EditableDiscountRate oldItem = list.get(i);

            if (oldItem.orderIndex() == i) {
                continue;
            }

            // orderIndexが一致しない(大きい)場合は詰める
            list.set(i, new EditableDiscountRate(
                    oldItem.localId(),
                    oldItem.rateId(),
                    oldItem.preferenceId(),
                    oldItem.per(),
                    i
            ));
        }
    }


    // per基準でリストを昇順に並び替え
    public void sortByPerAscending() {
        List<EditableDiscountRate> updated = workingListOrNull();
        if (updated == null) return;

        updated.sort(Comparator.comparingInt(EditableDiscountRate::per));

        reindex(updated);
        workingList.setValue(updated);
    }

    // per基準でリストを降順に並び替え
    public void sortByPerDescending() {
        List<EditableDiscountRate> updated = workingListOrNull();
        if (updated == null) return;

        updated.sort(Comparator.comparingInt(EditableDiscountRate::per).reversed());

        reindex(updated);
        workingList.setValue(updated);
    }

}
