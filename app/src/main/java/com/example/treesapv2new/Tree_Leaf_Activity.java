package com.example.treesapv2new;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Tree_Leaf_Activity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.camera_leaf_tree);

        Button b = (Button) findViewById(R.id.next_pic_leaf);
        b.setOnClickListener(new NextEvent());

        TextView skip = (TextView) findViewById(R.id.skip_leaf_tree);
        skip.setOnClickListener(new SkipEvent());
    }

    private class SkipEvent implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Tree_Leaf_Activity.this);
            builder.setCancelable(true);
            builder.setTitle("Are you sure?");
            builder.setMessage("Skipping the pictures will make it hard to verify your tree.");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Skip Anyway", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intentA = new Intent(Tree_Leaf_Activity.this, Tree_Pic_Activity.class);
                    startActivity(intentA);
                }
            });
            builder.show();
        }
    }

    private class NextEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intentA = new Intent(Tree_Leaf_Activity.this, Tree_Pic_Activity.class);
            startActivity(intentA);
        }
    }}
