package com.example.treesapv2new;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
//import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.example.treesapv2new.control.PrefManager;
import com.example.treesapv2new.control.Transform;
import com.example.treesapv2new.datasource.CityOfHollandDataSource;
import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.ExtendedCoHDataSource;
import com.example.treesapv2new.datasource.HopeCollegeDataSource;
import com.example.treesapv2new.datasource.ITreeDataSource;
import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.model.TreeLocation;
import com.example.treesapv2new.view.MapsActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Maps_Activity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GestureDetectorCompat gestureObject;


    private GoogleMap mMap;
    private static final String[] PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE
    };
    private static final int REQUEST_ID = 6;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Context parent;
    float personalMarker = BitmapDescriptorFactory.HUE_VIOLET;
    float treeMarker = BitmapDescriptorFactory.HUE_GREEN;
//    float zoom = 18-(2*PrefManager.getFloat("zoom", (float)1));
    float zoom = 16;
    boolean whichSource = false;
    Double longitude, latitude;


    private Location location;
    private TextView locationTv;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private LocationListener locationListener;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 seconds
    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.activity_map_new);

//        ViewPager pageRight = (ViewPager) findViewById(R.id.pageRight);
//        pageRight.setOnTouchListener(new pageRight.);

        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        googleApiClient.connect();




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMS, REQUEST_ID);
        }

        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        ImageButton addTreeButton = (ImageButton) findViewById(R.id.add_tree_button_2);
        addTreeButton.setOnClickListener(new Maps_Activity.AddTreeEvent());

        ImageButton settingsButton = (ImageButton) findViewById(R.id.setting_button_2);
        settingsButton.setOnClickListener(new Maps_Activity.AddSettingsEvent());

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_brb:
                        Intent intent1 = new Intent(Maps_Activity.this, Big_Red_Button.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_home:
                        Intent intent2 = new Intent(Maps_Activity.this, MainActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_coordinates:
                        Intent intent3 = new Intent(Maps_Activity.this, Coordinates_View_Activity.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_map:
                        Intent intent4 = new Intent(Maps_Activity.this, Maps_Activity.class);
                        startActivity(intent4);
                        break;
                    case R.id.navigation_qr:
                        Intent intent5 = new Intent(Maps_Activity.this, QR_Code_Activity.class);
                        startActivity(intent5);
                        break;
                }
                return false;
            }
        };

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.nav_view);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL).tiltGesturesEnabled(false);
        MapFragment.newInstance(options);
        mapFragment.getMapAsync(this);
        parent = (Context) getIntent().getSerializableExtra("parent");


    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Permissions ok, we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

//        if (location != null) {
//            locationTv.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
//        }

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

//    @Override
//    public void onLocationChanged(Location location) {
//        if (location != null) {
//            locationTv.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
//        }
//    }

    
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
                Intent intent1 = new Intent(Maps_Activity.this, Coordinates_View_Activity.class);
                finish();
                startActivity(intent1);
            }else if(event2.getX()<event1.getX()){
                //right to left swipe
                Intent intent1 = new Intent(Maps_Activity.this, QR_Code_Activity.class);
                finish();
                startActivity(intent1);
            }
            return true;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean locMarker = prefs.getBoolean("locationMarkerSwitch",true);

        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Bundle bundle = new Bundle();
                if(!marker.getTitle().equals("My Position")) {
//                    bundle.putDouble("longitude", marker.getPosition().longitude);
//                    bundle.putDouble("latitude", marker.getPosition().latitude);
//                    Intent mapsIntent = new Intent();
//                    mapsIntent.putExtras(bundle);
//                    setResult(RESULT_OK, mapsIntent);
//                    finish();
                    latitude = marker.getPosition().latitude;
                    longitude= marker.getPosition().longitude;

                    TreeLocation testing = new TreeLocation(latitude,longitude);

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    Set<String> sources = prefs.getStringSet("databasesUsedSelector",new HashSet<String>());

                    for (String source : sources) {
                        Log.d("MainActivity", "Searching.  Trying: " + source);
                        DataSource ds;
                        if (source.equals("HopeCollegeDataSource")) {
                            ds = new HopeCollegeDataSource();
                        } else if (source.equals("CityOfHollandDataSource")) {
                            ds = new CityOfHollandDataSource();
                        } else if(source.equals("ExtendedCoHDataSource")){
                            ds = new ExtendedCoHDataSource();
                        } else{
                            ds = new ITreeDataSource();
                        }

                        ds.initialize(Maps_Activity.this,null);
                        MainActivity.banana = ds.search(testing);
                        if (MainActivity.banana != null) {
                            if (MainActivity.banana.isFound()) break;  // and NOT just the closest
                        }
                    }


//                    HopeCollegeDataSource ds = new HopeCollegeDataSource();
//                    ds.initialize(Maps_Activity.this,null);
//                    MainActivity.banana = ds.search(testing);

                    Intent intentA = new Intent(Maps_Activity.this, Cereal_Box_Activity.class);
//            intentA.putExtra("treeClass", MainActivity.banana);
                    startActivity(intentA);

                } else {
                    android.support.v7.app.AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new android.support.v7.app.AlertDialog.Builder(Maps_Activity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                    } else {
                        builder = new android.support.v7.app.AlertDialog.Builder(Maps_Activity.this);
                    }
                    builder.setTitle("No Tree Identified")
                            .setMessage("You must select a tree to receive the information!")
                            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });


//        if(MainActivity.getSelectedDataSources().isEmpty()) {
//            android.support.v7.app.AlertDialog.Builder builder;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                builder = new android.support.v7.app.AlertDialog.Builder(Maps_Activity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
//            } else {
//                builder = new android.support.v7.app.AlertDialog.Builder(Maps_Activity.this);
//            }
//            builder.setTitle("No Data Sources")
//                    .setMessage("You must select a data source to view trees near you!")
//                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            startActivity(new Intent(Maps_Activity.this, MainActivity.class));
//                        }
//                    })
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();
//        }

       // String location42 = "";

        Set<String> sources = prefs.getStringSet("databasesUsedSelector",new HashSet<String>());
        for (String source : sources) {

            DataSource ds;
            if(source.equals("HopeCollegeDataSource")){
                ds = new HopeCollegeDataSource();
            }else if(source.equals("CityOfHollandDataSource")){
                ds = new CityOfHollandDataSource();
            }else if(source.equals("ExtendedCoHDataSource")){
                ds = new ExtendedCoHDataSource();
            }else{
                ds = new ITreeDataSource();
            }
//        HopeCollegeDataSource ds = new HopeCollegeDataSource();
//        ds.initialize(Maps_Activity.this, null);
//        Iterable<CSVRecord> stuff = null;
//        int treeField = 0;

//        stuff = ds.getCoordinates(Maps_Activity.this, "/data/user/0/com.example.treesapv2new/files/HCTreeData.csv");
//        treeField = 2;
//        location = "Hope College Pine Grove";
//        whichSource = true;

            ds.initialize(Maps_Activity.this, null);
            Iterable<CSVRecord> stuff = null;
            int treeField = 0;
            String location42 = "";

            if (ds instanceof CityOfHollandDataSource) {
                stuff = ds.getCoordinates(Maps_Activity.this, "/data/user/0/com.example.treesapv2new/files/COHTreeData.csv");
                treeField = 1;
                location42 = "blah blah blah";
                whichSource = false;
            } else if (ds instanceof HopeCollegeDataSource) {
                stuff = ds.getCoordinates(Maps_Activity.this, "/data/user/0/com.example.treesapv2new/files/HCTreeData.csv");
                treeField = 2;
                location42 = "Hope College Pine Grove";
                whichSource = true;
            } else if (ds instanceof ITreeDataSource) {
                stuff = ds.getCoordinates(Maps_Activity.this, "/data/user/0/com.example.treesapv2new/files/iTreeTreeData.csv");
                treeField = 2;
                location42 = "Hope College Pine Grove";
                whichSource = true;
            } else if (ds instanceof ExtendedCoHDataSource){
                stuff = ds.getCoordinates(Maps_Activity.this, "/data/user/0/com.example.treesapv2new/files/ECOHdata.csv");
                treeField = 1;
                location42= "Holland, MI";
                whichSource = false;
            }
            if (stuff == null) {
                continue;
            }
            int count =-1;
            for (CSVRecord record : stuff) {
                count++;
                if(count>2278){
                    break;
                }
                String latitude;
                String longitude;
                if(!whichSource) {
                    if(ds instanceof ExtendedCoHDataSource){
                        latitude = record.get("y_coord");
                        longitude = record.get("x_coord");
                    }else{
                        latitude = record.get("Latitude");
                        longitude = record.get("Longitude");
                    }
                }
                else{
                    latitude = record.get(Tree.LATITUDE);
                    longitude = record.get(Tree.LONGITUDE);
                }
                if (latitude != null && longitude != null) {
                    if (!latitude.equals("")) {
                        if (latitude.matches("^-?[0-9]\\d*(\\.\\d+)?$")) {

                            if (!longitude.equals("")) {
                                if (longitude.matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                                    LatLng coords = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                                    if (ds.getClass().equals(CityOfHollandDataSource.class)) {
                                        location42 = record.get("Park");
                                    }
                                    try {
                                        String name = Transform.ChangeName(record.get(treeField));
                                        mMap.addMarker(new MarkerOptions().position(coords).title(name).snippet(location42).icon(BitmapDescriptorFactory.defaultMarker(treeMarker)));
                                    }catch(ArrayIndexOutOfBoundsException e) {

                                    }

                                }
                            }
                        }
                    }
                }
            }
        }

        LatLng hope = new LatLng(42.788002, -86.105971);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMS, REQUEST_ID);
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hope, zoom));
            return;
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Criteria criteria = new Criteria();
            Location location1 = null;
            Location defaultLocation = new Location("");
            defaultLocation.setLatitude(42.788002);
            defaultLocation.setLongitude(-86.105971);
            List<String> provs = locationManager.getAllProviders();













            //LocationListener locationListenerGps = new LocationListener();
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, this);
            String a = LocationManager.GPS_PROVIDER;
            location1 = locationManager.getLastKnownLocation(a);
            //locationManager.removeUpdates(this);
            Location location2 = location;
            for(String prov : provs) {
                if (locationManager.getLastKnownLocation(prov) != null) {
                    if (!prov.equals("gps")) {
                        defaultLocation = locationManager.getLastKnownLocation(prov);
                    } else {
                        location1 = locationManager.getLastKnownLocation(prov);
                    }
                    if (location1 != null) {
                        break;
                    }
                }
            }
            if(location==null){
                location1 = defaultLocation;
            }
            onLocationChanged(location1);
            double latitude = location1.getLatitude();
            double longitude = location1.getLongitude();
            LatLng currentLocation = new LatLng(latitude, longitude);
            if (locMarker == true) {
                mMap.addMarker(new MarkerOptions().position(currentLocation)
                        .title("My Position").snippet("You are here.")
                        .icon(BitmapDescriptorFactory.defaultMarker(personalMarker)));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoom));
        }

    }

    @Override
    public void onLocationChanged(Location location)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean locMarker = prefs.getBoolean("locationMarkerSwitch",true);
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(locMarker == true) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(personalMarker));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
        }

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

    }
//    @Override
//    public void onStatusChanged(String s, int i, Bundle bundle) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String s) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String s) {
//
//    }

    private class AddTreeEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intentA = new Intent(Maps_Activity.this, Add_Tree_Activity.class);
            startActivity(intentA);
        }
    }

    private class AddSettingsEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intentA = new Intent(Maps_Activity.this, SettingsActivity.class);
            startActivity(intentA);
        }
    }

//    private class AddEventAction implements View.OnClickListener{
//        @Override
//        public void onClick(View v){
//
//
//            HopeCollegeDataSource ds = new HopeCollegeDataSource();
//            ds.initialize(Big_Red_Button.this,null);
//            MainActivity.banana = ds.search(testing);
//
//            Intent intentA = new Intent(Big_Red_Button.this, Cereal_Box_Activity.class);
////            intentA.putExtra("treeClass", MainActivity.banana);
//            startActivity(intentA);
//
////            startActivity(new Intent(Big_Red_Button.this, Cereal_Box_Activity.class));
//        }
//    }
}

