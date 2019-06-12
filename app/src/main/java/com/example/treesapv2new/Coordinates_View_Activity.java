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
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GestureDetectorCompat;
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

import com.example.treesapv2new.datasource.CityOfHollandDataSource;
import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.ExtendedCoHDataSource;
import com.example.treesapv2new.datasource.HopeCollegeDataSource;
import com.example.treesapv2new.datasource.ITreeDataSource;
import com.example.treesapv2new.datasource.UserTreeDataSource;
import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.model.TreeLocation;
import com.google.android.gms.common.util.NumberUtils;

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


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.activit_coordinates_view);

        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMS, REQUEST_ID);
        }

        ImageButton addTreeButton = (ImageButton) findViewById(R.id.add_tree_button_0);
        addTreeButton.setOnClickListener(new Coordinates_View_Activity.AddTreeEvent());

        ImageButton settingsButton = (ImageButton) findViewById(R.id.setting_button);
        settingsButton.setOnClickListener(new Coordinates_View_Activity.AddSettingsEvent());

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
            Intent intentA = new Intent(Coordinates_View_Activity.this, SettingsActivity.class);
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
                    }else{
                        ds = new ITreeDataSource();
                    }

                    ds.initialize(Coordinates_View_Activity.this, null);
                    MainActivity.banana = ds.search(testing);


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
