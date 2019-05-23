package com.example.treesapv2new.display;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.treesapv2new.R;
import com.example.treesapv2new.identify.Big_Red_Button_Identifier;
import com.example.treesapv2new.MainActivity;
import com.example.treesapv2new.view.MapsActivity;

public class AddNotesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.add_notes_display);
        BottomNavigationView bottomNav = findViewById(R.id.nav_view1);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_brb:
                    startActivity(new Intent(AddNotesActivity.this, Big_Red_Button_Identifier.class));
                    break;
                case R.id.navigation_map:
                    startActivity(new Intent(AddNotesActivity.this, MapsActivity.class));
                    break;
                case R.id.navigation_home:
                    startActivity(new Intent(AddNotesActivity.this, MainActivity.class));
                    break;
            }
            return true;
        }
    };
}
