package com.example.treesapv2new.display;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.treesapv2new.Cereal_Box_Activity;
import com.example.treesapv2new.MainActivity;
import com.example.treesapv2new.R;
import com.example.treesapv2new.Tree_Info_First;

import java.io.ByteArrayOutputStream;

public class AddNotesActivity extends AppCompatActivity {
    private ImageView mimagesView;
    private static final int REQUSET_IMGAGE_CAPTURE = 101;
    private byte[] byteArray;

    private static final String[] PERMS = {
            Manifest.permission.CAMERA,
    };
    private static final int REQUEST_ID = 1;
    private static final int[] PERMISSION_ALL = new int[0];
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(null);

//        TextView prevNotes = (TextView) findViewById(R.id.previousNotesText);
//        prevNotes.setVisibility(View.INVISIBLE);

        setContentView(R.layout.add_notes_display);
//        if (Cereal_Box_Activity.bmp != null) {
//            ImageView image = (ImageView) findViewById(R.id.user_add_tree_pic_appear);
//            image.setImageBitmap(Cereal_Box_Activity.bmp);
//        }
        Button button = (Button) findViewById(R.id.add_notes_button);
        button.setOnClickListener(new AddNotesEvent());


        ImageButton imageButton = (ImageButton) findViewById(R.id.user_add_tree_pic);
        imageButton.setOnClickListener(new addImageEvent());

    }

    private class AddNotesEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            //This is where we put the code to save it to the database when people add a note
            finish();
        }
    }

    private class addImageEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            mimagesView = findViewById(R.id.user_add_tree_pic_appear);
            findViewById(R.id.user_add_tree_pic_appear).setVisibility(View.VISIBLE);
            //findViewById(R.id.next_pic_bark).setVisibility(View.VISIBLE);
            if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMS, REQUEST_ID);
                }
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
            byteArray = stream.toByteArray();
            mimagesView.setImageBitmap(imageBitmap);

            MainActivity.banana.addPics("User pic",Base64.encodeToString(byteArray, Base64.DEFAULT));
        }
    }

}
