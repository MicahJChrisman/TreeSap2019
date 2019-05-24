package com.example.treesapv2new;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.treesapv2new.display.AddNotesActivity;

public class Tree_Info_First extends AppCompatActivity {

    private GestureDetectorCompat gestureObject;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.activity_tree_info_first);

        String commonName = MainActivity.banana.getCommonName();
        TextView commonNameText = (TextView) findViewById(R.id.CommonName);
        if(commonName != null) {
            commonNameText.setText("Common name: " + commonName);
        }else{
            //commonNameText.setText("Common name: " + "Unavailable");
            commonNameText.setVisibility(View.GONE);
        }

        String scientificName = MainActivity.banana.getScientificName();
        TextView scientificNameText = (TextView) findViewById(R.id.scientificName);
        if(scientificName != null) {
            scientificNameText.setText("Scientific name: " + MainActivity.banana.getScientificName());
        }else{
            // scientificNameText.setText("Scientific name: " + "Unavailable");
            scientificNameText.setVisibility(View.GONE);
        }

        String treeID = MainActivity.banana.getID();
        TextView treeIdText = (TextView) findViewById(R.id.treeid);
        if(treeID != null) {
            treeIdText.setText("Tree ID: " + treeID);
        }else{
            //treeIdText.setText("Tree ID: " + "Unavailable");
            treeIdText.setVisibility(View.GONE);
        }

        Double dbh = MainActivity.banana.getCurrentDBH();
        TextView dbhText = (TextView) findViewById(R.id.dbh);
        if(dbh != null) {
            dbhText.setText("DBH: " + dbh);
        }else{
            //dbhText.setText("DBH: " + "Unavailable");
            dbhText.setVisibility(View.GONE);
        }

        String gpsLocation = MainActivity.banana.getLocation().getLatitude() + ", " + MainActivity.banana.getLocation().getLongitude();
        TextView gpsLocationText = (TextView) findViewById(R.id.gpsLocation);
        if(gpsLocation != null) {
            gpsLocationText.setText("GPS location: " + gpsLocation);
        }else{
            //gpsLocationText.setText("GPS location: " + "Unavailable");
            gpsLocationText.setVisibility(View.GONE);
        }

        Object assetValue = MainActivity.banana.getInfo("Tree asset value");
        TextView assetValueText = (TextView) findViewById(R.id.treeAssetValue);
        if(assetValue != null) {
            assetValueText.setText("Asset value: " + assetValue);
        }else{
            //assetValueText.setText("Asset value: " + "Unavailable");
            assetValueText.setVisibility(View.GONE);
        }

        String otherInfo = MainActivity.banana.getAllInfo();
        TextView otherInfoText = (TextView) findViewById(R.id.otherInfo);
        if(otherInfo != null) {
            otherInfoText.setVisibility(0);
            otherInfoText.setText("Other info: \n" + otherInfo);
        }else{
            //otherInfoText.setText("Other info: \n" + "\tNo other information");
            otherInfoText.setVisibility(View.GONE);
        }

        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        ImageButton button = (ImageButton) findViewById(R.id.add3);
        button.setOnClickListener(new AddNotesEvent());
    }

    private class AddNotesEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intentA = new Intent(Tree_Info_First.this, AddNotesActivity.class);
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
