package com.example.treesapv2new.datasource;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.treesapv2new.MainActivity;
import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.model.TreeLocation;

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

public class ExtendedCoHDataSource extends DataSource {

    String internetFileName = "itree-august2019.csv";
    String localFileName = "ECOHdata.csv";

    Reader in;
    Iterable<CSVRecord> records;
    CSVRecord closestRecord;

    Context parent;
    boolean downloaded, alreadyRead;

    public ExtendedCoHDataSource() {}

    public ExtendedCoHDataSource(Context aParent) {
        parent = aParent;
    }

    public String getInternetFileName() { return internetFileBase + "/" + internetFileName; }

    @Override
    public String getSourceName() {
        return "Extended City of Holland Tree Data";
    }

    @Override
    public String getDescription() {
        return "Tree Data from the City of Holland via ArcGIS.";
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
            Log.i("Error", "Unable to download datasource.");
        }
    }

    @Override
    public Boolean initialize(Context aParent, String initString) {

        parent = aParent;
        downloaded = false;
        alreadyRead = false;

        if(!localFileName.contains("/")) {
            localFileName = parent.getFilesDir() + "/" + localFileName;
        }
        File ECOHfile = new File(localFileName);

        // Check to see if db is downloaded
        if (! ECOHfile.exists()) {
            updateDataSource();
        }

        return true;
    }

    private boolean readData() {
        if (alreadyRead) return true;

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

        alreadyRead = true;
        return true;
    }


    public Tree bestMatchFinder(Double dbh, TreeLocation treeLocation, String commonName){
        int entry, closestEntry;
        float[] results = new float[1];
        readData();
        float closestDistance1 = 999999999;
        float cap = 100000;
        closestEntry = 0;
        entry = -1;
        for (CSVRecord record : records) {
            entry++;
            try {
                String commonNameRecord = record.get("species_name");
                if(commonName.equals(commonNameRecord)){
                    Double dbhRecord = Double.valueOf(record.get("dbh_in"));
                    if(Math.abs(dbhRecord - dbh) < 0.5){
                        Double lati = Double.valueOf(record.get("y_coord"));
                        Double longi = Double.valueOf(record.get("x_coord"));

                        Location.distanceBetween(lati, longi,
                                treeLocation.getLatitude(), treeLocation.getLongitude(),
                                results);
                        if (results[0] < closestDistance1) {
                            closestDistance1 = results[0];
                            closestRecord = record;
                        }

                    }else{

                    }
                }else{

                }
            } catch (Exception e) {
                continue;
            }
        }

        if (closestDistance1 > cap)
            return null;
        else {
            //MATCH!  Build tree and return it.
            Tree tree = new Tree();
            tree.setClosest(closestDistance1);
            Double lat = new Double(closestRecord.get("y_coord"));
            Double longi = new Double(closestRecord.get("x_coord"));
            tree.setCommonName(closestRecord.get("species_name"));
            //tree.setScientificName(closestRecord.get("Scientific"));
            tree.setLocation(new TreeLocation(lat, longi));
            tree.setID(closestRecord.get(Tree.TREE_ID));
            tree.setCurrentDBH(new Double(closestRecord.get("dbh_in")));
//            if (closestRecord.get("Park").length() > 0)
//                tree.addInfo("Park", closestRecord.get("Park"));
            tree.setFound(true);
            tree.setIsClosest(true);
            tree.setDataSource("ExtendedCoH");

            return tree;
        }
    }


    float closestDistance;
    @Override
    public Tree search(TreeLocation location) {
        MainActivity.treesNearby.clear();
        float[] results = new float[1];

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(parent);
        float cap = Float.valueOf(prefs.getString("distanceFromTreePref","10f"));

        int entry, closestEntry;

        readData();
        closestDistance = 999999999;
        closestEntry = 0;
        entry = -1;
        for (CSVRecord record : records) {
            entry++;
            try {
                Double lati = Double.valueOf(record.get("y_coord"));
                Double longi = Double.valueOf(record.get("x_coord"));

                Location.distanceBetween(lati, longi,
                        location.getLatitude(), location.getLongitude(),
                        results);
                // if (results[0] > 2.0) {
                if (results[0] < closestDistance) {
                    closestDistance = results[0];
                    closestRecord = record;
                }

                if(results[0] < 10){
                    Tree tree = new Tree();
                    Double lat2 = new Double(record.get("y_coord"));
                    Double longi2 = new Double(record.get("x_coord"));
                    tree.setCommonName(record.get("species_name"));
                    //tree.setScientificName(closestRecord.get("Scientific"));
                    tree.setLocation(new TreeLocation(lat2, longi2));
                    tree.setID(record.get(Tree.TREE_ID));
                    tree.setCurrentDBH(new Double(record.get("dbh_in")));
//            if (closestRecord.get("Park").length() > 0)
//                tree.addInfo("Park", closestRecord.get("Park"));
                    tree.setFound(true);
                    tree.setDataSource("ExtendedCoH");
                    MainActivity.treesNearby.add(tree);
                }
                 /*   continue;
                //}

                //MATCH!  Build tree and return it.
                Tree tree = new Tree();
                tree.setCommonName(record.get("CommonName"));
                tree.setScientificName(record.get("Scientific"));
                tree.setLocation(new TreeLocation(lati, longi));
                tree.setID(record.get(Tree.TREE_ID));
                tree.setCurrentDBH(new Double(record.get("DBH")));
                if (record.get("Park").length() > 0)
                    tree.addInfo("Park", record.get("Park"));
                tree.setFound(true);
                tree.setIsClosest(true);

                return tree;*/
            } catch (Exception e) {
                continue;
            }
        }

        if (closestDistance > cap)
            return null;
        else {
            //MATCH!  Build tree and return it.
            Tree tree = new Tree();
            tree.setClosest(closestDistance);
            Double lat = new Double(closestRecord.get("y_coord"));
            Double longi = new Double(closestRecord.get("x_coord"));
            tree.setCommonName(closestRecord.get("species_name"));
            //tree.setScientificName(closestRecord.get("Scientific"));
            tree.setLocation(new TreeLocation(lat, longi));
            tree.setID(closestRecord.get(Tree.TREE_ID));
            tree.setCurrentDBH(new Double(closestRecord.get("dbh_in")));
//            if (closestRecord.get("Park").length() > 0)
//                tree.addInfo("Park", closestRecord.get("Park"));
            tree.setFound(true);
            tree.setIsClosest(true);
            tree.setDataSource("ExtendedCoH");

            for(Tree tree1 : MainActivity.treesNearby) {
                tree.addNearbyTrees(tree1);
            }
            return tree;
        }

    }

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
            MainActivity.banana.setDataSource("ExtendedCoH");
        }
//        tree.setDataSource("ExtendedCoH");
    }

    public void patchDataMatching(Tree tree){
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
//        if(MainActivity.banana.getDataSource() == null){
//            MainActivity.banana.setDataSource("ExtendedCoH");
//        }
        tree.setDataSource("ExtendedCoH");
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

        try {
            File ECOHfile = new File(localFileName);
            if (ECOHfile.exists()) {
                URL url = new URL(getInternetFileName());
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("HEAD");
                c.connect();
                InputStream in = c.getInputStream();
                return ECOHfile.lastModified() >= c.getLastModified();
            } else
                return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean updateDataSource() {
        Log.d("ECOHDataSource", "Downloading " + getSourceName() + " to " + localFileName);
        File ECOHfile = new File(localFileName);
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
            records = CSVFormat.EXCEL.withHeader().parse(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }



}
