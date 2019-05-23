package com.example.treesapv2new.identify;

import java.util.ArrayList;

public class IdentificationMethodList {

    private static ArrayList<Class> identificationClasses = new ArrayList<Class>();

    static {
        identificationClasses.add(GPS_Identifier.class);
        identificationClasses.add(SimpleIdentifier.class);
        identificationClasses.add(Google_Map_Identifier.class);
        identificationClasses.add(Barcode_Identifier.class);
    }

    public static ArrayList<Class> getList() {
        return identificationClasses;
    }

    public static Class get(int position) {
        return identificationClasses.get(position);
    }
}
