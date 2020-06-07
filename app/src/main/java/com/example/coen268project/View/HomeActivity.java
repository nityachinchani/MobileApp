package com.example.coen268project.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Model.AccountDao;
import com.example.coen268project.Presentation.Account;
import com.example.coen268project.Presentation.Utility;
import com.example.coen268project.R;
import com.google.android.material.navigation.NavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements BottomFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener{
    String from="";
    private Account account;
    CircleImageView c;
    TextView textView;
    private DrawerLayout drawerLayout;

    public enum FragmentType {
        EXPLORE, CHATS, SELL, ADS, ACCOUNT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        account = new Account();
        drawerLayout=findViewById(R.id.drawerLayout);
        NavigationView navigationView=findViewById(R.id.drawer_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.inflateHeaderView(R.layout.drawer_header);
        c = headerLayout.findViewById(R.id.drawerImageCircleImageView);  //setup the profile image here
        textView = headerLayout.findViewById(R.id.drawerTextView);


        account.getAccount(Utility.getCurrentUserId(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                AccountDao accountDao = (AccountDao) object;
                textView.setText(accountDao.getUserName());
                Glide.with(HomeActivity.this).load(accountDao.getPictureName()).into(c);
            }
            @Override
            public void onError(Object object)
            {

            }
        });


        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.hello_blank_fragment,R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        ExploreFragment defaultFragment = new ExploreFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, defaultFragment).commit();
        BottomFragment bottomFragment = new BottomFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.bottomContainer, bottomFragment).commit();
        getExtras();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.my_profile:
                startActivity(new Intent(this,MyProfileActivity.class));
                break;
            case R.id.my_billings:
                startActivity(new Intent(this,MyBillingActivity.class));
                break;
            case R.id.my_orders:
                startActivity(new Intent(this,MyOrdersActivity.class));
                break;
            case R.id.logout:

                startActivity(new Intent(this,Splash_screen.class));
                finish();
                break;
        }
        return true;
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
