package com.example.treesapv2new;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.example.treesapv2new.view.Settings;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class SettingsActivityTest {

    @Rule
    public ActivityTestRule<SettingsActivity> mActivityTestRule = new ActivityTestRule<SettingsActivity>(SettingsActivity.class);

    private SettingsActivity mActivity;

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
        Preference settings = mActivity.findPreference("settings");
        assertNotNull(settings);
        Preference acknowledgements = mActivity.findPreference("acknowledgements");
        assertNotNull(acknowledgements);
        Preference maxDist = mActivity.findPreference("distanceFromTreePref");
        assertNotNull(maxDist);
        Preference dbs = mActivity.findPreference("databasesUsedSelector");
        assertNotNull(dbs);
        Preference location = mActivity.findPreference("locationMarkerSwitch");
        assertNotNull(location);
        Preference iTreeAck = mActivity.findPreference("itree_ack");
        assertNotNull(iTreeAck);
        Preference NTBCAck = mActivity.findPreference("benefit_calc_ack");
        assertNotNull(NTBCAck);
        Preference creatorsAck = mActivity.findPreference("creators_ack");
        assertNotNull(creatorsAck);

    }

//    @Test TODO: Cannot get the preferences to be clicked on
//    public void locationPressed(){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity.getBaseContext());
//        boolean locationOn = prefs.getBoolean("locationMarkerSwitch", true);
//        onView(withId(R.id.location_toggle)).perform(RecyclerViewActions.actionOnItem(withId(R.id.location_toggle), click()));
//        onView(withId(R.id.location_toggle)).perform(click());
//        assertFalse(locationOn);
//        onView(withId(R.id.location_toggle)).perform(click());
//        assertTrue(locationOn);
//    }

    @Test
    public void onOptionsItemSelected() {
    }
}