package com.example.coen268project.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Presentation.Account;
import com.example.coen268project.R;

public class MainActivity extends AppCompatActivity {
    EditText usernameEditText, passwordEditText;
    Button logInBtn, guestLoginBtn;
    TextView signUpTextView, forgotPasswordTextView;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = new Account(this);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        logInBtn = findViewById(R.id.logInBtn);
        guestLoginBtn = findViewById(R.id.guestBtn);
        signUpTextView = findViewById(R.id.signUpTextView);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUp.class));
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty()) {
                    usernameEditText.setError("Please enter email id");
                    usernameEditText.requestFocus();
                } else if (password.isEmpty()) {
                    passwordEditText.setError("Please enter password");
                    passwordEditText.requestFocus();
                } else if (!(email.isEmpty() && password.isEmpty())) {
                    account.authenticateAccount(email, password, new CallBack() {
                        @Override
                        public void onSuccess(Object object) {
                            startActivity(new Intent(MainActivity.this, SignUp.class));  //change this class once homescreen is created
                        }

                        @Override
                        public void onError(Object object) {
                            Toast.makeText(MainActivity.this, "Login error, please check credentials", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        guestLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main_Fragment_Controller.class));

            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
            }
        });
    }
}
