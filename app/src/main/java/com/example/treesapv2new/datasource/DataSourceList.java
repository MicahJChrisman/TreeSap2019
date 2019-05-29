package com.example.treesapv2new.datasource;

import java.util.ArrayList;

public class DataSourceList {

    private static ArrayList<Class> dataSourceClasses = new ArrayList<Class>();

    static {
        dataSourceClasses.add(CityOfHollandDataSource.class);
        dataSourceClasses.add(HopeCollegeDataSource.class);
        dataSourceClasses.add(ITreeDataSource.class);
        dataSourceClasses.add(ExtendedCoHDataSource.class);
    }

    public static ArrayList<Class> getList() {
        return dataSourceClasses;
    }

    public static Class get(int position) {
        return dataSourceClasses.get(position);
    }

}
