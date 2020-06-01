package com.example.coen268project.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.coen268project.R;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity implements BottomFragment.OnFragmentInteractionListener {
    public enum FragmentType {
        EXPLORE, CHATS, SELL, ADS, ACCOUNT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment_controller);

        ExploreFragment defaultFragment = new ExploreFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, defaultFragment).commit();
        BottomFragment bottomFragment = new BottomFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.bottomContainer, bottomFragment).commit();
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
}
