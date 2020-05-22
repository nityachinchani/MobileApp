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
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Presentation.Account;
import com.example.coen268project.R;

public class SignUp extends AppCompatActivity {
    EditText fullnameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    Button signUpBtn;
    TextView signInTextView;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = new Account(this);
        setContentView(R.layout.activity_sign_up);
        fullnameEditText=findViewById(R.id.fullnameEditText);
        emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        confirmPasswordEditText=findViewById(R.id.confirmpasswordEditText);
        signUpBtn=findViewById(R.id.signUpBtn);
        signInTextView=findViewById(R.id.signInTextView);

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
                    account.createUserWithEmailAndPassword(email, password, new CallBack() {
                        @Override
                        public void onSuccess(Object object) {
                            startActivity(new Intent(SignUp.this, MainActivity.class));  //change this class once homescreen is created
                        }

                        @Override
                        public void onError(Object object) {
                            Toast.makeText(SignUp.this,"Signup unsuccessful, try again",Toast.LENGTH_SHORT).show();
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
