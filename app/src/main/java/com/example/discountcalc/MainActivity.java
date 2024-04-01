package com.example.discountcalc;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discountcalc.CustomAdapters.ResultLayoutAdapter;
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

        RecyclerView rv = findViewById(R.id.resultPriceList);
        ResultLayoutAdapter adapter = new ResultLayoutAdapter(this.getDiscountDataList());

        //
        rv.setHasFixedSize(true);

        //
        rv.setLayoutManager(new LinearLayoutManager(this));

        //
        rv.setAdapter(adapter);

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