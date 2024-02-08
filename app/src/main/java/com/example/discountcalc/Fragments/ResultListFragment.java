package com.example.discountcalc.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.datastore.rxjava3.RxDataStoreBuilder;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.discountcalc.CustomAdapters.ResultLayoutAdapter;
import com.example.discountcalc.CustomConfigData;
import com.example.discountcalc.DataBase.CustomConfigDataStoreHelper;
import com.example.discountcalc.DataBase.CustomConfigDataStoreSingleton;
import com.example.discountcalc.DataBase.CustomConfigSerializer;
import com.example.discountcalc.Params.DiscountData;
import com.example.discountcalc.databinding.ResultPriceListBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

import io.reactivex.rxjava3.core.Flowable;


class PriceViewModel extends ViewModel {
    private MutableLiveData<TextView> priceText;

    public LiveData<TextView> getPriceText() {
        return priceText;
    }
}

public class ResultListFragment extends Fragment {

    private static final String TAG_STORE_NAME = "custom_setting_data";
    private static final String PRICE_KEY ="query_key";

    private static final String PERLABEL_KEY="per_label_key";

    private static final String PERDATA_KEY="per_data_key";
    private int price;
    private PriceViewModel priceViewModel;
    private View view;

    RxDataStore<Preferences> datastoreRX;

    CustomConfigDataStoreSingleton customConfigDataStoreSingleton;
    private CustomConfigDataStoreHelper customConfigDataStoreHelper;

    private ResultPriceListBinding resultPriceListBinding;

    ArrayList<DiscountData> configDataList = new ArrayList<>();

    public ResultListFragment() {
        // Required empty public constructor

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = resultPriceListBinding.resultPriceList;
        ResultLayoutAdapter adapter = new ResultLayoutAdapter(this.getDiscountDataList());
        LinearLayoutManager llm = new LinearLayoutManager(resultPriceListBinding.getRoot().getContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        resultPriceListBinding = ResultPriceListBinding.inflate(inflater, container, false);
        view = resultPriceListBinding.getRoot();
        Log.i("resultFragment", getParentFragmentManager().toString());

        getConfigDataStoreInstance();


//        // Get the ViewModel.
//        priceViewModel=new ViewModelProvider(this).get(PriceViewModel.class);
//        // Create the observer which updates the UI.
//        final Observer<TextView> priceObserver= textView -> price=Integer.parseInt(textView.getText().toString());
//        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
//        priceViewModel.getPriceText().observe(getViewLifecycleOwner(),priceObserver);

        return view;
    }


    // DataStore取得
    private void getConfigDataStoreInstance() {
        customConfigDataStoreSingleton = CustomConfigDataStoreSingleton.getInstance();
        if (customConfigDataStoreSingleton == null) {
            datastoreRX = new RxPreferenceDataStoreBuilder(view.getContext(), TAG_STORE_NAME).build();
        } else {
            datastoreRX = customConfigDataStoreSingleton.getDatastore();
        }
        customConfigDataStoreSingleton.setDataStore(datastoreRX);
    }

    private void saveDataStore() {
       // datastoreRX=new RxPreferenceDataStoreBuilder<CustomConfigData>(view.getContext(),"customConfig_setting.pb",new CustomConfigSerializer()).build();
    }

    private void loadDataStore(){
        //Flowable<Integer> priceData=dataStore.data().map(CustomConfigData::getPrice);
    }
    @Override
    public void onPause() {
        super.onPause();
        saveDataStore();
    }


    private List<DiscountData> getDiscountDataList() {
        for (int i = 1; i < 10; i++) {
            DiscountData data = new DiscountData();
            data.setDiscount(i);
            data.setDiscountPrice(i * 10);
            data.setPrice(i * 100);

            configDataList.add(data);
        }
        return configDataList;
    }

}