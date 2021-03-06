package com.example.treesapv2new;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Criteria;
import android.location.Location;
//import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treesapv2new.datasource.AllUsersDataSource;
import com.example.treesapv2new.datasource.UserTreeDataSource;
//import com.example.treesapv2new.ConnectionCheck;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.example.treesapv2new.control.Transform;
import com.example.treesapv2new.datasource.CityOfHollandDataSource;
import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.ExtendedCoHDataSource;
import com.example.treesapv2new.datasource.HopeCollegeDataSource;
import com.example.treesapv2new.datasource.ITreeDataSource;
import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.model.TreeLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

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

    Random randomGenerator;
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
//    private ArrayList<String> permissionsToRequest;
//    private ArrayList<String> permissionsRejected = new ArrayList<>();
//    private ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request
//    private static final int ALL_PERMISSIONS_RESULT = 1011;

    Bitmap.Config conf = Bitmap.Config.ARGB_8888;
    Bitmap bmp = Bitmap.createBitmap(80, 80, conf);
    Canvas canvas1 = new Canvas(bmp);

    Tree closestTree;
    float closest1;
    SupportMapFragment mapFragment;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public void onStart(){
        super.onStart();
        MainActivity.banana = null;
        closestTree = null;
        closest1 =999999999;


        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        googleApiClient.connect();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NORMAL).tiltGesturesEnabled(false);
        MapFragment.newInstance(options);
        mapFragment.getMapAsync(this);
        parent = (Context) getIntent().getSerializableExtra("parent");

        FirebaseUser user = mAuth.getCurrentUser();
        NavigationView hamMenu = findViewById(R.id.hamburger_menu);
        boolean isConnectedToFirebase = ConnectionCheck.isConnectedToFirebase(Maps_Activity.this);
        if(!isConnectedToFirebase && !ConnectionCheck.offlineMessageShown){
            ConnectionCheck.showOfflineMessage(Maps_Activity.this);
            ConnectionCheck.offlineMessageShown = true;
        }else if(isConnectedToFirebase && ConnectionCheck.offlineMessageShown || ConnectionCheck.offlineCuratorMessageShown || ConnectionCheck.offlineNotificationsMessageShown){
            ConnectionCheck.offlineMessageShown = false;
            ConnectionCheck.offlineCuratorMessageShown = false;
            ConnectionCheck.offlineNotificationsMessageShown = false;
        }
        if(user != null) {
            hamMenu.getMenu().findItem(R.id.nav_login).setVisible(false);
            hamMenu.getMenu().findItem(R.id.nav_notifications).setVisible(true);
            hamMenu.getMenu().findItem(R.id.nav_logout).setVisible(true);



            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Task<QuerySnapshot> query = db.collection("notifications").whereEqualTo(FieldPath.of("treeData", "userID"), user.getUid()).get();
                    query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    int notificationCount = 0;
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            if(!(Boolean)documentSnapshot.get("read")){
                                notificationCount++;
                                findViewById(R.id.unread_notification).setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    if(notificationCount == 0){
                        findViewById(R.id.unread_notification).setVisibility(View.GONE);
                        hamMenu.getMenu().findItem(R.id.nav_notifications).setTitle("Notifications");
                    }else{
                        hamMenu.getMenu().findItem(R.id.nav_notifications).setTitle("Notifications (" + Integer.valueOf(notificationCount) + ")");
                    }
                }
            });

            db.collection("curators").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot doc : task.getResult()){
//                        String userDI = user.getUid();
//                        String docID = doc.getId();
                        if (user.getUid().equals(doc.getId())) {
                            hamMenu.getMenu().findItem(R.id.nav_curator_group).setVisible(true);
                            break;
                        }
                    }
                }
            });

        }else{
            hamMenu.getMenu().findItem(R.id.nav_login).setVisible(true);
            hamMenu.getMenu().findItem(R.id.nav_notifications).setVisible(false);
            hamMenu.getMenu().findItem(R.id.nav_curator_group).setVisible(false);
            hamMenu.getMenu().findItem(R.id.nav_logout).setVisible(false);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);

        MainActivity.banana=null;
        setContentView(R.layout.map_drawer);
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("notifications").whereEqualTo(FieldPath.of("treeData", "userID"), user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    int notificationCount = 0;
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            if (!(Boolean) documentSnapshot.get("read")) {
                                notificationCount++;
                                findViewById(R.id.unread_notification).setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    if (notificationCount == 0) {
                        findViewById(R.id.unread_notification).setVisibility(View.GONE);
                    }
                }
            });
        }
        MainActivity.treesNearby.clear();

        randomGenerator = new Random();



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
                        mMap.clear();
                        Intent intent1 = new Intent(Maps_Activity.this, Big_Red_Button.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_home:
                        mMap.clear();
                        Intent intent2 = new Intent(Maps_Activity.this, MainActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_coordinates:
                        mMap.clear();
                        Intent intent3 = new Intent(Maps_Activity.this, Coordinates_View_Activity.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_map:
//                        Intent intent4 = new Intent(Maps_Activity.this, Maps_Activity.class);
//                        startActivity(intent4);
                        break;
                    case R.id.navigation_qr:
                        mMap.clear();
                        Intent intent5 = new Intent(Maps_Activity.this, QR_Code_Activity.class);
                        startActivity(intent5);
                        break;
                }
                return false;
            }
        };

        NavigationView hamburgerView = findViewById(R.id.hamburger_menu);
        hamburgerView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_login:
                        Intent intent1 = new Intent(Maps_Activity.this, Login_Activity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_settings:
                        Intent intent2 = new Intent(Maps_Activity.this, SettingsActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_curator:
                        Intent intent3 = new Intent(Maps_Activity.this, CuratorApproveActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_send:
                        Intent intent4 = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",getResources().getString(R.string.emailFeedback), null));
                        intent4.putExtra(Intent.EXTRA_SUBJECT, "App Suggestion");
                        startActivity(Intent.createChooser(intent4, "Send Email"));
                        break;
                    case R.id.nav_more_info:
                        Intent intent5 = new Intent(Maps_Activity.this, MoreInformation.class);
                        startActivity(intent5);
                        break;
                    case R.id.nav_notifications:
                        Intent intent6 = new Intent(Maps_Activity.this, NotificationsActivity.class);
                        startActivity(intent6);
                        break;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        Toast toast = Toast.makeText(Maps_Activity.this, "You have been logged out.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0,0);
                        toast.show();
                        NavigationView hamMenu = findViewById(R.id.hamburger_menu);
                        hamMenu.getMenu().findItem(R.id.nav_login).setVisible(true);
                        hamMenu.getMenu().findItem(R.id.nav_notifications).setVisible(false);
                        hamMenu.getMenu().findItem(R.id.nav_curator_group).setVisible(false);
                        hamMenu.getMenu().findItem(R.id.nav_logout).setVisible(false);
                        break;
                    case R.id.nav_add_curator:
                        Intent intent7 = new Intent(Maps_Activity.this, AddCurator.class);
                        startActivity(intent7);
                        break;

                }
                DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.maps_container);
                mDrawerLayout.closeDrawer(Gravity.LEFT, false);
                return false;
            }
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.nav_view);
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
        googleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
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
        mMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                String title = marker.getTitle();
                latitude = marker.getPosition().latitude;
                longitude = marker.getPosition().longitude;
//                TreeLocation testing = new TreeLocation(latitude, longitude);
                if(marker.getTitle().equals("Unknown")){
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    Set<String> sources = prefs.getStringSet("databasesUsedSelector", new HashSet<String>());
                    Tree closestTree = null;
                    for (String source : sources) {
                        DataSource ds;
                        if(source.equals("HopeCollegeDataSource")){
                            ds = new HopeCollegeDataSource();
                        }else if(source.equals("CityOfHollandDataSource")){
                            ds = new CityOfHollandDataSource();
                        }else if(source.equals("ExtendedCoHDataSource")){
                            ds = new ExtendedCoHDataSource();
                        }else if(source.equals("UserTreeDataSource")) {
                            ds = MainActivity.userTreeDataSourceGlobal;
                        }else if(source.equals("AllUsersDataSource")){
                                ds = new AllUsersDataSource();
                        } else{
                            ds = new ITreeDataSource();
                        }
                        ds.initialize(Maps_Activity.this, null);
                        MainActivity.treesNearby.clear();
                        Tree newTree = ds.search(new TreeLocation(marker.getPosition().latitude, marker.getPosition().longitude));
                        if(newTree != null && !newTree.getCommonName().equals("Unknown")){
                            marker.setTitle(newTree.getCommonName());
                            if(marker.getSnippet().equals("Holland, MI") && newTree.getInfo("Park") != null){
                                marker.setSnippet(newTree.getInfo("Park").toString());
                            }
                            break;
                        }
                    }
                }
                if(marker.getSnippet().equals("Holland, MI")){
                    DataSource ds = new CityOfHollandDataSource();
                    ds.initialize(Maps_Activity.this, null);
                    MainActivity.treesNearby.clear();
                    Tree newTree = ds.search(new TreeLocation(marker.getPosition().latitude, marker.getPosition().longitude));
                    if(newTree.getInfo("Park") != null){
                        marker.setSnippet(newTree.getInfo("Park").toString());
                    }
                }
                marker.showInfoWindow();
                return true;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Bundle bundle = new Bundle();
                if(!marker.getTitle().equals("My Position")) {
                    if(!marker.getTitle().equals("Current Position")) {
                        latitude = marker.getPosition().latitude;
                        longitude = marker.getPosition().longitude;

                        TreeLocation testing = new TreeLocation(latitude, longitude);

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        Set<String> sources = prefs.getStringSet("databasesUsedSelector", new HashSet<String>());
                        for (String source : sources) {
                            Log.d("MainActivity", "Searching.  Trying: " + source);
                            DataSource ds;
                            if (source.equals("HopeCollegeDataSource")) {
                                ds = new HopeCollegeDataSource();
                            } else if (source.equals("CityOfHollandDataSource")) {
                                ds = new CityOfHollandDataSource();
                            } else if (source.equals("ExtendedCoHDataSource")) {
                                ds = new ExtendedCoHDataSource();
                            } else if (source.equals("UserTreeDataSource")) {
                                ds = MainActivity.userTreeDataSourceGlobal;
                            } else if (source.equals("AllUsersDataSource")) {
                                ds = new AllUsersDataSource();
                            } else {
                                ds = new ITreeDataSource();
                            }
                            if (ds instanceof AllUsersDataSource) {
                                MainActivity.banana = MainActivity.allUsersDataSource.search(testing);
                            }else if(ds instanceof UserTreeDataSource){
                                MainActivity.userTreeDataSourceGlobal.setUserTrees(MainActivity.allUsersDataSource.getUserTrees());
                                MainActivity.banana = MainActivity.userTreeDataSourceGlobal.search(testing);
                            }else{
                                ds.initialize(Maps_Activity.this, null);
                                MainActivity.treesNearby.clear();
                                MainActivity.banana = ds.search(testing);
                            }
                            if (MainActivity.banana != null) {
                                if(MainActivity.banana.getClosestDist() < closest1) {
                                    closest1 = MainActivity.banana.getClosestDist();
                                    closestTree = MainActivity.banana;
                                }
                            }
                        }

                        if(closestTree != null) {
                            MainActivity.banana = closestTree;
                        }
                        Intent intentA = new Intent(Maps_Activity.this, Tree_Info_First.class);
                        mMap.clear();
                        startActivity(intentA);
                    }

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

        Set<String> sources = prefs.getStringSet("databasesUsedSelector",new HashSet<String>());
        Iterator<String> it = sources.iterator();
        for (String source : sources) {

            DataSource ds;
            if(source.equals("HopeCollegeDataSource")){
                ds = new HopeCollegeDataSource();
            }else if(source.equals("CityOfHollandDataSource")){
                ds = new CityOfHollandDataSource();
            }else if(source.equals("ExtendedCoHDataSource")){
                ds = new ExtendedCoHDataSource();
            }else if(source.equals("UserTreeDataSource")){
                ds = MainActivity.userTreeDataSourceGlobal;
            }else if(source.equals("AllUsersDataSource")){
                ds = new AllUsersDataSource();
            }else{
                ds = new ITreeDataSource();
            }

            ds.initialize(Maps_Activity.this, null);
            MainActivity.treesNearby.clear();
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
            } else if (ds instanceof UserTreeDataSource){
                stuff = null;
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                db.collection("acceptedTrees").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayList<String> pic = (ArrayList<String>) document.getData().get("pictures");
                                String a = (String) document.getData().get("userID").toString();
                                String b = "";
                                try{
                                     b = user.getUid();
                                }catch (NullPointerException e){

                                }
                                if(a.equals(b)) {
                                    try {
                                        Double latitude = Double.valueOf(document.getData().get("latitude").toString());
                                        Double longitude = Double.valueOf(document.getData().get("longitude").toString());
                                        if (latitude != null && longitude != null) {
                                            LatLng coords = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                                            if (ds.getClass().equals(CityOfHollandDataSource.class)) {
                                            }
                                            try {
                                                String name = document.getData().get("commonName").toString();
                                                int iconInt = randomGenerator.nextInt(8);
                                                BitmapDescriptor icon;
                                                String user = (String) document.getData().get("userID");
                                                if(mAuth.getCurrentUser().getUid().equals(user)){
                                                    icon = BitmapDescriptorFactory.fromResource(R.drawable.user_tree_marker1);
                                                }else {
                                                    switch (iconInt) {
                                                        // Hey whoever works on this app next I would recommend making the icons better.
                                                        // I couldn't see a way to set both the color and picture of the marker programmatically,
                                                        // so I just made them myself in Paint3d and had the computer "randomly" choose one.
                                                        // If you guys know how to make them better or some way of programmatically setting them that would be dope - Josie
                                                        case 0:
                                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker0);
                                                            break;
                                                        case 1:
                                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker_1);
                                                            break;
                                                        case 2:
                                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker2);
                                                            break;
                                                        case 3:
                                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker3);
                                                            break;
                                                        case 4:
                                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker4);
                                                            break;
                                                        case 5:
                                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker5);
                                                            break;
                                                        case 6:
                                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker6);
                                                            break;
                                                        case 7:
                                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker7);
                                                            break;
                                                        default:
                                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker0);
                                                            break;
                                                    }
                                                }
                                                mMap.addMarker(new MarkerOptions().position(coords).title(name).snippet("Your tree").icon(icon));//BitmapDescriptorFactory.fromBitmap(bmp)));
                                            } catch (ArrayIndexOutOfBoundsException e) {

                                            }
                                        }
                                    } catch (Exception e) {
                                        continue;
                                    }
                                }
                            }
                        } else {
                            Toast toast = Toast.makeText(parent, "Unable to load trees.", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });

            } else if (ds instanceof AllUsersDataSource){
                stuff = null;
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference users = db.collection("pendingTrees");
                db.collection("acceptedTrees").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayList<String> pic = (ArrayList<String>) document.getData().get("pictures");
                                try {
                                    Double latitude = Double.valueOf(document.getData().get("latitude").toString());
                                    Double longitude = Double.valueOf(document.getData().get("longitude").toString());
                                    if (latitude != null && longitude != null) {
                                        LatLng coords = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                                        try {
                                            String user = (String) document.getData().get("userID");
                                            if(!mAuth.getCurrentUser().getUid().equals(user)) {
                                                String name = document.getData().get("commonName").toString();

                                                BitmapDescriptor icon;
                                                String snippet = "";
                                                int iconInt = randomGenerator.nextInt(8);
                                                switch (iconInt) {
                                                    case 0:
                                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker0);
                                                        break;
                                                    case 1:
                                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker_1);
                                                        break;
                                                    case 2:
                                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker2);
                                                        break;
                                                    case 3:
                                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker3);
                                                        break;
                                                    case 4:
                                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker4);
                                                        break;
                                                    case 5:
                                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker5);
                                                        break;
                                                    case 6:
                                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker6);
                                                        break;
                                                    case 7:
                                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker7);
                                                        break;
                                                    default:
                                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker0);
                                                        break;
                                                }

                                                if (snippet.equals("")) {
                                                    snippet = "User submitted";
                                                }
                                                mMap.addMarker(new MarkerOptions().position(coords).title(name).snippet(snippet).icon(icon));//BitmapDescriptorFactory.fromBitmap(bmp)));

                                            }
                                        }catch (ArrayIndexOutOfBoundsException e) {

                                        }
                                    }
                                } catch (Exception e) {
                                    continue;
                                }
                            }
                        } else {
                            Toast toast = Toast.makeText(parent, "Unable to load trees.", Toast.LENGTH_LONG);
                        }
                    }
                });
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
                    if (latitude.matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                        if (longitude.matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
                            LatLng coords = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                            if (ds.getClass().equals(CityOfHollandDataSource.class)) {
                                location42 = record.get("Park");
                            }
                            try {
                                String name = Transform.ChangeName(record.get(treeField));
                                if(name == ""){
                                    name = "N/A";
                                }
                                int iconInt = randomGenerator.nextInt(8);
                                BitmapDescriptor icon;
                                switch (iconInt){
                                    case 0:
                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker0);
                                        break;
                                    case 1:
                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker_1);
                                        break;
                                    case 2:
                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker2);
                                        break;
                                    case 3:
                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker3);
                                        break;
                                    case 4:
                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker4);
                                        break;
                                    case 5:
                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker5);
                                        break;
                                    case 6:
                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker6);
                                        break;
                                    case 7:
                                        icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker7);
                                        break;
                                        default:
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker0);
                                            break;
                                }
                                mMap.addMarker(new MarkerOptions().position(coords).title(name).snippet(location42).icon(icon));//BitmapDescriptorFactory.fromBitmap(bmp)));
                            }catch(ArrayIndexOutOfBoundsException e) {

                            }

                        }
                    }
                }
            }
            it.next();
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

            String a = LocationManager.GPS_PROVIDER;
            location1 = locationManager.getLastKnownLocation(a);
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
            double latitude,longitude;
            if(location1 == null){
                onLocationChanged(location2);
                latitude = location2.getLatitude();
                longitude = location2.getLongitude();
            }else {
                onLocationChanged(location1);
                latitude = location1.getLatitude();
                longitude = location1.getLongitude();
            }

            LatLng currentLocation = new LatLng(latitude, longitude);
            if (locMarker == true) {
                mCurrLocationMarker = mMap.addMarker(new MarkerOptions().position(currentLocation)
                        .title("Current Position").snippet("This is you!")
                        .icon(defaultMarker(personalMarker)));
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
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(locMarker == true) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.snippet("This is you!");
            markerOptions.icon(defaultMarker(personalMarker));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
        }

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

    }

    private class AddTreeEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mMap.clear();
            Intent intentA = new Intent(Maps_Activity.this, Add_Tree_Activity.class);
            startActivity(intentA);
        }
    }

    private class AddSettingsEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            mMap.clear();
            DrawerLayout mDrawerLayout = findViewById(R.id.maps_container);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            getSupportFragmentManager();
        }
    }
}

