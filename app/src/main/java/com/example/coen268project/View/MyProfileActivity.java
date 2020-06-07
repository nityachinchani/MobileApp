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
    private Utility utility;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int CAMERA_PERMISSION_CODE=100;
    Uri contentUri;
    File f;
    final String[] picture_name = {""};
    private Button btnUpdate;
    private Account account;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        account = new Account();
        utility = new Utility();
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
                final String profile_picture = f.getName();


                updates.put("userName", profile_name);
                updates.put("email", profile_email);
                updates.put("phoneNumber", profile_phone_number);
                updates.put("password", profile_password);
                updates.put("pictureName",profile_picture);

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


        img_ProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });

    }


    private void uploadPictureToFirebase() {

            picture_name[0] = f.getName();
            utility.uploadImageToStorage(picture_name[0], contentUri, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    btnUpdate.setEnabled(true);
                    picture_name[0] =  object.toString();
                    Toast.makeText(MyProfileActivity.this, "Image upload succeeded", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(Object object) {
                    Toast.makeText(MyProfileActivity.this, "Image upload in progresss" + (int) object, Toast.LENGTH_SHORT).show();
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
            img_ProfilePicture.setImageURI(Uri.fromFile(f));

            Log.d("tag","Absolute url of image"+Uri.fromFile(f));

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            contentUri = Uri.fromFile(f);

            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
        }
        uploadPictureToFirebase();
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

    private void BindAccount(AccountDao accountDao) {
        et_ProfileName.setText(accountDao.getUserName());
        et_Email.setText(accountDao.getEmail());
        et_PhoneNumber.setText(accountDao.getPhoneNumber());
        et_Password.setText(accountDao.getPassword());
        Glide.with(MyProfileActivity.this).load(accountDao.getPictureName()).into(img_ProfilePicture);
    }

}
