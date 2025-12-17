package com.example.discountcalc.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;

import com.example.discountcalc.R;
import com.example.discountcalc.databinding.ActivityMainBinding;
import com.example.discountcalc.fragments.ToolBarFragment;

public class MainActivity extends AppCompatActivity{
    private ActivityMainBinding binding;

    ToolBarFragment toolBarFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        Toolbar toolbar=binding.mainToolbar;
        setSupportActionBar(toolbar);
        addToolBarOption();

        toolBarFragment=new ToolBarFragment();
    }


    @Override
    protected void onStart() {
        super.onStart();

//        // ツールバーフラグメントを配置
//        FragmentManager fragmentManager=getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.Main_ToolBarView,toolBarFragment)
//                .commit();



        // 空白箇所タップでフォーカスを外す(子フラグメントのeditText用)
        binding.getRoot().setOnClickListener(v->{
            InputMethodManager imm=(InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            v.clearFocus();
        });
    }
    private void addToolBarOption(){
        addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_main,menu);
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.menu_action_settings){
                    openMenu();
                    return true;
                }
                return false;
            }
        });
    }

    private void openMenu() {
        Toast.makeText(this, "めにゅー", Toast.LENGTH_SHORT).show();
    }
}