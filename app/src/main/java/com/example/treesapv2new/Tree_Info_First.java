package com.example.treesapv2new;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.treesapv2new.datasource.CityOfHollandDataSource;
import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.ExtendedCoHDataSource;
import com.example.treesapv2new.datasource.HopeCollegeDataSource;
import com.example.treesapv2new.datasource.ITreeDataSource;
import com.example.treesapv2new.display.AddNotesActivity;
import com.example.treesapv2new.model.Tree;

import java.util.HashSet;
import java.util.Set;

public class Tree_Info_First extends AppCompatActivity {
    Dialog myDialog;
    private GestureDetectorCompat gestureObject;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.activity_tree_info_first);

        myDialog = new Dialog(this);

//        patchTreeData();

        String commonName = MainActivity.banana.getCommonName();
        TextView commonNameText = (TextView) findViewById(R.id.CommonName);
        if(commonName != null) {
            commonNameText.setText(commonName);
        }else{
            //commonNameText.setText("Common name: " + "Unavailable");
            commonNameText.setVisibility(View.GONE);
        }

        String scientificName = MainActivity.banana.getScientificName();
        TextView scientificNameText = (TextView) findViewById(R.id.scientificName);
        if(scientificName != null) {
            scientificNameText.setText(MainActivity.banana.getScientificName());
        }else{
            // scientificNameText.setText("Scientific name: " + "Unavailable");
            scientificNameText.setVisibility(View.GONE);
        }

        String treeID = MainActivity.banana.getID();
        TextView treeIdText = (TextView) findViewById(R.id.treeid);
        if(treeID != null) {
            treeIdText.setText(treeID);
        }else{
            //treeIdText.setText("Tree ID: " + "Unavailable");
            findViewById(R.id.idTitle).setVisibility(View.GONE);
            treeIdText.setVisibility(View.GONE);
        }

        Double dbh = MainActivity.banana.getCurrentDBH();
        TextView dbhText = (TextView) findViewById(R.id.dbh);
        if(dbh != null) {
            dbhText.setText(dbh+"");
        }else{
            //dbhText.setText("DBH: " + "Unavailable");
            findViewById(R.id.dbhTitle).setVisibility(View.GONE);
            dbhText.setVisibility(View.GONE);
        }

        String latitude = MainActivity.banana.getLocation().getLatitude()+"";
        TextView latitudeText = (TextView) findViewById(R.id.latitude);
        if(latitude != null) {
            latitudeText.setText(latitude);
        }else{
            //gpsLocationText.setText("GPS location: " + "Unavailable");
            findViewById(R.id.latitudeTitle).setVisibility(View.GONE);
            latitudeText.setVisibility(View.GONE);
        }

        String longitude = MainActivity.banana.getLocation().getLongitude() + "";
        TextView longitudeText = (TextView) findViewById(R.id.longitude);
        if(latitude != null) {
            longitudeText.setText(longitude);
        }else{
            //gpsLocationText.setText("GPS location: " + "Unavailable");
            findViewById(R.id.longitudeTitle).setVisibility(View.GONE);
            longitudeText.setVisibility(View.GONE);
        }

////        Object assetValue = MainActivity.banana.getInfo("Tree asset value");
//        TextView assetValueText = (TextView) findViewById(R.id.treeAssetValue);
////        if(assetValue != null) {
////            assetValueText.setText(assetValue+"");
////        }else{
////            //assetValueText.setText("Asset value: " + "Unavailable");
//            findViewById(R.id.assetTitle).setVisibility(View.GONE);
//            assetValueText.setVisibility(View.GONE);
////        }

        String otherInfo = MainActivity.banana.getAllInfo();
        TextView otherInfoText = (TextView) findViewById(R.id.otherInfo);
        if(otherInfo != null) {
            otherInfoText.setVisibility(View.VISIBLE);
            otherInfoText.setText(otherInfo);
        }else{
            //otherInfoText.setText("Other info: \n" + "\tNo other information");
            findViewById(R.id.otherTitle).setVisibility(View.GONE);
            otherInfoText.setVisibility(View.GONE);

        }

        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        ImageButton button = (ImageButton) findViewById(R.id.add3);
        button.setOnClickListener(new AddNotesEvent());
    }

//    public void patchTreeData(){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        Set<String> sources = prefs.getStringSet("databasesUsedSelector",new HashSet<String>());
//       // sources.remove(MainActivity.banana.getDataSource());
//        for (String source : sources) {
//            Log.d("MainActivity", "Searching.  Trying: "+source);
//            DataSource ds;
//            if(source.equals("HopeCollegeDataSource")){
//                ds = new HopeCollegeDataSource();
//            }else if(source.equals("CityOfHollandDataSource")) {
//                ds = new CityOfHollandDataSource();
//            }else if(source.equals("ExtendedCoHDataSource")){
//                ds = new ExtendedCoHDataSource();
//            }else{
//                ds = new ITreeDataSource();
//            }
//            ds.initialize(Tree_Info_First.this,null);
//            Tree tree = ds.search(MainActivity.banana.getLocation());
//
//            if(tree != null && tree.isFound()){
//                ds.patchData(tree);
//            }
//        }
//
//    }
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
        myDialog.setContentView(R.layout.add_notes_display);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        buttonSubmit = (Button) myDialog.findViewById(R.id.add_notes_button);
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
                Intent intent2 = new Intent(Tree_Info_First.this, Pie_Chart_Activity.class);
                finish();
                startActivity(intent2);

            }else if(event2.getX()<event1.getX()){
                //right to left swipe
                Intent intent1 = new Intent(Tree_Info_First.this, Cereal_Box_Activity.class);
                finish();
                startActivity(intent1);
            }
            return true;
        }
    }
}
