package com.example.treesapv2new;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.treesapv2new.datasource.UserTreeDataSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Tree_Other_Info_Activity extends AppCompatActivity {
    private int REQUEST_EXIT = 9000;
    private int RESULT_DONE = 4000;
    String[] passedArray = new String[10];

    String commonName;
    String scientificName;
    Double dbh;
    Double dbh2;
    Double dbh3;
    Double dbh4;
    Double lat;
    Double longit;
    String barkPic = "";
    String leafPic = "";
    String fullPic = "";
    String notes = "";
    String userID;
    Timestamp timestamp;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.add_tree_other_info);
//        initRecyclerView();


        findViewById(R.id.back_final_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                finish();
                Intent intentA = new Intent(Tree_Other_Info_Activity.this, Tree_Pic_Activity.class);
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
                    byte[] byteArrayFull = extras.getByteArray("full_pic_byte_array");
                    if (byteArrayLeaf != null) {
                        intentA.putExtra("full_pic_byte_array", byteArrayFull);
                    }
                    intentA.putExtra("lat_value", lat_value);
                    intentA.putExtra("long_value", long_value);
                }
//                    startActivity(intentA);
                startActivityForResult(intentA,REQUEST_EXIT);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Double lat_value = Double.valueOf(extras.getString("lat_value"));
            Double long_value = Double.valueOf(extras.getString("long_value"));
            TextView kevin = (TextView) findViewById(R.id.lat_thing);
            //kevin.setText(lat_value);
            TextView carl = (TextView) findViewById(R.id.long_thing);
            //carl.setText(long_value);

            byte[] byteArray = extras.getByteArray("bark_pic_byte_array");
            if(byteArray != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                ImageView image = (ImageView) findViewById(R.id.show_bark_pic);
                //image.setImageBitmap(bmp);
                barkPic = Base64.encodeToString(byteArray, Base64.DEFAULT);
                passedArray[7] = barkPic;
            }
            byte[] byteArrayLeaf = extras.getByteArray("leaf_pic_byte_array");
            if(byteArrayLeaf != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArrayLeaf, 0, byteArrayLeaf.length);
                ImageView image = (ImageView) findViewById(R.id.show_leaf_pic);
                //image.setImageBitmap(bmp);
                leafPic = Base64.encodeToString(byteArrayLeaf, Base64.DEFAULT);
                passedArray[8] = leafPic;
            }
            byte[] byteArrayFull = extras.getByteArray("full_pic_byte_array");
            if(byteArrayFull != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArrayFull, 0, byteArrayFull.length);
                ImageView image = (ImageView) findViewById(R.id.show_tree_pic);
                //image.setImageBitmap(bmp);
                fullPic = Base64.encodeToString(byteArrayFull, Base64.DEFAULT);
                passedArray[9] = fullPic;
            }
            lat = lat_value;
            longit = long_value;
//            passedArray[4] = lat_value;
//            passedArray[5] = long_value;
        }

        ((ImageButton) findViewById(R.id.add_more_dbh_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) findViewById(R.id.dbh_circum_add_2)).setVisibility(View.VISIBLE);
                ((ImageButton) findViewById(R.id.add_more_dbh_button)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout) findViewById(R.id.dbh_circum_add_3)).setVisibility(View.VISIBLE);
                        ((ImageButton) findViewById(R.id.add_more_dbh_button)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((LinearLayout) findViewById(R.id.dbh_circum_add_4)).setVisibility(View.VISIBLE);
                                ((ImageButton) findViewById(R.id.add_more_dbh_button)).setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        });


        Button sumbitButton = (Button) findViewById(R.id.add_tree_submit);
        sumbitButton.setOnClickListener(new SubmitEvent());
//
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
//
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
//                        Intent intentA = new Intent(Tree_Other_Info_Activity.this, MainActivity.class);
//                        startActivity(intentA);
                        setResult(RESULT_DONE,null);
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

        ((EditText) findViewById(R.id.dbh_edit)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String circumString = ((EditText) findViewById(R.id.dbh_edit)).getText().toString();
                if(((EditText) findViewById(R.id.dbh_edit)).hasFocus()) {
                    if (!circumString.equals("")) {
                        Double circumValue = Double.valueOf(circumString);
                        Double dbhValue = circumValue / 3.1415;
                        dbhValue = Math.round(dbhValue * 100.0) / 100.0;
                        ((EditText) findViewById(R.id.dbh_textView)).setText(dbhValue.toString());
                    } else {
                        ((EditText) findViewById(R.id.dbh_textView)).setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((EditText) findViewById(R.id.dbh_edit4)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String circumString = ((EditText) findViewById(R.id.dbh_edit4)).getText().toString();
                if(((EditText) findViewById(R.id.dbh_edit4)).hasFocus()) {
                    if (!circumString.equals("")) {
                        Double circumValue = Double.valueOf(circumString);
                        Double dbhValue = circumValue / 3.1415;
                        dbhValue = Math.round(dbhValue * 100.0) / 100.0;
                        ((EditText) findViewById(R.id.dbh_textView4)).setText(dbhValue.toString());
                    } else {
                        ((EditText) findViewById(R.id.dbh_textView4)).setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((EditText) findViewById(R.id.dbh_edit2)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String circumString = ((EditText) findViewById(R.id.dbh_edit2)).getText().toString();
                if(((EditText) findViewById(R.id.dbh_edit2)).hasFocus()) {
                    if (!circumString.equals("")) {
                        Double circumValue = Double.valueOf(circumString);
                        Double dbhValue = circumValue / 3.1415;
                        dbhValue = Math.round(dbhValue * 100.0) / 100.0;
                        ((EditText) findViewById(R.id.dbh_textView2)).setText(dbhValue.toString());
                    } else {
                        ((EditText) findViewById(R.id.dbh_textView2)).setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((EditText) findViewById(R.id.dbh_edit3)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(((EditText) findViewById(R.id.dbh_edit3)).hasFocus()) {
                    String circumString = ((EditText) findViewById(R.id.dbh_edit3)).getText().toString();
                    if (((EditText) findViewById(R.id.dbh_edit3)).hasFocus()) {
                        if (!circumString.equals("")) {
                            Double circumValue = Double.valueOf(circumString);
                            Double dbhValue = circumValue / 3.1415;
                            dbhValue = Math.round(dbhValue * 100.0) / 100.0;
                            ((EditText) findViewById(R.id.dbh_textView3)).setText(dbhValue.toString());
                        } else {
                            ((EditText) findViewById(R.id.dbh_textView3)).setText("");
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((EditText) findViewById(R.id.dbh_textView)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String circumString = ((EditText) findViewById(R.id.dbh_textView)).getText().toString();
                if(((EditText) findViewById(R.id.dbh_textView)).hasFocus()) {
                    if (!s.equals(circumString)) {
                        if (!circumString.equals("")) {
                            Double circumValue = Double.valueOf(circumString);
                            Double dbhValue = circumValue * 3.1415;
                            dbhValue = Math.round(dbhValue * 100.0) / 100.0;
                            ((EditText) findViewById(R.id.dbh_edit)).setText(dbhValue.toString());
                        } else {
                            ((EditText) findViewById(R.id.dbh_edit)).setText("");
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((EditText) findViewById(R.id.dbh_textView2)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String circumString = ((EditText) findViewById(R.id.dbh_textView2)).getText().toString();
                if(((EditText) findViewById(R.id.dbh_textView2)).hasFocus()) {
                    if (!circumString.equals("")) {
                        Double circumValue = Double.valueOf(circumString);
                        Double dbhValue = circumValue * 3.1415;
                        dbhValue = Math.round(dbhValue * 100.0) / 100.0;
                        ((EditText) findViewById(R.id.dbh_edit2)).setText(dbhValue.toString());
                    } else {
                        ((EditText) findViewById(R.id.dbh_edit2)).setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((EditText) findViewById(R.id.dbh_textView3)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String circumString = ((EditText) findViewById(R.id.dbh_textView3)).getText().toString();
                if(((EditText) findViewById(R.id.dbh_textView3)).hasFocus()) {
                    if (!circumString.equals("")) {
                        Double circumValue = Double.valueOf(circumString);
                        Double dbhValue = circumValue * 3.1415;
                        dbhValue = Math.round(dbhValue * 100.0) / 100.0;
                        ((EditText) findViewById(R.id.dbh_edit3)).setText(dbhValue.toString());
                    } else {
                        ((EditText) findViewById(R.id.dbh_edit3)).setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((EditText) findViewById(R.id.dbh_textView4)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String circumString = ((EditText) findViewById(R.id.dbh_textView4)).getText().toString();
                if(((EditText) findViewById(R.id.dbh_textView4)).hasFocus()) {
                    if (!circumString.equals("")) {
                        Double circumValue = Double.valueOf(circumString);
                        Double dbhValue = circumValue * 3.1415;
                        dbhValue = Math.round(dbhValue * 100.0) / 100.0;
                        ((EditText) findViewById(R.id.dbh_edit4)).setText(dbhValue.toString());
                    } else {
                        ((EditText) findViewById(R.id.dbh_edit4)).setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
//
//    private void initRecyclerView(){
//        RecyclerView recyclerView = findViewById(R.id.recycler_view_tree_other_info);
//        RecyclerViewTreeOtherInfo adapter = new RecyclerViewTreeOtherInfo(this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//    }

    public void onStop(){
        super.onStop();
        setResult(RESULT_DONE,null);
        finish();
    }

    public class SubmitEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            AlertDialog.Builder builder = new AlertDialog.Builder(Tree_Other_Info_Activity.this);
            builder.setCancelable(true);
            builder.setTitle("Submit tree for approval?");
            builder.setMessage("We will check over your submission and add it to the database if it is approved.");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    commonName = ((AutoCompleteTextView) findViewById(R.id.common_name)).getText().toString();
                    scientificName = ((TextView) findViewById(R.id.scientific_name)).getText().toString();
                    dbh = Double.valueOf(((EditText) findViewById(R.id.dbh_textView)).getText().toString());
                    if(!(((EditText) findViewById(R.id.dbh_textView2)).getText().toString()).equals("")) {
                        dbh2 = Double.valueOf(((EditText) findViewById(R.id.dbh_textView2)).getText().toString());
                    }
                    if(!(((EditText) findViewById(R.id.dbh_textView3)).getText().toString()).equals("")) {
                        dbh3 = Double.valueOf(((EditText)findViewById(R.id.dbh_textView3)).getText().toString());
                    }
                    if(!(((EditText) findViewById(R.id.dbh_textView4)).getText().toString()).equals("")) {
                        dbh4 = Double.valueOf(((EditText) findViewById(R.id.dbh_textView4)).getText().toString());
                    }
                    notes = ((EditText) findViewById(R.id.notes_about_tree)).getText().toString();



                    passedArray[0] = "0";
                    passedArray[1] = commonName;
                    passedArray[2] = scientificName;
                    if(((Switch) findViewById(R.id.units_switch)).isChecked()) {
                        Double t = Double.valueOf(dbh);
                        t=t*0.393701;

                        passedArray[3] = t.toString();
                    }else{
                        //passedArray[3] = dbh;
                    }
                    passedArray[6] = notes;
                    long time = System.currentTimeMillis();
                    timestamp = new Timestamp(time);

                    //Send new tree to the database
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                    CollectionReference users = db.collection("pendingTrees");

                    userID = firebaseUser.getUid();

                    Map<String, Object> user = new HashMap<>();
                    user.put("commonName",commonName);
                    user.put("scientificName", scientificName);
                    user.put("latitude", lat);
                    user.put("longitude",longit);
//                    user.put("dbh", dbh);
                    user.put("otherInfo", notes);
                    user.put("timestamp", timestamp);

                    ArrayList<String> picArray = new ArrayList<String>();
                    picArray.add(barkPic);
                    picArray.add(leafPic);
                    picArray.add(fullPic);
                    user.put("images", picArray);
                    user.put("userID", userID);


                    ArrayList<Double> dbhArray = new ArrayList<Double>();
                    if(dbh != null) {
                        dbhArray.add(dbh);
                    }
                    if(dbh2 != null) {
                        dbhArray.add(dbh2);
                    }
                    if(dbh3 != null) {
                        dbhArray.add(dbh3);
                    }
                    if(dbh4 != null) {
                        dbhArray.add(dbh4);
                    }
                    user.put("dbhArray", dbhArray);

//                    users.document(lat + ", " + longit + ", " + commonName).set(user);

                    users.document().set(user);


                    dialog.dismiss();

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Tree_Other_Info_Activity.this);
                    builder1.setCancelable(false);
                    builder1.setTitle("Success!");
                    builder1.setMessage("Your tree has been submitted for approval. While you wait, your tree will be available in the \"User Tree\" data set on your device.");
                    builder1.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.userTreeDataSourceGlobal.initialize(Tree_Other_Info_Activity.this,null);
                            MainActivity.userTreeDataSourceGlobal.addTree(passedArray);
                            Intent intentA = new Intent(Tree_Other_Info_Activity.this, MainActivity.class);
                            startActivity(intentA);
                        }
                    });
                    builder1.show();
                }
            });
            builder.show();
        }
    }
}
