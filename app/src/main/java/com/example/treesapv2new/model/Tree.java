package com.example.treesapv2new.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Tree {

    float closestDist;
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

    ArrayList<Double> currentDBH;
    ArrayList<String> dbsUsed = new ArrayList<>();
    ArrayList<Object> dbhArray = new ArrayList<>();
    ArrayList<String> notesArray = new ArrayList<>();
    HashMap<String, Object> otherInfo = new HashMap<String, Object>();
    HashMap<String, ArrayList<Object>> treePics = new HashMap<String, ArrayList<Object>>();
    HashMap<String, ArrayList<String>> treePhotos = new HashMap<String, ArrayList<String>>();
    ArrayList<Tree> nearbyTrees = new ArrayList<Tree>();


    public ArrayList<Tree> getNearbyTrees(){
        return nearbyTrees;
    }

    public void addNearbyTrees(Tree tree){
        nearbyTrees.add(tree);
    }

    public void addDb(String dbName){
        dbsUsed.add(dbName);
    }

    public ArrayList<String> getDbsUsed(){
        return dbsUsed;
    }

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

    public ArrayList<String> getNotesArray(){
        return notesArray;
    }

    public void setNotesArray(ArrayList<String> notesArray){
        this.notesArray = notesArray;
    }

    public void clearNotesArray(){
        notesArray.clear();
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

    public void setTreePhotos(HashMap<String, ArrayList<String>> treePhotos){
        this.treePhotos = treePhotos;
    }

    public HashMap<String, ArrayList<String>> getTreePhotos(){
        return treePhotos;
    }

    public void clearTreePhotos(){
        treePhotos.clear();
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
        return currentDBH.get(0);
    }

    public ArrayList<Double> getAllDBHs(){return currentDBH;}

    public void setCurrentDBH(Double currentDBH) {
        if(this.currentDBH == null){
            this.currentDBH = new ArrayList<>();
        }
        this.currentDBH.add(currentDBH);
    }

    public void setCurrentDBH(ArrayList<Double> dbhs){
        this.currentDBH = dbhs;
    }

    public void setDBHArray(ArrayList<Object> dbhArray){
        this.dbhArray = dbhArray;
    }

    public ArrayList<Object> getDBHArray(){
        return dbhArray;
    }

    public void addPics(String key, Object value) {
        if(treePics.get(key) == null){
            ArrayList<Object> arrayList = new ArrayList<>();
            arrayList.add(value);
            treePics.put(key, arrayList);
        }
        treePics.get(key).add(value);
    }

    public Object getPics(String key) {
        return treePics.get(key);
    }

    public String getAllPics() {

        if (treePics.size() == 0) return null;

        String out = "";

        for (String key : treePics.keySet()) {
            out += "\t" + key;// + ": " + treePics.get(key).toString() + "\n";
            for(Object pic : treePics.get(key)){
                out += ": " + pic + "\n";
            }
            out = out.substring(0, out.length()-1);
        }

//        if (out.length() > 0) out = out.substring(0, out.length()-1);
        return out;
    }

    public Collection getPicList(){
        return treePics.values();
    }

    public Boolean isOtherPicsPresent() {
        return treePics.size() > 0;
    }

    // For debugging
    public void setSearchFor(GPSCoordinates gps) {
        searchedfor = gps;
    }

    public GPSCoordinates getSearchedfor() {
        return searchedfor;
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

    public Collection getInfoList(){
        return otherInfo.values();
    }

    public Boolean isOtherInfoPresent() {
        return otherInfo.size() > 0;
    }

    public void clearInfo(){
        otherInfo.clear();
    }

    public void setClosest(float distanceTo){
        closestDist = distanceTo;
    }

    public float getClosestDist(){
        return closestDist;
    }
}
