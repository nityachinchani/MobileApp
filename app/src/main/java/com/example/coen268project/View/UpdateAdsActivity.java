package com.example.coen268project.View;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Model.ItemDao;
import com.example.coen268project.Presentation.Item;

import com.example.coen268project.Presentation.Utility;
import com.example.coen268project.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * create an instance of this fragment.
 */
public class UpdateAdsActivity extends AppCompatActivity {
    Button btnUpdate;
    Button btnDelete;
    private EditText et_ProductName;
    private EditText et_Price;
    private TextView et_Description;
    private Spinner statusSpinner;
    private Item item;
    private ImageView img_ProductPicture;
    private static String[] itemStatus = Utility.ItemStatus.toArray();
    private Utility utility;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int CAMERA_PERMISSION_CODE=100;
    Uri contentUri;
    File f;
    final String[] product_picture_name = {""};
    String currentPhotoPath;

    public UpdateAdsActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ads);
        Bundle bundle = getIntent().getExtras();
        item = new Item();
        utility = new Utility();
        et_ProductName = findViewById(R.id.et_ProductName);
        et_Price = findViewById(R.id.et_Price);
        et_Description = findViewById(R.id.et_Description);
        img_ProductPicture = findViewById(R.id.product_image);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        statusSpinner = findViewById(R.id.itemStatusSpinner);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(UpdateAdsActivity.this, R.layout.activity_spinner_row, R.id.text_id, itemStatus);
        statusSpinner.setAdapter(categoryAdapter);
        final String itemId = bundle.getString("ItemId");
        final Map<String, String> updates = new HashMap<>();
        item.getItem(itemId, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                ItemDao itemDao = (ItemDao) object;
                BindItems(itemDao);
            }

            @Override
            public void onError(Object object) {

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final String product_name = et_ProductName.getText().toString();
                final String product_price = et_Price.getText().toString();
                final String product_description = et_Description.getText().toString();
                updates.put("itemName", product_name);
                updates.put("description", product_description);
                updates.put("price", product_price);
                updates.put("itemStatus", statusSpinner.getSelectedItem().toString());
                if(statusSpinner.getSelectedItem().toString().equals(Utility.ItemStatus.BOOKED)) {
                    updates.put("buyerId", Utility.getCurrentUserId());
                }
                updates.put("productPictureName",product_picture_name[0]);
                item.updateItem(itemId, (HashMap) updates, new CallBack() {
                    @Override
                    public void onSuccess(Object object) {
                        Toast.makeText(UpdateAdsActivity.this,"Order updated successfully",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Object object) {
                        Toast.makeText(UpdateAdsActivity.this,"Order update failed",Toast.LENGTH_LONG).show();
                    }
                });
                Intent intent = new Intent(UpdateAdsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                item.deleteItem(itemId, new CallBack() {
                    @Override
                    public void onSuccess(Object object) {
                        Toast.makeText(UpdateAdsActivity.this,"Order deleted successfully",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Object object) {
                        Toast.makeText(UpdateAdsActivity.this,"Order update failed",Toast.LENGTH_LONG).show();
                    }
                });
                Intent intent = new Intent(UpdateAdsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        img_ProductPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });
    }

    private void uploadPictureToFirebase() {

        product_picture_name[0] = f.getName();
        utility.uploadImageToStorage(product_picture_name[0], contentUri, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                btnUpdate.setEnabled(true);
                product_picture_name[0] =  object.toString();
                Toast.makeText(UpdateAdsActivity.this, "Image upload succeeded", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Object object) {
                Toast.makeText(UpdateAdsActivity.this, "Image upload in progresss" + (int) object, Toast.LENGTH_SHORT).show();
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
            img_ProductPicture.setImageURI(Uri.fromFile(f));

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



    private void BindItems(ItemDao itemDao) {
        et_ProductName.setText(itemDao.getItemName());
        et_Description.setText(itemDao.getDescription());
        et_Price.setText(itemDao.getPrice());
        statusSpinner.setSelection(Utility.ItemStatus.getIndex(itemDao.getItemStatus()));
        Glide.with(UpdateAdsActivity.this).load(itemDao.getPictureName()).into(img_ProductPicture);
        if(statusSpinner.getSelectedItem().toString().equals(Utility.ItemStatus.SOLD.toString()))
        {
            Toast.makeText(UpdateAdsActivity.this,"Item already sold, you do not have permission to update.",Toast.LENGTH_LONG).show();
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
        }
    }

}