package com.example.treesapv2new;

import android.app.Instrumentation;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.text.Layout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class Add_Tree_ActivityTest {

    @Rule
    public ActivityTestRule<Add_Tree_Activity> mActivityTestRule = new ActivityTestRule<Add_Tree_Activity>(Add_Tree_Activity.class);

    private Add_Tree_Activity mActivity;

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

    @Test
    public void onCreate() {
        TextView exit = mActivity.findViewById(R.id.add_tree_close);
        assertNotNull(exit);
        TextView title = mActivity.findViewById(R.id.add_tree_header);
        assertNotNull(title);
        Button next = mActivity.findViewById(R.id.next_add_tree);
        assertNotNull(next);
        assertEquals(3, next.getVisibility());
        TextView instructions = mActivity.findViewById(R.id.addtreemessage);
        LinearLayout latLayout = mActivity.findViewById(R.id.lat_layout);
        assertNotNull(latLayout);
        assertEquals(3, latLayout.getVisibility());
        LinearLayout longLayout = mActivity.findViewById(R.id.long_layout);
        assertNotNull(longLayout);
        assertEquals(3, longLayout.getVisibility());
        //TextView
        TextView lat_putter = mActivity.findViewById(R.id.lat_putter);
        assertNotNull(lat_putter);
        //TextView
        TextView long_putter = mActivity.findViewById(R.id.long_putter);
        assertNotNull(long_putter);
        Button getLocation = mActivity.findViewById(R.id.get_location_button);
        assertNotNull(getLocation);
    }

    @Test
    public void getLocation() {
    }

    @Test
    public void onLocationChanged() {
    }

    @Test
    public void onStatusChanged() {
    }

    @Test
    public void onProviderEnabled() {
    }

    @Test
    public void onProviderDisabled() {
    }
}