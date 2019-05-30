package com.example.treesapv2new.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class Tree {

    String treeCommonName;
    String treeScientificName;
    String dataSource;
    TreeLocation location;
    GPSCoordinates searchedfor;
    String treeID;
    Boolean found = false;
    Boolean closest = false;
    public static final int TREE_ID = 0;
    public static final int NAME = 2;
    public static final int LATITUDE = 4;
    public static final int LONGITUDE = 5;
    public static final int DBH = 10;
    public static final int AGE_CLASS = 16;
    public static final int CONDITION = 17;
    public static final int TREE_ASSET_VALUE = 20;
    public static final int ROOT_INFRINGEMENT = 23;

    Double currentDBH;

    HashMap<String, Object> otherInfo = new HashMap<String, Object>();

    public String getCommonName() {
        return treeCommonName;
    }

    public void setDataSource(String dataSource){
        this.dataSource = dataSource;
    }

    public String getDataSource(){
        return dataSource;
    }

    public void setCommonName(String treeName) {
        this.treeCommonName = treeName;
    }

    public String getScientificName() {
        return treeScientificName;
    }

    public void setScientificName(String treeScientificName) {
        this.treeScientificName = treeScientificName;
    }

    public TreeLocation getLocation() {
        return location;
    }


    public void setLocation(TreeLocation location) {
        this.location = location;
    }

    public String getID() {
        return treeID;
    }

    public void setID(int treeID) {
        this.treeID = ""+treeID;
    }

    public void setID(String treeID) {
        this.treeID = treeID;
    }

    public void setFound(Boolean wasFound) {
        found = wasFound;
    }

    public Boolean isFound() {
        return found;
    }

    public void setIsClosest(Boolean isClosest) {
        closest = isClosest;
    }

    public Boolean isClosest() {
        return closest;
    }

    public Double getCurrentDBH() {
        return currentDBH;
    }

    public void setCurrentDBH(Double currentDBH) {
        this.currentDBH = currentDBH;
    }

    public void addInfo(String key, Object value) {
        otherInfo.put(key, value);
    }

    public Object getInfo(String key) {
        return otherInfo.get(key);
    }

    public String getAllInfo() {

        if (otherInfo.size() == 0) return null;

        String out = "";

        for (String key : otherInfo.keySet()) {
            out += "\t" + key + ": " + otherInfo.get(key).toString() + "\n";
        }

        if (out.length() > 0) out = out.substring(0, out.length()-1);
        return out;
    }

    public Boolean isOtherInfoPresent() {
        return otherInfo.size() > 0;
    }

    // For debugging
    public void setSearchFor(GPSCoordinates gps) {
        searchedfor = gps;
    }

    public GPSCoordinates getSearchedfor() {
        return searchedfor;
    }
}
