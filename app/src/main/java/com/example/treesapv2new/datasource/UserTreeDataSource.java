package com.example.treesapv2new.datasource;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.treesapv2new.MainActivity;
import com.example.treesapv2new.R;
import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.model.TreeLocation;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;

//import com.github.mikephil.charting.utils.FileUtils;
import com.google.android.gms.common.util.IOUtils;
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

import de.siegmar.fastcsv.writer.CsvWriter;

public class UserTreeDataSource extends DataSource {


    String internetFileName = "users.csv";
    String localFileName = "user_tree_database.csv";
    static String carl = "/data/user/0/com.example.treesapv2new/files/user_tree_database.csv";
    static File steve = new File(carl);

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
            CSVWriter writer = new CSVWriter(new FileWriter(steve, true));
            writer.writeNext(passed);
            writer.close();
        }catch (IOException e){
            Log.i("thing", "thing");
        }

    }

    private void download(String earl, String filename) {
//        String str;
//
//        downloaded = false;
//        try {
//            URL url = new URL(earl);
//            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
//
//            while ((str = in.readLine()) != null) {
//                out.write(str);
//                out.newLine();
//            }
//
//            in.close();
//            downloaded = true;
//            alreadyRead = false;
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            Log.i("Error", "Unable to download datasource.");
//        }
        downloaded = true;


//        Environment.getExternalStorageDirectory().getPath();
//        Context.getFilesDir().getPath();
    }

    @Override
    public Boolean initialize(Context aParent, String initString) {

        parent = aParent;
        downloaded = false;
        alreadyRead = false;

        if(!localFileName.contains("/")) {
            localFileName = parent.getFilesDir() + "/" + localFileName;
        }
        File userFile = new File(carl);

        // Check to see if db is downloaded
        if (! userFile.exists()) {
            InputStream inputStream = parent.getResources().openRawResource(R.raw.user_tree_database);
            try {
                FileUtils.copyInputStreamToFile(inputStream, steve);
            }catch (IOException e){

            }


//            try(OutputStream outputStream = new FileOutputStream(steve)){
//                IOUtils.copy(inputStream, outputStream);
//            } catch (FileNotFoundException e) {
//                // handle exception here
//            } catch (IOException e) {
//                // handle exception here
//            }
            updateDataSource();
        }

        return true;
    }

    private boolean readData() {
        if (alreadyRead) return true;

        try {
            in = new FileReader(steve);
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

    @Override
    public Tree search(TreeLocation location) {
        float[] results = new float[1];
        float closestDistance;
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
                Double lati = Double.valueOf(record.get("Latitude"));
                Double longi = Double.valueOf(record.get("Longitude"));

                Location.distanceBetween(lati, longi,
                        location.getLatitude(), location.getLongitude(),
                        results);
                // if (results[0] > 2.0) {
                if (results[0] < closestDistance) {
                    closestDistance = results[0];
                    closestRecord = record;
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
            Double lat = new Double(closestRecord.get("Latitude"));
            Double longi = new Double(closestRecord.get("Longitude"));
            tree.setCommonName(closestRecord.get("Species"));
            //tree.setScientificName(closestRecord.get("Scientific"));
            tree.setLocation(new TreeLocation(lat, longi));
            tree.setID(closestRecord.get(Tree.TREE_ID));
            tree.setCurrentDBH(new Double(closestRecord.get("DBH")));
//            if (closestRecord.get("Park").length() > 0)
//                tree.addInfo("Park", closestRecord.get("Park"));
            tree.setFound(true);
            tree.setIsClosest(true);
            tree.setDataSource("User");

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

        try {
            File userFile = new File(localFileName);
            if (userFile.exists()) {
                URL url = new URL(getInternetFileName());
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("HEAD");
                c.connect();
                InputStream in = c.getInputStream();
                return userFile.lastModified() >= c.getLastModified();
            } else
                return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean updateDataSource() {
        Log.d("UserTreeDataSource", "Downloading " + getSourceName() + " to " + localFileName);

        File userFile = new File(localFileName);
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
