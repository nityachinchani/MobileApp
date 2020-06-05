package com.example.coen268project.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.coen268project.R;
import android.os.Bundle;
import android.widget.TextView;

public class MyProfileActivity extends AppCompatActivity {
    private TextView profileName,profileEmail,profilePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        profileName=findViewById(R.id.profileName);
        profileEmail=findViewById(R.id.profileEmail);
        profilePhone=findViewById(R.id.profilePhone);

        profileName.setText("Name");
        profileEmail.setText("Email");
        profilePhone.setText("Phone");
    }
}
