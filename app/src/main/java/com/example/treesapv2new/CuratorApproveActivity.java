package com.example.treesapv2new;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class CuratorApproveActivity extends AppCompatActivity {
    private static final String TAG = "CuratorApproveActivity";

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.activity_curator_approve);
        initImageBitmaps();

        ((TextView) findViewById(R.id.curator_approve_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initImageBitmaps(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference users = db.collection("pendingTrees");
        //final DocumentReference docRef = db.collection("Pending Trees").document("Black locust");
        db.collection("pendingTrees").orderBy("Date and time", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//        db.collection("pendingTrees").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        ArrayList<String> pic = (ArrayList<String>) document.getData().get("images");
                        try{
                            String b = "";
                            for(String a : pic){
                                if(a!=null){
                                    b = a;
                                }else{
                                    b = "";
                                }
                            }
                            mImageUrls.add(b);
                        }catch(Exception e){
                            e.getMessage();
                            mImageUrls.add("");
                        }
                        mNames.add((String) document.getData().get("commonName"));
                    }
                    initRecyclerView();
                }else {
                    Toast toast = Toast.makeText(CuratorApproveActivity.this, "Unable to load trees.", Toast.LENGTH_LONG);
                }
            }
        });

//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    DocumentSnapshot document = task.getResult();
//                    ArrayList<String> pic = (ArrayList<String>) document.getData().get("Pictures");
//                    try{
//                        byte [] encodeByte= Base64.decode(pic.get(0),Base64.DEFAULT);
//
//                        InputStream inputStream  = new ByteArrayInputStream(encodeByte);
//                        mImageUrls.add(pic.get(0));
//                        Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
////                        ImageView image = (ImageView) findViewById(R.id.set_image_curator);
////                        image.setImageBitmap(bitmap);
//                    }catch(Exception e){
//                        e.getMessage();
//                    }
//                    mNames.add((String) document.getData().get("Common Name"));
//                    initRecyclerView();
//                }else{
//                    Toast toast = Toast.makeText(CuratorApproveActivity.this, "Unable to load trees.", Toast.LENGTH_LONG);
//                }
//            }
//        });

    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recycler_view_curator);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
