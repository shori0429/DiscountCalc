package com.example.discountcalc;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.discountcalc.CustomAdapters.ResultLayoutAdapter;
import com.example.discountcalc.Fragments.TitleFragment;
import com.example.discountcalc.Params.DiscountData;
import com.example.discountcalc.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //RecyclerView rv = findViewById(R.id.resultPriceList);
        ResultLayoutAdapter adapter = new ResultLayoutAdapter(this.getDiscountDataList());

        TitleFragment titleFragment=new TitleFragment();

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.calcTitleView,titleFragment);
        fragmentTransaction.commit();
        //
        //rv.setHasFixedSize(true);

        //
       // rv.setLayoutManager(new LinearLayoutManager(this));

        //
       // rv.setAdapter(adapter);

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