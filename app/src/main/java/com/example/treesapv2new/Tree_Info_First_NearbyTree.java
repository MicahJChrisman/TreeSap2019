package com.example.treesapv2new;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Base64;
import android.widget.Toast;

import com.example.treesapv2new.datasource.AllUsersDataSource;
import com.example.treesapv2new.datasource.CityOfHollandDataSource;
import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.ExtendedCoHDataSource;
import com.example.treesapv2new.datasource.HopeCollegeDataSource;
import com.example.treesapv2new.datasource.ITreeDataSource;
import com.example.treesapv2new.display.AddNotesActivity;
import com.example.treesapv2new.model.Tree;
import com.shivam.library.imageslider.ImageSlider;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Tree_Info_First_NearbyTree extends AppCompatActivity {
    Dialog myDialog;
    private GestureDetectorCompat gestureObject;
    ImageView picAppear;
    private byte[] byteArray;

    private static final int REQUEST_IMGAGE_CAPTURE = 101;
    private static final String[] PERMS = {
            Manifest.permission.CAMERA,
    };
    private static final int REQUEST_ID = 1;
    private static final int[] PERMISSION_ALL = new int[0];

    ArrayAdapter<String> adapter;
    ArrayList<String> images = new ArrayList<String>();
    //private GestureDetectorCompat gestureObject;

    public void onResume(){
        super.onResume();
        findViewById(R.id.nearby_trees_spinner).setVisibility(View.GONE);
    }


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.activity_tree_info_first);
        findViewById(R.id.nearby_trees_spinner).setVisibility(View.GONE);


        Tree_Info_First.nearTree.setIsNearbyTree(true);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.tree_info_first_menu);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem1 = menu.getItem(0);
        menuItem1.setCheckable(false);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_brb:
                        Tree_Info_First.oldMainTree = null;
                        finish();
                        Intent intent1 = new Intent(Tree_Info_First_NearbyTree.this, Big_Red_Button.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_home:
                        Tree_Info_First.oldMainTree = null;
                        finish();
                        Intent intent2 = new Intent(Tree_Info_First_NearbyTree.this, MainActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_coordinates:
                        Tree_Info_First.oldMainTree = null;
                        finish();
                        Intent intent3 = new Intent(Tree_Info_First_NearbyTree.this, Coordinates_View_Activity.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_map:
                        Tree_Info_First.oldMainTree = null;
                        finish();
                        Intent intent4 = new Intent(Tree_Info_First_NearbyTree.this, Maps_Activity.class);
                        startActivity(intent4);
                        break;
                    case R.id.navigation_qr:
                        Tree_Info_First.oldMainTree = null;
                        finish();
                        Intent intent5 = new Intent(Tree_Info_First_NearbyTree.this, QR_Code_Activity.class);
                        startActivity(intent5);
                        break;
                }
                return false;
            }
        };

        myDialog = new Dialog(this);
        patchTreeData();
        updateTree();


        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

//        ImageButton button = (ImageButton) findViewById(R.id.add3);
//        button.setOnClickListener(new AddNotesEvent());

        Button viewNotes = (Button) findViewById(R.id.view_notes_button);
        Button viewPhotos = (Button) findViewById(R.id.view_photos_button);
        viewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewNotes.getText().equals(" View Notes ")) {
                    viewPhotos.setText(" View Photos ");
                    findViewById(R.id.view_photos_slider).setVisibility(View.GONE);
                    viewNotes.setText(" Hide Notes ");
                    findViewById(R.id.notes_scroller).setVisibility(View.VISIBLE);
                }else{
                    viewNotes.setText(" View Notes ");
                    findViewById(R.id.notes_scroller).setVisibility(View.GONE);
                }
            }
        });

        viewPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPhotos.getText().equals(" View Photos ")) {
                    viewNotes.setText(" View Notes ");
                    findViewById(R.id.notes_scroller).setVisibility(View.GONE);
                    viewPhotos.setText(" Hide Photos ");
                    findViewById(R.id.view_photos_slider).setVisibility(View.VISIBLE);
                }else{
                    viewPhotos.setText(" View Photos ");
                    findViewById(R.id.view_photos_slider).setVisibility(View.GONE);
                }
            }
        });

        ImageButton circumButton = (ImageButton) findViewById(R.id.circum_info_button);
        circumButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(Tree_Info_First_NearbyTree.this);
                builder.setTitle("What does DBH mean?");
                builder.setMessage("DBH is an acronym for Diameter at Breast Height, where breast height is 4.5 feet above the ground. If multiple numbers are listed, it means that the tree splits below breast height.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        BottomNavigationView navView = findViewById(R.id.tree_info_first_menu);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.tree_info_first_menu);

    }

    public void updateTree() {

        if (Tree_Info_First.nearTree.getDataSource() == "User" && Tree_Info_First.nearTree.getPics("Image Tree") != null) {
            byte[] encodeByte = Base64.decode(Tree_Info_First.nearTree.getPics("Image Tree").toString(), Base64.DEFAULT);
            BitmapDrawable ob = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length));
            ((ConstraintLayout) findViewById(R.id.parent_constraint)).setBackground(ob);
        }
        if (Tree_Info_First.nearTree.getPics("User pic") != null) {
            byte[] encodeByte = Base64.decode(Tree_Info_First.nearTree.getPics("User pic").toString(), Base64.DEFAULT);
            BitmapDrawable ob = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length));
            ((ConstraintLayout) findViewById(R.id.parent_constraint)).setBackground(ob);
        }

        String commonName = Tree_Info_First.nearTree.getCommonName();
        TextView commonNameText = (TextView) findViewById(R.id.CommonName);
        if (commonName != null) {
            commonNameText.setText(commonName);
            commonName = commonName.replaceAll("\\s","_");
            commonName = commonName.toLowerCase();
            try{
                int id = this.getResources().getIdentifier(commonName, "mipmap", Tree_Info_First_NearbyTree.this.getPackageName());
                Drawable d = getDrawable(id);
                ((ImageView) findViewById(R.id.tree_info_first_background)).setImageDrawable(d);
            }catch (Exception e){

            }

        } else {
            //commonNameText.setText("Common name: " + "Unavailable");
            commonNameText.setVisibility(View.GONE);
        }

        String scientificName = Tree_Info_First.nearTree.getScientificName();
        TextView scientificNameText = (TextView) findViewById(R.id.scientificName);
        if (scientificName != null && scientificName != "") {
            scientificNameText.setText(Tree_Info_First.nearTree.getScientificName());
        } else {
            // scientificNameText.setText("Scientific name: " + "Unavailable");
            scientificNameText.setVisibility(View.GONE);
        }

        String treeID = Tree_Info_First.nearTree.getID();
        TextView treeIdText = (TextView) findViewById(R.id.treeid);
        if (treeID != null) {
            treeIdText.setText(treeID);
        } else {
            //treeIdText.setText("Tree ID: " + "Unavailable");
            findViewById(R.id.idTitle).setVisibility(View.GONE);
            treeIdText.setVisibility(View.GONE);
        }

        ArrayList<Object> dbhArray = Tree_Info_First.nearTree.getDBHArray();
        if(dbhArray.size() > 0) {
            String dbhList = "";
            for (Object dbh : dbhArray) {
                dbhList += dbh + "\" \n";
            }
            if (dbhList != "") {
                dbhList = dbhList.substring(0, dbhList.length() - 1);
                ((TextView) findViewById(R.id.dbh)).setText(dbhList);
            } else {
                //dbhText.setText("DBH: " + "Unavailable");
                findViewById(R.id.dbhLayout).setVisibility(View.GONE);
                //            dbhText.setVisibility(View.GONE);
            }
        }else {
            try {
                Double dbh = Tree_Info_First.nearTree.getCurrentDBH();
                TextView dbhText = (TextView) findViewById(R.id.dbh);
                if (dbh != null) {
                    dbhText.setText(dbh + "\"");
                } else {
                    //dbhText.setText("DBH: " + "Unavailable");
                    findViewById(R.id.dbhLayout).setVisibility(View.GONE);
//            dbhText.setVisibility(View.GONE);
                }
            }catch (Exception e ){
                findViewById(R.id.dbhLayout).setVisibility(View.GONE);
            }

        }

        Double latitude = Tree_Info_First.nearTree.getLocation().getLatitude();
        TextView latitudeText = (TextView) findViewById(R.id.latitude);
        if(latitude != null) {
            latitudeText.setText(latitude.toString());
        }else{
            //gpsLocationText.setText("GPS location: " + "Unavailable");
            findViewById(R.id.latitudeTitle).setVisibility(View.GONE);
            latitudeText.setVisibility(View.GONE);
        }

        Double longitude = Tree_Info_First.nearTree.getLocation().getLongitude();
        TextView longitudeText = (TextView) findViewById(R.id.longitude);
        if(latitude != null) {
            longitudeText.setText(longitude.toString());
        }else{
            //gpsLocationText.setText("GPS location: " + "Unavailable");
            findViewById(R.id.longitudeTitle).setVisibility(View.GONE);
            longitudeText.setVisibility(View.GONE);
        }

////        Object assetValue = Tree_Info_First.nearTree.getInfo("Tree asset value");
//        TextView assetValueText = (TextView) findViewById(R.id.treeAssetValue);
////        if(assetValue != null) {
////            assetValueText.setText(assetValue+"");
////        }else{
////            //assetValueText.setText("Asset value: " + "Unavailable");
//            findViewById(R.id.assetTitle).setVisibility(View.GONE);
//            assetValueText.setVisibility(View.GONE);
////        }


        ArrayList<String> notesArray = Tree_Info_First.nearTree.getNotesArray();
        if(notesArray.size() > 0){
            findViewById(R.id.view_notes_button).setVisibility(View.VISIBLE);
            for(String note : notesArray){
                TextView tv = new TextView(this);
                tv.setText(note);
                tv.setTextSize(18f);
                tv.setTextColor(getResources().getColor(R.color.black));
                LinearLayout linearLayout = findViewById(R.id.notes_linear);
                linearLayout.addView(tv);
            }
        }else{
            findViewById(R.id.view_notes_button).setVisibility(View.GONE);
            findViewById(R.id.notes_scroller).setVisibility(View.GONE);
        }

        HashMap<String, ArrayList<String>> photosMap = Tree_Info_First.nearTree.getTreePhotos();
        try {
            for (String photo : photosMap.get("full")) {
                images.add(photo);
            }
        } catch (Exception e) {
        }
        try {
            for (String photo : photosMap.get("leaf")) {
                images.add(photo);
            }
        } catch (Exception e) {
        }
        try {
            for (String photo : photosMap.get("bark")) {
                images.add(photo);
            }
        } catch (Exception e) {
        }
        if (images.size() > 0) {
            ImageSlider imageSlider = findViewById(R.id.view_photos_slider);
            SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            imageSlider.setAdapter(mSectionsPagerAdapter);
        } else {
            ((Button) findViewById(R.id.view_photos_button)).setVisibility(View.GONE);
        }



    }

    public void patchTreeDataSwitch(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Set<String> sources = prefs.getStringSet("databasesUsedSelector",new HashSet<String>());
        // sources.remove(Tree_Info_First.nearTree.getDataSource());
        DataSource ds;
        if(Tree_Info_First.nearTree.getDataSource() == "ExtendedCoH"){
            ds = new ExtendedCoHDataSource();
        }else if(Tree_Info_First.nearTree.getDataSource() == "iTreeData"){
            ds = new ITreeDataSource();
        }else if(Tree_Info_First.nearTree.getDataSource()=="HopeCollegeData" ){
            ds = new HopeCollegeDataSource();
        }else if (Tree_Info_First.nearTree.getDataSource()== "CoHdatabase") {
            ds = new CityOfHollandDataSource();
        }else if(Tree_Info_First.nearTree.getDataSource()== "AllUserDB"){
            ds = new AllUsersDataSource();
        }else{
            ds = MainActivity.userTreeDataSourceGlobal;
        }
        ds.initialize(Tree_Info_First_NearbyTree.this,null);
        Tree tree = ds.search(Tree_Info_First.nearTree.getLocation());
        if(tree != null && tree.isFound()){
            ds.patchData(tree);
        }


//        for (String source : sources) {
//            Log.d("MainActivity", "Searching.  Trying: "+source);
//            DataSource ds;
//            if(source.equals("HopeCollegeDataSource")){
//                ds = new HopeCollegeDataSource();
//            }else if(source.equals("CityOfHollandDataSource")) {
//                ds = new CityOfHollandDataSource();
//            }else if(source.equals("ExtendedCoHDataSource")){
//                ds = new ExtendedCoHDataSource();
//            }else if (source.equals("UserTreeDataSource")) {
//                ds = MainActivity.userTreeDataSourceGlobal;
//            }else{
//                ds = new ITreeDataSource();
//            }
//            ds.initialize(Tree_Info_First.this,null);
//            Tree tree = ds.search(Tree_Info_First.nearTree.getLocation());
//
//            if(tree != null && tree.isFound()){
//                ds.patchData(tree);
//            }
//        }

    }

    public void patchTreeData(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Set<String> sources = prefs.getStringSet("databasesUsedSelector",new HashSet<String>());
        // sources.remove(Tree_Info_First.nearTree.getDataSource());
        DataSource ds;
        if(Tree_Info_First.nearTree.getDataSource() == "ExtendedCoH"){
            ds = new ExtendedCoHDataSource();
        }else if(Tree_Info_First.nearTree.getDataSource() == "iTreeData"){
            ds = new ITreeDataSource();
        }else if(Tree_Info_First.nearTree.getDataSource()=="HopeCollegeData" ){
            ds = new HopeCollegeDataSource();
        }else if (Tree_Info_First.nearTree.getDataSource()== "CoHdatabase") {
            ds = new CityOfHollandDataSource();
        }else if(Tree_Info_First.nearTree.getDataSource()== "AllUserDB"){
            ds = new AllUsersDataSource();
        }else{
            ds = MainActivity.userTreeDataSourceGlobal;
        }
        ds.initialize(Tree_Info_First_NearbyTree.this,null);
        Tree tree = ds.search(Tree_Info_First.nearTree.getLocation());
        if(tree != null && tree.isFound()){
            ds.patchData(tree);
        }


//        for (String source : sources) {
//            Log.d("MainActivity", "Searching.  Trying: "+source);
//            DataSource ds;
//            if(source.equals("HopeCollegeDataSource")){
//                ds = new HopeCollegeDataSource();
//            }else if(source.equals("CityOfHollandDataSource")) {
//                ds = new CityOfHollandDataSource();
//            }else if(source.equals("ExtendedCoHDataSource")){
//                ds = new ExtendedCoHDataSource();
//            }else if (source.equals("UserTreeDataSource")) {
//                ds = MainActivity.userTreeDataSourceGlobal;
//            }else{
//                ds = new ITreeDataSource();
//            }
//            ds.initialize(Tree_Info_First.this,null);
//            Tree tree = ds.search(Tree_Info_First.nearTree.getLocation());
//
//            if(tree != null && tree.isFound()){
//                ds.patchData(tree);
//            }
//        }

    }
    private class AddNotesEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
//            Intent intentA = new Intent(Cereal_Box_Activity.this, AddNotesActivity.class);
//            startActivity(intentA);
            ShowPopup(v);
        }
    }

    public void ShowPopup(View v){
        TextView txtclose;
        Button buttonSubmit;
        ImageButton imageButton;
        myDialog.setContentView(R.layout.add_notes_display);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        buttonSubmit = (Button) myDialog.findViewById(R.id.add_notes_button);
        imageButton = (ImageButton) myDialog.findViewById(R.id.user_add_tree_pic);
        imageButton.setOnClickListener(new addImageEvent());
        txtclose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                myDialog.dismiss();
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public class addImageEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            picAppear = findViewById(R.id.user_add_tree_pic_appear_info);

            if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMS, REQUEST_ID);
                }
            }else{
                onRequestPermissionsResult(REQUEST_ID,PERMS,PERMISSION_ALL);
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (imageTakeIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(imageTakeIntent, REQUEST_IMGAGE_CAPTURE);
            }
        }else{
            Toast.makeText(getBaseContext(), "Permissions are not right", Toast.LENGTH_SHORT).show();
        }
    }
    public static Bitmap bmp;

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMGAGE_CAPTURE && resultCode==RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byteArray = stream.toByteArray();
            //   picAppear.setImageBitmap(imageBitmap);

            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            BitmapDrawable dBmp = new BitmapDrawable(getResources(), bmp);
            ImageView image = (ImageView) findViewById(R.id.user_add_tree_pic_appear_info);
            //image.setImageBitmap(bmp);
            Tree_Info_First.nearTree.addPics("User pic", Base64.encodeToString(byteArray, Base64.DEFAULT));
            myDialog.dismiss();
            Toast.makeText(getBaseContext(), "Photo added. ", Toast.LENGTH_LONG).show();
            ((ConstraintLayout) findViewById(R.id.parent_constraint)).setBackground(dBmp);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onStop(){
        super.onStop();
//        finish();
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {

        }

        public static NotificationsActivity.PlaceholderFragment newInstance(byte[] pic) {

            NotificationsActivity.PlaceholderFragment fragment = new NotificationsActivity.PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt("index", pic);
//            fragment.setArguments(args);

            Bundle args1 = new Bundle();
            args1.putByteArray("index",pic);
            fragment.setArguments(args1);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main3, container, false);
            Bundle args = getArguments();
            int index = args.getInt("index", 0);
            byte[] index1 = args.getByteArray("index");
            ImageView imageView=(ImageView)rootView.findViewById(R.id.image);
//            imageView.setImageResource(index1);
            Bitmap bitmap = BitmapFactory.decodeByteArray(index1,0,index1.length);

            imageView.setImageBitmap(bitmap);

            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {   // adapter to set in ImageSlider

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
//            String byteConvert = images.get(position);
            byte [] encodeByte=Base64.decode(images.get(position),Base64.DEFAULT);
            return NotificationsActivity.PlaceholderFragment.newInstance(encodeByte);
        }

        @Override
        public int getCount() {

            return images.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return null;
        }
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY){
            if(event2.getX()>event1.getX()){
                //left to right swipe
                MainActivity.banana = Tree_Info_First.nearTree;
                Intent intent2 = new Intent(Tree_Info_First_NearbyTree.this, Pie_Chart_Activity_NearbyTree.class);
                finish();

                startActivity(intent2);

            }else if(event2.getX()<event1.getX()){
                //right to left swipe
                MainActivity.banana = Tree_Info_First.nearTree;
                Intent intent1 = new Intent(Tree_Info_First_NearbyTree.this, Cereal_Box_Activity_NearbyTree.class);
                finish();
                startActivity(intent1);
            }
            return true;
        }
    }
}
