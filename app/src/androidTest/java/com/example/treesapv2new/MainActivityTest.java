package com.example.treesapv2new;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.design.widget.BottomNavigationView;
import android.support.test.rule.ActivityTestRule;
import android.text.Layout;
import android.transition.Scene;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParentIndex;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mActivity;

    Instrumentation.ActivityMonitor monitor1 = getInstrumentation().addMonitor(Big_Red_Button.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor2 = getInstrumentation().addMonitor(Coordinates_View_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor3 = getInstrumentation().addMonitor(Maps_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor4 = getInstrumentation().addMonitor(QR_Code_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor settingsMonitor = getInstrumentation().addMonitor(SettingsActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor addTreeMonitor = getInstrumentation().addMonitor(Add_Tree_Activity.class.getName(), null, false);


    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    /**
     * UI tests
     */
    @Test
    public void onCreate() {
        ImageView logo = mActivity.findViewById(R.id.treesap_logo);
        assertNotNull(logo);
        TextView title = mActivity.findViewById(R.id.home_header);
        assertNotNull(title);
        TextView message = mActivity.findViewById(R.id.message);
        assertNotNull(message);
        TextView message1 = mActivity.findViewById(R.id.message1);
        assertNotNull(message1);
        TextView message2 = mActivity.findViewById(R.id.message2);
        assertNotNull(message2);
    }

    @Test
    public void testGoToBRBFromNavigation(){
        BottomNavigationView navView = mActivity.findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        assertNotNull(menu.getItem(1));
        onView(withId(R.id.navigation_brb)).perform(click());
        Activity brbActivity = getInstrumentation().waitForMonitorWithTimeout(monitor1, 5000);
        assertNotNull(brbActivity);
    }

    @Test
    public void testGoToCoordinateActivityFromNavigation(){
        BottomNavigationView navView = mActivity.findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        assertNotNull(menu.getItem(2));
        onView(withId(R.id.navigation_coordinates)).perform(click());
        Activity coordinateActivity = getInstrumentation().waitForMonitorWithTimeout(monitor2, 5000);
        assertNotNull(coordinateActivity);
    }

    @Test
    public void testGoToMapFromNavigation(){
        BottomNavigationView navView = mActivity.findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        assertNotNull(menu.getItem(3));
        onView(withId(R.id.navigation_map)).perform(click());
        Activity mapActivity = getInstrumentation().waitForMonitorWithTimeout(monitor3, 5000);
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
        ImageButton settingsBtn = mActivity.findViewById(R.id.settings);
        assertNotNull(settingsBtn);
        //settingsBtn.performClick();
        onView(withId(R.id.settings)).perform(click());
        Activity settingsActivity = getInstrumentation().waitForMonitorWithTimeout(settingsMonitor, 5000);
        assertNotNull(settingsActivity);
    }

    @Test
    public void testGoToAddTree(){
        ImageButton addTreeBtn = mActivity.findViewById(R.id.add_tree_button_0);
        assertNotNull(addTreeBtn);
        //settingsBtn.performClick();
        onView(withId(R.id.add_tree_button_0)).perform(click());
        Activity addTreeActivity = getInstrumentation().waitForMonitorWithTimeout(addTreeMonitor, 5000);
        assertNotNull(addTreeActivity);
    }

    @Test
    public void testGoToBRBFromFling(){
//        ViewGroup viewGroup = mActivity.getContentScene().getSceneRoot();
//        assertNotNull(viewGroup);
        onView(isRoot()).perform(swipeLeft());
        Activity brbActivity = getInstrumentation().waitForMonitorWithTimeout(monitor1, 5000);
        assertNotNull(brbActivity);
    }

//    @Test
//    public void testBackButton(){
//        pressBack();
//        Scene scene = mActivity.getContentScene();
//        onView(isRoot()).check()
//    }





//    @Test
//    public void onTouchEvent() {
//    }
//
//    @Test
//    public void onRequestPermissionsResult() {
//    }
//
//    @Test
//    public void onBackPressed() {
//    }
//
//    @Test
//    public void onActivityResult() {
//    }
//
//    @Test
//    public void setSelectedIDMethod() {
//    }
//
//    @Test
//    public void getSelectedIDMethod() {
//    }
//
//    @Test
//    public void removeSelectedIDMethod() {
//    }
//
//    @Test
//    public void addSelectedDataSource() {
//    }
//
//    @Test
//    public void clearDataSources() {
//    }
//
//    @Test
//    public void getSelectedDataSources() {
//    }
//
//    @Test
//    public void setSelectedDisplayMethod() {
//    }
//
//    @Test
//    public void getSelectedDisplayMethod() {
//    }
}