package com.example.treesapv2new;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class FullScreenViewPager extends AppCompatActivity {

    ImageButton closeButton;
    ClickableViewPager viewPager;
    ArrayList<BitmapDrawable> dBmpList;


    @Override
    public void onCreate(Bundle onSavedInstance) {
        super.onCreate(onSavedInstance);
        setContentView(R.layout.fullscreen_viewpager);

        closeButton = (ImageButton) findViewById(R.id.close_full_pager);
        viewPager = findViewById(R.id.pager);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenViewPager.this.onBackPressed();
            }
        });

//        if(getCallingActivity().equals(CuratorActivity.class)){
            this.dBmpList = CuratorActivity.dBmpList;
//        };

        viewPager = findViewById(R.id.pager);

        ImageAdapter adapter = new ImageAdapter(FullScreenViewPager.this, dBmpList, new ArrayAdapter(FullScreenViewPager.this, R.layout.fullscreen_viewpager));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(dBmpList.size() - 1);
        viewPager.setCurrentItem((int) getIntent().getExtras().get("position"));
        viewPager.setOnItemClickListener(new ClickableViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //TODO make view go in and out of fullscreen
//                requestWindowFeature(Window.FEATURE_NO_TITLE);
//                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("position", viewPager.getCurrentItem());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
