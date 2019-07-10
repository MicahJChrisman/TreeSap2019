package com.example.treesapv2new;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.model.TreeLocation;
import com.example.treesapv2new.view.CuratorMessage;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

public class CuratorActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
//    private NewArrayAdapter arrayAdapter;
//    private ImageAdapter imageAdapter;
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

    Marker mCurrLocationMarker;
    float personalMarker = BitmapDescriptorFactory.HUE_VIOLET;
    Marker currentTreeMarker;
    LatLng coordinates;

    float zoom = 16;
//    boolean whichSource = false;

    private Location location;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;

    int index;
    List<Tree> penTrees;
    FirebaseFirestore db;
    CollectionReference treesRef;
    CollectionReference apprRef;
//    SwipeFlingAdapterView flingContainer;
    Tree currentTree;
//    NestedScrollView nestedScrollView;
    LinearLayout nestedScrollChild;
    TextView commonName;
    TextView scientificName;
    TextView dbhs;
    TextView latitude;
    TextView longitude;
    TextView notes;
    static ArrayList<BitmapDrawable> dBmpList;
    ClickableViewPager viewPager;
    Stack<DocSnap> previousTrees;
    ImageButton directionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        index = (Integer) getIntent().getExtras().get("index");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.curate_activity);

        penTrees = new ArrayList<Tree>();
        db = FirebaseFirestore.getInstance();
        treesRef = db.collection("pendingTrees");
        apprRef = db.collection("acceptedTrees");

        previousTrees = new Stack<>();
        new CuratorActivity.DownloadFilesTask().execute();
        commonName = findViewById(R.id.common_name);
        scientificName = findViewById(R.id.scientific_name);
        dbhs = findViewById(R.id.dbhs);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        notes = findViewById(R.id.notes);

        googleApiClient = new GoogleApiClient.Builder(CuratorActivity.this).addApi(LocationServices.API).addConnectionCallbacks(CuratorActivity.this).addOnConnectionFailedListener(CuratorActivity.this::onConnectionFailed).build();
        googleApiClient.connect();

        NestedScrollView nestedScrollView = (NestedScrollView) findViewById (R.id.nested_scrolll_view);
        nestedScrollView.setFillViewport (true);
        nestedScrollChild = findViewById(R.id.nested_scroll_child);

        FragmentManager fm = getSupportFragmentManager();
        if(mapFragment == null){
            fm = getSupportFragmentManager();
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.placeholder, mapFragment);
            ft.hide(mapFragment);
            ft.commit();
        }

        directionsButton = findViewById(R.id.directions_button);
        directionsButton.setVisibility(View.GONE);
        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                boolean locMarker = prefs.getBoolean("locationMarkerSwitch",true);
                if(locMarker == true){
                    double latitude = currentTree.getLocation().getLatitude();
                    double longitude = currentTree.getLocation().getLongitude();
                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+latitude+ ",+" + longitude);
                    // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    // Make the Intent explicit by setting the Google Maps package
                    mapIntent.setPackage("com.google.android.apps.maps");
                    // Makes sure Google Maps is installed
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

        BottomNavigationView navView = findViewById(R.id.nav_view);
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.reject_button:
                       showMessageDialogue(false);
                        break;
                    case R.id.undo_button:
                        if (!previousTrees.isEmpty()) {
                            DocSnap document = previousTrees.pop();
                            if(document.wasDeleted()) {
                                DocumentReference docum = db.collection("pendingTrees").document(document.getId());
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
                                        penTrees.add(index, makeTree(document.getDoc()));

                                        setCurrentTree(index);

                                        CollectionReference notifications = db.collection("notifications");
                                        Map<String, Object> dataMap = new HashMap<String,Object>();
                                        dataMap.put("treeData", FieldValue.delete());
                                        dataMap.put("accepted", FieldValue.delete());
                                        dataMap.put("message", FieldValue.delete());
                                        dataMap.put("read", FieldValue.delete());
                                        dataMap.put("timestamp", FieldValue.delete());
                                        notifications.document(document.getId()).update(dataMap);
                                        setView();
                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        previousTrees.push(document);
                                        Log.w("error", "Error writing document. Tree file has been pushed back on stack of previous trees", e);
                                    }
                                });

                            }else{
                                penTrees.add(index, makeTree(document.getDoc()));
                                setCurrentTree(index);
                                setView();
                            }
                            hideMap();
                            Toast.makeText(CuratorActivity.this, "Undone", Toast.LENGTH_SHORT).show();
                        } else {
                            if(penTrees.isEmpty() || index == 0){
                                Toast.makeText(CuratorActivity.this, "No previous trees", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                index--;
                                setCurrentTree(index);
                                setView();
                                hideMap();
                            }
                        }
                        break;
                    case R.id.map_button:
                        FragmentManager fm = getSupportFragmentManager();
                        if(mapFragment.isVisible()){
                            directionsButton.setVisibility(View.GONE);
                            hideMap();
                        }else {
                            coordinates = new LatLng(currentTree.getLocation().getLatitude(), currentTree.getLocation().getLongitude());
                            fm.beginTransaction()
                                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                                    .show(mapFragment)
                                    .addToBackStack(null)
                                    .commit();
                            mapFragment.getMapAsync(CuratorActivity.this::onMapReady);
                        }
                        break;
                    case R.id.skip_button:
                        if (penTrees.size() > 1 && index < (penTrees.size()-1)){
                            DocumentReference doc = treesRef.document(currentTree.getID());
                            doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    DocSnap docSnap = new DocSnap(false, documentSnapshot.getId(), documentSnapshot, false);
                                    previousTrees.push(docSnap);
                                    penTrees.remove(index);
                                    setCurrentTree(index);
                                    setView();
                                    Toast.makeText(CuratorActivity.this, "Skipped", Toast.LENGTH_SHORT).show();
                                    hideMap();
                                }
                            });
                        }else{
                            Toast.makeText(CuratorActivity.this, "No more trees", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.accept_button:
                        showMessageDialogue(true);
                        break;
                }
                return false;
            }
        };
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    public void showMessageDialogue(boolean accepted) {
        final AlertDialog.Builder a_builder = new AlertDialog.Builder(CuratorActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        a_builder.setMessage("Would you like to send a message to the user who submitted this tree?").setCancelable(true)
                .setPositiveButton("Add message", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(CuratorActivity.this, CuratorMessage.class);
                        if(accepted == true) {
                            intent.putExtra("accepted", true);
                        }else{
                            intent.putExtra("accepted",false);
                        }
                        startActivityForResult(intent, 0);
                        //TODO add message
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if(accepted == true){
                a_builder.setNegativeButton("Accept without message", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    acceptTree("");
                    hideMap();
                }
            });
        }else{
            a_builder.setNegativeButton("Reject without message", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    rejectTree("");
                    hideMap();
                }
            });
        }
        AlertDialog alert = a_builder.create();
        alert.setTitle("Add message?");
        alert.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED && data != null){
            if(requestCode == 0){
                String message = (String) data.getExtras().get("message");
                if((boolean) data.getExtras().get("accepted") == true){
                    acceptTree(message);
                }else{
                    rejectTree(message);
                }
            }
            else if(requestCode == 1){
                viewPager.setCurrentItem((int) data.getExtras().get("position"));
            }
        }
    }

    @Override
    public void onBackPressed(){
            directionsButton.setVisibility(View.GONE);
            super.onBackPressed();
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

    public ArrayList<BitmapDrawable> getDbmpList(){
        return dBmpList;
    }

    public void setView(){
        if(penTrees.isEmpty()){
            findViewById(R.id.empty_message).setVisibility(View.VISIBLE);
            findViewById(R.id.cardView).setVisibility(View.GONE);

        }else {
            dBmpList = populateList();
            viewPager = (ClickableViewPager) findViewById(R.id.pager);

            ImageAdapter adapter = new ImageAdapter(CuratorActivity.this, dBmpList, new ArrayAdapter(CuratorActivity.this, R.layout.curate_activity));
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(dBmpList.size() - 1);
            viewPager.setOnItemClickListener(new ClickableViewPager.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if(!dBmpList.isEmpty()) {
                        Intent intent = new Intent(CuratorActivity.this, FullScreenViewPager.class);
                        intent.putExtra("position", position);
                        startActivityForResult(intent, 1);
                    }
                }
            });

            TextView noPicsMessage = findViewById(R.id.no_pics_message);
            if (dBmpList.size() == 0) {
                viewPager.setBackgroundResource(R.drawable.dark_logo);

            } else {
                viewPager.setBackgroundColor(ContextCompat.getColor(this, R.color.image_pager_background));
            }
            String cName = (String) currentTree.getCommonName();
            if (cName != null && cName != "") {
                commonName.setText(cName);
            } else {
                commonName.setText("N/A");
            }
            String sName = (String) currentTree.getScientificName();
            if (cName != null && cName != "") {
                scientificName.setText(sName);
            } else {
                scientificName.setText("N/A");
            }
            ArrayList<Object> dbhList = currentTree.getDBHArray();
            String dbhText = "";
            if (dbhList != null && !dbhList.isEmpty()) {
                dbhText += dbhList.get(0);
                if (dbhList.size() > 1) {
                    for (int i = 1; i < dbhList.size(); i++) {
                        dbhText += "\n" + dbhList.get(i);
                    }
                }
                if(dbhText!=null && !dbhText.equals("")) {
                    dbhs.setText(dbhText);
                }else{
                    dbhs.setText("N/A");
                }
            } else {
                dbhs.setText("N/A");
            }
            TreeLocation treeLocation = currentTree.getLocation();
            latitude.setText(Double.toString(treeLocation.getLatitude()));
            longitude.setText(Double.toString(treeLocation.getLongitude()));
            ArrayList<String> notesList = (ArrayList<String>) currentTree.getNotesArray();
            if (notesList != null && !notesList.isEmpty()) {
                String finalNote = notesList.get(0);
                if (notesList.size() > 1) {
                    for (int i = 1; i < notesList.size(); i++) {
                        finalNote += "\n" + notesList.get(i);
                    }
                }
                if (!finalNote.equals("")) {
                    notes.setText(finalNote);
                } else {
                    notes.setText("N/A");
                }
            } else {
                notes.setText("N/A");
            }
        }
    }


    public void setCurrentTree(int index){
        if(penTrees.size() > 0 && index >= penTrees.size()){
            index = index-1;
        }
        if(penTrees.size() > 0 && index < penTrees.size()) {
            currentTree = penTrees.get(index);
        }
    }

    public void rejectTree(String message){
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
                        dataMap.put("accepted", false);
                        dataMap.put("message", message);
                        dataMap.put("read", false);
                        Date date = new Date();
                        Timestamp ts = new Timestamp(date.getTime());
                        dataMap.put("timestamp", ts);
                        notifications.document(doc.getId()).set(dataMap);


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
                        penTrees.remove(index);
                        if(penTrees.size()>0) {
                            setCurrentTree(index);
                            setView();
                        }
                    } else {
                        Log.d("Problem:", "No such document in rejectTree()");
                    }
                } else {
                    Log.d("Reject task ailed:", "get failed with ", task.getException());
                }
            }
        });
    }

    public void acceptTree(String message){
        DocumentReference doc = treesRef.document(currentTree.getID());

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        CollectionReference notifications = db.collection("notifications");
                        Map<String, Object> dataMap = new HashMap<String,Object>();
                        Map<String, Object> documentData = document.getData();
                        dataMap.put("treeData", documentData);
                        dataMap.put("accepted", true);
                        dataMap.put("message", message);
                        dataMap.put("read", false);
                        Date date = new Date();
                        Timestamp ts = new Timestamp(date.getTime());
                        dataMap.put("timestamp", ts);
                        notifications.document(currentTree.getID()).set(dataMap);

                        db.collection("acceptedTrees").document(doc.getId()).set(document.getData())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        DocumentSnapshot document = task.getResult();
                                        previousTrees.push(new DocSnap(true, document.getId(), document, true));
                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("commonName", FieldValue.delete());
                                        updates.put("dbhArray", FieldValue.delete());
                                        updates.put("images", FieldValue.delete());
                                        updates.put("latitude", FieldValue.delete());
                                        updates.put("longitude", FieldValue.delete());
//                                        updates.put("otherInfo", FieldValue.delete());
                                        updates.put("notes", FieldValue.delete());
                                        updates.put("scientificName", FieldValue.delete());
                                        updates.put("timestamp", FieldValue.delete());
                                        updates.put("userID", FieldValue.delete());
                                        doc.update(updates);
                                        doc.delete();
                                        Toast.makeText(CuratorActivity.this, "Accepted!", Toast.LENGTH_SHORT).show();
                                        penTrees.remove(index);
                                        if(penTrees.size()>0) {
                                            setCurrentTree(index);
                                            setView();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Error:", "Error writing document", e);
                                    }
                                });
                    } else {
                        Log.d("Problem:", "No such document");
                    }
                } else {
                    Log.d("Accept task failed", "get failed with ", task.getException());
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
        Number latitude = (Number) document.get("latitude");
        Number longitude = (Number) document.get("longitude");
        if(latitude != null && !latitude.equals("") && longitude != null && !longitude.equals("")){
            TreeLocation treeLocation = new TreeLocation(Double.parseDouble(latitude.toString()), Double.parseDouble(longitude.toString()));
            tree.setLocation(treeLocation);
        }
        String scientificName = (String) document.get("scientificName");
        if(scientificName == null || scientificName.equals("")){
            tree.setScientificName("N/A");
        }else {
            tree.setScientificName(scientificName);
        }
        ArrayList<Object> dbhList = (ArrayList<Object>) document.get("dbhArray");
        if(dbhList != null && !dbhList.isEmpty()){
            tree.setDBHArray(dbhList);
        }
        tree.setID(document.getId());
        ArrayList<String> notes = (ArrayList<String>) document.get("notes");
        tree.setNotesArray(notes);
        HashMap<String, ArrayList<String>> stringPics = (HashMap<String, ArrayList<String>>) document.get("images");
        tree.setTreePhotos(stringPics);

        TreeLocation location = new TreeLocation(Double.valueOf(document.get("latitude").toString()), Double.valueOf(document.get("longitude").toString()));
        tree.setLocation(location);
        return tree;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mapFragment.getView().setVisibility(View.GONE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
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
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(currentTreeMarker!=null){
            currentTreeMarker.remove();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean locMarker = prefs.getBoolean("locationMarkerSwitch",true);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.tree_marker2);
        currentTreeMarker = mMap.addMarker(new MarkerOptions().position(coordinates).title((String) currentTree.getCommonName()).snippet(coordinates.toString()).icon(icon));
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
            UiSettings uiSettings = mMap.getUiSettings();
            uiSettings.setMyLocationButtonEnabled(true);
            uiSettings.setMapToolbarEnabled(true);
            uiSettings.setTiltGesturesEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, zoom));
            directionsButton.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<BitmapDrawable> populateList(){
        ArrayList<BitmapDrawable> dBmpList = new ArrayList<>();

        HashMap<String, ArrayList<String>> picMap = currentTree.getTreePhotos();
        ArrayList<String> picList;
        for(String key : picMap.keySet()){
            picList = picMap.get(key);
            for(String pic : picList){
                if(!pic.equals("")) {
                    byte[] encodeByte = Base64.decode(pic, Base64.DEFAULT);
                    InputStream is = new ByteArrayInputStream(encodeByte);
                    Bitmap bmp = BitmapFactory.decodeStream(is);
                    BitmapDrawable dBmp = new BitmapDrawable(getResources(), bmp);
                    dBmpList.add(dBmp);
                }
            }
        }


        return dBmpList;
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
//                            treeSnaps.add(document);
                        }
                        setCurrentTree(index);
                        setView();
                    } else {
                        Toast toast = Toast.makeText(CuratorActivity.this, "Unable to load trees.", Toast.LENGTH_LONG);
                    }
                }
            });
            return new Long(1);
        }
    }
}
