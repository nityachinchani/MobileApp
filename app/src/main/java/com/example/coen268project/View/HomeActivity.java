package com.example.coen268project.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.coen268project.R;

import android.content.Intent;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity implements BottomFragment.OnFragmentInteractionListener {
    String from="";

    public enum FragmentType {
        EXPLORE, CHATS, SELL, ADS, ACCOUNT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ExploreFragment defaultFragment = new ExploreFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, defaultFragment).commit();
        BottomFragment bottomFragment = new BottomFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.bottomContainer, bottomFragment).commit();
        getExtras();
    }

    @Override
    public void replaceFragment(FragmentType type) {
        switch(type){
            case EXPLORE:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ExploreFragment()).addToBackStack(null).commit();
                break;
            case CHATS:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ChatsFragment()).addToBackStack(null).commit();
                break;
            case SELL:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new SellFragment()).addToBackStack(null).commit();
                break;
            case ADS:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new AdsFragment()).addToBackStack(null).commit();
                break;
            case ACCOUNT:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new AccountFragment()).addToBackStack(null).commit();
                break;
        }
    }

    private void getExtras() {
        Intent intent = getIntent();
        if (intent.hasExtra("from")) {
            from = intent.getExtras().getString("from");
        }

        if (from.equals(UploadFragment.class.getSimpleName())){
            Bundle bundle = new Bundle();
            final String item_2 = getIntent().getStringExtra("Item_1");
            final String location_2 = getIntent().getStringExtra("Location");
            bundle.putString("Item",item_2);
            bundle.putString("Location",location_2);
            UploadFragment upload_fragment = new UploadFragment();
            upload_fragment.setArguments(bundle);
            fragmentTransaction(upload_fragment);
        }

        if (from.equals(SellDescriptionFragment.class.getSimpleName()))
        {
            Bundle bundle = new Bundle();
            final String item_3 = getIntent().getStringExtra("Item");
            final String location_3 = getIntent().getStringExtra("Location");
            final String path_3  = getIntent().getStringExtra("Path");
            bundle.putString("Item",item_3);
            bundle.putString("Location",location_3);
            bundle.putString("Path",path_3);
            SellDescriptionFragment sell_descriptionFragment = new SellDescriptionFragment();
            sell_descriptionFragment.setArguments(bundle);
            fragmentTransaction(sell_descriptionFragment);
        }
    }

    private void fragmentTransaction(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
    }
}
