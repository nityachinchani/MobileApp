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

public class SignUp extends AppCompatActivity {
    EditText fullnameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    Button signUpBtn;
    TextView signInTextView;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullnameEditText=findViewById(R.id.fullnameEditText);
        emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        confirmPasswordEditText=findViewById(R.id.confirmpasswordEditText);
        signUpBtn=findViewById(R.id.signUpBtn);
        signInTextView=findViewById(R.id.signInTextView);

        mFirebaseAuth=FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname=fullnameEditText.getText().toString();
                String email=emailEditText.getText().toString();
                String password=passwordEditText.getText().toString();
                String confirmPassword=confirmPasswordEditText.getText().toString();

                if(email.isEmpty() ){
                    emailEditText.setError("Please enter email id");
                    emailEditText.requestFocus();
                }

                else if(fullname.isEmpty() ){
                    fullnameEditText.setError("Please enter fullname");
                    fullnameEditText.requestFocus();
                }

                else if(password.isEmpty() ){
                    passwordEditText.setError("Please enter password");
                    passwordEditText.requestFocus();
                }

                else if(confirmPassword.isEmpty() ){
                    confirmPasswordEditText.setError("Please enter password");
                    confirmPasswordEditText.requestFocus();
                }

                else if(!password.equals(confirmPassword) ){
                    Toast.makeText(SignUp.this,"Passwords dont match",Toast.LENGTH_SHORT).show();
                }


                else if (!(email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUp.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(SignUp.this,"Signup unsuccessful, try again",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        startActivity(new Intent(SignUp.this, MainActivity.class));  //change this class once homescreen is created
                                    }
                                }
                            });
                }

            }
        });


        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this,MainActivity.class));
            }
        });

    }
}
