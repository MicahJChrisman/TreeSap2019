package com.example.treesapv2new;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.design.widget.BottomNavigationView;
import android.support.test.rule.ActivityTestRule;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class Coordinates_View_ActivityTest {
    @Rule
    public ActivityTestRule<Coordinates_View_Activity> mActivityTestRule = new ActivityTestRule<Coordinates_View_Activity>(Coordinates_View_Activity.class);

    private Coordinates_View_Activity coordActivity;

    Instrumentation.ActivityMonitor monitor0 = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor1 = getInstrumentation().addMonitor(Big_Red_Button.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor3 = getInstrumentation().addMonitor(Maps_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor4 = getInstrumentation().addMonitor(QR_Code_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor settingsMonitor = getInstrumentation().addMonitor(SettingsActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor addTreeMonitor = getInstrumentation().addMonitor(Add_Tree_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor cerealBoxMonitor = getInstrumentation().addMonitor(Cereal_Box_Activity.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        coordActivity = mActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        coordActivity = null;
    }

    @Test
    public void onCreate() {
        ImageButton addButton = coordActivity.findViewById(R.id.add_tree_button_0);
        assertNotNull(addButton);
        ImageButton settingsButton = coordActivity.findViewById(R.id.setting_button);
        assertNotNull(settingsButton);
        TextView instructions = coordActivity.findViewById(R.id.coordinates_instructions);
        assertNotNull(instructions);
        TextView title = coordActivity.findViewById(R.id.coordinates_header);
        assertNotNull(title);
        TextView latText = coordActivity.findViewById(R.id.latitude_coord);
        assertNotNull(latText);
        TextView longi = coordActivity.findViewById(R.id.longitude_coord);
        assertNotNull(longi);
        EditText latInput = coordActivity.findViewById(R.id.lat_edit);
        assertNotNull(latInput);
        TextView longInput = coordActivity.findViewById(R.id.long_edit);
        assertNotNull(longInput);
        Button submit = coordActivity.findViewById(R.id.sub_coord_but);
        assertNotNull(submit);
    }@Test
    public void testGoToMainFromNavigation(){
        BottomNavigationView navView = coordActivity.findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        assertNotNull(menu.getItem(0));
        onView(withId(R.id.navigation_home)).perform(click());
        Activity mainActivity = getInstrumentation().waitForMonitorWithTimeout(monitor0, 5000);
        assertNotNull(mainActivity);
    }

    @Test
    public void testGoToBrbActivityFromNavigation(){
        BottomNavigationView navView = coordActivity.findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        assertNotNull(menu.getItem(1));
        onView(withId(R.id.navigation_brb)).perform(click());
        Activity coordinateActivity = getInstrumentation().waitForMonitorWithTimeout(monitor1, 5000);
        assertNotNull(coordinateActivity);
    }

    @Test
    public void testGoToMapFromNavigation(){
        BottomNavigationView navView = coordActivity.findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        assertNotNull(menu.getItem(3));
        onView(withId(R.id.navigation_map)).perform(click());
        Activity mapActivity = getInstrumentation().waitForMonitorWithTimeout(monitor3, 5000);
        assertNotNull(mapActivity);
    }

    @Test
    public void testGoToQrScannerFromNavigation(){
        BottomNavigationView navView = coordActivity.findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        assertNotNull(menu.getItem(4));
        onView(withId(R.id.navigation_qr)).perform(click());
        Activity qrActivity = getInstrumentation().waitForMonitorWithTimeout(monitor4, 5000);
        assertNotNull(qrActivity);
    }

    @Test
    public void testGoToSettings(){
        ImageButton settingsBtn = coordActivity.findViewById(R.id.setting_button);
        assertNotNull(settingsBtn);
        //settingsBtn.performClick();
        onView(withId(R.id.setting_button)).perform(click());
        Activity settingsActivity = getInstrumentation().waitForMonitorWithTimeout(settingsMonitor, 5000);
        assertNotNull(settingsActivity);
    }

    @Test
    public void testGoToAddTree(){
        ImageButton addTreeBtn = coordActivity.findViewById(R.id.add_tree_button_0);
        assertNotNull(addTreeBtn);
        //settingsBtn.performClick();
        onView(withId(R.id.add_tree_button_0)).perform(click());
        Activity addTreeActivity = getInstrumentation().waitForMonitorWithTimeout(addTreeMonitor, 5000);
        assertNotNull(addTreeActivity);
    }


    @Test
    public void onSubmitPressed(){
        //Button submit = coordActivity.findViewById(R.id.sub_coord_but);
        onView(withId(R.id.lat_edit)).perform(typeText("42.78773499"));
        onView(withId(R.id.long_edit)).perform(typeText("-86.10578919"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.sub_coord_but)).perform(click());
        assertNotNull(MainActivity.banana);
        Activity cerealBoxActivity = getInstrumentation().waitForMonitorWithTimeout(cerealBoxMonitor, 5000);
        assertNotNull(cerealBoxActivity);
    }

    @Test
    public void onFlings(){
        onView(isRoot()).perform(swipeRight());
        Activity brbActivity = getInstrumentation().waitForMonitorWithTimeout(monitor1, 5000);
        assertNotNull(brbActivity);
        onView(isRoot()).perform(swipeLeft());
        Activity mapActivity = getInstrumentation().waitForMonitorWithTimeout(monitor3, 5000);
        assertNotNull(mapActivity);
    }

//    @Test
//    public void onTouchEvent() {
//    }
}