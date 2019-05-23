package com.example.treesapv2new.datasource;

/*
 *  A debugging gimmick.  Place some specific tree locations into a HashMap and
 *  look for specific keys.
 */

import android.content.Context;

import java.util.HashMap;

import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.model.TreeLocation;

public class HashMapDataSource extends DataSource {

   HashMap<String, String> treemap;

   public HashMapDataSource() {
       treemap = new HashMap<String, String>();
   }

    @Override
    public String getSourceName() {
        return "Debugger Data Source";
    }

    @Override
    public Boolean initialize(Context aParent, String initString) {

        treemap.put("(90.0, 0.0)", "Northern Debugger");  // GPS coordinates of the North Pole
        treemap.put("(-90.0, 0.0)", "Southern Debugger");  // GPS coordinates of the South Pole

        return true;
    }

    @Override
    public Tree search(TreeLocation location) {
        Tree tree = null;
        if (treemap.containsKey(location.toString())) {
            tree = new Tree();
            tree.setLocation(location);
            tree.setCommonName(treemap.get(location.toString()));
        }
        return tree;
    }

    @Override
    public Boolean isDownloadable() {
       return false;
    }

    @Override
    public Boolean checkForUpdate() {
        return null;
    }

    @Override
    public String getDescription() {
        return "the debugger data source.";
    }

    @Override
    public Boolean updateDataSource() {
        return null;
    }

    @Override
    public Boolean isUpToDate() {
        return null;
    }

}


