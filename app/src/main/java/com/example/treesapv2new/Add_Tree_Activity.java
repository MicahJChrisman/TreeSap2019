package com.example.treesapv2new;

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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Nonnull;

public class Add_Tree_Activity extends AppCompatActivity implements LocationListener {
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
    private int REQUEST_EXIT = 9000;
    private int RESULT_DONE = 4000;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        //Authorization Stuff
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            //user is signed in
            addTreesMethod();
        }else{
            //user is signed out
            startActivityForResult(new Intent(getBaseContext(), Login_Activity.class), 1); //Result is received in onActivityResult() below
        }
    }

    public void onStop(){
        super.onStop();
        setResult(RESULT_DONE,null);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_EXIT && resultCode == RESULT_DONE) {
            this.finish();
        }
        //Authorization Stuff
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            //user is signed in
            addTreesMethod();
        }else{
            //user is signed out
            finish();
        }
    }

    public void addTreesMethod(){
        setContentView(R.layout.add_tree_page);

        Button b = (Button) findViewById(R.id.next_add_tree);
        b.setOnClickListener(new NextEvent());

        Button getGPSbutton = (Button) findViewById(R.id.get_location_button);
        getGPSbutton.setOnClickListener(new getGpsEvent());

        TextView manualCoordinates = (TextView) findViewById(R.id.manual_coordinates_text);
        manualCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // User has decided they want to manually enter coordinates
                ((LinearLayout) findViewById(R.id.lat_layout)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.long_layout)).setVisibility(View.VISIBLE);
                findViewById(R.id.man_coords_layout).setVisibility(View.GONE);
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // User has not previously agreed to let the app access their location
            Toast.makeText(getBaseContext(), "Permissions are not right", Toast.LENGTH_SHORT).show();
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, this);

        ImageView txtclose = (ImageView) findViewById(R.id.add_tree_close);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Add_Tree_Activity.this);
                builder.setCancelable(true);
                builder.setTitle("Discard your tree?");
                builder.setMessage("This will get rid of the data you entered.");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Discard Tree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.show();
            }
        });

        boolean isConnectedToFirebase;
            isConnectedToFirebase = ConnectionCheck.isConnectedToFirebase(Add_Tree_Activity.this);
        if(!isConnectedToFirebase){
            ConnectionCheck.showOfflineAddTreeMessage1(Add_Tree_Activity.this);
        }else if(isConnectedToFirebase && ConnectionCheck.offlineMessageShown || ConnectionCheck.offlineNotificationsMessageShown){
            ConnectionCheck.offlineMessageShown = false;
            ConnectionCheck.offlineCuratorMessageShown = false;
            ConnectionCheck.offlineNotificationsMessageShown = false;
        }
    }

    public void getLocation(){
        Location location = null;
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMS, REQUEST_ID);
                }
                return;
            } else {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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

    private class getGpsEvent implements View.OnClickListener{
        public void onClick(View v){
            getLocation();
            TextView latPut = (TextView) findViewById(R.id.lat_putter);
            latPut.setText(Double.toString(latitude));
            TextView longPut = (TextView) findViewById(R.id.long_putter);
            longPut.setText(Double.toString(longitude));
            findViewById(R.id.lat_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.long_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.next_add_tree).setVisibility(View.VISIBLE);
            findViewById(R.id.man_coords_layout).setVisibility(View.GONE);
        }
    }


    private class NextEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intentA = new Intent(Add_Tree_Activity.this, Tree_Bark_Activity.class);
            TextView latString = (TextView) findViewById(R.id.lat_putter);
            TextView longString = (TextView) findViewById(R.id.long_putter);
            intentA.putExtra("lat_value", latString.getText().toString());
            intentA.putExtra("long_value", longString.getText().toString());
            startActivityForResult(intentA, REQUEST_EXIT);
//            startActivity(intentA);
        }
    }

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
}
