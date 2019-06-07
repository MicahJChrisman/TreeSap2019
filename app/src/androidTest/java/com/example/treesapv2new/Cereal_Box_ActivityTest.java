package com.example.treesapv2new;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class Cereal_Box_ActivityTest {
    @Rule
    public ActivityTestRule<Cereal_Box_Activity> mActivityTestRule = new ActivityTestRule<Cereal_Box_Activity>(Cereal_Box_Activity.class);
    public ActivityTestRule<Coordinates_View_Activity> cActivityTestRule = new ActivityTestRule<Coordinates_View_Activity>(Coordinates_View_Activity.class);
    Instrumentation.ActivityMonitor treeInfoMonitor = getInstrumentation().addMonitor(Tree_Info_First.class.getName(), null, false);

    private Coordinates_View_Activity cActivity;

    private Tree fullData;
    private Tree noScientificName;
    private Tree noData;
    private TreeLocation[] locations;



    @Before
    public void setUp() throws Exception {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cActivity);
        SharedPreferences.Editor editor = prefs.edit();
        HashSet<String> dbs = new HashSet<String>();
        dbs.add("HopeCollegeDataSource");
        dbs.add("CityOfHollandDataSource");
        dbs.add("ExtendedCoHDataSource");
        dbs.add("ITreeDataSource");
        editor.putStringSet("databasesUsedSelector", dbs);
        editor.apply();
        locations = new TreeLocation[3];
        locations[0] = new TreeLocation(Double.parseDouble("42.78794788"), Double.parseDouble("-86.10832519"));
        locations[1] = new TreeLocation(Double.parseDouble("42.78799703"), Double.parseDouble("-86.10782972"));
        locations[2] = new TreeLocation(Double.parseDouble("42.786762895"), Double.parseDouble("-86.108588638"));
        Set<String> sources = prefs.getStringSet("databasesUsedSelector", new HashSet<String>());
        for(int i = 0; i<3; i++){
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
                ds.initialize(cActivity,null);
                //MainActivity.banana = ds.search(location);
                Tree tree = ds.search(locations[i]);

//            if (MainActivity.banana != null) {
//                if (MainActivity.banana.isFound()) break;  // and NOT just the closest
//            }
                if(tree != null && tree.isFound()){
                    if(i == 0){
                        fullData = tree;
                    }
                    else if(i == 1){
                        noScientificName = tree;
                    }
                    else{
                        noData = tree;
                    }
                }
            }
        }

        cActivity = cActivityTestRule.getActivity();


//        fullData = findTree(new TreeLocation(Double.parseDouble("42.78794788"), Double.parseDouble("-86.10832519")));
//        noScientificName = findTree(new TreeLocation(Double.parseDouble("42.78799703"), Double.parseDouble("-86.10782972")));
//        noData = findTree(new TreeLocation(Double.parseDouble("42.786762895"), Double.parseDouble("-86.108588638")));


        //findTree(fullData);
    }

//    public Tree findTree(TreeLocation location){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
//        Set<String> sources = prefs.getStringSet("databasesUsedSelector",new HashSet<String>());
//        for(String source : sources) {
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
//            ds.initialize(mActivity,null);
//            //MainActivity.banana = ds.search(location);
//            Tree tree = ds.search(location);
//
////            if (MainActivity.banana != null) {
////                if (MainActivity.banana.isFound()) break;  // and NOT just the closest
////            }
//            if(tree != null && tree.isFound()){
//                return tree;
//            }
//        }
//        return null;
//        //Bundle savedInstanceState = new Bundle();
//        //savedInstanceState.put
//    }

    @After
    public void tearDown() throws Exception {
        cActivity = null;
    }

    @Test
    public void onCreateFullData() {
        //Button submit = coordActivity.findViewById(R.id.sub_coord_but);
        onView(withId(R.id.lat_edit)).perform(typeText("42.78794788"));
        onView(withId(R.id.long_edit)).perform(typeText("-86.10832519"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.sub_coord_but)).perform(click());
        assertNotNull(MainActivity.banana);
        Activity mActivity = getInstrumentation().waitForMonitorWithTimeout(treeInfoMonitor, 5000);

        MainActivity.banana = fullData;

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
        //float y = mActivity.testY;
        //assertEquals(196.0, y, 0);
        ImageView treePic = mActivity.findViewById(R.id.user_add_tree_pic_appear_cereal);
        assertNotNull(treePic);
    }

//    @Test
//    public void onCreateNoScientificName() {
//        //findTree(noScientificName);
////        Bundle savedInstance = new Bundle(new);
//        MainActivity.banana = noScientificName;
//        assertNotNull(MainActivity.banana);
//        TextView title = mActivity.findViewById(R.id.cereal_chart_benefits);
//        assertNotNull(title);
//        ImageView cerealImage = mActivity.findViewById(R.id.cereal_image);
//        assertNotNull(cerealImage);
//        assertEquals(0, cerealImage.getVisibility());
//        TextView noDataMessage = mActivity.findViewById(R.id.no_ben);
//        assertNotNull(noDataMessage);
//        assertEquals(8, noDataMessage.getVisibility());
//        ImageButton addNotes = mActivity.findViewById(R.id.add_notes);
//        assertNotNull(addNotes);
//        assertEquals(116.0, mActivity.testY, 0);
//        ImageView treePic = mActivity.findViewById(R.id.user_add_tree_pic_appear_cereal);
//        assertNotNull(treePic);
//    }
//
//    @Test
//    public void onCreateNoData() {
//        //findTree(noData);
//        MainActivity.banana = noData;
//        assertNotNull(MainActivity.banana);
//        TextView title = mActivity.findViewById(R.id.cereal_chart_benefits);
//        assertNotNull(title);
//        ImageView cerealImage = mActivity.findViewById(R.id.cereal_image);
//        assertNotNull(cerealImage);
//        assertEquals(8, cerealImage.getVisibility());
//        TextView noDataMessage = mActivity.findViewById(R.id.no_ben);
//        assertNotNull(noDataMessage);
//        assertEquals(0, noDataMessage.getVisibility());
//        ImageButton addNotes = mActivity.findViewById(R.id.add_notes);
//        assertNotNull(addNotes);
//    }
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