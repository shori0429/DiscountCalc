package com.example.discountcalc.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.discountcalc.R;
import com.example.discountcalc.databinding.CalcTitleBinding;

public class TitleFragment extends Fragment {

    private CalcTitleBinding calcTitleBinding;

    OnClickListener _clickListener;
    public TitleFragment(){
        super();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        calcTitleBinding = CalcTitleBinding.inflate(inflater, container, false);
        Log.i("TitleFragment",getParentFragmentManager().toString());
        return calcTitleBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            _clickListener=(OnClickListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString()+"must implement");
        }
    }

    private void setClickListeners(View view) {
        String text = "Button Click";
        calcTitleBinding.configButton.setOnClickListener(v -> {
            Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT).show();
            Log.i("test", text);
            _clickListener.onClick();

        });
    }

    public interface OnClickListener{
        void onClick();
    }
}
