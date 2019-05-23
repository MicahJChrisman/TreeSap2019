package com.example.treesapv2new.identify;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import com.example.treesapv2new.R;
import com.example.treesapv2new.control.PrefManager;
import com.example.treesapv2new.MainActivity;
import com.example.treesapv2new.view.MapsActivity;

public class Big_Red_Button_Identifier extends AppCompatActivity
        implements View.OnClickListener, LocationListener {

    String mprovider;
    Double longitude, latitude;
    LocationManager locationManager;

    final long LOCATION_REFRESH_TIME = 1;     // 1 minute
    final long LOCATION_REFRESH_DISTANCE = 1; // 10 meters
    private static final String[] PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE
    };
    private static final int REQUEST_ID = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(PrefManager.getBoolean("scheme", false)) {
            setTheme(R.style.AltTheme);
        }
        setContentView(R.layout.bigredbutton);
        requestPermissions(PERMS, REQUEST_ID);

        BottomNavigationView bottomNav = findViewById(R.id.nav_view1);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getBaseContext(), "Permissions are not right", Toast.LENGTH_SHORT).show();
//            Intent intentSettings = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
//            startActivity(intentSettings);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE,
                this);

        ImageButton button = (ImageButton) findViewById(R.id.bigredbutton);
        button.setOnClickListener(this);

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSEnabled) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Location Services are not enabled")
                    .setMessage("The location services on your phone are not enabled.  Please turn on GPS.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    }).show();
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_brb:
                    startActivity(new Intent(Big_Red_Button_Identifier.this, Big_Red_Button_Identifier.class));
                    break;
                case R.id.navigation_map:
                    startActivity(new Intent(Big_Red_Button_Identifier.this, MapsActivity.class));
                    break;
                case R.id.navigation_home:
                    startActivity(new Intent(Big_Red_Button_Identifier.this, MainActivity.class));
                    break;
            }
            return true;
        }
    };

    public void onClick(View view) {
        Location location = null;
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMS, REQUEST_ID);
                }
                return;
            } else {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                Location defaultLocation = new Location("");
                defaultLocation.setLatitude(42.788002);
                defaultLocation.setLongitude(-86.105971);
                List<String> provs = locationManager.getAllProviders();
                for(String prov : provs) {
                    if (locationManager.getLastKnownLocation(prov) != null) {
                        if (!prov.equals("gps")) {
                            defaultLocation = locationManager.getLastKnownLocation(prov);
                        } else {
                            location = locationManager.getLastKnownLocation(prov);
                        }
                        if (location != null) {
                            break;
                        }
                    }
                }
                if(location==null){
                    location = defaultLocation;
                }
            }

            onLocationChanged(location);
            Bundle bundle = new Bundle();
            bundle.putDouble("longitude", longitude);
            bundle.putDouble("latitude", latitude);
            Intent deviceIntent = new Intent();
            deviceIntent.putExtras(bundle);
            setResult(RESULT_OK, deviceIntent);
            finish();

        } else {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Location Services are not enabled")
                    .setMessage("The location services on your phone are not enabled.  Please turn on GPS.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    }).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void onRequestPermissionResult(int requestCode, String permissions, int[] grantResults) {
        switch(requestCode) {
            case 6: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(Big_Red_Button_Identifier.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
