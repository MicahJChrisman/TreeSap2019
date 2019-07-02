package com.example.treesapv2new;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class MoreInformation extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.more_infomation_screen);

        ((TextView) findViewById(R.id.itree_link)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.inaturalist_link)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) findViewById(R.id.nat_tree_link)).setMovementMethod(LinkMovementMethod.getInstance());


        findViewById(R.id.more_info_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
