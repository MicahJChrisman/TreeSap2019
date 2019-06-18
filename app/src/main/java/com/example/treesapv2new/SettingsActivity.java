package com.example.treesapv2new;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.common.io.Resources;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onResume(){
        super.onResume();
        Preference userNamePref = (Preference) findPreference("change_username");
        if(userNamePref !=null) {
            userNamePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    //changeUsername(getListView());
                    startActivity(new Intent(SettingsActivity.this, changeUsername.class));
                    return true;
                }
            });
        }
        Preference passwordPref = (Preference) findPreference("change_password");
        if(passwordPref !=null) {
            passwordPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    //changeUsername(getListView());
                    startActivity(new Intent(SettingsActivity.this, changePassword.class));
                    return true;
                }
            });
        }

        if(Login_Activity.mAuth.getCurrentUser() !=null) {
            if(findPreference("change_username") !=null) {
                ((Preference) findPreference("change_username")).setSummary(getPreferenceScreen().getSharedPreferences().getString("change_username", changeUsername.newUsername));
            }
//            }else{
//                ((Preference) findPreference("change_username")).setSummary(getPreferenceScreen().getSharedPreferences().getString("change_username", Login_Activity.mAuth.getCurrentUser().toString()));
//            }
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);



        addPreferencesFromResource(R.xml.preferences);

        if(Login_Activity.mAuth.getCurrentUser()!=null) {
            PreferenceCategory b = (PreferenceCategory) findPreference("account");
            b.removePreference(findPreference("login_pref"));

            b.addPreference(findPreference("change_username"));
            b.addPreference(findPreference("change_password"));
            b.addPreference(findPreference("logout_pref"));


        }else{
//            PreferenceScreen a = (PreferenceScreen) findPreference("account_screen");
//            PreferenceCategory b = (PreferenceCategory) findPreference("not_logged_in_account");
//            b.removePreference(a);
            PreferenceCategory b = (PreferenceCategory) findPreference("account");
            b.removePreference(findPreference("change_username"));
            b.removePreference(findPreference("change_password"));
            b.removePreference(findPreference("logout_pref"));

            b.addPreference(findPreference("login_pref"));
        }




//        String sp = getPreferenceScreen().getSharedPreferences().getString("change_username","No user logged in.");



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

    public void logOut(View v){
        Login_Activity.mAuth.signOut();
//        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        ((Preference) findPreference("change_username")).setSummary( getPreferenceScreen().getSharedPreferences().getString("change_username", "No user logged in"));
        Toast toast = Toast.makeText(SettingsActivity.this, "You have been logged out.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();
        finish();
        startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
    }

    public void logIn(View v){
        finish();
        startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
        startActivity(new Intent(SettingsActivity.this, Login_Activity.class));

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

//thing thing

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
