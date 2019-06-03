package com.example.treesapv2new;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.text.Layout;
import android.transition.Visibility;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class Add_Tree_ActivityTest {

    @Rule
    public ActivityTestRule<Add_Tree_Activity> mActivityTestRule = new ActivityTestRule<Add_Tree_Activity>(Add_Tree_Activity.class);

    private Add_Tree_Activity mActivity;

    Instrumentation.ActivityMonitor nextMonitor = getInstrumentation().addMonitor(Tree_Bark_Activity.class.getName(), null, false);



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
        assertEquals(8, next.getVisibility());
        TextView instructions = mActivity.findViewById(R.id.addtreemessage);
        LinearLayout latLayout = mActivity.findViewById(R.id.lat_layout);
        assertNotNull(latLayout);
        assertEquals(8, latLayout.getVisibility());
        LinearLayout longLayout = mActivity.findViewById(R.id.long_layout);
        assertNotNull(longLayout);
        assertEquals(8, longLayout.getVisibility());
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
    public void getLocationPressed(){
        onView(withId(R.id.get_location_button)).perform(click());
        assertEquals(0, mActivity.findViewById(R.id.lat_layout).getVisibility());
        assertEquals(0, mActivity.findViewById(R.id.long_layout).getVisibility());
        assertEquals(0, mActivity.findViewById(R.id.next_add_tree).getVisibility());
        //onView(withId(R.id.lat_putter)).check(matches(withText("-?\\d{1,2}(\\.\\d+)?")));
        //onView(withId(R.id.long_putter)).check(matches(withText("-?\\d+(\\.\\d+)?")));
    }

    @Test
    public void nextPressed(){
        onView(withId(R.id.get_location_button)).perform(click());
        onView(withId(R.id.next_add_tree)).perform(click());
        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(nextMonitor, 5000);
        assertNotNull(nextActivity);
    }

//    @Test
//    public void getLocation() {
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
}