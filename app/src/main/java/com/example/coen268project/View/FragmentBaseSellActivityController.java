package com.example.coen268project.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.coen268project.R;

public class FragmentBaseSellActivityController extends AppCompatActivity {
    String from="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_base);

       getExtras();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void getExtras() {
        Intent intent = getIntent();
        if (intent.hasExtra("from")){
           from=intent.getExtras().getString("from");
        }

        if (from.equals(Upload_fragment.class.getSimpleName())){
            fragmentTransaction(new Upload_fragment());
        }

        if (from.equals(Explore_fragment.class.getSimpleName())){
            fragmentTransaction(new Explore_fragment());
        }


    }
    private void fragmentTransaction(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer,fragment ).addToBackStack(null).commit();
    }
}
