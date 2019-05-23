package com.example.treesapv2new.display;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import com.example.treesapv2new.R;
import com.example.treesapv2new.Tree_Info_First;

public class AddNotesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.add_notes_display);

        Button button = (Button) findViewById(R.id.add_notes_button);
        button.setOnClickListener(new AddNotesEvent());
    }

    private class AddNotesEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
//            Intent intentA = new Intent(AddNotesActivity.this, AddNotesActivity.class);
//            startActivity(intentA);
            finish();
        }
    }

}
