package com.example.treesapv2new;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.service.autofill.FieldClassification;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treesapv2new.datasource.CityOfHollandDataSource;
import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.ExtendedCoHDataSource;
import com.example.treesapv2new.datasource.HopeCollegeDataSource;
import com.example.treesapv2new.datasource.ITreeDataSource;
import com.example.treesapv2new.model.TreeLocation;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QR_Code_Activity extends AppCompatActivity {

    private GestureDetectorCompat gestureObject;

//This is a test to see how bit bucket works

    SurfaceView cameraPreview;
    TextView txtResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    int RequestCameraPermissionID = 1001;

    Double latD;
    Double longD;


    private static final String[] PERMS = {
            Manifest.permission.CAMERA,
            Manifest.permission.VIBRATE
    };

    private static final int REQUEST_ID = 2;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.activity_qr_code);

        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMS, REQUEST_ID);
        }

        ImageButton addTreeButton = (ImageButton) findViewById(R.id.add_tree_button_3);
        addTreeButton.setOnClickListener(new QR_Code_Activity.AddTreeEvent());

        ImageButton settingsButton = (ImageButton) findViewById(R.id.setting_button_3);
        settingsButton.setOnClickListener(new QR_Code_Activity.AddSettingsEvent());

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        final Button b = (Button) findViewById(R.id.sub_coord_but2);
        b.setOnClickListener(new NextEvent());
        b.setVisibility(View.INVISIBLE);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_brb:
                        Intent intent1 = new Intent(QR_Code_Activity.this, Big_Red_Button.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_home:
                        Intent intent2 = new Intent(QR_Code_Activity.this, MainActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_coordinates:
                        Intent intent3 = new Intent(QR_Code_Activity.this, Coordinates_View_Activity.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_map:
                        Intent intent4 = new Intent(QR_Code_Activity.this, Maps_Activity.class);
                        startActivity(intent4);
                        break;
                    case R.id.navigation_qr:
                        Intent intent5 = new Intent(QR_Code_Activity.this, QR_Code_Activity.class);
                        startActivity(intent5);
                        break;
                }
                return false;
            }
        };


        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.nav_view);


        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);
        txtResult = (TextView) findViewById(R.id.txtResult);

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this,barcodeDetector).setRequestedPreviewSize(100,100).build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getBaseContext(), "Permissions are not right", Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    cameraSource.start(cameraPreview.getHolder());
                }catch (IOException e){
                    Log.i("Camera failed", "did not work exception");
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        //final SparseArray<Barcode> qrCodes = barcodeDetector.getDetectedItems();
//        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//        final SparseArray<Barcode> qrCodes = barcodeDetector.detect(frame);
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if(qrCodes.size()!=0){
                    txtResult.post(new Runnable(){
                        @Override
                        public void run(){
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate((500));
                            String q = qrCodes.valueAt(0).displayValue;

                            ArrayList<String> listBuffer = new ArrayList<String>();
                            Pattern pattern = Pattern.compile("([0-9]+[.][0-9]+)");
                            Matcher matcher = pattern.matcher(q);
                            while(matcher.find()){
                                listBuffer.add(matcher.group());
                            }
                            if(listBuffer.size() == 2){
                                String lat = listBuffer.get(0);
                                String longit = listBuffer.get(1);
                                latD = Double.parseDouble(lat);
                                longD = Double.parseDouble(longit);
                                longD = -longD;
                                if((Math.abs(latD) >90)|| (Math.abs(longD)>180)){
                                    txtResult.setText("Invalid QR code for this app.");

                                    AlertDialog.Builder builder = new AlertDialog.Builder(QR_Code_Activity.this);
                                    builder.setCancelable(true);
                                    builder.setTitle("Invalid QR Code for this app.");
                                    builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            cameraSource.release();
                                        }
                                    });
                                    builder.show();

                                }else {

                                    txtResult.setText("Location: " + lat + ", -" + longit);
                                    //b.setVisibility(View.VISIBLE);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(QR_Code_Activity.this);
                                    builder.setCancelable(true);
                                    builder.setTitle("Location: " + lat + ", -" + longit);
                                    builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.setPositiveButton("Get Info", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            TreeLocation testing = new TreeLocation(latD,longD);

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

                                                ds.initialize(QR_Code_Activity.this,null);
                                                MainActivity.banana = ds.search(testing);
                                                if (MainActivity.banana != null) {
                                                    if (MainActivity.banana.isFound()) break;  // and NOT just the closest
                                                }



//                                            HopeCollegeDataSource ds = new HopeCollegeDataSource();
//                                            ds.initialize(QR_Code_Activity.this,null);
//                                            MainActivity.banana = ds.search(testing);
//

                                            }
                                            if(MainActivity.banana != null) {
                                                Intent intentA = new Intent(QR_Code_Activity.this, Tree_Info_First.class);
                                                startActivity(intentA);
                                            }else{
                                                txtResult.setText("Invalid QR code for this app.");
                                                AlertDialog.Builder builder = new AlertDialog.Builder(QR_Code_Activity.this);
                                                builder.setCancelable(true);
                                                builder.setTitle("Location is not in any database");
                                                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });
                                                builder.show();
                                            }

                                        }
                                    });
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            barcodeDetector.release();
                                        }
                                    });
                                    builder.show();
                                }
                            }else{
                                txtResult.setText("Invalid QR code for this app.");

                                AlertDialog.Builder builder = new AlertDialog.Builder(QR_Code_Activity.this);
                                builder.setCancelable(true);
                                builder.setTitle("Invalid QR Code for this app.");
                                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        barcodeDetector.release();
                                    }
                                });
                                builder.show();


                            }


//                            txtResult.setText("Location: " +qrCodes.valueAt(0).displayValue);
                        }
                    });
                }
            }
        });
    }

    private class AddTreeEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intentA = new Intent(QR_Code_Activity.this, Add_Tree_Activity.class);
            startActivity(intentA);
        }
    }

    private class AddSettingsEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intentA = new Intent(QR_Code_Activity.this, SettingsActivity.class);
            startActivity(intentA);
        }
    }


    private class NextEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            TreeLocation testing = new TreeLocation(latD,longD);

            HopeCollegeDataSource ds = new HopeCollegeDataSource();
            ds.initialize(QR_Code_Activity.this,null);
            MainActivity.banana = ds.search(testing);

            Intent intentA = new Intent(QR_Code_Activity.this, Cereal_Box_Activity.class);
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
                Intent intent1 = new Intent(QR_Code_Activity.this, Maps_Activity.class);
                finish();
                startActivity(intent1);
            }else if(event2.getX()<event1.getX()){
                //right to left swipe
            }
            return true;
        }
    }
}