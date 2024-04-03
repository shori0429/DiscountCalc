package com.example.discountcalc.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.discountcalc.R;
import com.example.discountcalc.ViewModels.DiscountCalcViewModel;
import com.example.discountcalc.databinding.CalcTitleBinding;

public class TitleFragment extends Fragment implements TextWatcher {

    private CalcTitleBinding calcTitleBinding;


    InputMethodManager inputMethodManager;

    DiscountCalcViewModel discountCalcViewModel;

    public TitleFragment(){
        super();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        calcTitleBinding = CalcTitleBinding.inflate(inflater, container, false);
        Log.i("TitleFragment",getParentFragmentManager().toString());
        inputMethodManager=(InputMethodManager) calcTitleBinding.getRoot().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return calcTitleBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        discountCalcViewModel=new ViewModelProvider(requireActivity()).get(DiscountCalcViewModel.class);


        calcTitleBinding.priceTextField.addTextChangedListener(this);

        ResultListFragment resultListFragment=new ResultListFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.resultListView,resultListFragment)
                .setReorderingAllowed(true)
                .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = calcTitleBinding.getRoot();
        setClickListeners(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        calcTitleBinding = null;
    }

    // navigationGraphのDestination遷移を実装
    private void setNavGraphDestination(){
        NavHostFragment navHostFragment=(NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.host_fragment);
        assert navHostFragment != null:"null navHostFragment. TitleFragment.java line:89";
        NavController navHostController=navHostFragment.getNavController();
        NavDirections navDirections=TitleFragmentDirections.actionTitleFragmentToSettingsFragment();
        navHostController.navigate(navDirections);
    }



    private void setClickListeners(View view) {
        String text = "Button Click";
        calcTitleBinding.configButton.setOnClickListener(v -> {
            Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT).show();
            Log.i("test", text);
            setNavGraphDestination();
        });
        view.setOnClickListener(v->{
            // キーボードを隠す
            view.setOnClickListener(c-> inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS));
        });
    }


    // 文字列が修正される直前に呼び出されるメソッド
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    // 文字一つを入力したときに呼び出される
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    // 最後にこのメソッドが呼び出される
    @Override
    public void afterTextChanged(Editable s) {
        String inputStr=s.toString();

        if(inputStr.length()>0){
            // TextViewに入力された値をリアルタイムで反映
            discountCalcViewModel.setPrice(Integer.parseInt(calcTitleBinding.priceTextField.getText().toString()));
        }
        else{
            discountCalcViewModel.setPrice(0);
        }
        Log.i("priceText","price:"+discountCalcViewModel.getPrice().getValue());
    }
}
