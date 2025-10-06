package com.example.discountcalc.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.discountcalc.R;
import com.example.discountcalc.viewModels.DiscountCalcViewModel;
import com.example.discountcalc.databinding.CalcTitleBinding;

public class TitleFragment extends Fragment implements CustomTextWatcher {

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

        // ViewModelデータ取得
        discountCalcViewModel=new ViewModelProvider(requireActivity()).get(DiscountCalcViewModel.class);

        // 金額入力欄の入力イベント取得設定
        calcTitleBinding.priceTextField.addTextChangedListener(this);

        // ツールバーFragment設定
//        ToolBarFragment toolBarFragment=new ToolBarFragment();
//        getChildFragmentManager().beginTransaction()
//                .replace(R.id.title_ToolBarView, toolBarFragment)
//                .setReorderingAllowed(true)
//                .commit();

        // 結果表示Fragment設定
        ResultListFragment resultListFragment=new ResultListFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.title_resultListView,resultListFragment)
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

        // androidキーボードのenter押下イベント
        // (エミュ:何故か少し長押ししてから離さないと認識しない)
        // (実機:未検証)
        calcTitleBinding.priceTextField.setOnKeyListener((v, keyCode, event) -> {
            if(event.getKeyCode()==KeyEvent.KEYCODE_ENTER){
                switch(event.getAction()){
                    // 押したとき
                    case KeyEvent.ACTION_DOWN:
                        Log.i("key_event", "enter down");
                        break;
                    // 離した時
                    case KeyEvent.ACTION_UP:
                        Log.i("key_event", "enter up");
                        // キーボードの表示を消す
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        break;
                    default:

                }
                return true;
            }
            return false;
        });


    }

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
