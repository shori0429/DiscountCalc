package com.example.discountcalc.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
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
import com.example.discountcalc.DataBase.CustomConfigDataStoreSingleton;
import com.example.discountcalc.Params.DiscountData;
import com.example.discountcalc.databinding.ResultPriceListBinding;

import java.util.ArrayList;
import java.util.List;


class PriceViewModel extends ViewModel {
    private MutableLiveData<TextView> priceText;

    public LiveData<TextView> getPriceText() {
        return priceText;
    }
}

public class ResultListFragment extends Fragment {

    private static final String TAG_STORE_NAME = "custom_setting_data";
    private int price;
    private PriceViewModel priceViewModel;
    private View view;

    RxDataStore<Preferences> datastoreRX;
    CustomConfigDataStoreSingleton customConfigDataStoreSingleton;

    private ResultPriceListBinding resultPriceListBinding;
    private Preferences pref_error;

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

        customConfigDataStoreSingleton=CustomConfigDataStoreSingleton.getInstance();
        if(customConfigDataStoreSingleton==null){
            datastoreRX=new RxPreferenceDataStoreBuilder(view.getContext(),TAG_STORE_NAME).build();
        }else{
            datastoreRX=customConfigDataStoreSingleton.getDatastore();
        }

//        // Get the ViewModel.
//        priceViewModel=new ViewModelProvider(this).get(PriceViewModel.class);
//        // Create the observer which updates the UI.
//        final Observer<TextView> priceObserver= textView -> price=Integer.parseInt(textView.getText().toString());
//        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
//        priceViewModel.getPriceText().observe(getViewLifecycleOwner(),priceObserver);

        return view;
    }

    private List<DiscountData> getDiscountDataList() {
        ArrayList<DiscountData> list = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            DiscountData data = new DiscountData();
            data.setDiscount(i);
            data.setDiscountPrice(i * 10);
            data.setPrice(i * 100);

            list.add(data);
        }
        return list;
    }

}