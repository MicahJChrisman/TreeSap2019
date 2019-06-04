package com.example.treesapv2new;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.treesapv2new.datasource.CityOfHollandDataSource;
import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.ExtendedCoHDataSource;
import com.example.treesapv2new.datasource.HopeCollegeDataSource;
import com.example.treesapv2new.datasource.ITreeDataSource;
import com.example.treesapv2new.model.Tree;
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
    private TreeLocation fullData;
    private TreeLocation noScientificName;
    private TreeLocation noData;

    /**
     * TODO: Should add tests about tree picture once that feature is implemented
     */

    @Before
    public void setUp() throws Exception {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        Set<String> sources = prefs.getStringSet("databasesUsedSelector", new HashSet<String>());
        SharedPreferences.Editor editor = prefs.edit();
        HashSet<String> dbs = new HashSet<String>();
        dbs.add("HopeCollegeDataSource");
        dbs.add("CityOfHollandDataSource");
        dbs.add("ExtendedCoHDataSource");
        dbs.add("ITreeDataSource");
        editor.putStringSet("databasesUsedSelector", dbs);
        editor.apply();
        fullData = new TreeLocation(Double.parseDouble("42.78794788"), Double.parseDouble("-86.10832519"));
        noScientificName = new TreeLocation(Double.parseDouble("42.78799703"), Double.parseDouble("-86.10782972"));
        noData = new TreeLocation(Double.parseDouble("42.786762895"), Double.parseDouble("-86.108588638"));
        findTree(fullData);

        mActivity = mActivityTestRule.getActivity();
    }

    public void findTree(TreeLocation location){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        Set<String> sources = prefs.getStringSet("databasesUsedSelector",new HashSet<String>());
        for(String source : sources) {
            Log.d("MainActivity", "Searching.  Trying: "+source);
            DataSource ds;
            if(source.equals("HopeCollegeDataSource")){
                ds = new HopeCollegeDataSource();
            }else if(source.equals("CityOfHollandDataSource")) {
                ds = new CityOfHollandDataSource();
            }else if(source.equals("ExtendedCoHDataSource")){
                ds = new ExtendedCoHDataSource();
            }else{
                ds = new ITreeDataSource();
            }
            ds.initialize(mActivity,null);
            MainActivity.banana = ds.search(location);

            if (MainActivity.banana != null) {
                if (MainActivity.banana.isFound()) break;  // and NOT just the closest
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;

    }

    @Test
    public void onCreateFullData() {
        findTree(fullData);
        assertNotNull(MainActivity.banana);
        TextView title = mActivity.findViewById(R.id.cereal_chart_benefits);
        assertNotNull(title);
        ImageView cerealImage = mActivity.findViewById(R.id.cereal_image);
        assertNotNull(cerealImage);
        assertEquals(0, cerealImage.getVisibility());
        TextView noDataMessage = mActivity.findViewById(R.id.no_ben);
        assertNotNull(noDataMessage);
        assertEquals(8, noDataMessage.getVisibility());
        ImageButton addNotes = mActivity.findViewById(R.id.add_notes);
        assertNotNull(addNotes);
        float y = mActivity.testY;
        assertEquals(196.0, y, 0);
        ImageView treePic = mActivity.findViewById(R.id.user_add_tree_pic_appear_cereal);
        assertNotNull(treePic);
    }

    @Test
    public void onCreateNoScientificName() {
        findTree(noScientificName);
        assertNotNull(MainActivity.banana);
        TextView title = mActivity.findViewById(R.id.cereal_chart_benefits);
        assertNotNull(title);
        ImageView cerealImage = mActivity.findViewById(R.id.cereal_image);
        assertNotNull(cerealImage);
        assertEquals(0, cerealImage.getVisibility());
        TextView noDataMessage = mActivity.findViewById(R.id.no_ben);
        assertNotNull(noDataMessage);
        assertEquals(8, noDataMessage.getVisibility());
        ImageButton addNotes = mActivity.findViewById(R.id.add_notes);
        assertNotNull(addNotes);
        assertEquals(116.0, mActivity.testY, 0);
        ImageView treePic = mActivity.findViewById(R.id.user_add_tree_pic_appear_cereal);
        assertNotNull(treePic);
    }

    @Test
    public void onCreateNoData() {
        findTree(noData);
        assertNotNull(MainActivity.banana);
        TextView title = mActivity.findViewById(R.id.cereal_chart_benefits);
        assertNotNull(title);
        ImageView cerealImage = mActivity.findViewById(R.id.cereal_image);
        assertNotNull(cerealImage);
        assertEquals(8, cerealImage.getVisibility());
        TextView noDataMessage = mActivity.findViewById(R.id.no_ben);
        assertNotNull(noDataMessage);
        assertEquals(0, noDataMessage.getVisibility());
        ImageButton addNotes = mActivity.findViewById(R.id.add_notes);
        assertNotNull(addNotes);
    }
//
//    @Test
//    public void dpToPx() {
//    }
//
//    @Test
//    public void pxToDp() {
//    }
//
//    @Test
//    public void findInfo() {
//    }
//
//    @Test
//    public void display() {
//    }
//
//    @Test
//    public void getPopupWindow() {
//    }
//
//    @Test
//    public void dismiss() {
//    }
//
//    @Test
//    public void onTouchEvent() {
//    }
//
//    @Test
//    public void showPopup() {
//    }
//
//    @Test
//    public void onActivityResult() {
//    }
}