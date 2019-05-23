package com.example.treesapv2new.display;

import android.content.Context;
import android.widget.PopupWindow;

import java.util.HashMap;

import com.example.treesapv2new.model.SettingsOption;
import com.example.treesapv2new.model.Tree;

public abstract class DisplayMethod {

    Context parent;
    HashMap<String, SettingsOption> preferences;
    static final int STORM_WATER = 9;
    static final int POLLUTION = 13;
    static final int CO2 = 7;
    static final int TOTAL = 15;
    static final int CARBON_LBS = 6;
    static final int WATER_GAL = 8;
    static final int POLLUTION_OZ = 12;

    public DisplayMethod() { parent = null; }
    public DisplayMethod(Context context) {
        parent = context;
    }
    public void setParent(Context context) { parent = context; }

    public abstract String getMethodName();
    public String getDescription() { return null; };

    public abstract void display(Tree tree);

    public PopupWindow getPopupWindow(){
        return null;
    }
    public void dismiss(){}
    public HashMap<String, SettingsOption> getPreferences() {return preferences;}

}
