package com.example.coen268project.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.coen268project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class location_fragment extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "location";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1245;
    private static final float DEFAULT_ZOOM = 15f;
    private EditText searchText;
    private ImageView currentLocation;
    private ImageView findHomeLocation;
    private Boolean locationPermissionsGranted = false;
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_fragment);
        searchText = findViewById(R.id.edit_search);
        currentLocation = findViewById(R.id.ic_current);
        findHomeLocation = findViewById(R.id.ic_go);
        button = findViewById(R.id.button_continue);
        final String item_1 = getIntent().getCharSequenceExtra("Item").toString();
        final String location_1 = searchText.getText().toString();
        getLocationPermission();

        //location_fragment.this.getActionBar().setTitle("Choose Location");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(location_fragment.this, FragmentBaseSellActivityController.class);
                intent.putExtra("from", Upload_fragment.class.getSimpleName());
                intent.putExtra("Item_1",item_1);
                intent.putExtra("Location","south sanfrancisco");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        map = googleMap;
        if (locationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.setMyLocationEnabled(false);
            //map.getUiSettings().setMyLocationButtonEnabled(false);
            initializeControls();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        locationPermissionsGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            locationPermissionsGranted = false;
                            Log.d(TAG, "Grant permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "Grant permission success");
                    locationPermissionsGranted = true;
                    initializeMap();
                }
            }
        }
    }

    /**
     * Method to get permission to access location details
     */
    private void getLocationPermission() {
        Log.d(TAG, "Get location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationPermissionsGranted = true;
                initializeMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Method that initializes the controls with events
     */
    private void initializeControls() {
        Log.d(TAG, "init: initializing");
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    geoLocate();
                }
                return false;
            }
        });

        findHomeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "clicked on Go icon");
                geoLocate();
            }
        });

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "clicked on current location icon");
                getDeviceLocation();
            }
        });

    }

    /**
     * Method to initialize map
     */
    private void initializeMap() {
        Log.d(TAG, "initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(location_fragment.this);
    }

    /**
     * Method to geo locate the pinned location
     */
    private void geoLocate() {
        Log.d(TAG, "geoLocate the current position");
        String searchString = searchText.getText().toString();
        Geocoder geocoder = new Geocoder(location_fragment.this);
        List<Address> addressList = new ArrayList<>();
        try {
            addressList = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "IOException in geoLocate: " + e.getMessage());
        }
        if (addressList.size() > 0) {
            Address address = addressList.get(0);
            Log.d(TAG, "Location: " + address.toString());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }

    /**
     * Method to get the current device location
     */
    private void getDeviceLocation()
    {
        Log.d(TAG, "Get the device current location");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try
        {
            if (locationPermissionsGranted)
            {
                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Location found!");
                            android.location.Location currentLocation = (android.location.Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "Current Location");
                        } else {
                            Log.d(TAG, "Location not found");
                            Toast.makeText(location_fragment.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        catch (SecurityException e)
        {
            Log.e(TAG, "SecurityException: " + e.getMessage());
        }
    }

    /**
     * Method to move and focus the camera to a specific loaction
     *
     * @param latLong - latitude and longitude position to move to
     * @param zoom    - magnification value
     * @param title   - title of the location
     */
    private void moveCamera(LatLng latLong, float zoom, String title) {
        Log.d(TAG, "moving the camera to: latitude: " + latLong.latitude + ", longitude: " + latLong.longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, zoom));

        if (!title.equals("Current Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLong)
                    .title(title);
            map.addMarker(options);
        }
    }
}