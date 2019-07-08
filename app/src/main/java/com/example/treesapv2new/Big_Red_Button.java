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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
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
import com.example.treesapv2new.datasource.AllUsersDataSource;
import com.example.treesapv2new.datasource.CityOfHollandDataSource;
import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.ExtendedCoHDataSource;
import com.example.treesapv2new.datasource.HopeCollegeDataSource;
import com.example.treesapv2new.datasource.ITreeDataSource;
import com.example.treesapv2new.datasource.UserTreeDataSource;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

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
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        NavigationView hamMenu = findViewById(R.id.hamburger_menu);
        if(user != null) {
            hamMenu.getMenu().findItem(R.id.nav_login).setVisible(false);
            hamMenu.getMenu().findItem(R.id.nav_notifications).setVisible(true);
            hamMenu.getMenu().findItem(R.id.nav_logout).setVisible(true);
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("notifications").whereEqualTo(FieldPath.of("treeData", "userID"), user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                    }
                }
            });

            db.collection("curators").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        String userDI = user.getUid();
                        String docID = doc.getId();
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

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.big_red_button_drawer);
        gestureObject = new GestureDetectorCompat(this, new LearnGesture());
        MainActivity.treesNearby.clear();
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
//                        Intent intent1 = new Intent(Big_Red_Button.this, Big_Red_Button.class);
//                        startActivity(intent1);
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

        NavigationView hamburgerView = findViewById(R.id.hamburger_menu);
        hamburgerView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_login:
                        Intent intent1 = new Intent(Big_Red_Button.this, Login_Activity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_settings:
                        Intent intent2 = new Intent(Big_Red_Button.this, SettingsActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_curator:
                        Intent intent3 = new Intent(Big_Red_Button.this, CuratorApproveActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_send:
                        Intent intent4 = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",getResources().getString(R.string.emailFeedback), null));
                        intent4.putExtra(Intent.EXTRA_SUBJECT, "App Suggestion");
                        startActivity(Intent.createChooser(intent4, "Send Email"));
                        break;
                    case R.id.nav_more_info:
                        Intent intent5 = new Intent(Big_Red_Button.this, MoreInformation.class);
                        startActivity(intent5);
                        break;
                    case R.id.nav_notifications:
                        Intent intent6 = new Intent(Big_Red_Button.this, NotificationsActivity.class);
                        startActivity(intent6);
                        break;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        Toast toast = Toast.makeText(Big_Red_Button.this, "You have been logged out.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0,0);
                        toast.show();
                        NavigationView hamMenu = findViewById(R.id.hamburger_menu);
                        hamMenu.getMenu().findItem(R.id.nav_login).setVisible(true);
                        hamMenu.getMenu().findItem(R.id.nav_notifications).setVisible(false);
                        hamMenu.getMenu().findItem(R.id.nav_curator_group).setVisible(false);
                        hamMenu.getMenu().findItem(R.id.nav_logout).setVisible(false);
                    case R.id.nav_add_curator:
                        Intent intent7 = new Intent(Big_Red_Button.this, AddCurator.class);
                        startActivity(intent7);

                }
                DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.brb_container);
                mDrawerLayout.closeDrawer(Gravity.LEFT, false);
                return false;
            }
        });

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
//            Intent intentA = new Intent(Big_Red_Button.this, SettingsActivity.class);
//            startActivity(intentA);
            DrawerLayout mDrawerLayout = findViewById(R.id.brb_container);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
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
            Tree closestTree = null;
            float closest1 =999999999;
            for (String source : sources) {
                Log.d("MainActivity", "Searching.  Trying: "+source);
                DataSource ds;
                if(source.equals("HopeCollegeDataSource")){
                    ds = new HopeCollegeDataSource();
                }else if(source.equals("CityOfHollandDataSource")) {
                    ds = new CityOfHollandDataSource();
                }else if(source.equals("ExtendedCoHDataSource")){
                    ds = new ExtendedCoHDataSource();
                }else if(source.equals("UserTreeDataSource")) {
                    ds = MainActivity.userTreeDataSourceGlobal;
                }else if(source.equals("AllUsersDataSource")){
                    ds = new AllUsersDataSource();
                }else{
                    ds = new ITreeDataSource();
                }

                if(ds instanceof AllUsersDataSource){
                    MainActivity.banana = MainActivity.allUsersDataSource.search(testing);
                }else if(ds instanceof UserTreeDataSource){
                    MainActivity.userTreeDataSourceGlobal.initialize(Big_Red_Button.this, null);
                    MainActivity.userTreeDataSourceGlobal.setUserTrees(MainActivity.allUsersDataSource.getUserTrees());
                    MainActivity.banana = MainActivity.userTreeDataSourceGlobal.search(testing);
                }else {
                    ds.initialize(Big_Red_Button.this, null);
                    MainActivity.banana = ds.search(testing);
                }


                if (MainActivity.banana != null) {
                    if(MainActivity.banana.getClosestDist() < closest1) {
                        closest1 = MainActivity.banana.getClosestDist();
                        closestTree = MainActivity.banana;
                    }
                }
            }
//            checkTree(sources);

//            HopeCollegeDataSource ds = new HopeCollegeDataSource();
//            ds.initialize(Big_Red_Button.this,null);
//            MainActivity.banana = ds.search(testing);
            if(closestTree != null) {
                MainActivity.banana = closestTree;
            }
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
