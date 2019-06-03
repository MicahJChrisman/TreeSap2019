package com.example.treesapv2new;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class Tree_Leaf_Activity extends AppCompatActivity {
    private ImageView mimagesView;
    private static final int REQUSET_IMGAGE_CAPTURE = 101;
    private byte[] byteArrayLeaf;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.camera_leaf_tree);

        Button b = (Button) findViewById(R.id.next_pic_leaf);
        b.setOnClickListener(new NextEvent());

        ImageButton imageClickedbutton = (ImageButton) findViewById(R.id.add_leaf_pic);
        imageClickedbutton.setOnClickListener(new addImageEvent());

        TextView skip = (TextView) findViewById(R.id.skip_leaf_tree);
        skip.setOnClickListener(new SkipEvent());

        TextView txtclose = (TextView) findViewById(R.id.leaf_pic_close);
        txtclose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(Tree_Leaf_Activity.this);
                builder.setCancelable(true);
                builder.setTitle("Discard your tree?");
                builder.setMessage("This will get rid of the data you entered.");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Discard Tree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intentA = new Intent(Tree_Leaf_Activity.this, MainActivity.class);
                        startActivity(intentA);
                    }
                });
                builder.show();
            }
        });
    }

    private class addImageEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            mimagesView = findViewById(R.id.camera_appear_leaf);
            findViewById(R.id.camera_appear_leaf).setVisibility(View.VISIBLE);
            findViewById(R.id.next_pic_leaf).setVisibility(View.VISIBLE);
            Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(imageTakeIntent.resolveActivity(getPackageManager()) != null){
                startActivityForResult(imageTakeIntent,REQUSET_IMGAGE_CAPTURE);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUSET_IMGAGE_CAPTURE && resultCode==RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byteArrayLeaf = stream.toByteArray();
            mimagesView.setImageBitmap(imageBitmap);
        }
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
                    Bundle extras = getIntent().getExtras();
                    if (extras != null) {
                        String lat_value = extras.getString("lat_value");
                        String long_value = extras.getString("long_value");
                        byte[] byteArray = extras.getByteArray("bark_pic_byte_array");
                        if (byteArray != null) {
                            intentA.putExtra("bark_pic_byte_array", byteArray);
                        }
                        intentA.putExtra("lat_value", lat_value);
                        intentA.putExtra("long_value", long_value);
                    }
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
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String lat_value = extras.getString("lat_value");
                String long_value = extras.getString("long_value");
                byte[] byteArray = extras.getByteArray("bark_pic_byte_array");
                intentA.putExtra("bark_pic_byte_array", byteArray);
                intentA.putExtra("lat_value", lat_value);
                intentA.putExtra("long_value", long_value);
                intentA.putExtra("leaf_pic_byte_array", byteArrayLeaf);
            }
            startActivity(intentA);
        }
    }}
