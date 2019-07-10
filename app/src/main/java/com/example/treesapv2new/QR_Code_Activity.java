package com.example.treesapv2new;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.service.autofill.FieldClassification;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.Gravity;
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

import com.example.treesapv2new.datasource.AllUsersDataSource;
import com.example.treesapv2new.datasource.CityOfHollandDataSource;
import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.ExtendedCoHDataSource;
import com.example.treesapv2new.datasource.HopeCollegeDataSource;
import com.example.treesapv2new.datasource.ITreeDataSource;
import com.example.treesapv2new.datasource.UserTreeDataSource;
import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.model.TreeLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
    static String source = null;

    Double latD;
    Double longD;
    final Object synchLock = new Object();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    private static final String[] PERMS = {
            Manifest.permission.CAMERA,
            Manifest.permission.VIBRATE
    };

    private static final int REQUEST_ID = 2;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        NavigationView hamMenu = findViewById(R.id.hamburger_menu);
        boolean isConnectedToFirebase = ConnectionCheck.isConnectedToFirebase(QR_Code_Activity.this);
        if (!isConnectedToFirebase && !ConnectionCheck.offlineMessageShown) {
            ConnectionCheck.showOfflineMessage(QR_Code_Activity.this);
            ConnectionCheck.offlineMessageShown = true;
        } else if (isConnectedToFirebase && ConnectionCheck.offlineMessageShown || ConnectionCheck.offlineCuratorMessageShown || ConnectionCheck.offlineAccountMessageShown) {
            ConnectionCheck.offlineMessageShown = false;
            ConnectionCheck.offlineCuratorMessageShown = false;
            ConnectionCheck.offlineAccountMessageShown = false;
        }
        if (user != null) {
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
                            if (!(Boolean) documentSnapshot.get("read")) {
                                notificationCount++;
                                findViewById(R.id.unread_notification).setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    if (notificationCount == 0) {
                        findViewById(R.id.unread_notification).setVisibility(View.GONE);
                        hamMenu.getMenu().findItem(R.id.nav_notifications).setTitle("Notifications");
                    } else {
                        hamMenu.getMenu().findItem(R.id.nav_notifications).setTitle("Notifications (" + Integer.valueOf(notificationCount) + ")");
                    }
                }
            });
            db.collection("curators").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String userDI = user.getUid();
                        String docID = doc.getId();
                        if (user.getUid().equals(doc.getId())) {
                            hamMenu.getMenu().findItem(R.id.nav_curator_group).setVisible(true);
                            break;
                        }
                    }
                }
            });

        } else {
            hamMenu.getMenu().findItem(R.id.nav_login).setVisible(true);
            hamMenu.getMenu().findItem(R.id.nav_notifications).setVisible(false);
            hamMenu.getMenu().findItem(R.id.nav_curator_group).setVisible(false);
            hamMenu.getMenu().findItem(R.id.nav_logout).setVisible(false);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.qr_code_drawer);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
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
                        ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(QR_Code_Activity.this);
                        progressDialog.setMax(100);
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setTitle("Loading Map");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        final Handler handle = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                progressDialog.incrementProgressBy(1);
                            }
                        };
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    while (progressDialog.getProgress() <= progressDialog
                                            .getMax()) {
                                        Thread.sleep(30);
                                        handle.sendMessage(handle.obtainMessage());
                                        if (progressDialog.getProgress() == progressDialog
                                                .getMax()) {
                                            progressDialog.dismiss();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        Intent intent4 = new Intent(QR_Code_Activity.this, Maps_Activity.class);
                        startActivity(intent4);
                        break;
                    case R.id.navigation_qr:
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
                        Intent intent1 = new Intent(QR_Code_Activity.this, Login_Activity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_settings:
                        Intent intent2 = new Intent(QR_Code_Activity.this, SettingsActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_curator:
                        Intent intent3 = new Intent(QR_Code_Activity.this, CuratorApproveActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_send:
                        Intent intent4 = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", getResources().getString(R.string.emailFeedback), null));
                        intent4.putExtra(Intent.EXTRA_SUBJECT, "App Suggestion");
                        startActivity(Intent.createChooser(intent4, "Send Email"));
                        break;
                    case R.id.nav_more_info:
                        Intent intent5 = new Intent(QR_Code_Activity.this, MoreInformation.class);
                        startActivity(intent5);
                        break;
                    case R.id.nav_notifications:
                        Intent intent6 = new Intent(QR_Code_Activity.this, NotificationsActivity.class);
                        startActivity(intent6);
                        break;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        Toast toast = Toast.makeText(QR_Code_Activity.this, "You have been logged out.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        NavigationView hamMenu = findViewById(R.id.hamburger_menu);
                        hamMenu.getMenu().findItem(R.id.nav_login).setVisible(true);
                        hamMenu.getMenu().findItem(R.id.nav_notifications).setVisible(false);
                        hamMenu.getMenu().findItem(R.id.nav_curator_group).setVisible(false);
                        hamMenu.getMenu().findItem(R.id.nav_logout).setVisible(false);
                        break;
                    case R.id.nav_add_curator:
                        Intent intent7 = new Intent(QR_Code_Activity.this, AddCurator.class);
                        startActivity(intent7);
                        break;

                }
                DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.qr_code_container);
                mDrawerLayout.closeDrawer(Gravity.LEFT, false);
                return false;
            }
        });


        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.nav_view);


        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);
        txtResult = (TextView) findViewById(R.id.txtResult);

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(100, 100).setAutoFocusEnabled(true).build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(), "Permissions are not right", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
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
                Handler handler = new Handler(Looper.getMainLooper());
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (qrCodes.size() != 0) {
                            synchronized (synchLock) {
                                txtResult.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        cameraSource.stop();
                                        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                        vibrator.vibrate((500));
                                        String q = qrCodes.valueAt(0).displayValue;
                                        ArrayList<String> listBuffer = new ArrayList<String>();

                                        List<String> list = Arrays.asList(q.split(","));
                                        for (int i = 0; i < list.size(); i++) {
                                            if (i == 0) {
                                                try {
                                                    latD = Double.parseDouble(list.get(i));
                                                } catch (Exception e) {

                                                }

                                            }
                                            if (i == 1) {
                                                try {
                                                    longD = Double.parseDouble(list.get(i));
                                                } catch (Exception e) {

                                                }
                                            }
                                            if (i == 2) {
                                                source = list.get(i);
                                            }
                                        }
                                        if (list.size() == 3) {
                                            try {
                                                String lat = latD.toString();
                                                String longit = longD.toString();

                                                Handler handler = new Handler(Looper.getMainLooper());
                                                if ((Math.abs(latD) > 90) || (Math.abs(longD) > 180)) {
                                                    txtResult.setText("Invalid QR code for this app.");
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(QR_Code_Activity.this);
                                                    builder.setCancelable(true);
                                                    builder.setTitle("Invalid QR Code for this app.");
                                                    builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                                                Toast.makeText(getBaseContext(), "Permissions are not right", Toast.LENGTH_SHORT).show();
                                                                return;
                                                            }
                                                            try {
                                                                cameraSource.start(cameraPreview.getHolder());
                                                            } catch (IOException e) {
                                                                Log.i("Camera failed", "did not work exception");
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    txtResult.setText("Location: " + lat + ", " + longit);
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(QR_Code_Activity.this);
                                                    builder.setCancelable(true);
                                                    builder.setTitle("Location: " + lat + ", " + longit);
                                                    builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                                                Toast.makeText(getBaseContext(), "Permissions are not right", Toast.LENGTH_SHORT).show();
                                                                return;
                                                            }
                                                            try {
                                                                cameraSource.start(cameraPreview.getHolder());
                                                            } catch (IOException e) {
                                                                Log.i("Camera failed", "did not work exception");
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                                    builder.setPositiveButton("Get Info", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            TreeLocation testing = new TreeLocation(latD, longD);
                                                            DataSource ds = null;
                                                            Tree closestTree = null;
                                                            if (source != null) {
                                                                Log.d("MainActivity", "Searching.  Trying: " + source);

                                                                if (source.equals("Hope College Trees")) {
                                                                    ds = new HopeCollegeDataSource();
                                                                } else if (source.equals("City of Holland Tree Inventory")) {
                                                                    ds = new CityOfHollandDataSource();
                                                                } else if (source.equals("Tree Benefit Data")) {
                                                                    ds = new ExtendedCoHDataSource();
                                                                } else if (source.equals("My Pending Trees")) {
                                                                    ds = MainActivity.userTreeDataSourceGlobal;
                                                                } else if (source.equals("User Trees")) {
                                                                    ds = new AllUsersDataSource();
                                                                } else {
                                                                    txtResult.setText("Invalid QR code for this app.");
                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(QR_Code_Activity.this);
                                                                    builder.setCancelable(true);
                                                                    builder.setTitle("Invalid QR Code for this app.");
                                                                    builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            dialog.cancel();
                                                                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                                                                Toast.makeText(getBaseContext(), "Permissions are not right", Toast.LENGTH_SHORT).show();
                                                                                return;
                                                                            }
                                                                            try {
                                                                                cameraSource.start(cameraPreview.getHolder());
                                                                            } catch (IOException e) {
                                                                                Log.i("Camera failed", "did not work exception");
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    });
                                                                    builder.show();
                                                                }

                                                                if(ds!=null) {
                                                                    if (ds instanceof AllUsersDataSource) {
                                                                        MainActivity.banana = MainActivity.allUsersDataSource.search(testing);
                                                                    } else if (ds instanceof UserTreeDataSource) {
                                                                        MainActivity.userTreeDataSourceGlobal.setUserTrees(MainActivity.allUsersDataSource.getUserTrees());
                                                                        MainActivity.banana = MainActivity.userTreeDataSourceGlobal.search(testing);
                                                                    } else {
                                                                        ds.initialize(QR_Code_Activity.this, null);
                                                                        MainActivity.banana = ds.search(testing);
                                                                    }


                                                                    float closest1 = 0;
                                                                    if (MainActivity.banana != null) {
                                                                        if (MainActivity.banana.getClosestDist() < closest1) {
                                                                            closest1 = MainActivity.banana.getClosestDist();
                                                                            closestTree = MainActivity.banana;
                                                                        }
                                                                    }
                                                                }
//
                                                            }
                                                            if (closestTree != null) {
                                                                MainActivity.banana = closestTree;
                                                            }
                                                            if (MainActivity.banana != null) {
                                                                Intent intentA = new Intent(QR_Code_Activity.this, Tree_Info_First.class);
                                                                startActivity(intentA);
                                                            } else {
                                                                txtResult.setText("Invalid QR code for this app.");
                                                            }
                                                        }
                                                    });
                                                    builder.show();
                                                }
                                            } catch (Exception e) {
                                                txtResult.setText("Invalid QR code for this app.");
                                                AlertDialog.Builder builder = new AlertDialog.Builder(QR_Code_Activity.this);
                                                builder.setCancelable(true);
                                                builder.setTitle("Invalid QR Code for this app.");
                                                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                                            Toast.makeText(getBaseContext(), "Permissions are not right", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }
                                                        try {
                                                            cameraSource.start(cameraPreview.getHolder());
                                                        } catch (IOException e) {
                                                            Log.i("Camera failed", "did not work exception");
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                                builder.show();
                                            }
                                        } else {
                                            txtResult.setText("Invalid QR code for this app.");
                                            AlertDialog.Builder builder = new AlertDialog.Builder(QR_Code_Activity.this);
                                            builder.setCancelable(true);
                                            builder.setTitle("Invalid QR Code for this app.");
                                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                                        Toast.makeText(getBaseContext(), "Permissions are not right", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                    try {
                                                        cameraSource.start(cameraPreview.getHolder());
                                                    } catch (IOException e) {
                                                        Log.i("Camera failed", "did not work exception");
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                            builder.show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
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

    private class AddSettingsEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            DrawerLayout mDrawerLayout = findViewById(R.id.qr_code_container);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }


    private class NextEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            TreeLocation testing = new TreeLocation(latD, longD);

            HopeCollegeDataSource ds = new HopeCollegeDataSource();
            ds.initialize(QR_Code_Activity.this, null);
            MainActivity.banana = ds.search(testing);

            Intent intentA = new Intent(QR_Code_Activity.this, Cereal_Box_Activity.class);
            startActivity(intentA);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            if (event2.getX() > event1.getX()) {
                //left to right swipe
                Intent intent1 = new Intent(QR_Code_Activity.this, Maps_Activity.class);
                finish();
                startActivity(intent1);
            } else if (event2.getX() < event1.getX()) {
                //right to left swipe
            }
            return true;
        }
    }
}