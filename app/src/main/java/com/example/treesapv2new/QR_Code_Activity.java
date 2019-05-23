package com.example.treesapv2new;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Vibrator;
import android.service.autofill.FieldClassification;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.treesapv2new.datasource.HopeCollegeDataSource;
import com.example.treesapv2new.model.TreeLocation;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
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

        requestPermissions(PERMS, REQUEST_ID);

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
                                if((Math.abs(latD) >90)|| (Math.abs(longD)>180)){
                                    txtResult.setText("Invalid QR code for this app.");
                                }else {
                                    txtResult.setText("Location: " + lat + ", " + longit);
                                    b.setVisibility(View.VISIBLE);
                                }
                            }else{
                                txtResult.setText("Invalid QR code for this app.");
                            }


//                            txtResult.setText("Location: " +qrCodes.valueAt(0).displayValue);
                        }
                    });
                }
            }
        });
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