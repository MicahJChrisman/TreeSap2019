package com.example.treesapv2new;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

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
import com.example.treesapv2new.datasource.CityOfHollandDataSource;
import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.DataSourceList;
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


public class MainActivity extends AppCompatActivity {
    public static Tree banana = null;

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
    String emailAddress = "jipping@hope.edu";
    String sharingLink = "https://play.google.com/store";

    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_main_new);

        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMS, REQUEST_ID);
        }

        ImageButton settingsButton = (ImageButton) findViewById(R.id.settings);
        settingsButton.setOnClickListener(new NextEvent());

        ImageButton addTreeButton = (ImageButton) findViewById(R.id.add_tree_button_0);
        addTreeButton.setOnClickListener(new AddTreeEvent());


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
                        Intent intent2 = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_coordinates:
                        Intent intent3 = new Intent(MainActivity.this, Coordinates_View_Activity.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_map:
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
//        prefs.edit().putString("HopeCollegeDataSource", "");
//        prefs.edit().putString("CityOfHollandDataSource", "");
//        prefs.edit().putString("ExtendedCoHDataSource", "");
//        prefs.edit().putString("ITreeDataSource", "");

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.nav_view);
        //sharedPreferences = getSharedPreferences("com.example.treesapv2new", MODE_PRIVATE);
        //checkFirstRun();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = prefs.edit();
        HashSet<String> dbs = new HashSet<String>();
        dbs.add("HopeCollegeDataSource");
        dbs.add("CityOfHollandDataSource");
        dbs.add("ExtendedCoHDataSource");
        dbs.add("ITreeDataSource");
        editor.putStringSet("databasesUsedSelector", dbs);
        editor.apply();

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        boolean firstStart = prefs.getBoolean("firstStart", true);         **Meant to detect when the app is open for the first time, but when I implement this, adding the databases suddenly doesn't work.**
//        if(firstStart){                                                    **Commented out to fiz later**
//            setDatabases();
//        }
    }

//    public void setDatabases(){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        SharedPreferences.Editor editor = prefs.edit();
//        HashSet<String> dbs = new HashSet<String>();
//        dbs.add("HopeCollegeDataSource");
//        dbs.add("CityOfHollandDataSource");
//        dbs.add("ExtendedCoHDataSource");
//        dbs.add("ITreeDataSource");
//        editor.putStringSet("dataBasesUsedSelector", dbs);
//        editor.apply();
//        editor.putBoolean("firstStart", false);
//        editor.apply();
//        Set<String> sources = prefs.getStringSet("databasesUsedSelector",new HashSet<String>());
//
//    }
    private class NextEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intentA = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intentA);
        }
    }

    private class AddTreeEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intentA = new Intent(MainActivity.this, Add_Tree_Activity.class);
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
            }else if(event2.getX()<event1.getX()){
                //right to left swipe
                Intent intent1 = new Intent(MainActivity.this, Big_Red_Button.class);
                finish();
                startActivity(intent1);
            }
            return true;
        }
    }

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

//    private View setupRecyclerView() {
//        LayoutInflater inflater = (LayoutInflater) parent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.treeid_view, null);
//        RecyclerView rv = (RecyclerView) ll.findViewById(R.id.treeid_element_view);
//        LinearLayoutManager lm = new LinearLayoutManager(this);
//        rv.setLayoutManager(lm);
//
//        ArrayList<BulletedListItem> items = generateIDItems();
//        idMethodsAdapter = new CardViewListAdapter(this, items);
//        rv.setAdapter(idMethodsAdapter);
//
//        //final BoxSwipeController swipeController = new BoxSwipeController();
//        final BoxSwipeController swipeController = new BoxSwipeController(new SwipeControllerActions() {
//            @Override
//            public void onRightClicked(int position) {
//                ArrayList<BulletedListItem> items = generateIDItems();
//                if (position == 0) {
//                    selectedIDMethod = null;
//                    PrefManager.putInteger("lastIDMethod", -1);
//                } else if (position < items.size()-1) {
//                    position--;
//                    if(PrefManager.getString("lastDataSources", "").equals("")){
//
//                    }
//                    else {
//                        if (!selectedDataSources.isEmpty()) {
//                            selectedDataSources.remove(position);
//                            String lastDataSources = PrefManager.getString("lastDataSources", "");
//                            String[] dsources = lastDataSources.split(",");
//                            lastDataSources = "";
//                            for (int i = 0; i < dsources.length; i++) {
//                                if (i < position) {
//                                    if (i > 0) lastDataSources += ",";
//                                    lastDataSources += "" + i;
//                                } else if (i > position) {
//                                    if (i > 0) lastDataSources += ",";
//                                    lastDataSources += "" + (i - 1);
//                                }
//                            }
//                            if (lastDataSources.length() == 2) {
//                                lastDataSources = lastDataSources.replaceAll(",", "");
//                            }
//                            PrefManager.putString("lastDataSources", lastDataSources);
//                        }
//                    }
//                } else {
//                    selectedDisplayMethod = null;
//                    PrefManager.putInteger("lastDisplayMethod", -1);
//                }
//                items = generateIDItems();
//                idMethodsAdapter.setItems(items);
//                idMethodsAdapter.notifyDataSetChanged();
//                adapter.notifyDataSetChanged();
//                //idMethodsAdapter.notifyItemRemoved(position);
//                //idMethodsAdapter.notifyItemRangeChanged(position, idMethodsAdapter.getItemCount());
//            }
//        });
//
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(), lm.getOrientation());
//        rv.addItemDecoration(dividerItemDecoration);
//        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//                swipeController.onDraw(c);
//            }
//        });
//
//        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
//        itemTouchhelper.attachToRecyclerView(rv);
//
//        return ll;
//    }

//    private void createTreeIDConfigBox(boolean add) {
//        String methods;
//
//        idItems = new BoxItem(this);
//        idItems.setTitle("Tree Identification");
//        idItems.setSubtitle("This is how trees are identified.  You need an ID method, data sources, and a display method.");
//
//        // Setup a change button
//        ImageButton change = new ImageButton(this);
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.edit);
//        change.setImageBitmap(bm);
//        change.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        change.setOnClickListener(new ChangeMethodsAction(this));
//        idItems.setButton(change);
//
//        idItems.setContent(setupRecyclerView());
//
//        if (add) adapter.addBoxItem(idItems);
//    }
//
//    private void createConnectionsBox() {
//        BoxItem bi = new BoxItem(this);
//        bi.setTitle("Connections");
//        bi.setSubtitle("These are connections to Web sites about tree benefits and tree measurements.");
//
//        ArrayList<BulletedListItem> items = new ArrayList<BulletedListItem>();
//        items.add(new BulletedListItem("iTree Web Tools", "http://itreetools.org"));
//        items.add(new BulletedListItem("National Tree Benefit Calculator", "http://www.treebenefits.com/calculator"));
//        items.add(new BulletedListItem("iNaturalist", "https://www.inaturalist.org/"));
//        items.add(new BulletedListItem("The TreeSap App Page", "http://www.treesap.info"));
//        connectionsWebView = new BulletedWebView(this, items);
//        connectionsWebView.setItems(items);
//        bi.setContent(connectionsWebView);
//        adapter.addBoxItem(bi);
//    }
//
//    private void createITreeAcknowledgement() {
//        BoxItem bi = new BoxItem(this);
//        bi.setTitle("Acknowledgement");
//        LayoutInflater inflater = LayoutInflater.from(this);
//        final View view = inflater.inflate(R.layout.itree_box, null);
//        bi.setContent(view);
//        adapter.addBoxItem(bi);
//    }
//
//    private void createTipsTricksBox() {
//        String str;
//        int background = R.color.colorPrimary;
//
//        int color = ResourcesCompat.getColor(getResources(), background, null);
//        String hex = Integer.toHexString(color).substring(2);
//        String tipsString = "<html><style>body {background-color: #"+hex+";} li {font-size: 24px;}</style><body>";
//
//        try {
//            URL url = new URL("http://treesap.info/tipsandtricks.html");
//            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//
//            while ((str = in.readLine()) != null) {
//                tipsString += str;
//            }
//
//            in.close();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return;
//        } catch (IOException e) {
//            return;
//        }
//
//        if (tipsString.length() == 0) return;
//
//        BoxItem bi = new BoxItem(this);
//        bi.setTitle("Tips and Tricks");
//        WebView tv = new WebView(this);
//        tv.loadData(tipsString, "text/html", null);
//        bi.setContent(tv);
//        adapter.addBoxItem(bi);
//    }
//
//    private void createAcknowledgementsBox() {
//        BoxItem bi = new BoxItem(this);
//        bi.setTitle("About");
//        LayoutInflater inflater = LayoutInflater.from(this);
//        final View view = inflater.inflate(R.layout.about_box, null);
////        TextView tv = new TextView(this);
////        tv.setText("This software is (C) Hope College, 2018. It was written by Mike Jipping, Louis Kopp, and Caleb Tallquist.  All rights are reserved.");
//        bi.setContent(view);
//        adapter.addBoxItem(bi);
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(selectedDisplayMethod!=null) {
            if (selectedDisplayMethod.getPopupWindow() != null) {
                selectedDisplayMethod.dismiss();
                return;
            }
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
        }
    }

    String mCurrentPhotoPath;

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

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//            StrictMode.setVmPolicy(builder.build());
//            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                File photoFile = null;
//                try {
//                    photoFile = createImageFile();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//                if (photoFile != null) {
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                }
//            }
//
//        } else if(id==R.id.nav_suggest){
//            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
//                    "mailto",emailAddress, null));
//            intent.putExtra(Intent.EXTRA_SUBJECT, "App Suggestion");
//            startActivity(Intent.createChooser(intent, "Send Email"));
//
//        } else if (id == R.id.nav_share) {
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setType("text/html");
//            intent.putExtra(Intent.EXTRA_SUBJECT, "Treesap Link");
//            intent.putExtra(Intent.EXTRA_TEXT, sharingLink);
//            startActivity(Intent.createChooser(intent, "Send Email"));
//        } else if (id == R.id.nav_send) {
//            try {
//                Process process = Runtime.getRuntime().exec("logcat -d");
//                BufferedReader bufferedReader = new BufferedReader(
//                        new InputStreamReader(process.getInputStream()));
//
//                StringBuilder log=new StringBuilder();
//                String line = "";
//                while ((line = bufferedReader.readLine()) != null) {
//                    log.append(line);
//                }
//                newLog = log.toString();
//                final AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
//                a_builder.setMessage("").setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
//                                        "mailto",emailAddress, null));
//                                intent.putExtra(Intent.EXTRA_SUBJECT, "Treesap Logcat");
//                                intent.putExtra(Intent.EXTRA_TEXT, newLog);
//                                startActivity(Intent.createChooser(intent, "Send Email"));
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//
//                            }
//
//                        });
//                AlertDialog alert = a_builder.create();
//                alert.setTitle("Would you like to send information to the developer?");
//                alert.show();}
//            catch (IOException e) {}
//
//        }
//        else if (id == R.id.nav_settings) {
//            startActivity(new Intent(MainActivity.this, Settings.class));
//        }
//
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

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
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //mImageView.setImageBitmap(imageBitmap);
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
                //Toast.makeText(parent, "No tree was found at"+treeLocation.toString(), Toast.LENGTH_LONG).show();
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
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (sharedPreferences.getBoolean("firstRun", false)) {
//            //You can perform anything over here. This will call only first time
//            editor = sharedPreferences.edit();
//            editor.putBoolean("firstRun", false);
//            editor.commit();
//
//        }
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        if (prefs.getBoolean("firstrun", false)) {
//        }
//        else{
//            HashSet<String> dbs = new HashSet<String>();
//            dbs.add("HopeCollegeDataSource");
//            dbs.add("CityOfHollandDataSource");
//            dbs.add("ExtendedCoHDataSource");
//            dbs.add("ITreeDataSource");
//            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//            prefs.edit().putStringSet("dataBasesUsedSelector", dbs);
//            prefs.edit().putBoolean("firstrun", false).commit();
//        }
//    }


}
