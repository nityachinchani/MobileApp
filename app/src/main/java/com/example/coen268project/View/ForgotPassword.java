package com.example.coen268project.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.coen268project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotPassword extends AppCompatActivity {
    Button forgotPasswordBtn;
    TextView forgotEmailTextView;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotEmailTextView = findViewById(R.id.forgotEmailTextView);
        forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn);
        mFirebaseAuth = FirebaseAuth.getInstance();

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.sendPasswordResetEmail(forgotEmailTextView.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassword.this,"Password sent to your email",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(ForgotPassword.this,"Email doesnt exist",Toast.LENGTH_SHORT).show();
                                }

                                startActivity(new Intent(ForgotPassword.this,MainActivity.class));
                            }
                        });

            }
        });
    }
}
