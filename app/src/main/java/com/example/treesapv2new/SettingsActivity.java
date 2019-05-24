package com.example.treesapv2new;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    SwitchCompat showLocationMarker;
    boolean stateShowLocationMarker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        getSupportFragmentManager().beginTransaction().replace(0, new MainSettingsFragment()).commit();
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


    }

    public static class MainSettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }
    }
}
