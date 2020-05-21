package com.example.coen268project.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.coen268project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText usernameEditText, passwordEditText;
    Button logInBtn, guestLoginBtn;
    TextView signUpTextView, forgotPasswordTextView;
    FirebaseAuth mFirebaseAuth ;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        logInBtn = findViewById(R.id.logInBtn);
        guestLoginBtn = findViewById(R.id.guestBtn);
        signUpTextView = findViewById(R.id.signUpTextView);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        mFirebaseAuth=FirebaseAuth.getInstance();

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignUp.class));
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=usernameEditText.getText().toString();
                String password=passwordEditText.getText().toString();

                if(email.isEmpty() ){
                    usernameEditText.setError("Please enter email id");
                    usernameEditText.requestFocus();
                }
                else if(password.isEmpty() ){
                    passwordEditText.setError("Please enter password");
                    passwordEditText.requestFocus();
                }

                else if (!(email.isEmpty() && password.isEmpty() )){
                    mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(MainActivity.this,"Login error, please check credentials",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        startActivity(new Intent(MainActivity.this, SignUp.class));  //change this class once homescreen is created
                                    }
                                }
                            });
                }


            }
        });

        guestLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignUp.class)); // change this to homescreen once activity is created
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle forgot password
            }
        });

    }


}
