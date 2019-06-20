package com.example.treesapv2new;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.text.Layout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class Tree_Info_FirstTest {
    @Rule
    public ActivityTestRule<Tree_Info_First> mActivityTestRule = new ActivityTestRule<Tree_Info_First>(Tree_Info_First.class);
    public ActivityTestRule<Coordinates_View_Activity> cActivityTestRule = new ActivityTestRule<Coordinates_View_Activity>(Coordinates_View_Activity.class);
    Instrumentation.ActivityMonitor treeInfoMonitor = getInstrumentation().addMonitor(Tree_Info_First.class.getName(), null, false);

    Instrumentation.ActivityMonitor monitor0 = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor1 = getInstrumentation().addMonitor(Big_Red_Button.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor3 = getInstrumentation().addMonitor(Maps_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor4 = getInstrumentation().addMonitor(QR_Code_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor addTreeMonitor = getInstrumentation().addMonitor(Add_Tree_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor cerealMonitor = getInstrumentation().addMonitor(Cereal_Box_Activity.class.getName(), null, false);

    private Coordinates_View_Activity cActivity;

    private Tree_Info_First mActivity;

    @Before
    public void setUp() throws Exception {
        cActivity = cActivityTestRule.getActivity();

    }

    @After
    public void tearDown() throws Exception {
        cActivity =  null;
    }

    @Test
    public void onCreate() {
        onView(withId(R.id.lat_edit)).perform(typeText("42.78773499"));
        onView(withId(R.id.long_edit)).perform(typeText("-86.10578919"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.sub_coord_but)).perform(click());
        assertNotNull(MainActivity.banana);
        Activity mActivity = getInstrumentation().waitForMonitorWithTimeout(treeInfoMonitor, 5000);
        LinearLayout blackLayout = mActivity.findViewById(R.id.blackLayout);
        assertNotNull(blackLayout);
        TextView commonName = mActivity.findViewById(R.id.CommonName);
        assertNotEquals("", commonName.getText());
        TextView sciName = mActivity.findViewById(R.id.scientificName);
        assertNotEquals("", sciName.getText());
        LinearLayout idLayout = mActivity.findViewById(R.id.idLayout);
        assertNotNull(idLayout);
        TextView idTitle = mActivity.findViewById(R.id.idTitle);
        assertNotNull(idTitle);
        TextView id = mActivity.findViewById(R.id.treeid);
        assertNotEquals("", id.getText());
        LinearLayout dbhLayout = mActivity.findViewById(R.id.dbhLayout);
        assertNotNull(dbhLayout);
        TextView dbhTitle = mActivity.findViewById(R.id.dbhTitle);
        assertNotNull(dbhTitle);
        TextView dbh = mActivity.findViewById(R.id.dbh);
        assertNotEquals("", dbh.getText());
        LinearLayout latLayout = mActivity.findViewById(R.id.latLayout);
        assertNotNull(latLayout);
        TextView latTitle = mActivity.findViewById(R.id.latitudeTitle);
        assertNotNull(latTitle);
        TextView lat = mActivity.findViewById(R.id.latitude);
        assertNotEquals("", lat.getText());
        LinearLayout longLayout = mActivity.findViewById(R.id.longLayout);
        assertNotNull(longLayout);
        TextView longTitle = mActivity.findViewById(R.id.longitudeTitle);
        assertNotNull(longTitle);
        TextView longi = mActivity.findViewById(R.id.longitude);
        assertNotEquals("", longi.getText());
        LinearLayout otherLayout = mActivity.findViewById(R.id.otherLayout);
        assertNotNull(otherLayout);
        TextView otherTitle = mActivity.findViewById(R.id.otherTitle);
        assertNotNull(otherTitle);
        TextView otherInfo = mActivity.findViewById(R.id.otherInfo);
        assertNotEquals("", otherInfo.getText());
        ImageButton addButton = mActivity.findViewById(R.id.add3);
        assertNotNull(addButton);
        TextView swipeInstruct = mActivity.findViewById(R.id.swipe_instructions);
        assertNotNull(swipeInstruct);
    }



//    @Test
//    public void showPopup() {
//    }
//
//    @Test
//    public void onTouchEvent() {
//    }
}