package com.example.coen268project.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.coen268project.R;

public class account_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        TextView wikipedia= findViewById(R.id.textViewId);
        Bundle bundle= getIntent().getExtras();
        if(bundle!=null){
            wikipedia.setText(bundle.getString("Android_version"));
        }
    }
}
