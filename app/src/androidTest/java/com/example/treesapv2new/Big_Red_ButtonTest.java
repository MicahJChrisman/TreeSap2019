package com.example.treesapv2new;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.design.widget.BottomNavigationView;
import android.support.test.rule.ActivityTestRule;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class Big_Red_ButtonTest {
    @Rule
    public ActivityTestRule<Big_Red_Button> mActivityTestRule = new ActivityTestRule<Big_Red_Button>(Big_Red_Button.class);

    private Big_Red_Button brbActivity;

    Instrumentation.ActivityMonitor monitor0 = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor2 = getInstrumentation().addMonitor(Coordinates_View_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor3 = getInstrumentation().addMonitor(Maps_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor4 = getInstrumentation().addMonitor(QR_Code_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor settingsMonitor = getInstrumentation().addMonitor(SettingsActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor addTreeMonitor = getInstrumentation().addMonitor(Add_Tree_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor cerealBoxMonitor = getInstrumentation().addMonitor(Cereal_Box_Activity.class.getName(), null, false);


    @Before
    public void setUp() throws Exception {
        brbActivity = mActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        brbActivity = null;
    }

    /**
     * Fling test is not fixed
     */

    @Test
    public void testGoToMainFromNavigation(){
        BottomNavigationView navView = brbActivity.findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        assertNotNull(menu.getItem(0));
        onView(withId(R.id.navigation_home)).perform(click());
        Activity mainActivity = getInstrumentation().waitForMonitorWithTimeout(monitor0, 5000);
        assertNotNull(mainActivity);
    }

    @Test
    public void testGoToCoordinateActivityFromNavigation(){
        BottomNavigationView navView = brbActivity.findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        assertNotNull(menu.getItem(2));
        onView(withId(R.id.navigation_coordinates)).perform(click());
        Activity coordinateActivity = getInstrumentation().waitForMonitorWithTimeout(monitor2, 5000);
        assertNotNull(coordinateActivity);
    }

    @Test
    public void testGoToMapFromNavigation(){
        BottomNavigationView navView = brbActivity.findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        assertNotNull(menu.getItem(3));
        onView(withId(R.id.navigation_map)).perform(click());
        Activity mapActivity = getInstrumentation().waitForMonitorWithTimeout(monitor3, 5000);
        assertNotNull(mapActivity);
    }

    @Test
    public void testGoToQrScannerFromNavigation(){
        BottomNavigationView navView = brbActivity.findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        assertNotNull(menu.getItem(4));
        onView(withId(R.id.navigation_qr)).perform(click());
        Activity qrActivity = getInstrumentation().waitForMonitorWithTimeout(monitor4, 5000);
        assertNotNull(qrActivity);
    }

    @Test
    public void testGoToSettings(){
        ImageButton settingsBtn = brbActivity.findViewById(R.id.setting_button_1);
        assertNotNull(settingsBtn);
        //settingsBtn.performClick();
        onView(withId(R.id.setting_button_1)).perform(click());
        Activity settingsActivity = getInstrumentation().waitForMonitorWithTimeout(settingsMonitor, 5000);
        assertNotNull(settingsActivity);
    }

    @Test
    public void testGoToAddTree(){
        ImageButton addTreeBtn = brbActivity.findViewById(R.id.add_tree_button_1);
        assertNotNull(addTreeBtn);
        //settingsBtn.performClick();
        onView(withId(R.id.add_tree_button_1)).perform(click());
        Activity addTreeActivity = getInstrumentation().waitForMonitorWithTimeout(addTreeMonitor, 5000);
        assertNotNull(addTreeActivity);
    }

//    @Test
//    public void testFlings(){
//        Object testView = onView(isRoot());
//        onView(allOf(isRoot(), not(isClickable()))).perform(swipeRight());
//        //onView(withId(R.id.relativeLayout)).perform(swipeRight());
//        Activity mainActivity = getInstrumentation().waitForMonitorWithTimeout(monitor0, 5000);
//        assertNotNull(mainActivity);
//
//        onView(isRoot()).perform(swipeLeft());
//        Activity coordinateActivity = getInstrumentation().waitForMonitorWithTimeout(monitor2, 5000);
//        assertNotNull(coordinateActivity);
//    }

    @Test
    public void onCreate() {
        View brb = brbActivity.findViewById(R.id.bigredbutton);
        assertNotNull(brb);
        TextView title = brbActivity.findViewById(R.id.button_button);
        assertNotNull(title);
    }

    @Test
    public void onBrbPressed(){
        ImageButton brb = brbActivity.findViewById(R.id.bigredbutton);
        onView(withId(R.id.bigredbutton)).perform(click());
        assertNotNull(MainActivity.banana);
        Activity cerealBoxActivity = getInstrumentation().waitForMonitorWithTimeout(cerealBoxMonitor, 5000);
        assertNotNull(cerealBoxActivity);
    }


//    @Test
//    public void getLocation() {
//
//    }
//
//    @Test
//    public void onLocationChanged() {
//    }
//
//    @Test
//    public void onStatusChanged() {
//    }
//
//    @Test
//    public void onProviderEnabled() {
//    }
//
//    @Test
//    public void onProviderDisabled() {
//    }
//
//    @Test
//    public void onTouchEvent() {
//    }
}