package com.example.treesapv2new;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.example.treesapv2new.display.AddNotesActivity;

public class Tree_Info_First extends AppCompatActivity {

    private GestureDetectorCompat gestureObject;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.activity_tree_info_first);

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
