package com.example.treesapv2new.datasource;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.treesapv2new.CuratorApproveActivity;
import com.example.treesapv2new.MainActivity;
import com.example.treesapv2new.Maps_Activity;
import com.example.treesapv2new.R;
import com.example.treesapv2new.Tree_Info_First;
import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.model.TreeLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.opencsv.CSVWriter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;

public class AllUsersDataSource extends DataSource {

    Reader in;
    Iterable<CSVRecord> records;
    CSVRecord closestRecord;

    Context parent;
    boolean downloaded, alreadyRead;

    public AllUsersDataSource() {}

    public AllUsersDataSource(Context aParent) {
        parent = aParent;
    }

    public String getInternetFileName() { return ""; }

    @Override
    public String getSourceName() {
        return "Trees added by all users";
    }

    @Override
    public String getDescription() {
        return "Good work team.";
    }

    public static void addTree(String[] passed){
    }

    @Override
    public Boolean initialize(Context aParent, String initString) {

        parent = aParent;
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference users = db.collection("Pending Trees");
//        //final DocumentReference docRef = db.collection("Pending Trees").document("Black locust");
//        db.collection("Pending Trees").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for(QueryDocumentSnapshot document : task.getResult()){
//                        ArrayList<String> pic = (ArrayList<String>) document.getData().get("Pictures");
//                        try{
////
//                        }catch(Exception e){
//                            e.getMessage();
//                        }
//                    }
//                }else {
//                    Toast toast = Toast.makeText(parent, "Unable to load trees.", Toast.LENGTH_LONG);
//                }
//            }
//        });

        return true;
    }

//    private boolean readData() {
//        if (alreadyRead) return true;
//
//        try {
//            in = new FileReader(treeDataFile);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            records = CSVFormat.EXCEL.withHeader().parse(in);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        alreadyRead = true;
//        return true;
//    }
    float closestDistance;
    @Override
    public Tree search(TreeLocation location) {






//            AsyncTask q = new searchAsync(location);
//            q.execute(location, null, null);
        TreeLocation treeLocation;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(parent);
        float cap = Float.valueOf(prefs.getString("distanceFromTreePref","10f"));
        float[] results = new float[1];
        int entry, closestEntry;

//        readData();
        closestDistance = 999999999;
        closestEntry = 0;
        entry = -1;

        Tree tree = new Tree();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference users = db.collection("Pending Trees");
        db.collection("acceptedTrees").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        ArrayList<String> pic = (ArrayList<String>) document.getData().get("pictures");

                        try {
                            Double lati = Double.valueOf(document.getData().get("latitude").toString());
                            Double longi = Double.valueOf(document.getData().get("longitude").toString());
                            Location.distanceBetween(lati, longi,
                                    location.getLatitude(), location.getLongitude(),
                                    results);
                            if (results[0] < closestDistance) {
                                closestDistance = results[0];
                            }
                            if (closestDistance > cap)
                                break;
                            else {
                                //MATCH!  Build tree and return it.

                                tree.setClosest(closestDistance);
                                tree.setCommonName(document.getData().get("commonName").toString());
                                tree.setScientificName(document.getData().get("scientificName").toString());
                                tree.setLocation(new TreeLocation(lati, longi));
                                tree.setCurrentDBH(Double.parseDouble(document.getData().get("dbh").toString()));
                                tree.setFound(true);
                                tree.setIsClosest(true);
                                tree.addInfo("Source", document.getData().get("userID").toString());
//                                if(closestRecord.get("Notes")!="") {
//                                    tree.addInfo("Notes", closestRecord.get("Notes"));
//                                }
//
//                                if(closestRecord.get("Image Leaf")!="") {
//                                    tree.addPics("Image Leaf", closestRecord.get("Image Leaf"));
//                                }
//                                if(closestRecord.get("Image Bark")!="") {
//                                    tree.addPics("Image Bark", closestRecord.get("Image Bark"));
//                                }
//                                if(closestRecord.get("Image Tree")!="") {
//                                    tree.addPics("Image Tree", closestRecord.get("Image Tree"));
//                                }
                                tree.setDataSource("AllUserDB");

                                finishedBoolean = true;
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }else {
                    Toast toast = Toast.makeText(parent, "Unable to load trees.", Toast.LENGTH_LONG);
                }
            }
        });
        while(finishedBoolean == false) {
            try {
                Thread.sleep(40);
            } catch (InterruptedException x) {

            }
        }
//        return a;
        return tree;
    }

    public static Tree a = new Tree();

//    private class searchAsync extends AsyncTask<Object,Void,Tree> {
//        TreeLocation treeLocation;
//        Tree tree = new Tree();
//        searchAsync(TreeLocation treeLocation1){
//            treeLocation = treeLocation1;
////            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(parent);
////            float cap = Float.valueOf(prefs.getString("distanceFromTreePref","10f"));
////            float[] results = new float[1];
////            int entry, closestEntry;
////
////            closestDistance = 999999999;
////            closestEntry = 0;
////            entry = -1;
////
////            FirebaseFirestore db = FirebaseFirestore.getInstance();
////            CollectionReference users = db.collection("Pending Trees");
////            db.collection("acceptedTrees").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
////                @Override
////                public void onComplete(@NonNull Task<QuerySnapshot> task) {
////                    if(task.isSuccessful()){
////                        for(QueryDocumentSnapshot document : task.getResult()){
////                            ArrayList<String> pic = (ArrayList<String>) document.getData().get("pictures");
////
////                            try {
////                                Double lati = Double.valueOf(document.getData().get("latitude").toString());
////                                Double longi = Double.valueOf(document.getData().get("longitude").toString());
////                                Location.distanceBetween(lati, longi,
////                                        treeLocation.getLatitude(), treeLocation.getLongitude(),
////                                        results);
////                                if (results[0] < closestDistance) {
////                                    closestDistance = results[0];
////                                }
////                                if (closestDistance > cap)
////                                    break;
////                                else {
////                                    //MATCH!  Build tree and return it.
////
////                                    tree.setClosest(closestDistance);
////                                    tree.setCommonName(document.getData().get("commonName").toString());
////                                    tree.setScientificName(document.getData().get("scientificName").toString());
////                                    tree.setLocation(new TreeLocation(lati, longi));
////                                    tree.setCurrentDBH(Double.parseDouble(document.getData().get("dbh").toString()));
////                                    tree.setFound(true);
////                                    tree.setIsClosest(true);
////                                    tree.addInfo("Source", document.getData().get("userID").toString());
//////                                if(closestRecord.get("Notes")!="") {
//////                                    tree.addInfo("Notes", closestRecord.get("Notes"));
//////                                }
//////
//////                                if(closestRecord.get("Image Leaf")!="") {
//////                                    tree.addPics("Image Leaf", closestRecord.get("Image Leaf"));
//////                                }
//////                                if(closestRecord.get("Image Bark")!="") {
//////                                    tree.addPics("Image Bark", closestRecord.get("Image Bark"));
//////                                }
//////                                if(closestRecord.get("Image Tree")!="") {
//////                                    tree.addPics("Image Tree", closestRecord.get("Image Tree"));
//////                                }
////                                    tree.setDataSource("AllUserDB");
////
////                                    finishedBoolean = true;
////                                }
////                            } catch (Exception e) {
////                                continue;
////                            }
////                        }
////                    }else {
////                        Toast toast = Toast.makeText(parent, "Unable to load trees.", Toast.LENGTH_LONG);
////                    }
////                }
////            });
//        }
//
//        @Override
//        protected Tree doInBackground(Object...treeLocations) {
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(parent);
//            float cap = Float.valueOf(prefs.getString("distanceFromTreePref","10f"));
//            float[] results = new float[1];
//            int entry, closestEntry;
//
//            closestDistance = 999999999;
//            closestEntry = 0;
//            entry = -1;
//
//            Tree tree = new Tree();
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            CollectionReference users = db.collection("Pending Trees");
//            db.collection("acceptedTrees").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if(task.isSuccessful()){
//                        for(QueryDocumentSnapshot document : task.getResult()){
//                            ArrayList<String> pic = (ArrayList<String>) document.getData().get("pictures");
//
//                            try {
//                                Double lati = Double.valueOf(document.getData().get("latitude").toString());
//                                Double longi = Double.valueOf(document.getData().get("longitude").toString());
//                                Location.distanceBetween(lati, longi,
//                                        treeLocation.getLatitude(), treeLocation.getLongitude(),
//                                        results);
//                                if (results[0] < closestDistance) {
//                                    closestDistance = results[0];
//                                }
//                                if (closestDistance > cap)
//                                    break;
//                                else {
//                                    //MATCH!  Build tree and return it.
//
//                                    tree.setClosest(closestDistance);
//                                    tree.setCommonName(document.getData().get("commonName").toString());
//                                    tree.setScientificName(document.getData().get("scientificName").toString());
//                                    tree.setLocation(new TreeLocation(lati, longi));
//                                    tree.setCurrentDBH(Double.parseDouble(document.getData().get("dbh").toString()));
//                                    tree.setFound(true);
//                                    tree.setIsClosest(true);
//                                    tree.addInfo("Source", document.getData().get("userID").toString());
////                                if(closestRecord.get("Notes")!="") {
////                                    tree.addInfo("Notes", closestRecord.get("Notes"));
////                                }
////
////                                if(closestRecord.get("Image Leaf")!="") {
////                                    tree.addPics("Image Leaf", closestRecord.get("Image Leaf"));
////                                }
////                                if(closestRecord.get("Image Bark")!="") {
////                                    tree.addPics("Image Bark", closestRecord.get("Image Bark"));
////                                }
////                                if(closestRecord.get("Image Tree")!="") {
////                                    tree.addPics("Image Tree", closestRecord.get("Image Tree"));
////                                }
//                                    tree.setDataSource("AllUserDB");
//
//                                    finishedBoolean = true;
//                                }
//                            } catch (Exception e) {
//                                continue;
//                            }
//                        }
//                    }else {
//                        Toast toast = Toast.makeText(parent, "Unable to load trees.", Toast.LENGTH_LONG);
//                    }
//                }
//            });
//
//            return tree;
//        }
//        @Override
//        protected void onPostExecute(Tree t){
//            a = t;
//        }
//    }

    public void patchData(Tree tree){
        if (MainActivity.banana.getCommonName() == (null)) {
            MainActivity.banana.setCommonName(tree.getCommonName());
        }

        if (MainActivity.banana.getScientificName() == (null)) {
            MainActivity.banana.setScientificName(tree.getScientificName());
        }
        if (MainActivity.banana.getLocation() == (null)) {
            MainActivity.banana.setLocation(tree.getLocation());
        }
        if (MainActivity.banana.getID() == (null)) {
            MainActivity.banana.setID(tree.getID());
        }
        if (MainActivity.banana.getCurrentDBH() == (null)) {
            MainActivity.banana.setCurrentDBH(tree.getCurrentDBH());
        }
        if(MainActivity.banana.getDataSource() == null){
            MainActivity.banana.setDataSource("AllUserDB");
        }
    }

    @Override
    public Boolean isDownloadable() {
        return true;
    }

    @Override
    public Boolean checkForUpdate() {
        return !isUpToDate();
    }

    @Override
    public Boolean isUpToDate() {
        return true;
    }

    @Override
    public Boolean updateDataSource() {
        return true;
    }

    @Override
    public Iterable<CSVRecord> getCoordinates(Context context, String file) {
        localFileName = file;
        try {
            in = new FileReader(localFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            records = CSVFormat.EXCEL.withHeader().parse(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    public static boolean finishedBoolean = false;

}
