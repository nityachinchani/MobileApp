package com.example.coen268project.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coen268project.R;

public class AccountFragment extends Fragment {
private ListView accountListView;
private TextView accountTextView;
private ImageView accountImage;

    private static  String[] titles = new String[]{"MyProfile", "MyBillings",
            "MyOrders", "Logout"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account,container,false);

        accountListView=(ListView) view.findViewById(R.id.accountListView);
        accountTextView=(TextView) view.findViewById(R.id.nameTextView);
        accountImage=(ImageView) view.findViewById(R.id.account_Image);

        accountTextView.setText("Name will be displayed here");

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(getActivity(), R.layout.activity_row,R.id.text_id,titles);
        accountListView.setAdapter(adapter);
        accountListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent;
                switch (accountListView.getItemAtPosition(i).toString()){
                    case "MyProfile":
                        intent = new Intent(getActivity(), MyProfileActivity.class);
                        break;
                    case "MyBillings":
                        intent = new Intent(getActivity(), MyBillingActivity.class);
                        break;
                    case "MyOrders":
                        intent = new Intent(getActivity(), MyOrdersActivity.class);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + accountListView.getItemAtPosition(i).toString());
                }
                startActivity(intent);


            }
        });

        return view;
    }
}
