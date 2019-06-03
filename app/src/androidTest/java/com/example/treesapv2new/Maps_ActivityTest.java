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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class Maps_ActivityTest {
    @Rule
    public ActivityTestRule<Maps_Activity> mActivityTestRule = new ActivityTestRule<Maps_Activity>(Maps_Activity.class);

    private Maps_Activity mActivity;

    Instrumentation.ActivityMonitor monitor0 = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor1 = getInstrumentation().addMonitor(Big_Red_Button.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor2 = getInstrumentation().addMonitor(Coordinates_View_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor4 = getInstrumentation().addMonitor(QR_Code_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor settingsMonitor = getInstrumentation().addMonitor(SettingsActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor addTreeMonitor = getInstrumentation().addMonitor(Add_Tree_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor cerealBoxMonitor = getInstrumentation().addMonitor(Cereal_Box_Activity.class.getName(), null, false);


    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    @Test
    public void onCreate() {
        ImageButton addButton = mActivity.findViewById(R.id.add_tree_button_2);
        assertNotNull(addButton);
        ImageButton settingsButton = mActivity.findViewById(R.id.setting_button_2);
        assertNotNull(settingsButton);
        View map = mActivity.findViewById(R.id.map);
        assertNotNull(map);
        TextView title = mActivity.findViewById(R.id.button_button);
        assertNotNull(title);
    }

    @Test
    public void testGoToMainFromNavigation(){
        BottomNavigationView navView = mActivity.findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        assertNotNull(menu.getItem(0));
        onView(withId(R.id.navigation_home)).perform(click());
        Activity mainActivity = getInstrumentation().waitForMonitorWithTimeout(monitor0, 5000);
        assertNotNull(mainActivity);
    }

    @Test
    public void testGoToBrbActivityFromNavigation(){
        BottomNavigationView navView = mActivity.findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        assertNotNull(menu.getItem(1));
        onView(withId(R.id.navigation_brb)).perform(click());
        Activity coordinateActivity = getInstrumentation().waitForMonitorWithTimeout(monitor1, 5000);
        assertNotNull(coordinateActivity);
    }

    @Test
    public void testGoToCoordinatesFromNavigation(){
        BottomNavigationView navView = mActivity.findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        assertNotNull(menu.getItem(2));
        onView(withId(R.id.navigation_coordinates)).perform(click());
        Activity mapActivity = getInstrumentation().waitForMonitorWithTimeout(monitor2, 5000);
        assertNotNull(mapActivity);
    }

    @Test
    public void testGoToQrScannerFromNavigation(){
        BottomNavigationView navView = mActivity.findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        assertNotNull(menu.getItem(4));
        onView(withId(R.id.navigation_qr)).perform(click());
        Activity qrActivity = getInstrumentation().waitForMonitorWithTimeout(monitor4, 5000);
        assertNotNull(qrActivity);
    }

    @Test
    public void testGoToSettings(){
        ImageButton settingsBtn = mActivity.findViewById(R.id.setting_button_2);
        assertNotNull(settingsBtn);
        //settingsBtn.performClick();
        onView(withId(R.id.setting_button_2)).perform(click());
        Activity settingsActivity = getInstrumentation().waitForMonitorWithTimeout(settingsMonitor, 5000);
        assertNotNull(settingsActivity);
    }

    @Test
    public void testGoToAddTree(){
        ImageButton addTreeBtn = mActivity.findViewById(R.id.add_tree_button_2);
        assertNotNull(addTreeBtn);
        //settingsBtn.performClick();
        onView(withId(R.id.add_tree_button_2)).perform(click());
        Activity addTreeActivity = getInstrumentation().waitForMonitorWithTimeout(addTreeMonitor, 5000);
        assertNotNull(addTreeActivity);
    }



//    @Test
//    public void onConnected() {
//    }
//
//    @Test
//    public void onConnectionSuspended() {
//    }
//
//    @Test
//    public void onConnectionFailed() {
//    }
//
//    @Test
//    public void onTouchEvent() {
//
//    }
//
//    @Test
//    public void onMapReady() {
//    }
//
//    @Test
//    public void onLocationChanged() {
//    }
}