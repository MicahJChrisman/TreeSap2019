package com.example.treesapv2new;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class Tree_Pic_Activity extends AppCompatActivity {
    private ImageView mimagesView;
    private static final int REQUSET_IMGAGE_CAPTURE = 101;
    private byte[] byteArrayFull;
    private static final int[] PERMISSION_ALL = new int[0];

    private static final String[] PERMS = {
            Manifest.permission.CAMERA,
    };
    private static final int REQUEST_ID = 1;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.camera_full_tree);




        Button b = (Button) findViewById(R.id.next_pic_full);
        b.setOnClickListener(new NextEvent());

        ImageButton imageClickedbutton = (ImageButton) findViewById(R.id.add_full_pic);
        imageClickedbutton.setOnClickListener(new addImageEvent());

        TextView skip = (TextView) findViewById(R.id.skip_full_tree);
        skip.setOnClickListener(new SkipEvent());

        findViewById(R.id.back_full_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((TextView) findViewById(R.id.camera_disappear_full)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.camera_disappear_full).setVisibility(View.GONE);
                findViewById(R.id.camera_appear_full).setVisibility(View.GONE);
                findViewById(R.id.next_pic_full).setVisibility(View.GONE);
                byteArrayFull = null;
                findViewById(R.id.skip_full_tree).setVisibility(View.VISIBLE);
            }
        });

        TextView txtclose = (TextView) findViewById(R.id.full_pic_close);
        txtclose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(Tree_Pic_Activity.this);
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
                        Intent intentA = new Intent(Tree_Pic_Activity.this, MainActivity.class);
                        startActivity(intentA);
                        finish();
                    }
                });
                builder.show();
            }
        });
    }

    private class addImageEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            mimagesView = findViewById(R.id.camera_appear_full);
            findViewById(R.id.camera_appear_full).setVisibility(View.VISIBLE);
            //findViewById(R.id.next_pic_full).setVisibility(View.VISIBLE);
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
                startActivityForResult(imageTakeIntent, REQUSET_IMGAGE_CAPTURE);
            }
        }else{
            Toast.makeText(getBaseContext(), "Permissions are not right", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUSET_IMGAGE_CAPTURE && resultCode==RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byteArrayFull = stream.toByteArray();
            mimagesView.setImageBitmap(imageBitmap);
            findViewById(R.id.next_pic_full).setVisibility(View.VISIBLE);
            findViewById(R.id.camera_appear_full).setVisibility(View.VISIBLE);
            findViewById(R.id.camera_disappear_full).setVisibility(View.VISIBLE);
        }
    }

    private class SkipEvent implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Tree_Pic_Activity.this);
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
                    Intent intentA = new Intent(Tree_Pic_Activity.this, Tree_Other_Info_Activity.class);
                    Bundle extras = getIntent().getExtras();
                    if (extras != null) {
                        String lat_value = extras.getString("lat_value");
                        String long_value = extras.getString("long_value");
                        byte[] byteArray = extras.getByteArray("bark_pic_byte_array");
                        if (byteArray != null) {
                            intentA.putExtra("bark_pic_byte_array", byteArray);
                        }
                        byte[] byteArrayLeaf = extras.getByteArray("leaf_pic_byte_array");
                        if (byteArrayLeaf != null) {
                            intentA.putExtra("leaf_pic_byte_array", byteArrayLeaf);
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
            Intent intentA = new Intent(Tree_Pic_Activity.this, Tree_Other_Info_Activity.class);
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String lat_value = extras.getString("lat_value");
                String long_value = extras.getString("long_value");
                byte[] byteArray = extras.getByteArray("bark_pic_byte_array");
                if (byteArray != null) {
                    intentA.putExtra("bark_pic_byte_array", byteArray);
                }
                byte[] byteArrayLeaf = extras.getByteArray("leaf_pic_byte_array");
                if (byteArrayLeaf != null) {
                    intentA.putExtra("leaf_pic_byte_array", byteArrayLeaf);
                }
                intentA.putExtra("full_pic_byte_array", byteArrayFull);
                intentA.putExtra("lat_value", lat_value);
                intentA.putExtra("long_value", long_value);
            }
            startActivity(intentA);
        }
    }}
