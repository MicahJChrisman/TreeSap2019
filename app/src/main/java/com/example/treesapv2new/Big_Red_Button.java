package com.example.treesapv2new;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treesapv2new.control.IntentIntegrator;
import com.example.treesapv2new.control.IntentResult;
import com.example.treesapv2new.control.PrefManager;
import com.example.treesapv2new.datasource.CityOfHollandDataSource;
import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.ExtendedCoHDataSource;
import com.example.treesapv2new.datasource.HopeCollegeDataSource;
import com.example.treesapv2new.datasource.ITreeDataSource;
import com.example.treesapv2new.display.CerealBoxDisplay;
import com.example.treesapv2new.display.DisplayMethod;
import com.example.treesapv2new.identify.Barcode_Identifier;
import com.example.treesapv2new.identify.GPS_Identifier;
import com.example.treesapv2new.identify.Google_Map_Identifier;
import com.example.treesapv2new.identify.IdentificationMethod;
import com.example.treesapv2new.identify.IdentificationMethodList;
import com.example.treesapv2new.identify.SimpleIdentifier;
import com.example.treesapv2new.model.BoxItem;
import com.example.treesapv2new.model.BulletedListItem;
import com.example.treesapv2new.model.GPSCoordinates;
import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.model.TreeLocation;
import com.example.treesapv2new.view.BulletedWebView;
import com.example.treesapv2new.view.MapsActivity;
import com.example.treesapv2new.view.UserViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Big_Red_Button extends AppCompatActivity implements LocationListener {

    private GestureDetectorCompat gestureObject;

//    public static Tree banana = null;
    public static String sendString = "Big_Red_Button.banana";

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
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.big_red_button_page);
        gestureObject = new GestureDetectorCompat(this, new LearnGesture());



        Button button = (Button) findViewById(R.id.biggreenbutton);
        button.setOnClickListener(new AddEventAction());

        ImageButton addTreeButton = (ImageButton) findViewById(R.id.add_tree_button_1);
        addTreeButton.setOnClickListener(new AddTreeEvent());

        ImageButton settingsButton = (ImageButton) findViewById(R.id.setting_button_1);
        settingsButton.setOnClickListener(new AddSettingsEvent());

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_brb:
                        Intent intent1 = new Intent(Big_Red_Button.this, Big_Red_Button.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_home:
                        Intent intent2 = new Intent(Big_Red_Button.this, MainActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_coordinates:
                        Intent intent3 = new Intent(Big_Red_Button.this, Coordinates_View_Activity.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_map:
                        Intent intent4 = new Intent(Big_Red_Button.this, Maps_Activity.class);
                        startActivity(intent4);
                        break;
                    case R.id.navigation_qr:
                        Intent intent5 = new Intent(Big_Red_Button.this, QR_Code_Activity.class);
                        startActivity(intent5);
                        break;
                }
                return false;
            }
        };

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.nav_view);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getBaseContext(), "Permissions are not right", Toast.LENGTH_SHORT).show();
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    public void getLocation(){
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

    private class AddTreeEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intentA = new Intent(Big_Red_Button.this, Add_Tree_Activity.class);
            startActivity(intentA);
        }
    }

    private class AddSettingsEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intentA = new Intent(Big_Red_Button.this, SettingsActivity.class);
            startActivity(intentA);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY){
            if(event2.getX()>event1.getX()){
                //left to right swipe
                Intent intent1 = new Intent(Big_Red_Button.this, MainActivity.class);
                finish();
                startActivity(intent1);
            }else if(event2.getX()<event1.getX()){
                //right to left swipe
                Intent intent2 = new Intent(Big_Red_Button.this, Coordinates_View_Activity.class);
                finish();
                startActivity(intent2);
            }
            return true;
        }
    }


    private class AddEventAction implements View.OnClickListener, View.OnTouchListener{
        @Override
        public void onClick(View v){
            getLocation();
            TreeLocation testing = new TreeLocation(latitude,longitude);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            Set<String> sources = prefs.getStringSet("databasesUsedSelector",new HashSet<String>());

            for (String source : sources) {
                Log.d("MainActivity", "Searching.  Trying: "+source);
                DataSource ds;
                if(source.equals("HopeCollegeDataSource")){
                    ds = new HopeCollegeDataSource();
                }else if(source.equals("CityOfHollandDataSource")) {
                    ds = new CityOfHollandDataSource();
                }else if(source.equals("ExtendedCoHDataSource")){
                    ds = new ExtendedCoHDataSource();
                }else if(source.equals("UserTreeDataSource")){
                    ds = MainActivity.userTreeDataSourceGlobal;
                }else{
                    ds = new ITreeDataSource();
                }
                ds.initialize(Big_Red_Button.this,null);
                MainActivity.banana = ds.search(testing);

                if (MainActivity.banana != null) {
                    if (MainActivity.banana.isFound()) break;  // and NOT just the closest
                }
            }
//            checkTree(sources);

//            HopeCollegeDataSource ds = new HopeCollegeDataSource();
//            ds.initialize(Big_Red_Button.this,null);
//            MainActivity.banana = ds.search(testing);

//            button.layout(67, 117, 67, 47);
            if(MainActivity.banana != null){
                Intent intentA = new Intent(Big_Red_Button.this, Tree_Info_First.class);
//            intentA.putExtra("treeClass", MainActivity.banana);
                startActivity(intentA);
            }else{
//                AlertDialog.Builder builder;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    builder = new AlertDialog.Builder(getBaseContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
//                } else {
//                    builder = new AlertDialog.Builder(getBaseContext(), android.R.style.Theme_Material_Light_Dialog_Alert);
//                }
//                builder.setTitle("No Tree was Found!")
//                        .setMessage("No tree was found at the GPS coordinates that you gave using the data sources that you specified.")
//                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
                Toast.makeText(getBaseContext(), "There are no trees near enough!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Button button = findViewById(R.id.biggreenbutton);
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                float x = (float) .75;
                float y = (float) .75;

                button.setScaleX(x);
                button.setScaleY(y);
            button.setBackground(Drawable.createFromPath("@drawable/smaller_ripple"));
            }
            else if(event.getAction() == MotionEvent.ACTION_UP)
            {
                float x = 1;
                float y = 1;
//
                button.setScaleX(x);
                button.setScaleY(y);
                button.setBackground(Drawable.createFromPath("@drawable/ripple_overlay"));

            }
            return false;
        }


//        private void checkTree(Set<String> sources){
//            if(MainActivity.banana != null){
//                if(MainActivity.banana.getCommonName().equals(null)){
//                    for (String source : sources) {
//                        for(String sourceUsed : MainActivity.banana.getDbsUsed()){
//                            Log.d("MainActivity", "Searching.  Trying: "+source);
//                            DataSource ds;
//                            if(source.equals(sourceUsed)) {
//                            }else if(source.equals("HopeCollegeDataSource")){
//                                ds = new HopeCollegeDataSource();
//                            }else if(source.equals("CityOfHollandDataSource")) {
//                                ds = new CityOfHollandDataSource();
//                            }else if(source.equals("ExtendedCoHDataSource"))){
//                                ds = new ExtendedCoHDataSource();
//                            }else{
//                                ds = new ITreeDataSource();
//                            }
//                            ds.initialize(Big_Red_Button.this,null);
//                            Tree tree = ds.search(MainActivity.banana.getLocation());
//                            MainActivity.banana.addDb(source);
//                            if(!tree.getCommonName().equals(null)){
//                                MainActivity.banana.setCommonName(tree.getCommonName());
//                            }
//                        }
//                    }
//                }if(MainActivity.banana.getScientificName().equals(null)){
//                    for (String source : sources){
//                        for (String sourceUsed : MainActivity.banana.getDbsUsed()) {
//                            Log.d("MainActivity", "Searching.  Trying: " + source);
//                            DataSource ds;
//                            if (source.equals(sourceUsed)) {
//                            } else if (source.equals("HopeCollegeDataSource")) {
//                                ds = new HopeCollegeDataSource();
//                            } else if (source.equals("CityOfHollandDataSource")) {
//                                ds = new CityOfHollandDataSource();
//                            } else if (source.equals("ExtendedCoHDataSource")) {
//                                ds = new ExtendedCoHDataSource();
//                            } else {
//                                ds = new ITreeDataSource();
//                            }
//                            ds.initialize(Big_Red_Button.this, null);
//                            Tree tree = ds.search(MainActivity.banana.getLocation());
//                            MainActivity.banana.addDb(source);
//                            if (!tree.getScientificName().equals(null)) {
//                                MainActivity.banana.setScientificName(tree.getScientificName());
//                            }
//                        }
//                    }
//                }if(MainActivity.banana.getID().equals(null)){
//                    for (String source : sources){
//                        for (String sourceUsed : MainActivity.banana.getDbsUsed()) {
//                            Log.d("MainActivity", "Searching.  Trying: " + source);
//                            DataSource ds;
//                            if (source.equals(sourceUsed)) {
//                            } else if (source.equals("HopeCollegeDataSource")) {
//                                ds = new HopeCollegeDataSource();
//                            } else if (source.equals("CityOfHollandDataSource")) {
//                                ds = new CityOfHollandDataSource();
//                            } else if (source.equals("ExtendedCoHDataSource")) {
//                                ds = new ExtendedCoHDataSource();
//                            } else {
//                                ds = new ITreeDataSource();
//                            }
//                            ds.initialize(Big_Red_Button.this, null);
//                            Tree tree = ds.search(MainActivity.banana.getLocation());
//                            MainActivity.banana.addDb(source);
//                            if (!tree.getID().equals(null)) {
//                                MainActivity.banana.setID(tree.getID());
//                            }
//                            if(!MainActivity.banana.getID().equals(null)){
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

//    private Tree treeToSend;
//    private Bundle bundleToSend;
//
//    @Override
//    public int describeContents(){
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel out, int flags){
//        out.writeBundle(banana);
//    }

}
