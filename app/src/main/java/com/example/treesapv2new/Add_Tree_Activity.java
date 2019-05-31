package com.example.treesapv2new;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Add_Tree_Activity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.add_tree_page);

        Button b = (Button) findViewById(R.id.next_add_tree);
        b.setOnClickListener(new NextEvent());
    }


    private class NextEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intentA = new Intent(Add_Tree_Activity.this, Tree_Bark_Activity.class);
            startActivity(intentA);
        }
    }
}
