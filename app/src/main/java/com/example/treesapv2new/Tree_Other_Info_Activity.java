package com.example.treesapv2new;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.treesapv2new.datasource.UserTreeDataSource;

public class Tree_Other_Info_Activity extends AppCompatActivity {
    String[] passedArray = new String[10];



    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.add_tree_other_info);

        findViewById(R.id.back_final_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String lat_value = extras.getString("lat_value");
            String long_value = extras.getString("long_value");
            TextView kevin = (TextView) findViewById(R.id.lat_thing);
            //kevin.setText(lat_value);
            TextView carl = (TextView) findViewById(R.id.long_thing);
            //carl.setText(long_value);

            byte[] byteArray = extras.getByteArray("bark_pic_byte_array");
            if(byteArray != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                ImageView image = (ImageView) findViewById(R.id.show_bark_pic);
                //image.setImageBitmap(bmp);
                passedArray[7] = Base64.encodeToString(byteArray, Base64.DEFAULT);
            }
            byte[] byteArrayLeaf = extras.getByteArray("leaf_pic_byte_array");
            if(byteArrayLeaf != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArrayLeaf, 0, byteArrayLeaf.length);
                ImageView image = (ImageView) findViewById(R.id.show_leaf_pic);
                //image.setImageBitmap(bmp);
                passedArray[8] = Base64.encodeToString(byteArrayLeaf, Base64.DEFAULT);
            }
            byte[] byteArrayFull = extras.getByteArray("full_pic_byte_array");
            if(byteArrayFull != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArrayFull, 0, byteArrayFull.length);
                ImageView image = (ImageView) findViewById(R.id.show_tree_pic);
                //image.setImageBitmap(bmp);
                passedArray[9] = Base64.encodeToString(byteArrayFull, Base64.DEFAULT);
            }

            passedArray[4] = lat_value;
            passedArray[5] = long_value;
        }

        Button sumbitButton = (Button) findViewById(R.id.add_tree_submit);
        sumbitButton.setOnClickListener(new SubmitEvent());

        ImageButton circumButton = (ImageButton) findViewById(R.id.circum_info_button);
        circumButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(Tree_Other_Info_Activity.this);
                builder.setTitle("This is necessary to calculate the benefits of a tree.");
                builder.setMessage("Please measure the circumference at chest height.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        TextView txtclose = (TextView) findViewById(R.id.other_info_close);
        txtclose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(Tree_Other_Info_Activity.this);
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
                        Intent intentA = new Intent(Tree_Other_Info_Activity.this, MainActivity.class);
                        startActivity(intentA);
                        finish();
                    }
                });
                builder.show();
            }
        });

        String[] treeTypes = getResources().getStringArray(R.array.known_trees);
        //String[] treeTypes = getResources().getStringArray(R.array.known_trees_full);

        AutoCompleteTextView editTrees = findViewById(R.id.common_name);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,treeTypes);
        editTrees.setAdapter(adapter);
    }

    private class SubmitEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            AlertDialog.Builder builder = new AlertDialog.Builder(Tree_Other_Info_Activity.this);
            builder.setCancelable(true);
            builder.setTitle("Thank you for submitting your tree!");
            builder.setMessage("We will check over your submission and add it to the database if all looks good.");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    passedArray[0] = "0";
                    passedArray[1] = ((AutoCompleteTextView) findViewById(R.id.common_name)).getText().toString();
                    passedArray[2] = ((TextView) findViewById(R.id.scientific_name)).getText().toString();
                    if(((Switch) findViewById(R.id.units_switch)).isChecked()) {
                        Double t = Double.valueOf(((TextView) findViewById(R.id.dbh_edit)).getText().toString());
                        t=t*0.393701;

                        passedArray[3] = t.toString();
                    }else{
                        passedArray[3] = ((TextView) findViewById(R.id.dbh_edit)).getText().toString();
                    }
                    passedArray[6] = ((TextView) findViewById(R.id.notes_about_tree)).getText().toString();
                    dialog.dismiss();
                    MainActivity.userTreeDataSourceGlobal.initialize(Tree_Other_Info_Activity.this,null);
                    MainActivity.userTreeDataSourceGlobal.addTree(passedArray);
                    Intent intentA = new Intent(Tree_Other_Info_Activity.this, MainActivity.class);
                    startActivity(intentA);
                }
            });
            builder.show();
        }
    }
}
