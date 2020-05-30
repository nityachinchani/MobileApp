package com.example.coen268project.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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
import android.widget.TextView;
import android.widget.Toast;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Presentation.Account;
import com.example.coen268project.Presentation.Utility;
import com.example.coen268project.R;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUp extends AppCompatActivity {
    public static final int CAMERA_REQUEST_CODE = 102;
    EditText fullnameEditText, emailEditText, numberEditText, passwordEditText, confirmPasswordEditText;
    Button signUpBtn;
    TextView signInTextView;
    private Account account;
    private Utility utility;
    ImageView profileImage;
    public static final int CAMERA_PERMISSION_CODE=100;
    String currentPhotoPath;
    Uri imageUri;
    Uri contentUri;
    File f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = new Account();
        utility = new Utility();
        setContentView(R.layout.activity_sign_up);
        fullnameEditText=findViewById(R.id.fullnameEditText);
        emailEditText=findViewById(R.id.emailEditText);
        numberEditText=findViewById(R.id.numberEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        confirmPasswordEditText=findViewById(R.id.confirmpasswordEditText);
        signUpBtn=findViewById(R.id.signUpBtn);
        signInTextView=findViewById(R.id.signInTextView);
        profileImage=findViewById(R.id.profileImage);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullName=fullnameEditText.getText().toString();
                final String email=emailEditText.getText().toString();
                final String number=numberEditText.getText().toString();
                final String password=passwordEditText.getText().toString();
                String confirmPassword=confirmPasswordEditText.getText().toString();

                if(email.isEmpty() ){
                    emailEditText.setError("Please enter email id");
                    emailEditText.requestFocus();
                }

                else if(fullName.isEmpty() ){
                    fullnameEditText.setError("Please enter fullname");
                    fullnameEditText.requestFocus();
                }

                else if(number.isEmpty() ){
                    numberEditText.setError("Please enter number");
                    numberEditText.requestFocus();
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
                            utility.setCurrentUserId(object.toString());
                            utility.uploadImageToStorage(f.getName(), contentUri, new CallBack() {
                                @Override
                                public void onSuccess(Object object) {
                                    Toast.makeText(SignUp.this,"Image upload succeeded",Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(Object object) {
                                    Toast.makeText(SignUp.this,"Image upload failed",Toast.LENGTH_SHORT).show();
                                }
                            });

                            account.createAccount(object.toString(), fullName, email, number, password, f.getName(), new CallBack() {
                                @Override
                                public void onSuccess(Object object) {
                                    startActivity(new Intent(SignUp.this, MainActivity.class));  //change this class once homescreen is created
                                }

                                @Override
                                public void onError(Object object) {
                                    // TO Do: Delete authentication entry on user creation failure
                                    Toast.makeText(SignUp.this,"User creation unsuccessful, try again",Toast.LENGTH_SHORT).show();
                                }
                            });

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

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });

    }

    private void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE);
        }
        else {
            dispatchTakePictureIntent();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }
            else {
                Toast.makeText(this,"Camera permission is required to use camera",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
             f= new File(currentPhotoPath);
            profileImage.setImageURI(Uri.fromFile(f));

            Log.d("tag","Absolute url of image"+Uri.fromFile(f));

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
             contentUri = Uri.fromFile(f);

            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

        }
    }

    private File createImageFile() throws IOException {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = image.getAbsolutePath();
            // Create an image file name
            return image;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
       return null;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

}
