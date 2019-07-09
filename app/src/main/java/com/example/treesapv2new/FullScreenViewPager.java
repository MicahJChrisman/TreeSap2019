package com.example.treesapv2new;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class FullScreenViewPager extends AppCompatActivity implements  GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

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
        viewPager.setOnItemClickListener(null);
//        viewPager.setOnItemClickListener(new ClickableViewPager.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                int flg = getWindow().getAttributes().flags;
//                boolean flag = false;
//                if ((flg & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
//                    View decorView = getWindow().getDecorView();
//                    decorView.setSystemUiVisibility(
//                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//                }else{
//                    View decorView = getWindow().getDecorView();
//                    decorView.setSystemUiVisibility(
//                            View.SYSTEM_UI_FLAG_IMMERSIVE
//                                    // Set the content to appear under the system bars so that the
//                                    // content doesn't resize when the system bars hide and show.
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                    // Hide the nav bar and status bar
//                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
//                }
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("position", viewPager.getCurrentItem());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        int flg = getWindow().getAttributes().flags;
        boolean flag = false;
        if ((flg & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }else{
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show.
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
        return onSingleTapUp(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d("TEST", "onDoubleTap");
        Toast.makeText(FullScreenViewPager.this, "Double tap", Toast.LENGTH_SHORT).show();
        return onDoubleTap(e);
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d("TEST", "onDoubleTap");
        Toast.makeText(FullScreenViewPager.this, "Double tap", Toast.LENGTH_SHORT).show();
        return onDoubleTap(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        int flg = getWindow().getAttributes().flags;
        boolean flag = false;
        if ((flg & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            closeButton.setVisibility(View.GONE);
        }else{
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show.
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
            closeButton.setVisibility(View.VISIBLE);
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
