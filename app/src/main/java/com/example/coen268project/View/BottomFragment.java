package com.example.coen268project.View;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.coen268project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * create an instance of this fragment.
 */
public class BottomFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {
    OnFragmentInteractionListener listener;

    public BottomFragment() {
        // Required empty public constructor
    }

    public interface OnFragmentInteractionListener{
        void replaceFragment(HomeActivity.FragmentType type);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        BottomNavigationView bottomNavigationView;
        bottomNavigationView= view.findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.explore);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.explore:
                listener.replaceFragment(HomeActivity.FragmentType.EXPLORE);
                break;

            case R.id.chats:
                listener.replaceFragment(HomeActivity.FragmentType.CHATS);
                break;

            case R.id.sell:
                listener.replaceFragment(HomeActivity.FragmentType.SELL);
                break;

            case R.id.ads:
                listener.replaceFragment(HomeActivity.FragmentType.ADS);
                break;

            case R.id.account:
                listener.replaceFragment(HomeActivity.FragmentType.ACCOUNT);
                break;
        }

        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
