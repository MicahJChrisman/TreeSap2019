package com.example.treesapv2new;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.BottomNavigationView;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.view.Menu;
import android.view.SurfaceView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasPackage;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class QR_Code_ActivityTest {
    @Rule
    public ActivityTestRule<QR_Code_Activity> mActivityTestRule = new ActivityTestRule<QR_Code_Activity>(QR_Code_Activity.class);
//    @Rule
    public IntentsTestRule<QR_Code_Activity> intentsTestRule = new IntentsTestRule<>(QR_Code_Activity.class);

    private QR_Code_Activity mActivity;

    Instrumentation.ActivityMonitor monitor0 = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor1 = getInstrumentation().addMonitor(Big_Red_Button.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor2 = getInstrumentation().addMonitor(Coordinates_View_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitor3 = getInstrumentation().addMonitor(Maps_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor settingsMonitor = getInstrumentation().addMonitor(SettingsActivity.class.getName(), null, false);
    Instrumentation.ActivityMonitor addTreeMonitor = getInstrumentation().addMonitor(Add_Tree_Activity.class.getName(), null, false);
    Instrumentation.ActivityMonitor treeInfoMonitor = getInstrumentation().addMonitor(Tree_Info_First.class.getName(), null, false);


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
        ImageButton addButton = mActivity.findViewById(R.id.add_tree_button_3);
        assertNotNull(addButton);
        ImageButton settingsButton = mActivity.findViewById(R.id.setting_button_3);
        assertNotNull(settingsButton);
        TextView title = mActivity.findViewById(R.id.qr_header);
        assertNotNull(title);
        SurfaceView cameraView = mActivity.findViewById(R.id.cameraPreview);
        assertNotNull(cameraView);
        ImageView qrSquare = mActivity.findViewById(R.id.qr_square);
        assertNotNull(qrSquare);
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
    public void testGoToMapFromNavigation(){
        BottomNavigationView navView = mActivity.findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        assertNotNull(menu.getItem(3));
        onView(withId(R.id.navigation_map)).perform(click());
        Activity mapActivity = getInstrumentation().waitForMonitorWithTimeout(monitor3, 5000);
        assertNotNull(mapActivity);
    }

    @Test
    public void testGoToSettings(){
        ImageButton settingsBtn = mActivity.findViewById(R.id.setting_button_3);
        assertNotNull(settingsBtn);
        //settingsBtn.performClick();
        onView(withId(R.id.setting_button_3)).perform(click());
        Activity settingsActivity = getInstrumentation().waitForMonitorWithTimeout(settingsMonitor, 5000);
        assertNotNull(settingsActivity);
    }

    @Test
    public void testGoToAddTree(){
        ImageButton addTreeBtn = mActivity.findViewById(R.id.add_tree_button_3);
        assertNotNull(addTreeBtn);
        //settingsBtn.performClick();
        onView(withId(R.id.add_tree_button_3)).perform(click());
        Activity addTreeActivity = getInstrumentation().waitForMonitorWithTimeout(addTreeMonitor, 5000);
        assertNotNull(addTreeActivity);
    }@Test
    public void testGoToBRBFromFling(){
//        ViewGroup viewGroup = mActivity.getContentScene().getSceneRoot();
//        assertNotNull(viewGroup);
        onView(isRoot()).perform(swipeRight());
        Activity mapActivity = getInstrumentation().waitForMonitorWithTimeout(monitor3, 5000);
        assertNotNull(mapActivity);
    }

    @Test
    public void qrCodeDetection(){//TODO
        Bitmap icon = BitmapFactory.decodeResource(InstrumentationRegistry.getTargetContext().getResources(), R.mipmap.qrcode2);

        Intent resultData = new Intent();
        resultData.putExtra("data", icon);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasPackage("com.sec.android.app.camera")).respondWith(result); //Getting a null pointer exception
                                                                                                // "Attempt to invoke virtual method 'android.support.test.espresso.intent.OngoingStubbing android.support.test.espresso.intent.Intents.internalIntending(org.hamcrest.Matcher)' on a null object reference"
        mActivity = mActivityTestRule.getActivity();
        intended(hasPackage("com.sec.android.app.camera"));
        //int



    }

//    @Test
//    public void onTouchEvent() {
//    }
}