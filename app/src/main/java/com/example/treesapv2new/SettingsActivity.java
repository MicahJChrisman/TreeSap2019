package com.example.treesapv2new;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        addPreferencesFromResource(R.xml.preferences);

//        FragmentTransaction fragmentTransaction;
//        FragmentManager fragmentManager;
//        Fragment fragment = new MainSettingsFragment();
//        fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.commit();
//        getSupportFragmentManager().beginTransaction().replace(findViewById(R.id.fragment), new MainSettingsFragment()).commit();
//        setContentView(R.layout.activity_settings);
//
//        showLocationMarker = (SwitchCompat) findViewById(R.id.showLocationSwitch);
//        showLocationMarker.setChecked(stateShowLocationMarker);
//        showLocationMarker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stateShowLocationMarker =!stateShowLocationMarker;
//                showLocationMarker.setChecked(stateShowLocationMarker);
//            }
//        });
//  Thing to test thing

        // gallery EditText change listener
        bindPreferenceSummaryToValue(findPreference(getString(R.string.distanceFromTreePref)));

        // notification preference change listener

        // feedback preference click listener
//        Preference myPref = findPreference(getString(R.string.key_send_feedback));
//        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            public boolean onPreferenceClick(Preference preference) {
//                //sendFeedback(getActivity());
//                return true;
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            if (preference instanceof ListPreference) {

                MultiSelectListPreference multiSelectListPreference = (MultiSelectListPreference) preference;
                int index = multiSelectListPreference.findIndexOfValue(stringValue);
                preference.setSummary(index>= 0 ? multiSelectListPreference.getEntries()[index] : null);


//                // For list preferences, look up the correct display value in
//                // the preference's 'entries' list.
//                ListPreference listPreference = (ListPreference) preference;
//                int index = listPreference.findIndexOfValue(stringValue);
//
//                // Set the summary to reflect the new value.
//                preference.setSummary(
//                        index >= 0
//                                ? listPreference.getEntries()[index]
//                                : null);

            } else if (preference instanceof EditTextPreference) {
                if (preference.getKey().equals("distanceFromTreePref")) {
                    // update the changed gallery name to summary filed
                    preference.setSummary(stringValue);
                }
            }else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

}
