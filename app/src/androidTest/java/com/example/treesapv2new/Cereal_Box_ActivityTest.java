package com.example.treesapv2new;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.treesapv2new.datasource.CityOfHollandDataSource;
import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.ExtendedCoHDataSource;
import com.example.treesapv2new.datasource.HopeCollegeDataSource;
import com.example.treesapv2new.datasource.ITreeDataSource;
import com.example.treesapv2new.model.TreeLocation;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class Cereal_Box_ActivityTest {
    @Rule
    public ActivityTestRule<Cereal_Box_Activity> mActivityTestRule = new ActivityTestRule<Cereal_Box_Activity>(Cereal_Box_Activity.class);

    private Cereal_Box_Activity mActivity;

//    @Before
//    public void setUp() throws Exception {
//        mActivity = mActivityTestRule.getActivity();
//        TreeLocation testing = new TreeLocation(latitude,longitude);
//
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        Set<String> sources = prefs.getStringSet("databasesUsedSelector",new HashSet<String>());
//        for (String source : sources) {
//            Log.d("MainActivity", "Searching.  Trying: "+source);
//            DataSource ds;
//            if(source.equals("HopeCollegeDataSource")){
//                ds = new HopeCollegeDataSource();
//            }else if(source.equals("CityOfHollandDataSource")) {
//                ds = new CityOfHollandDataSource();
//            }else if(source.equals("ExtendedCoHDataSource")){
//                ds = new ExtendedCoHDataSource();
//            }else{
//                ds = new ITreeDataSource();
//            }
//            ds.initialize(Big_Red_Button.this,null);
//            MainActivity.banana = ds.search(testing);
//
//            if (MainActivity.banana != null) {
//                if (MainActivity.banana.isFound()) break;  // and NOT just the closest
//            }
//        }
//        MainActivity.banana =
//    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;

    }

    @Test
    public void onCreate() {
        assertNotNull(MainActivity.banana);
        TextView title = mActivity.findViewById(R.id.cereal_chart_benefits);
        assertNotNull(title);
        ImageView cerealImage = mActivity.findViewById(R.id.cereal_image);
        assertNotNull(cerealImage);

    }

    @Test
    public void dpToPx() {
    }

    @Test
    public void pxToDp() {
    }

    @Test
    public void findInfo() {
    }

    @Test
    public void display() {
    }

    @Test
    public void getPopupWindow() {
    }

    @Test
    public void dismiss() {
    }

    @Test
    public void onTouchEvent() {
    }

    @Test
    public void showPopup() {
    }

    @Test
    public void onActivityResult() {
    }
}