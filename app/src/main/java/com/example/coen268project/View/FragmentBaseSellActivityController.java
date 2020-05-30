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
        if (intent.hasExtra("from")) {
            from = intent.getExtras().getString("from");
        }

        if (from.equals(Upload_fragment.class.getSimpleName())){
            Bundle bundle = new Bundle();
            final String item_2 = getIntent().getStringExtra("Item_1");
            final String location_2 = getIntent().getStringExtra("Location");
            bundle.putString("Item",item_2);
            bundle.putString("Location",location_2);
            Upload_fragment upload_fragment = new Upload_fragment();
            upload_fragment.setArguments(bundle);
            fragmentTransaction(upload_fragment);
        }

        if (from.equals(Sell_Description.class.getSimpleName()))
        {
            Bundle bundle = new Bundle();
            final String item_3 = getIntent().getStringExtra("Item");
            final String location_3 = getIntent().getStringExtra("Location");
            final String path_3  = getIntent().getStringExtra("Path");
            bundle.putString("Item",item_3);
            bundle.putString("Location",location_3);
            bundle.putString("Path",path_3);
            Sell_Description sell_description = new Sell_Description();
            sell_description.setArguments(bundle);
            fragmentTransaction(sell_description);
        }

        if (from.equals(Update_Ads.class.getSimpleName()))
        {
            Bundle bundle = new Bundle();
            final String ItemId = getIntent().getStringExtra("ItemId");
            bundle.putString("ItemId",ItemId);
            Update_Ads updateAds = new Update_Ads();
            updateAds.setArguments(bundle);
            fragmentTransaction(updateAds);
        }
    }
    private void fragmentTransaction(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer,fragment ).addToBackStack(null).commit();
    }
}
