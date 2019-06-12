package com.example.treesapv2new.datasource;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.treesapv2new.MainActivity;
import com.example.treesapv2new.control.PrefManager;
import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.model.TreeLocation;
import android.preference.PreferenceManager;


public class HopeCollegeDataSource extends DataSource {

    private String internetFileName = "dataExport_119_HopeTrees_7may2018.csv";
    private String localFileName = "HCTreeData.csv";

    private Reader in;
    private Iterable<CSVRecord> records;
    private CSVRecord closestRecord;

    private Context parent;
    private boolean downloaded, alreadyRead;



    public HopeCollegeDataSource() {
    }
    public HopeCollegeDataSource(Context aParent) {

        parent = aParent;

        downloaded = false;
        alreadyRead = false;
    }

    public String getInternetFileName() {
        return internetFileBase + "/" + internetFileName;
    }

    @Override
    public Boolean isDownloadable() {
        return true;
    }

    @Override
    public String getSourceName() {
        return "Hope College Tree Data";
    }

    @Override
    public String getDescription() {
        return "Tree Data from Hope College (from the Pine Grove)";
    }

    private void download(String earl, String filename) {
        String str;

        downloaded = false;
        try {
            URL url = new URL(earl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));

            while ((str = in.readLine()) != null) {
                out.write(str);
                out.newLine();
            }

            in.close();
            downloaded = true;
            alreadyRead = false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }

    }

    @Override
    public Boolean initialize(Context aParent, String initString) {

        parent = aParent;

        if(!localFileName.contains("/")) {
            localFileName = parent.getFilesDir() + "/" + localFileName;
        }
        File HCfile = new File(localFileName);

        // Check to see if db is downloaded
        if (!HCfile.exists()) {
            updateDataSource();
        }

        return true;
    }

    private boolean readData() {

        try {
            in = new FileReader(localFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            records = CSVFormat.EXCEL.parse(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        alreadyRead = true;
        return true;
    }

    @Override
    public Tree search(TreeLocation location) {
        float[] results = new float[1];
        float closestDistance;
//        float close = PrefManager.getFloat("tree result", 10f);
//        float close = 999999999;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(parent);
        float close = Float.valueOf(prefs.getString("distanceFromTreePref","10f"));

//        float close =

        readData();
        closestDistance = 999999999;
        for (CSVRecord record : records) {
            try {
                Double lat = Double.valueOf(record.get(Tree.LATITUDE));
                Double longi = Double.valueOf(record.get(Tree.LONGITUDE));

                Location.distanceBetween(lat, longi, location.getLatitude(), location.getLongitude(), results);
//                if (results[0] > 2.0) {
                if (results[0] < closestDistance) {
                    closestDistance = results[0];
                    closestRecord = record;
                }
//                    continue;
//                }
//
//                //MATCH!  Build tree and return it.
//                Tree tree = new Tree();
//                tree.setCommonName(record.get(Tree.LONGITUDE));
//                tree.setLocation(new TreeLocation(lat, longi));
//                tree.setID(record.get(Tree.TREE_ID));
//                tree.setCurrentDBH(new Double(record.get(Tree.DBH)));
//                tree.addInfo("Age class", record.get(Tree.AGE_CLASS));
//                tree.addInfo("Condition", record.get(Tree.CONDITION));
//                tree.addInfo("Tree asset value", record.get(Tree.TREE_ASSET_VALUE));
//                tree.addInfo("Root infringement", record.get(Tree.ROOT_INFRINGEMENT));
//                tree.setFound(true);
//                tree.setIsClosest(true);
//
//                return tree;
            } catch (Exception e) {
                continue;
            }
        }

        if (closestDistance > close)
            return null;
        else {
            //MATCH!  Build tree and return it.
            Tree tree = new Tree();
            tree.setClosest(closestDistance);
            Double lat = Double.valueOf(closestRecord.get(Tree.LATITUDE));
            Double longi = Double.valueOf(closestRecord.get(Tree.LONGITUDE));
            tree.setCommonName(closestRecord.get(Tree.NAME));
            tree.setLocation(new TreeLocation(lat, longi));
            tree.setID(closestRecord.get(Tree.TREE_ID));
            tree.setCurrentDBH(Double.valueOf(closestRecord.get(Tree.DBH)));
            tree.addInfo("Age class", closestRecord.get(Tree.AGE_CLASS));
            tree.addInfo("Condition", closestRecord.get(Tree.CONDITION));
            tree.addInfo("Tree asset value", closestRecord.get(Tree.TREE_ASSET_VALUE));
            tree.addInfo("Root infringement", closestRecord.get(Tree.ROOT_INFRINGEMENT));
            tree.setFound(true);
            tree.setIsClosest(true);
            tree.setDataSource("HopeCollegeData");


            return tree;
        }

    }

    public void patchData(Tree tree){
        if (MainActivity.banana.getCommonName() == (null)) {
            MainActivity.banana.setCommonName(tree.getCommonName());
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
        if(MainActivity.banana.getInfo("Age class") == (null)){
            MainActivity.banana.addInfo("Age class", tree.getInfo("Age class"));
        }
        if(MainActivity.banana.getInfo("Condition") == (null)){
            MainActivity.banana.addInfo("Condition", tree.getInfo("Condition"));
        }
        if(MainActivity.banana.getInfo("Tree asset value") == (null)){
//            MainActivity.banana.addInfo("Tree asset value", closestRecord.get(Tree.TREE_ASSET_VALUE));
            MainActivity.banana.addInfo("Tree asset value", tree.getInfo("Tree asset value"));
        }
        if(MainActivity.banana.getInfo("Root infringement") == (null)){
//            MainActivity.banana.addInfo("Root infringement", closestRecord.get(Tree.ROOT_INFRINGEMENT));
            MainActivity.banana.addInfo("Root infringement", tree.getInfo("Root infringement"));
        }
    }

    @Override
    public Boolean checkForUpdate() {
        return !isUpToDate();
    }

    @Override
    public Boolean isUpToDate() {

        try {
            File HCfile = new File(localFileName);
            if (HCfile.exists()) {
                URL url = new URL(getInternetFileName());
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("HEAD");
                c.connect();
                InputStream in = c.getInputStream();
                return HCfile.lastModified() >= c.getLastModified();
            } else
                return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean updateDataSource() {
        Log.d("HCDataSource", "Downloading " + getInternetFileName() + " to " + localFileName);
        File HCfile = new File(localFileName);
        download(getInternetFileName(), localFileName);
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
            records = CSVFormat.EXCEL.parse(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

}
