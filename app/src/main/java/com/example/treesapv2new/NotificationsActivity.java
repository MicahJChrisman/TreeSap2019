package com.example.treesapv2new;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.Line;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shivam.library.imageslider.ImageSlider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class NotificationsActivity extends AppCompatActivity {


    ArrayList<String> images = new ArrayList<String>();
    ArrayList<String> notList = new ArrayList<String>();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    LinearLayout linearLayout;
    LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.notifications_activity);

        updateNotifications();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
            }

        };



//        String v = user.getUid();
        if(user != null) {
            db.collection("notifications").whereEqualTo(FieldPath.of("treeData","userID"), user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            try {
                                HashMap<String, Object> treeData = (HashMap<String,Object>) documentSnapshot.getData().get("treeData");
                                View view = inflater.inflate(R.layout.notification_tree_single, null);
                                TextView acceptedMessage = view.findViewById(R.id.tree_accept_reject);
                                TextView message = view.findViewById(R.id.tree_message);
                                boolean accepted = (Boolean) documentSnapshot.getData().get("accepted");
                                if(accepted){
                                    acceptedMessage.setText("Tree Accepted");
                                }else{
                                    acceptedMessage.setText("Tree Rejected");
                                }
                                String messageNotif = documentSnapshot.getData().get("message").toString();
                                message.setText(messageNotif);
                                linearLayout.addView(view);
                                view.setOnClickListener(new notificationSelected());
                                view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setContentView(R.layout.notification_information);
                                        ((TextView) findViewById(R.id.notification_specific_close)).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                setContentView(R.layout.notifications_activity);
                                                updateNotifications();
                                            }
                                        });
                                        if(accepted){
                                            ((TextView) findViewById(R.id.accept_or_reject_text)).setText("Tree Accepted");
                                            try{
                                                ((TextView) findViewById(R.id.accept_or_reject_text_message)).setText("Your " + treeData.get("commonName") + " was added to the database.");
                                            }catch (Exception e){
                                                ((TextView) findViewById(R.id.accept_or_reject_text_message)).setText("Your tree was added to the database.");
                                            }
                                        }else{
                                            ((TextView) findViewById(R.id.accept_or_reject_text)).setText("Tree Accepted");
                                            try{
                                                ((TextView) findViewById(R.id.accept_or_reject_text_message)).setText("Your " + treeData.get("commonName") + " was removed from the database.");
                                            }catch (Exception e){
                                                ((TextView) findViewById(R.id.accept_or_reject_text_message)).setText("Your tree was removed from the database.");
                                            }
                                        }

                                        try {
                                            ((TextView) findViewById(R.id.notification_info_common_name)).setText(treeData.get("commonName").toString());
                                        }catch (Exception e){
                                        }
                                        try {
                                            ((TextView) findViewById(R.id.notification_info_scientific_name)).setText(treeData.get("scientificName").toString());
                                        }catch (Exception e){
                                        }
                                        try {
                                            ((TextView) findViewById(R.id.notification_info_latitude)).setText(treeData.get("latitude").toString());
                                        }catch (Exception e){
                                        }
                                        try {
                                            ((TextView) findViewById(R.id.notification_info_longitude)).setText(treeData.get("longitude").toString());
                                        }catch (Exception e){
                                        }
                                        ArrayList<Double> dbhArray= new ArrayList<Double>();
                                        try {
                                            dbhArray = (ArrayList<Double>) treeData.get("dbhArray");
                                        }catch (Exception e){
                                        }
                                        if(dbhArray.size() > 0) {
                                            ((TextView) findViewById(R.id.notification_info_dbh)).setText(dbhArray.get(0).toString() + "");
                                        }else {
                                            ((TextView) findViewById(R.id.notification_info_dbh)).setVisibility(View.GONE);
                                            ((TextView) findViewById(R.id.dbh_notification)).setVisibility(View.GONE);
                                        }
                                        if(dbhArray.size()>1) {
                                            ((TextView) findViewById(R.id.notification_info_dbh2)).setText(dbhArray.get(1).toString() + "");
                                        }else {
                                            ((TextView) findViewById(R.id.notification_info_dbh2)).setVisibility(View.GONE);
                                            ((TextView) findViewById(R.id.dbh_notification2)).setVisibility(View.GONE);
                                        }
                                        if(dbhArray.size() >2) {
                                            ((TextView) findViewById(R.id.notification_info_dbh3)).setText(dbhArray.get(2).toString() + "");
                                        }else {
                                            ((TextView) findViewById(R.id.notification_info_dbh3)).setVisibility(View.GONE);
                                            ((TextView) findViewById(R.id.dbh_notification3)).setVisibility(View.GONE);
                                        }
                                        if(dbhArray.size()>3) {
                                            ((TextView) findViewById(R.id.notification_info_dbh4)).setText(dbhArray.get(3).toString() + "");
                                        }else{
                                            ((TextView) findViewById(R.id.notification_info_dbh4)).setVisibility(View.GONE);
                                            ((TextView) findViewById(R.id.dbh_notification4)).setVisibility(View.GONE);
                                        }
                                        ArrayList<String> notesArray = new ArrayList<String >();
                                        try{
                                            notesArray = (ArrayList<String>) treeData.get("notes");
                                            if(notesArray.size() > 0) {
                                                String note = "";
                                                for (String notes : notesArray) {
                                                    note += notes;
                                                    note += "\n";
                                                }
                                                ((TextView) findViewById(R.id.notes_section_notification)).setText(note);
                                            }else{
                                                ((TextView) findViewById(R.id.notification_info_notes)).setVisibility(View.GONE);
                                            }
                                        }catch (Exception e){
                                        }
                                        HashMap<String, ArrayList<String>> photosArray = new HashMap<String, ArrayList<String>>();
                                        try{
                                            photosArray = (HashMap<String, ArrayList<String>>) treeData.get("images");
                                            try {
                                                for (String photo : photosArray.get("full")) {
                                                    images.add(photo);
                                                }
                                            }catch (Exception e){
                                            }
                                            try {
                                                for (String photo : photosArray.get("leaf")) {
                                                    images.add(photo);
                                                }
                                            }catch (Exception e){
                                            }
                                            try {
                                                for (String photo : photosArray.get("bark")) {
                                                    images.add(photo);
                                                }
                                            }catch (Exception e){
                                            }
                                            if(images.size() >0) {
                                                ImageSlider imageSlider = findViewById(R.id.images_section_notification);
                                                SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                                                imageSlider.setAdapter(mSectionsPagerAdapter);
                                                if(images.size() > 1){
                                                    findViewById(R.id.swipe_for_pics).setVisibility(View.VISIBLE);
                                                }
                                            }else{
                                                ((TextView) findViewById(R.id.photos_section_notification)).setVisibility(View.GONE);
                                                ((ImageSlider) findViewById(R.id.images_section_notification)).setVisibility(View.GONE);
                                            }
                                        }catch (Exception e){
                                            ((TextView) findViewById(R.id.photos_section_notification)).setVisibility(View.GONE);
                                            ((ImageSlider) findViewById(R.id.images_section_notification)).setVisibility(View.GONE);
                                        }
                                        try{
                                            ((TextView) findViewById(R.id.notification_info_curator_comments)).setText(messageNotif);
                                        } catch (Exception e){

                                        }

                                    }
                                });
                            }catch (Exception e){

                            }
                        }
                    }
                }
            });

//            CollectionReference citiesRef = db.collection("pendingTrees").whereEqualTo(new FieldPath("treeData", "userID" : ));
//            CollectionReference citiesRef1 = db.collection("pendingTrees").where(firebase.firestore().FieldPath.of("treeData", "userID"));
//            citiesRef.whereEqualTo(FieldPath)
//            CollectionReference notifications = db.collection("notifications").whereEqualTo("treeData", true).whereEqualTo("userID", true);
//            for(String notify : notifications){

//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                            try {
//                                HashMap<String, Object> treeData = (HashMap<String,Object>) documentSnapshot.getData().get("treeData");
//                                String userOfNotification = treeData.get("userID").toString();
//                                if(user.getUid().equals(userOfNotification)){
//                                    View view = inflater.inflate(R.layout.notification_tree_single, null);
//                                    TextView acceptedMessage = view.findViewById(R.id.tree_accept_reject);
//                                    TextView message = view.findViewById(R.id.tree_message);
//                                    boolean accepted = (Boolean) documentSnapshot.getData().get("accepted");
//                                    if(accepted){
//                                        acceptedMessage.setText("Tree Accepted");
//                                    }else{
//                                        acceptedMessage.setText("Tree Rejected");
//                                    }
//                                    String messageNot = documentSnapshot.getData().get("message").toString();
//                                    message.setText(messageNot);
//                                    linearLayout.addView(view);
//                                }
//                            } catch (Exception e) {
//                                Toast.makeText(NotificationsActivity.this, "An error occurred", Toast.LENGTH_SHORT);
//                            }
//                        }
//                    }
//                }
//            });
        }
    }

    public void updateNotifications(){
        notList.add("There is something in here");
        linearLayout = ((LinearLayout) findViewById(R.id.notifications_linear_layout));
        inflater = LayoutInflater.from(this);
    }

    public class notificationSelected implements View.OnClickListener{
        @Override
        public void onClick(View v){

        }
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
//            String byteConvert = images.get(position);
            byte [] encodeByte=Base64.decode(images.get(position),Base64.DEFAULT);
            return PlaceholderFragment.newInstance(encodeByte);
        }

        @Override
        public int getCount() {

            return images.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return null;
        }
    }

}
