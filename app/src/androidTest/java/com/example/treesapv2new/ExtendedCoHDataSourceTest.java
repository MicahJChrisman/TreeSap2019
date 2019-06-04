package com.example.treesapv2new;

import android.app.Instrumentation;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.example.treesapv2new.datasource.CityOfHollandDataSource;
import com.example.treesapv2new.datasource.ExtendedCoHDataSource;
import com.example.treesapv2new.datasource.HopeCollegeDataSource;
import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.model.TreeLocation;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
//import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class ExtendedCoHDataSourceTest {
    @Rule
    public ActivityTestRule<Big_Red_Button> mActivityTestRule = new ActivityTestRule<Big_Red_Button>(Big_Red_Button.class);

    private Big_Red_Button brb;
    private ExtendedCoHDataSource ds = new ExtendedCoHDataSource();
    @Before
    public void setUp() throws Exception {
        brb = mActivityTestRule.getActivity();
        ds.initialize(brb.getBaseContext(),null);
    }

    @Test
    public void testBasicInfo(){
        TreeLocation testing = new TreeLocation(42.7878,-86.1057);
        Tree tree = ds.search(testing);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getContext());
        SharedPreferences.Editor editor = prefs.edit();
        HashSet<String> dbs = new HashSet<String>();
        dbs.add("1000");
        editor.putStringSet("distanceFromTreePref", dbs);
        editor.apply();

        assertEquals("Sugar maple",tree.getCommonName());
        assertEquals("Acer saccharum", tree.getScientificName());
        assertEquals(42.787707512, tree.getLocation().getLatitude(), 0.000001);
        assertEquals(-86.107341049, tree.getLocation().getLongitude(), 0.000001);
        assertEquals(13.220000267,tree.getCurrentDBH(),0.01);
        assertEquals("94", tree.getID());
        assertEquals("Centennial Park", tree.getInfo("Park"));
    }


    @Test
    public void testOutsideOfRange(){
        TreeLocation testing2 = new TreeLocation(10.0,-10.0);
        Tree tree2 = ds.search(testing2);
        assertThrows(NullPointerException.class,()-> {tree2.getCommonName();});
        assertThrows(NullPointerException.class,()-> {tree2.getLocation();});
        assertThrows(NullPointerException.class,()-> {tree2.getCurrentDBH();});
        assertThrows(NullPointerException.class,()-> {tree2.getID();});
        assertThrows(NullPointerException.class,()-> {tree2.getAllInfo();});

    }
}
