package com.example.coen268project.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.coen268project.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Upload_fragment extends Fragment {

    Button button, butcam, butupload;
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final int PICK_IMAGE_REQUEST = 103;

    ImageView imageView;
    private static final int CAMERA_PERMISSION_CODE=100;
    String currentPhotoPath;
    Uri imageUri;
    Uri contentUri;
    File f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_fragment, container, false);
        button = (Button) view.findViewById(R.id.button_con);
        butcam = (Button) view.findViewById(R.id.button_camera);
        butupload = (Button) view.findViewById(R.id.button_choose);
        imageView = (ImageView) view.findViewById(R.id.upload_image);


        butupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        butcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                Intent intent = new Intent(getActivity(), Main_Fragment_Controller.class);
                startActivity(intent);
            }
        });
        return view;

    }

    public void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE);
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
                Log.d("tag","Camera permission is required to use camera");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE || requestCode == PICK_IMAGE_REQUEST) {
            f= new File(currentPhotoPath);
            imageView.setImageURI(Uri.fromFile(f));

            Log.d("tag","Absolute url of image"+Uri.fromFile(f));

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            contentUri = Uri.fromFile(f);

            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);

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

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go

            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


}
