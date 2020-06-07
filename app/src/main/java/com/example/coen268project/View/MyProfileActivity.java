package com.example.coen268project.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Model.AccountDao;
import com.example.coen268project.Presentation.Account;
import com.example.coen268project.Presentation.Utility;
import com.example.coen268project.R;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyProfileActivity extends AppCompatActivity {
    private EditText et_ProfileName, et_Email, et_PhoneNumber, et_Password;
    private ImageView img_ProfilePicture;
    private Button btnUpdate;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        account = new Account();
        final Map<String, String> updates = new HashMap<>();
        et_ProfileName = findViewById(R.id.profileName);
        et_Email = findViewById(R.id.profileEmail);
        et_PhoneNumber =findViewById(R.id.profilePhoneNumber);
        et_Password = findViewById(R.id.profilePassword);
        img_ProfilePicture = findViewById(R.id.profileImage);
        btnUpdate = findViewById(R.id.btnUpdate);

        account.getAccount(Utility.getCurrentUserId(), new CallBack() {
            @Override
            public void onSuccess(Object object) {
                AccountDao accountDao = (AccountDao) object;
                BindAccount(accountDao);
            }

            @Override
            public void onError(Object object) {

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final String profile_name = et_ProfileName.getText().toString();
                final String profile_email = et_Email.getText().toString();
                final String profile_phone_number = et_PhoneNumber.getText().toString();
                final String profile_password= et_Password.getText().toString();


                updates.put("userName", profile_name);
                updates.put("email", profile_email);
                updates.put("phoneNumber", profile_phone_number);
                updates.put("password", profile_password);

                account.updateAccount(Utility.getCurrentUserId(), (HashMap) updates, new CallBack() {
                    @Override
                    public void onSuccess(Object object) {
                        Toast.makeText(MyProfileActivity.this,"My Profile updated successfully",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Object object) {
                        Toast.makeText(MyProfileActivity.this,"My Profile update failed",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    private void BindAccount(AccountDao accountDao) {
        et_ProfileName.setText(accountDao.getUserName());
        et_Email.setText(accountDao.getEmail());
        et_PhoneNumber.setText(accountDao.getPhoneNumber());
        et_Password.setText(accountDao.getPassword());
        Glide.with(MyProfileActivity.this).load(accountDao.getPictureName()).into(img_ProfilePicture);
    }
}
