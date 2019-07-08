package com.example.treesapv2new;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shivam.library.imageslider.ImageSlider;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Tree_Pic_Activity extends AppCompatActivity {
    private ImageView mimagesView;
    private static final int REQUSET_IMGAGE_CAPTURE = 101;
    private byte[] byteArrayFull;
    private static final int[] PERMISSION_ALL = new int[0];

    private static final String[] PERMS = {
            Manifest.permission.CAMERA,
    };
    private static final int REQUEST_ID = 1;
    private int REQUEST_EXIT = 9000;
    private int RESULT_DONE = 4000;
    private boolean CAMERA_ACTIVITY = false;

    ImageSlider slider;
    SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
    private ArrayList<byte[]> images = new ArrayList<byte[]>();
    private ArrayList<String> imagesString = new ArrayList<String>();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.camera_full_tree);




        Button b = (Button) findViewById(R.id.next_pic_full);
        b.setOnClickListener(new NextEvent());

        Button imageClickedbutton = (Button) findViewById(R.id.add_full_pic);
        imageClickedbutton.setOnClickListener(new addImageEvent());

        TextView skip = (TextView) findViewById(R.id.skip_full_tree);
        skip.setOnClickListener(new SkipEvent());

        findViewById(R.id.back_full_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                Intent intentA = new Intent(Tree_Pic_Activity.this, Tree_Leaf_Activity.class);
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String lat_value = extras.getString("lat_value");
                    String long_value = extras.getString("long_value");
//                    byte[] byteArray = extras.getByteArray("bark_pic_byte_array");
//                    if (byteArray != null) {
//                        intentA.putExtra("bark_pic_byte_array", byteArray);
//                    }
//                    byte[] byteArrayLeaf = extras.getByteArray("leaf_pic_byte_array");
//                    if (byteArrayLeaf != null) {
//                        intentA.putExtra("leaf_pic_byte_array", byteArrayLeaf);
//                    }
                    intentA.putExtra("lat_value", lat_value);
                    intentA.putExtra("long_value", long_value);
                }
//                    startActivity(intentA);
                startActivityForResult(intentA,REQUEST_EXIT);
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

        ImageView txtclose = (ImageView) findViewById(R.id.full_pic_close);
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
//                        Intent intentA = new Intent(Tree_Pic_Activity.this, MainActivity.class);
//                        startActivity(intentA);
                        setResult(RESULT_DONE,null);
                        finish();
                    }
                });
                builder.show();
            }
        });
    }


    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {

        }

        public static PlaceholderFragment newInstance(byte[] pic) {

            PlaceholderFragment fragment = new PlaceholderFragment();
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
            return Tree_Bark_Activity.PlaceholderFragment.newInstance(Base64.decode(imagesString.get(position), Base64.DEFAULT));
        }

        @Override
        public int getCount() {
            return imagesString.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return null;
        }
    }

    private class addImageEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            mimagesView = findViewById(R.id.camera_appear_full);
//            findViewById(R.id.camera_appear_full).setVisibility(View.VISIBLE);
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
                CAMERA_ACTIVITY = true;
                startActivityForResult(imageTakeIntent, REQUSET_IMGAGE_CAPTURE);
            }
        }else{
            Toast.makeText(getBaseContext(), "Permissions are not right", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_EXIT){
            if (resultCode == RESULT_DONE) {
                this.finish();

            }
        }
        if(requestCode == REQUSET_IMGAGE_CAPTURE && resultCode==RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byteArrayFull = stream.toByteArray();
            mimagesView.setImageBitmap(imageBitmap);
            findViewById(R.id.next_pic_full).setVisibility(View.VISIBLE);

            String imageBase64 = Base64.encodeToString(byteArrayFull, Base64.DEFAULT);
            imagesString.add(imageBase64);
            mSectionsPagerAdapter.notifyDataSetChanged();



            if(imagesString.size() ==1) {
                slider = (ImageSlider) findViewById(R.id.pager);

                slider.setAdapter(mSectionsPagerAdapter);
            }

            slider.setIndicatorsSize(0);
            ((Button) findViewById(R.id.add_full_pic)).setText("Take another picture");
            if(imagesString.size() > 1){
                findViewById(R.id.swipe_for_pics).setVisibility(View.VISIBLE);
            }
            if(imagesString.size() == 5){
                ((Button) findViewById(R.id.add_full_pic)).setVisibility(View.GONE);
            }
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
//                        ArrayList<String> byteArray = extras.getStringArrayList("bark_pic_byte_array");
//                        if (byteArray != null) {
//                            intentA.putExtra("bark_pic_byte_array", byteArray);
//                        }
//                        ArrayList<String> byteArrayLeaf = extras.getStringArrayList("leaf_pic_byte_array");
//                        if (byteArrayLeaf != null) {
//                            intentA.putExtra("leaf_pic_byte_array", byteArrayLeaf);
//                        }
                        intentA.putExtra("lat_value", lat_value);
                        intentA.putExtra("long_value", long_value);
                    }
//                    startActivity(intentA);
                    MainActivity.storedImages.put("full", imagesString);
                    startActivityForResult(intentA,REQUEST_EXIT);
                }
            });
            builder.show();
        }
    }

    public void onStop(){
        super.onStop();
        if(!CAMERA_ACTIVITY) {
            setResult(RESULT_DONE, null);
            finish();
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
//                ArrayList<String> byteArray = extras.getStringArrayList("bark_pic_byte_array");
//                if (byteArray != null) {
//                    intentA.putExtra("bark_pic_byte_array", byteArray);
//                }
//                ArrayList<String> byteArrayLeaf = extras.getStringArrayList("leaf_pic_byte_array");
//                if (byteArrayLeaf != null) {
//                    intentA.putExtra("leaf_pic_byte_array", byteArrayLeaf);
//                }
//                ArrayList<String> images_to_String = new ArrayList<String>();
//                for(byte[] image : images){
//                    String temp= Base64.encodeToString(image, Base64.DEFAULT);
//                    images_to_String.add(temp);
////                    images_to_String.add(new String(image));
//                }
//                intentA.putExtra("full_pic_byte_array", images_to_String);
                intentA.putExtra("lat_value", lat_value);
                intentA.putExtra("long_value", long_value);
            }
//            startActivity(intentA);
            MainActivity.storedImages.put("full",imagesString);
            startActivityForResult(intentA,REQUEST_EXIT);
        }
    }}
