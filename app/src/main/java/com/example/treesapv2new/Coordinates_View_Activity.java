package com.example.treesapv2new;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treesapv2new.datasource.AllUsersDataSource;
import com.example.treesapv2new.datasource.CityOfHollandDataSource;
import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.ExtendedCoHDataSource;
import com.example.treesapv2new.datasource.HopeCollegeDataSource;
import com.example.treesapv2new.datasource.ITreeDataSource;
import com.example.treesapv2new.datasource.UserTreeDataSource;
import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.model.TreeLocation;
import com.google.android.gms.common.util.NumberUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Coordinates_View_Activity extends AppCompatActivity {
    private GestureDetectorCompat gestureObject;
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

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.acitivity_coordinates_drawer);
        MainActivity.treesNearby.clear();

        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMS, REQUEST_ID);
        }

        ImageButton addTreeButton = (ImageButton) findViewById(R.id.add_tree_button_0);
        addTreeButton.setOnClickListener(new Coordinates_View_Activity.AddTreeEvent());

        ImageButton settingsButton = (ImageButton) findViewById(R.id.setting_button);
        settingsButton.setOnClickListener(new AddSettingsEvent());

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_brb:
                        Intent intent1 = new Intent(Coordinates_View_Activity.this, Big_Red_Button.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_home:
                        Intent intent2 = new Intent(Coordinates_View_Activity.this, MainActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_coordinates:
//                        Intent intent3 = new Intent(Coordinates_View_Activity.this, Coordinates_View_Activity.class);
//                        startActivity(intent3);
                        break;
                    case R.id.navigation_map:
                        Intent intent4 = new Intent(Coordinates_View_Activity.this, Maps_Activity.class);
                        startActivity(intent4);
                        break;
                    case R.id.navigation_qr:
                        Intent intent5 = new Intent(Coordinates_View_Activity.this, QR_Code_Activity.class);
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
                        Intent intent1 = new Intent(Coordinates_View_Activity.this, Login_Activity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_settings:
                        Intent intent2 = new Intent(Coordinates_View_Activity.this, SettingsActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_curator:
                        Intent intent3 = new Intent(Coordinates_View_Activity.this, CuratorApproveActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_send:
                        Intent intent4 = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",getResources().getString(R.string.emailFeedback), null));
                        intent4.putExtra(Intent.EXTRA_SUBJECT, "App Suggestion");
                        startActivity(Intent.createChooser(intent4, "Send Email"));
                        break;
                    case R.id.nav_more_info:
                        Intent intent5 = new Intent(Coordinates_View_Activity.this, MoreInformation.class);
                        startActivity(intent5);
                        break;
                    case R.id.nav_notifications:
                        Intent intent6 = new Intent(Coordinates_View_Activity.this, NotificationsActivity.class);
                        startActivity(intent6);
                        break;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        Toast toast = Toast.makeText(Coordinates_View_Activity.this, "You have been logged out.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0,0);
                        toast.show();
                        NavigationView hamMenu = findViewById(R.id.hamburger_menu);
                        hamMenu.getMenu().findItem(R.id.nav_login).setVisible(true);
                        hamMenu.getMenu().findItem(R.id.nav_notifications).setVisible(false);
                        hamMenu.getMenu().findItem(R.id.nav_curator_group).setVisible(false);
                        hamMenu.getMenu().findItem(R.id.nav_logout).setVisible(false);
                    case R.id.nav_add_curator:
                        Intent intent7 = new Intent(Coordinates_View_Activity.this, AddCurator.class);
                        startActivity(intent7);

                }
                DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.coord_container);
                mDrawerLayout.closeDrawer(Gravity.LEFT, false);
                return false;
            }
        });

        Button b = (Button) findViewById(R.id.sub_coord_but);
        b.setOnClickListener(new NextEvent());




        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.nav_view);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private class AddTreeEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intentA = new Intent(Coordinates_View_Activity.this, Add_Tree_Activity.class);
            startActivity(intentA);
        }
    }

    private class AddSettingsEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
//            Intent intentA = new Intent(Coordinates_View_Activity.this, SettingsActivity.class);
//            startActivity(intentA);
            DrawerLayout mDrawerLayout = findViewById(R.id.coord_container);
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
                Intent intent1 = new Intent(Coordinates_View_Activity.this, Big_Red_Button.class);
                startActivity(intent1);
                finish();
            }else if(event2.getX()<event1.getX()){
                //right to left swipe
                Intent intent1 = new Intent(Coordinates_View_Activity.this, Maps_Activity.class);
                startActivity(intent1);
                finish();
            }
            return true;
        }
    }

    private class NextEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            EditText lat_val = (EditText) findViewById(R.id.lat_edit);
            EditText long_val = (EditText) findViewById(R.id.long_edit);
//            Log.i( "message given",  x);
            try{
                latitude = Double.parseDouble(lat_val.getText().toString());
                longitude = Double.parseDouble(long_val.getText().toString());

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

                    if(ds instanceof AllUsersDataSource) {
                        MainActivity.banana = MainActivity.allUsersDataSource.search(testing);
                    }else if(ds instanceof UserTreeDataSource){
                        MainActivity.userTreeDataSourceGlobal.setUserTrees(MainActivity.allUsersDataSource.getUserTrees());
                        MainActivity.banana = MainActivity.userTreeDataSourceGlobal.search(testing);
                    }else {
                        ds.initialize(Coordinates_View_Activity.this, null);
                        MainActivity.banana = ds.search(testing);
                    }
//                    ds.initialize(Coordinates_View_Activity.this, null);
//                    MainActivity.banana = ds.search(testing);

                    if (MainActivity.banana != null) {
                        if(MainActivity.banana.getClosestDist() < closest1) {
                            closest1 = MainActivity.banana.getClosestDist();
                            closestTree = MainActivity.banana;
                        }
                    }
                }

//                HopeCollegeDataSource ds = new HopeCollegeDataSource();
//                ds.initialize(Coordinates_View_Activity.this,null);
//                MainActivity.banana = ds.search(testing);
                if(closestTree != null) {
                    MainActivity.banana = closestTree;
                }
                if(MainActivity.banana != null) {
                    Intent intentA = new Intent(Coordinates_View_Activity.this, Tree_Info_First.class);
                    //              intentA.putExtra("treeClass", MainActivity.banana);
                    startActivity(intentA);
                }else{
                    Toast.makeText(getBaseContext(), "There are no trees near enough!", Toast.LENGTH_LONG).show();
                }
            }catch(java.lang.NumberFormatException e){
                Toast toast = Toast.makeText(Coordinates_View_Activity.this, "Please enter a numeric value", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }
//    public static boolean isNumeric(String str) {
//        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal. DOES NOT WORK ATM
//    }

}
