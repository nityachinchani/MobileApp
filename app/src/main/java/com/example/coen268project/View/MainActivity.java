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
import com.example.coen268project.Firebase.FirebaseConstants;
import com.example.coen268project.Firebase.FirebaseInstance;
import com.example.coen268project.Presentation.Account;
import com.example.coen268project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText usernameEditText, passwordEditText;
    Button logInBtn, guestLoginBtn;
    TextView signUpTextView, forgotPasswordTextView;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = new Account();
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
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = usernameEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                if (email.isEmpty()) {
                    usernameEditText.setError("Enter email id");
                    usernameEditText.requestFocus();
                } else if (password.isEmpty()) {
                    passwordEditText.setError("Enter password");
                    passwordEditText.requestFocus();
                } else if (!(email.isEmpty() && password.isEmpty())) {

                    account.validateUser(email, new CallBack() {
                        @Override
                        public void onSuccess(Object object) {
                            if(!(boolean)object){
                                Toast.makeText(MainActivity.this,"Register to login",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else {
                                account.authenticateAccount(email, password, new CallBack() {
                                    @Override
                                    public void onSuccess(Object object) {
                                        startActivity(new Intent(MainActivity.this, HomeActivity.class));  //change this class once homescreen is created
                                    }

                                    @Override
                                    public void onError(Object object) {
                                        Toast.makeText(MainActivity.this, "Login error, check credentials", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(Object object) {

                        }
                    });

                }
            }
        });


        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

}