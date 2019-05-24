package com.example.treesapv2new;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    SwitchCompat showLocationMarker;
    boolean stateShowLocationMarker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_settings);

        showLocationMarker = (SwitchCompat) findViewById(R.id.showLocationSwitch);
        showLocationMarker.setChecked(stateShowLocationMarker);
        showLocationMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateShowLocationMarker =!stateShowLocationMarker;
                showLocationMarker.setChecked(stateShowLocationMarker);
            }
        });


    }
}
