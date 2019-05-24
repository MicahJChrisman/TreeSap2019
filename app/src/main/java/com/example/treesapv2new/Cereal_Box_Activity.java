package com.example.treesapv2new;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.HopeCollegeDataSource;
import com.example.treesapv2new.display.AddNotesActivity;
import com.example.treesapv2new.display.DisplayMethod;
import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.model.TreeLocation;

import java.util.List;


public class Cereal_Box_Activity extends AppCompatActivity {
//    TreeLocation testing = new TreeLocation(42.7878,-86.1057);
    String sentString;
    Dialog myDialog;

//    public Cereal_Box_Activity(String sentString){
//        this.sentString = sentString;
//    }

    private GestureDetectorCompat gestureObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_cereal_box);

        TextView eventer = (TextView) findViewById(R.id.treeNamePlease);
        eventer.setText(MainActivity.banana.getCommonName());

        TextView eventer1 = (TextView) findViewById(R.id.latitudetree);
        eventer1.setText(String.valueOf(MainActivity.banana.getLocation().getLatitude()));

        TextView eventer2= (TextView) findViewById(R.id.longitudetree);
        eventer2.setText(String.valueOf(MainActivity.banana.getLocation().getLongitude()));

        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        myDialog = new Dialog(this);

        ImageButton button = (ImageButton) findViewById(R.id.add);
        button.setOnClickListener(new AddNotesEvent());
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
                Intent intent1 = new Intent(Cereal_Box_Activity.this, Tree_Info_First.class);
                finish();
                startActivity(intent1);
            }else if(event2.getX()<event1.getX()){
                //right to left swipe
                Intent intent2 = new Intent(Cereal_Box_Activity.this, Pie_Chart_Activity.class);
                finish();
                startActivity(intent2);
            }
            return true;
        }
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
}