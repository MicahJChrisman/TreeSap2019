package com.example.treesapv2new;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.example.treesapv2new.R;
import com.example.treesapv2new.control.BoxSwipeController;
import com.example.treesapv2new.control.ChangeMethodsAction;
import com.example.treesapv2new.control.IntentIntegrator;
import com.example.treesapv2new.control.IntentResult;
import com.example.treesapv2new.control.PrefManager;
import com.example.treesapv2new.control.SwipeControllerActions;
import com.example.treesapv2new.datasource.AllUsersDataSource;
import com.example.treesapv2new.datasource.CityOfHollandDataSource;
import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.DataSourceList;
import com.example.treesapv2new.datasource.UserTreeDataSource;
import com.example.treesapv2new.display.AddNotesActivity;
import com.example.treesapv2new.display.DisplayMethod;
import com.example.treesapv2new.display.DisplayMethodList;
import com.example.treesapv2new.identify.Barcode_Identifier;
import com.example.treesapv2new.identify.Big_Red_Button_Identifier;
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
import com.example.treesapv2new.view.CardViewListAdapter;
import com.example.treesapv2new.view.MapsActivity;
import com.example.treesapv2new.view.UserViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nonnull;


public class MainActivity extends AppCompatActivity {
    public static Tree banana = null;
    public static UserTreeDataSource userTreeDataSourceGlobal = new UserTreeDataSource();
    public static ArrayList<Tree> treesNearby = new ArrayList<>();
    public static HashMap<String, ArrayList<String>> storedImages = new HashMap<String, ArrayList<String>>();
    private TextView mTextMessage;
    private GestureDetectorCompat gestureObject;

    IdentificationMethod selectedIDMethod;
    static List<DataSource> selectedDataSources;
    DisplayMethod selectedDisplayMethod;

    UserViewAdapter adapter;
    Context parent;
    TreeLocation selectedLocation;
    BulletedWebView treeIDWebView, connectionsWebView;
    BoxItem idItems;

    CardViewListAdapter idMethodsAdapter;
    static final int REQUEST_IMAGE_CAPTURE = 5;
    private String newLog;
    String sharingLink = "https://play.google.com/store";

    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
    protected void onStart(){
        super.onStart();

        //Gives an error dialog if app is not connected to internet
        boolean isConnectedToFirebase = ConnectionCheck.isConnectedToFirebase(MainActivity.this);
        if(!isConnectedToFirebase && !ConnectionCheck.offlineMessageShown){
            ConnectionCheck.showOfflineMessage(MainActivity.this);
            ConnectionCheck.offlineMessageShown = true;
        }else if(isConnectedToFirebase && ConnectionCheck.offlineMessageShown || ConnectionCheck.offlineCuratorMessageShown || ConnectionCheck.offlineNotificationsMessageShown){
            ConnectionCheck.offlineMessageShown = false;
            ConnectionCheck.offlineCuratorMessageShown = false;
            ConnectionCheck.offlineNotificationsMessageShown = false;
        }
        FirebaseUser user = mAuth.getCurrentUser();
        NavigationView hamMenu = findViewById(R.id.hamburger_menu);
        if(user != null) {
            hamMenu.getMenu().findItem(R.id.nav_login).setVisible(false);
            hamMenu.getMenu().findItem(R.id.nav_notifications).setVisible(true);
            hamMenu.getMenu().findItem(R.id.nav_logout).setVisible(true);

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            //Gets the notifications for when a tree was approved or rejected
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
                        hamMenu.getMenu().findItem(R.id.nav_notifications).setTitle("Notifications");
                    }else{
                        hamMenu.getMenu().findItem(R.id.nav_notifications).setTitle("Notifications (" + Integer.valueOf(notificationCount) + ")");
                    }
                }
            });






            //Checks if current user is a curator
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

    public static AllUsersDataSource allUsersDataSource = new AllUsersDataSource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_main_new);
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            //Gets the notifications for when a tree was approved or rejected
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
        //Trees nearby must be cleared in order for Tree_Info_First to work correctly
        MainActivity.treesNearby.clear();

        //Initializes the data source so that the search does not have async problems
        if(!allUsersDataSource.finishedBoolean) {
            allUsersDataSource.initialize(this, null);
        }


        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);


        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMS, REQUEST_ID);
        }

        ImageButton settingsButton = (ImageButton) findViewById(R.id.settings);
        settingsButton.setOnClickListener(new SettingsEvent());

        ImageButton addTreeButton = (ImageButton) findViewById(R.id.add_tree_button_0);
        addTreeButton.setOnClickListener(new AddTreeEvent());

        //Bottom Nav bar listener
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_brb:
                        Intent intent1 = new Intent(MainActivity.this, Big_Red_Button.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_home:
//                        Intent intent2 = new Intent(MainActivity.this, MainActivity.class);
//                        startActivity(intent2);
                        break;
                    case R.id.navigation_coordinates:
                        Intent intent3 = new Intent(MainActivity.this, Coordinates_View_Activity.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_map:
                        //Creates loading circle when waiting for map to load
                        ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(MainActivity.this);
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
                        Intent intent4 = new Intent(MainActivity.this, Maps_Activity.class);
                        startActivity(intent4);
                        break;
                    case R.id.navigation_qr:
                        Intent intent5 = new Intent(MainActivity.this, QR_Code_Activity.class);
                        startActivity(intent5);
                        break;
                }
                return false;
            }
        };

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.nav_view);

        //Listener for the drawer menu
        NavigationView hamburgerView = findViewById(R.id.hamburger_menu);
        hamburgerView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_login:
                        Intent intent1 = new Intent(MainActivity.this, Login_Activity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_settings:
                        Intent intent2 = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_curator:
                        Intent intent3 = new Intent(MainActivity.this, CuratorApproveActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_send:
                        Intent intent4 = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",getResources().getString(R.string.emailFeedback), null));
                        intent4.putExtra(Intent.EXTRA_SUBJECT, "App Suggestion");
                        startActivity(Intent.createChooser(intent4, "Send Email"));
                        break;
                    case R.id.nav_more_info:
                        Intent intent5 = new Intent(MainActivity.this, MoreInformation.class);
                        startActivity(intent5);
                        break;
                    case R.id.nav_notifications:
                        Intent intent6 = new Intent(MainActivity.this, NotificationsActivity.class);
                        startActivity(intent6);
                        break;
                    case R.id.nav_logout:
                        mAuth.signOut();
                        Toast toast = Toast.makeText(MainActivity.this, "You have been logged out.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0,0);
                        toast.show();
                        NavigationView hamMenu = findViewById(R.id.hamburger_menu);
                        hamMenu.getMenu().findItem(R.id.nav_login).setVisible(true);
                        hamMenu.getMenu().findItem(R.id.nav_notifications).setVisible(false);
                        hamMenu.getMenu().findItem(R.id.nav_curator_group).setVisible(false);
                        hamMenu.getMenu().findItem(R.id.nav_logout).setVisible(false);
                        break;
                    case R.id.nav_add_curator:
                        Intent intent7 = new Intent(MainActivity.this, AddCurator.class);
                        startActivity(intent7);
                        break;
                }
                DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.container);
                mDrawerLayout.closeDrawer(Gravity.LEFT, false);
                return false;
            }
        });

        //Sets the default settings for when app is first downloaded
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Set<String> sources = prefs.getStringSet("databasesUsedSelector",new HashSet<String>());
        SharedPreferences.Editor editor = prefs.edit();
        if(sources.size()==0) {
            HashSet<String> dbs = new HashSet<String>();
//            dbs.add("HopeCollegeDataSource");
//            dbs.add("CityOfHollandDataSource");
            dbs.add("ExtendedCoHDataSource");
            dbs.add("UserTreeDataSource");
            dbs.add("AllUsersDataSource");
            editor.putStringSet("databasesUsedSelector", dbs);
            editor.apply();
        }
        if(prefs.getString("distanceFromTreePref", "") .equals("")){
            editor.putString("distanceFromTreePref", "100");
        }

    }

    private class SettingsEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            DrawerLayout mDrawerLayout = findViewById(R.id.container);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    private class AddTreeEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intentA = new Intent(MainActivity.this, Add_Tree_Activity.class);
            startActivity(intentA);
        }
    }

    //Probably not needed anymore
    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    //Swipe is no longer active, so this is not needed
    class LearnGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY){
            if(event2.getX()>event1.getX()){
                //left to right swipe

            }else if(event2.getX()<event1.getX()){
                //right to left swipe
                Intent intent1 = new Intent(MainActivity.this, Big_Red_Button.class);
                finish();
                startActivity(intent1);
            }
            return true;
        }
    }

    //Requests for gps, camera, and storage permissions
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length == 0) {
        }

        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(parent, android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(parent);
            }
            builder.setTitle("No GPS Permissions")
                    .setMessage("You will not be able to use your phone's GPS sensor for identifying trees.")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(parent, android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(parent);
            }
            builder.setTitle("No Storage Permissions")
                    .setMessage("You will not be able to use any data sources. Data sources must be be copied to your phone's storage.")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
        if(grantResults[2] != PackageManager.PERMISSION_GRANTED){
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(parent, android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(parent);
            }
            builder.setTitle("No Camera Permissions")
                    .setMessage("You will not be able to use your camera from this app to take any pictures.")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }


    }


    private ArrayList<BulletedListItem> generateIDItems() {

        String methods;
        ArrayList<BulletedListItem> items = new ArrayList<BulletedListItem>();

        if (selectedIDMethod == null) {
            items.add(new BulletedListItem("ID:No identifier specified", "ID:You have to specify a way to identify trees!", Color.YELLOW));
        } else {
            if (selectedIDMethod.getDescription() == null) {
                items.add(new BulletedListItem("ID: " + selectedIDMethod.getMethodName(),
                        "iddisplay:No Description Found"));
            } else {
                items.add(new BulletedListItem("ID: " + selectedIDMethod.getMethodName(),
                        "iddisplay:" + selectedIDMethod.getDescription()));
            }
        }

        try {
            if (selectedDataSources.size() == 0) {
                items.add(new BulletedListItem("Data:No data source specified", "Data:You have to specify a data collection of trees.", Color.YELLOW));
            } else {
                for (DataSource dataSource : selectedDataSources) {
                    if (dataSource.getDescription() == null) {
                        items.add(new BulletedListItem("Data: " + dataSource.getSourceName()));
                    } else {
                        items.add(new BulletedListItem("Data: " + dataSource.getSourceName(), dataSource.getDescription()));
                    }
                }
            }
        } catch (Exception e) {
            methods = "ERROR! " + e.getLocalizedMessage();
        }

        try {
            if (selectedDisplayMethod == null) {
                items.add(new BulletedListItem("Display:No display method specified", "Display:You have to specify a way to display tree data.", Color.YELLOW));
            } else {
                if (selectedDisplayMethod.getDescription() == null) {
                    items.add(new BulletedListItem("ID: " + selectedDisplayMethod.getMethodName(),
                            "display:No Description Found"));
                } else {
                    items.add(new BulletedListItem("ID: " + selectedDisplayMethod.getMethodName(),
                            "display:" + selectedDisplayMethod.getDescription()));
                }
            }
        } catch (Exception e) {
            methods = "ERROR! " + e.getLocalizedMessage();
        }

        return items;

    }

    //Prevents app from crashing when back is pressed on the home screen
    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        DrawerLayout layout = (DrawerLayout) findViewById(R.id.container);
        if(selectedDisplayMethod!=null) {
            if (selectedDisplayMethod.getPopupWindow() != null) {
                selectedDisplayMethod.dismiss();
                return;
            }
        }
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        a_builder.setMessage("").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finishAffinity();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
            AlertDialog alert = a_builder.create();
            alert.setTitle("Are you sure you want to exit?");
            alert.show();
//        }
    }

    String mCurrentPhotoPath;

    //Unsure if any of the following methods are currently used in the app
    private File createImageFile() throws IOException {
        File storageDir = Environment.getExternalStorageDirectory();
        int now = (int) System.currentTimeMillis();
        String id = String.valueOf(now);
        File image = File.createTempFile(
                id,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult!=null&&scanningResult.getContents() != null) {
            String scanContent = scanningResult.getContents();
            String latitude = scanContent.split(",")[0];
            String longitude = scanContent.split(",")[1];
            TreeLocation location = new TreeLocation();
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            selectedLocation = location;
            SimpleIdentifier simpleIdentifier = new SimpleIdentifier();
            simpleIdentifier.setLocation(location);
            Barcode_Identifier barcodeIdentifier = (Barcode_Identifier) selectedIDMethod;
            selectedIDMethod = simpleIdentifier;
            IDCallback idCallback = new IDCallback();
            idCallback.idCompleted();
            selectedIDMethod = barcodeIdentifier;
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            galleryAddPic();
        }

        // If there is an error, simply return
        if (resultCode != Activity.RESULT_OK) return;

        // GPSIdenitifier has returned
        if (requestCode == 1000) {
            Double longitude = intent.getDoubleExtra("longitude", 90.0);
            Double latitude = intent.getDoubleExtra("latitude", 0.0);
            TreeLocation treeLocation = new TreeLocation();
            treeLocation.setLongitude(longitude);
            treeLocation.setLatitude(latitude);
            Log.d("MainActivity", "GPS is "+treeLocation.toString());
            selectedLocation = treeLocation;
            ((GPS_Identifier)selectedIDMethod).setLocation(treeLocation);
            selectedIDMethod.callbackClass.idCompleted();
        } else if (requestCode == 2000) {
            Double longitude = intent.getDoubleExtra("longitude", 90.0);
            Double latitude = intent.getDoubleExtra("latitude", 0.0);
            TreeLocation treeLocation = new TreeLocation();
            treeLocation.setLongitude(longitude);
            treeLocation.setLatitude(latitude);
            Log.d("MainActivity", "GPS is "+treeLocation.toString());
            selectedLocation = treeLocation;
            ((Google_Map_Identifier)selectedIDMethod).setLocation(treeLocation);
            selectedIDMethod.callbackClass.idCompleted();
        }

    }

    public void setSelectedIDMethod(IdentificationMethod idMethod, Integer position) {
        selectedIDMethod = idMethod;
        PrefManager.putInteger("lastIDMethod", position);
        ArrayList<BulletedListItem> items = generateIDItems();
        idMethodsAdapter.setItems(items);
        idMethodsAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    public IdentificationMethod getSelectedIDMethod() {
        return selectedIDMethod;
    }

    public void removeSelectedIDMethod() {
        selectedIDMethod = null;
        PrefManager.putInteger("lastIDMethod", -1);
        ArrayList<BulletedListItem> items = generateIDItems();
        idMethodsAdapter.setItems(items);
        idMethodsAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    public void addSelectedDataSource(DataSource dataSource, Integer position) {
        for (DataSource ds : selectedDataSources) {
            if (ds.getSourceName().equals(dataSource.getSourceName())) return;
        }

        String lastDataSources = PrefManager.getString("lastDataSources", "");
        if (lastDataSources.length() > 0) lastDataSources += ",";
        lastDataSources += ""+position;
        PrefManager.putString("lastDataSources", lastDataSources);

        selectedDataSources = new ArrayList<DataSource>();
        String[] lastList = lastDataSources.split(",");
        for (String src : lastList) {
            Integer pos = new Integer(src);
            try {
                DataSource ds = (DataSource) (DataSourceList.get(pos).newInstance());
                ds.setParent(this);
                selectedDataSources.add(ds);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        ArrayList<BulletedListItem> items = generateIDItems();
        idMethodsAdapter.setItems(items);
        idMethodsAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    public void clearDataSources() {
        selectedDataSources.clear();
    }

    public static List<DataSource> getSelectedDataSources() {
        return selectedDataSources;
    }

    public void setSelectedDisplayMethod(DisplayMethod dMethod, Integer position) {
        selectedDisplayMethod = dMethod;
        PrefManager.putInteger("lastDisplayMethod", position);
        ArrayList<BulletedListItem> items = generateIDItems();
        idMethodsAdapter.setItems(items);
        idMethodsAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    public DisplayMethod getSelectedDisplayMethod() {
        return selectedDisplayMethod;
    }

    public class IDCallback implements IdentificationMethod.IdentificationCallback {
        @Override
        public void idCompleted() {
            TreeLocation treeLocation = selectedIDMethod.getLocation();
            Tree tree = null;

            Log.i("MainActivity",
                    "ID Completed. Tree location = "+treeLocation.toString());

            for (DataSource source : selectedDataSources) {
                Log.d("MainActivity", "Searching.  Trying: "+source.getSourceName());
                source.initialize(parent,null);
                tree = source.search(treeLocation);
                if (tree != null) {
                    if (tree.isFound()) break;  // and NOT just the closest
                }
            }
            if (tree == null) {
                // flag a tree not found
                Log.i("MainActivity", "No tree was found at "+treeLocation.toString());
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(parent, android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(parent);
                }
                builder.setTitle("No Tree was Found!")
                        .setMessage("No tree was found at the GPS coordinates that you gave using the data sources that you specified.")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else if (selectedDisplayMethod != null) {
                tree.setSearchFor(new GPSCoordinates(treeLocation.getLatitude(), treeLocation.getLongitude()));
                selectedDisplayMethod.display(tree);
            } else {
                Log.i("MainActivity", "No selected display for "+tree.getCommonName());
            }
        }
    }

}
