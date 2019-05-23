package com.example.treesapv2new.display;

import java.util.ArrayList;

public class DisplayMethodList {

    private static ArrayList<Class> displayMethodClasses = new ArrayList<Class>();

    static {
        displayMethodClasses.add(SimpleDisplay.class);
        displayMethodClasses.add(CerealBoxDisplay.class);
        displayMethodClasses.add(PieChartDisplay.class);
    }

    public static ArrayList<Class> getList() {
        return displayMethodClasses;
    }

    public static Class get(int position) {
        return displayMethodClasses.get(position);
    }

}
