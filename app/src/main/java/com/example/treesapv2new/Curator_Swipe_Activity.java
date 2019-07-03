package com.example.treesapv2new;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.model.TreeLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Maps;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.lorentzos.flingswipe.FlingCardListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.sql.Array;
import java.util.Date;
import java.sql.Timestamp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;


/**
 * https://www.youtube.com/watch?v=Q2FPDI99-as
 */



public class Curator_Swipe_Activity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {  //implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private NewArrayAdapter arrayAdapter;
    private int i;
    GoogleMap mMap;
    SupportMapFragment mapFragment;
    private final String[] PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE
    };
    private static final int REQUEST_ID = 6;

    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 seconds
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Context parent;
    float personalMarker = BitmapDescriptorFactory.HUE_VIOLET;
    float treeMarker = BitmapDescriptorFactory.HUE_GREEN;
    LatLng coordinates;

    float zoom = 16;
    boolean whichSource = false;
    //Double longitude, latitude;
    //LatLng coords;

    private Location location;
    private TextView locationTv;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;


    ListView listView;
    List<Tree> rowItems;
    List<Tree> penTrees;
    FirebaseFirestore db;
    CollectionReference treesRef;
    CollectionReference apprRef;
    SwipeFlingAdapterView flingContainer;
    Tree currentTree;
    Tree previousTree;
    Stack<DocSnap> previousTrees;
    FloatingActionButton undoButton;
    FloatingActionButton skipButton;
    FloatingActionButton rejectButton;
    FloatingActionButton acceptButton;
    FloatingActionButton mapButton;
    com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton directionsButton;



    //https://www.youtube.com/watch?v=118wylgD_ig
    //yt: "show and hide map fragment android tutorial"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntent().getExtras();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.curator_swipe);

        penTrees = new ArrayList<Tree>();
//                List<Tree> rowItems;
        //FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        treesRef = db.collection("pendingTrees");
        apprRef = db.collection("approvedTrees");

        previousTrees = new Stack<>();
//        Tree tree = new Tree();
//        tree.setCommonName("Test tree");
//        tree.setScientificName("Testus treeus");
//        tree.setCurrentDBH(50.0);
//        penTrees.add(tree);
        //Object[] objects = {db};
        //new DownloadFilesTask();
        new DownloadFilesTask().execute();
        try {
            Thread.sleep(1600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);


        googleApiClient = new GoogleApiClient.Builder(Curator_Swipe_Activity.this).addApi(LocationServices.API).addConnectionCallbacks(Curator_Swipe_Activity.this).addOnConnectionFailedListener(Curator_Swipe_Activity.this::onConnectionFailed).build();
        googleApiClient.connect();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        FragmentManager fm = getSupportFragmentManager();
        if(mapFragment == null){
            fm = getSupportFragmentManager();
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.placeholder, mapFragment);
            ft.hide(mapFragment);
            ft.commit();
        }

        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .hide(mapFragment)
//                .addToBackStack(null)
                .commit();
        directionsButton = findViewById(R.id.directions_button);
        directionsButton.setVisibility(View.GONE);
        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                boolean locMarker = prefs.getBoolean("locationMarkerSwitch",true);
                if(locMarker == true){
                    //TODO get directions
                    // Create a Uri from an intent string. Use the result to create an Intent.
                    double latitude = currentTree.getLocation().getLatitude();
                    double longitude = currentTree.getLocation().getLongitude();
                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+latitude+ ",+" + longitude);
                    // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    // Make the Intent explicit by setting the Google Maps package
                    mapIntent.setPackage("com.google.android.apps.maps");
                    //Makes sure Google Maps is installed
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        // Attempt to start an activity that can handle the Intent
                        startActivity(mapIntent);
                    }
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMS, REQUEST_ID);
        }

//        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
//            @Override
//            public void removeFirstObjectInAdapter() {
//                // this is the simplest way to delete an object from the Adapter (/AdapterView)
//                Log.d("LIST", "removed object!");
//                penTrees.remove(0);
//                arrayAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onLeftCardExit(Object dataObject) {
//                //Do something on the left!
//                //You also have access to the original object.
//                //If you want to use it just cast it (String) dataObject
//                Toast.makeText(Curator_Swipe_Activity.this, "Rejected!", Toast.LENGTH_SHORT).show();
//                rejectTree();
////                try {
////                    Thread.sleep(1000);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
//                hideMap();
//                //removeFirstObjectInAdapter();
//            }
//
//            @Override
//            public void onRightCardExit(Object dataObject) {
//                acceptTree();
//                hideMap();
//            }
//
//            @Override
//            public void onAdapterAboutToEmpty(int itemsInAdapter) {
//                // Ask for more data here
//                //rowItems.add("XML ".concat(String.valueOf(i)));
//                arrayAdapter.notifyDataSetChanged();
//                Log.d("LIST", "notified");
//                i++;
//            }
//
//            @Override
//            public void onScroll(float scrollProgressPercent) {
//                View view = flingContainer.getSelectedView();
//               //view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
//                //view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
//            }
//
////            @Override
////            public void onCardExited(){
////
////            }
//        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(Curator_Swipe_Activity.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

       //arrayAdapter = new NewArrayAdapter(this, R.layout.item, penTrees);
        rejectButton = findViewById(R.id.reject_button);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectTree();
                Toast.makeText(Curator_Swipe_Activity.this, "Rejected!", Toast.LENGTH_SHORT).show();
//                FlingCardListener listener = flingContainer.getTopCardListener();
//                //flingContainer.dispatchNestedFling(400,400, false);
//                // Obtain MotionEvent object
//                long downTime = SystemClock.uptimeMillis();
//                long eventTime = SystemClock.uptimeMillis() + 100;
//                float x = 0.0f;
//                float y = -50.0f;
//// List of meta states found here:     developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
//                int metaState = 0;
//                MotionEvent motionEvent = MotionEvent.obtain(
//                        downTime,
//                        eventTime,
//                        MotionEvent.ACTION_MOVE,
//                        x,
//                        y,
//                        metaState
//                );

// Dispatch touch event to view
               //flingContainer.getSelectedView().dispatchTouchEvent(motionEvent);

                //flingContainer.getSelectedView().getRootView()..getDisplay();
                penTrees.remove(0);
                arrayAdapter.notifyDataSetChanged();
                if(penTrees.size()>0) {
                    arrayAdapter.getView(0, flingContainer.getSelectedView(), null);
                    setCurrentTree();
                }
                hideMap();
//                penTrees.remove(0);
//                arrayAdapter.notifyDataSetChanged();
                //listener.onTouch(flingContainer.getSelectedView(), MotionEvent.obtain);
//                flingContainer.dispatchNestedFling(Float.valueOf(10000), Float.valueOf(10000), true);
            }
        });

        acceptButton = findViewById(R.id.accept_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptTree();
                Toast.makeText(Curator_Swipe_Activity.this, "Accepted!", Toast.LENGTH_SHORT).show();
                penTrees.remove(0);
                arrayAdapter.notifyDataSetChanged();
                if(penTrees.size()>0) {
                    arrayAdapter.getView(0, flingContainer.getSelectedView(), null);
                    setCurrentTree();
                }
                hideMap();
//
//                penTrees.remove(0);
//                flingContainer.getSelectedView().setVisibility(View.GONE);
//                arrayAdapter.notifyDataSetChanged();
            }
        });

        undoButton = findViewById(R.id.undo_button);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!previousTrees.isEmpty()) {
                    DocSnap document = previousTrees.pop();
                    if(document.wasDeleted()) {
                        DocumentReference docum = db.collection("pendingTrees").document();
                        docum.set(document.getDoc().getData()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                if (document.wasApproved()) {
                                    DocumentReference doc = apprRef.document(document.getId());
                                    if (doc != null) {
                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("commonName", FieldValue.delete());
                                        updates.put("dbhArray", FieldValue.delete());
                                        updates.put("images", FieldValue.delete());
                                        updates.put("latitude", FieldValue.delete());
                                        updates.put("longitude", FieldValue.delete());
                                        updates.put("otherInfo", FieldValue.delete());
                                        updates.put("scientificName", FieldValue.delete());
                                        updates.put("timestamp", FieldValue.delete());
                                        updates.put("userID", FieldValue.delete());
                                        doc.update(updates);
                                        doc.delete();
                                    }
                                }
                                penTrees.add(0, makeTree(document.getDoc()));
                                penTrees.get(0).setID(docum.getId());
                                arrayAdapter.notifyDataSetChanged();
                                arrayAdapter.getView(0, flingContainer.getSelectedView(), null);
                                setCurrentTree();
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                previousTrees.push(document);
                                Log.w("error", "Error writing document. Tree file has been pushed back on stack of previous trees", e);
                            }
                        });
                    }else{
                        penTrees.add(0, makeTree(document.getDoc()));
//                        penTrees.get(0).setID(docum.getId());
                        arrayAdapter.notifyDataSetChanged();
                        arrayAdapter.getView(0, flingContainer.getSelectedView(), null);
                        setCurrentTree();
                    }
                    hideMap();
                    Toast.makeText(Curator_Swipe_Activity.this, "Undone", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Curator_Swipe_Activity.this, "No previous trees", Toast.LENGTH_SHORT).show();
                }
            }
        });


        skipButton = findViewById(R.id.skip_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (penTrees.size() > 1){
                    DocumentReference doc = treesRef.document(currentTree.getID());
                    doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        DocSnap docSnap = new DocSnap(false, documentSnapshot.getId(), documentSnapshot, false);
                        previousTrees.push(docSnap);
                        penTrees.remove(0);
                        arrayAdapter.notifyDataSetChanged();
                        arrayAdapter.getView(0, flingContainer.getSelectedView(), null);
                        setCurrentTree();
                        Toast.makeText(Curator_Swipe_Activity.this, "Skipped!", Toast.LENGTH_SHORT).show();
                        hideMap();
                    }
                    });
                }else{
                    Toast.makeText(Curator_Swipe_Activity.this, "No more trees", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mapButton = findViewById(R.id.map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mapButton.setActivated(false);
//                mapButton.setClickable(false);
                FragmentManager fm = getSupportFragmentManager();
                if(mapFragment.isVisible()){
                    hideMap();
                }else {
                    coordinates = new LatLng(currentTree.getLocation().getLatitude(), currentTree.getLocation().getLongitude());
//                GMap gMap = new GMap(coords);
                    fm.beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                            .show(mapFragment)
                            .addToBackStack(null)
                            .commit();
                    directionsButton.setVisibility(View.VISIBLE);
                    //mapFragment.getView().setVisibility(View.VISIBLE);
                    mapFragment.getMapAsync(Curator_Swipe_Activity.this::onMapReady);
                }
            }
        });
    }

    public void hideMap(){
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .hide(mapFragment)
                .commit();
        fm.popBackStack();
        directionsButton.setVisibility(View.GONE);
    }

    public void setCurrentTree(){
        if(penTrees.size() > 0) {
            currentTree = penTrees.get(0);
        }
    }

    public void rejectTree(){
        DocumentReference doc = treesRef.document(currentTree.getID());
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        previousTrees.push(new DocSnap(false, doc.getId(), document, true));
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("commonName", FieldValue.delete());
                        updates.put("dbhArray", FieldValue.delete());
                        updates.put("images", FieldValue.delete());
                        updates.put("latitude", FieldValue.delete());
                        updates.put("longitude", FieldValue.delete());
                        updates.put("otherInfo", FieldValue.delete());
                        updates.put("scientificName", FieldValue.delete());
                        updates.put("timestamp", FieldValue.delete());
                        updates.put("userID", FieldValue.delete());
                        doc.update(updates);
                        doc.delete();
                        arrayAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("problem:", "No such document in rejectTree()");
                    }
                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
//        flingContainer.
        //setCurrentTree();
        });
    }

    public void acceptTree(){
        DocumentReference doc = treesRef.document(currentTree.getID());
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        CollectionReference notifications = db.collection("notifications");
                        Map<String, Object> dataMap = new HashMap<String,Object>();
//                        for(DocumentSnapshot docs : document){
//
//                        }
                        Map<String, Object> documentData = document.getData();
                        dataMap.put("treeData", documentData);
                        dataMap.put("accepted", true);
                        dataMap.put("message", "");
                        dataMap.put("read", false);
                        Date date= new Date();
                        Timestamp ts = new Timestamp(date.getTime());
                        dataMap.put("timestamp", ts);
                        notifications.document().set(dataMap);





                        db.collection("acceptedTrees").document().set(document.getData())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Log.d(TAG, "DocumentSnapshot successfully written!");
//                                        rejectTree();//just to delete currentTree
                                        DocumentSnapshot document = task.getResult();
                                        previousTrees.push(new DocSnap(true, document.getId(), document, true));
                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("commonName", FieldValue.delete());
                                        updates.put("dbhArray", FieldValue.delete());
                                        updates.put("images", FieldValue.delete());
                                        updates.put("latitude", FieldValue.delete());
                                        updates.put("longitude", FieldValue.delete());
                                        updates.put("otherInfo", FieldValue.delete());
                                        updates.put("scientificName", FieldValue.delete());
                                        updates.put("timestamp", FieldValue.delete());
                                        updates.put("userID", FieldValue.delete());
                                        doc.update(updates);
                                        doc.delete();
                                        arrayAdapter.notifyDataSetChanged();
                                        setCurrentTree();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
//                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
                    } else {
//                        Log.d(TAG, "No such document");
                    }
                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public Tree makeTree(DocumentSnapshot document){
        Tree tree = new Tree();
        String commonName = (String) document.get("commonName");
        if(commonName == null || commonName.equals("")){
            tree.setCommonName("N/A");
        }else {
            tree.setCommonName(commonName);
        }
        String scientificName = (String) document.get("scientificName");
        if(scientificName == null || scientificName.equals("")){
            tree.setScientificName("N/A");
        }else {
            tree.setScientificName(scientificName);
        }
        ArrayList<Number> dbhList = ((ArrayList<Number>) document.get("dbhArray"));
//                            Number dbh = ((ArrayList<Number>) document.get("dbhArray")).get(0);
        if(dbhList == null || dbhList.isEmpty()){
            tree.setCurrentDBH(0.0);
        }else{
//                                tree.setCurrentDBHetCurrentDBH(new Double(dbh));
//                                tree.setCurrentDBH(dbh);
            tree.setCurrentDBH(dbhList.get(0).doubleValue());
        }
        tree.setID(document.getId());
        ArrayList<String> notes = (ArrayList<String>) document.get("notes");
        if(notes != null && !notes.isEmpty()) {
            String finalNote = "";
            for (String note : notes) {
                finalNote += note + "\n";
            }
            //String note = otherInfo.get("Notes");
//          while(i>=0) {
//          String notes = (String) tree.getInfo("Notes");
//          notes = notes + "/n" + otherInfo[i];
//          }
            if (!finalNote.equals("")) {
                tree.addInfo("Notes", finalNote);
            }
        }

        Map<Object, Object> stringPics = (Map<Object, Object>) document.get("images");
        ArrayList<Object> fullPics = (ArrayList<Object>) ((Map<Object, Object>) document.get("images")).get("full");
        ArrayList<Object> pics;
        //String[] pics = stringPics.split("\n?\t.*: ");
        for(Object key : stringPics.keySet()){
            pics = (ArrayList<Object>) stringPics.get(key);
            for(Object pic : pics){
                if(pic != null){
                    tree.addPics((String)key, pic);
                }
            }
        }
//        int i = 0;
//        if(stringPics!=null) {
//            while(i<stringPics.size()){
//                String key;
//                if(i == 0){
//                    key = "Image Bark";
//                }else if(i == 1){
//                    key = "Image Leaf";
//                }else{
//                    key = "Image Tree";
//                }
//                String pic = (String) stringPics.get(i);
////                                    byte[] encodeByte = Base64.decode(pic, Base64.DEFAULT);
////                                    InputStream is = new ByteArrayInputStream(encodeByte);
////
////                                    Bitmap bmp = BitmapFactory.decodeStream(is);
////                                    BitmapDrawable dBmp = new BitmapDrawable(getResources(), bmp);
//
//                if(pic != null) {
//                    tree.addPics(key, stringPics.get(i));
//                }
//                i++;
//            }
//        }
        TreeLocation location = new TreeLocation(Double.valueOf(document.get("latitude").toString()), Double.valueOf(document.get("longitude").toString()));
        tree.setLocation(location);
        return tree;
    }

//    @Override
//    public void onB

//    @Override
//    public void onAttach()

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mapFragment.getView().setVisibility(View.GONE);
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
        googleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean locMarker = prefs.getBoolean("locationMarkerSwitch",true);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker2);
        mMap.addMarker(new MarkerOptions().position(coordinates).title(currentTree.getCommonName()).snippet(coordinates.toString()).icon(icon));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
        mMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });

        LatLng hope = new LatLng(42.788002, -86.105971);



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMS, REQUEST_ID);
            }
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hope, zoom));
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
            UiSettings uiSettings = mMap.getUiSettings();
            //uiSettings.setZoomControlsEnabled(true);
//            uiSettings.setTiltGesturesEnabled(true);
            uiSettings.setMyLocationButtonEnabled(true);
            uiSettings.setMapToolbarEnabled(true);
            uiSettings.setTiltGesturesEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, zoom));
        }
    }

    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
        @Override
        protected Long doInBackground(URL... urls) {
//            FirebaseFirestore db = (FirebaseFirestore) objects[0];
            db.collection("pendingTrees").orderBy("timestamp", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Tree tree = makeTree(document);
                            penTrees.add(tree);
                        }
                    } else {
                        Toast toast = Toast.makeText(Curator_Swipe_Activity.this, "Unable to load trees.", Toast.LENGTH_LONG);
                    }
                }
            });
            return new Long(1);
        }

        protected void onPostExecute(Long result) {
            arrayAdapter = new NewArrayAdapter(Curator_Swipe_Activity.this, R.layout.item, penTrees, getSupportFragmentManager());
            flingContainer.setAdapter(arrayAdapter);
            setCurrentTree();
        }
    }



//    private class GMap extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
//
//        GoogleMap mMap;
//        SupportMapFragment mapFragment;
//        private final String[] PERMS = {
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.INTERNET,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.ACCESS_NETWORK_STATE
//        };
//        private static final int REQUEST_ID = 6;
//        Location mLastLocation;
//        Marker mCurrLocationMarker;
//        Context parent;
//        float personalMarker = BitmapDescriptorFactory.HUE_VIOLET;
//        float treeMarker = BitmapDescriptorFactory.HUE_GREEN;
//
//        float zoom = 16;
//        boolean whichSource = false;
//        //Double longitude, latitude;
//        LatLng coords;
//
//        private Location location;
//        private TextView locationTv;
//        private GoogleApiClient googleApiClient;
//        private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
//        private LocationRequest locationRequest;
//
//        public GMap(LatLng coords){
//            super();
//            //TODO permissions
//
////            googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
////            googleApiClient.connect();
////
////            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
////            StrictMode.setThreadPolicy(policy);
////            mapFragment = (SupportMapFragment) getSupportFragmentManager()
////                    .findFragmentById(R.id.map);
////            mapFragment.getMapAsync(this);
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                requestPermissions(PERMS, REQUEST_ID);
////            }
//            this.coords = coords;
//        }
//
//
//
//
//        public GoogleMap getMap(){
//            return mMap;
//        }
//    }
}
