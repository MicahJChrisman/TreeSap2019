package com.example.treesapv2new;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Tree_Pic_Activity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.camera_full_tree);

        Button b = (Button) findViewById(R.id.next_pic_full);
        b.setOnClickListener(new NextEvent());
    }

    private class NextEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intentA = new Intent(Tree_Pic_Activity.this, Tree_Pic_Activity.class);
            startActivity(intentA);
        }
    }}
