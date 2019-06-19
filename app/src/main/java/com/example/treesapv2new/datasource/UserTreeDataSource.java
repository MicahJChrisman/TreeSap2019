package com.example.treesapv2new.datasource;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.treesapv2new.Coordinates_View_Activity;
import com.example.treesapv2new.MainActivity;
import com.example.treesapv2new.R;
import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.model.TreeLocation;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;

import com.google.android.gms.common.util.IOUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.siegmar.fastcsv.writer.CsvWriter;

public class UserTreeDataSource extends DataSource {


    String internetFileName = "users.csv";
    String localFileName = "user_tree_database.csv";
    static String treeDataStream = "/data/user/0/com.example.treesapv2new/files/user_tree_database.csv";
    static File treeDataFile = new File(treeDataStream);

    Reader in;
    Iterable<CSVRecord> records;
    CSVRecord closestRecord;

    Context parent;
    boolean downloaded, alreadyRead;

    public UserTreeDataSource() {}

    public UserTreeDataSource(Context aParent) {
        parent = aParent;
    }

    public String getInternetFileName() { return internetFileBase + "/" + internetFileName; }

    @Override
    public String getSourceName() {
        return "User Tree Data";
    }

    @Override
    public String getDescription() {
        return "Trees that the users added";
    }

    public static void addTree(String[] passed){
//        "/data/user/0/com.example.treesapv2new/files/ECOHdata.csv"
//        InputStream input = getResources().openRawResource(R.raw.katelyns_database);
        String csv = "/data/user/0/com.example.treesapv2new/files/user_tree_database.csv";
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(treeDataFile, true));
            writer.writeNext(passed);
            writer.close();
        }catch (IOException e){
            Log.i("thing", "thing");
        }

    }

//    @Override
//    public Boolean initialize(Context aParent, String initString) {
//
//        parent = aParent;
//        alreadyRead = false;
//        File userFile = new File(treeDataStream);
//
//        // Check to see if db is downloaded
//        if (! userFile.exists()) {
//            InputStream inputStream = parent.getResources().openRawResource(R.raw.user_tree_database);
//            try {
//                FileUtils.copyInputStreamToFile(inputStream, treeDataFile);
//            }catch (IOException e){
//
//            }
//            updateDataSource();
//        }
//
//        return true;
//    }

    @Override
    public Boolean initialize(Context aParent, String initString){
        return true;
    }

    private boolean readData() {
        if (alreadyRead) return true;

        try {
            in = new FileReader(treeDataFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            records = CSVFormat.EXCEL.withHeader().parse(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        alreadyRead = true;
        return true;
    }
    float closestDistance;

    ArrayList<Tree> userTrees = new ArrayList<Tree>();

    public void setUserTrees(ArrayList<Tree> treeArrayList){
        userTrees = treeArrayList;
    }

    @Override
    public Tree search(TreeLocation location){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        float[] results = new float[1];
        float closestDistance = 999999999;
        Tree treeReturn = null;
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(parent);
//        float cap = Float.valueOf(prefs.getString("distanceFromTreePref","10f"));
        float cap = 99999999;
        if(user !=null) {
            for (Tree tree : userTrees) {
                String a = (String) tree.getInfo("Source");
                String b = user.getUid();
                if (a.equals(b)) {
                    TreeLocation locOfTree = tree.getLocation();
                    Double lati = locOfTree.getLatitude();
                    Double longi = locOfTree.getLongitude();
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

                        if (results[0] == closestDistance) {
                            treeReturn = new Tree();
                            treeReturn.setClosest(closestDistance);
//                                tree.setCommonName(document.getData().get("commonName").toString());
                            treeReturn.setCommonName(tree.getCommonName());
//                                tree.setScientificName(document.getData().get("scientificName").toString());
                            treeReturn.setScientificName(tree.getScientificName());
//                                tree.setLocation(new TreeLocation(lati, longi));
                            treeReturn.setLocation(locOfTree);
//                                tree.setCurrentDBH(Double.parseDouble(document.getData().get("dbh").toString()));
                            treeReturn.setCurrentDBH(tree.getCurrentDBH());

                            treeReturn.setIsClosest(true);
//                                tree.addInfo("Source", document.getData().get("userID").toString());
                            treeReturn.addInfo("Source", user.getUid());
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
                            treeReturn.setDataSource("User");
                            treeReturn.setFound(true);
                        }
                    }
                }

            }
        }

        return treeReturn;
    }

//    @Override
//    public Tree search(TreeLocation location) {
//        float[] results = new float[1];
//
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(parent);
//        float cap = Float.valueOf(prefs.getString("distanceFromTreePref","10f"));
//
//        int entry, closestEntry;
//
//        readData();
//        closestDistance = 999999999;
//        closestEntry = 0;
//        entry = -1;
//        for (CSVRecord record : records) {
//            entry++;
//            try {
//                Double lati = Double.valueOf(record.get("Latitude"));
//                Double longi = Double.valueOf(record.get("Longitude"));
//
//                Location.distanceBetween(lati, longi,
//                        location.getLatitude(), location.getLongitude(),
//                        results);
//                // if (results[0] > 2.0) {
//                if (results[0] < closestDistance) {
//                    closestDistance = results[0];
//                    closestRecord = record;
//                }
//            } catch (Exception e) {
//                continue;
//            }
//        }
//
//        if (closestDistance > cap)
//            return null;
//        else {
//            //MATCH!  Build tree and return it.
//            Tree tree = new Tree();
//            tree.setClosest(closestDistance);
//            Double lat = new Double(closestRecord.get("Latitude"));
//            Double longi = new Double(closestRecord.get("Longitude"));
//            if(closestRecord.get("Species") !="") {
//                tree.setCommonName(closestRecord.get("Species"));
//            }
//            if(closestRecord.get("Scientific Name") != "") {
//                tree.setScientificName(closestRecord.get("Scientific Name"));
//            }
//            tree.setLocation(new TreeLocation(lat, longi));
//            if(closestRecord.get("DBH")!="") {
//                tree.setCurrentDBH(new Double(closestRecord.get("DBH")));
//            }
//            tree.setFound(true);
//            tree.setIsClosest(true);
//            tree.addInfo("Source", "You submitted this tree");
//            if(closestRecord.get("Notes")!="") {
//                tree.addInfo("Notes", closestRecord.get("Notes"));
//            }
//
//            if(closestRecord.get("Image Leaf")!="") {
//                tree.addPics("Image Leaf", closestRecord.get("Image Leaf"));
//            }
//            if(closestRecord.get("Image Bark")!="") {
//                tree.addPics("Image Bark", closestRecord.get("Image Bark"));
//            }
//            if(closestRecord.get("Image Tree")!="") {
//                tree.addPics("Image Tree", closestRecord.get("Image Tree"));
//            }
//            tree.setDataSource("User");
//
//            return tree;
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
            MainActivity.banana.setDataSource("User");
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



}
