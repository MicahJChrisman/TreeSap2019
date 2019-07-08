package com.example.treesapv2new;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private Object[] dBmpList;
    ArrayAdapter arrayAdapter;

    ImageAdapter(Context context, ArrayList<BitmapDrawable>  dBmpList, ArrayAdapter arrayAdapter){
        this.context = context;
        this.dBmpList = (Object[]) dBmpList.toArray();
        this.arrayAdapter = arrayAdapter;
    }

    @Override
    public int getCount() {
        return dBmpList.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView;
        if(context instanceof FullScreenViewPager){
//            imageView = new ImageViewTouch(context, );
            imageView = new TouchImageView(context);
        }else{
            imageView = new ImageView(context);
        }
        imageView.setImageDrawable((BitmapDrawable) dBmpList[position]);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        //imageView.getDrawable().setBounds(0,0,-1242, 1242);
       // imageView.getDrawable().setColorFilter(Color.CYAN, PorterDuff.Mode.ADD);
        BitmapDrawable dBmp = (BitmapDrawable) dBmpList[position];
        //dBmp.setBounds(0,0,-1242,1242);
//        imageView.setVisibility(View.VISIBLE);
        container.addView(imageView);
//        container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                    viewPager.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
////                    viewPager.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
////                    viewPager.bringToFront();
//                    Intent intent = new Intent(context, FullScreenViewPager.class);
//                    intent.putExtra("position", position);
//                (Activity)context.startActivityForResult(intent, 1);
//
//            }
//        });;
//        container.setBackgroundColor(ContextCompat.getColor(context, R.color.highlight));
//        container.setBackgroundColor(Color.parseColor("#7DB162"));
//        container.setVisibility(View.VISIBLE);
//        container.setBackgroundColor(Color.YELLOW);
//        container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

       setPrimaryItem(container, position, imageView);

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }



//    @Override
//    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.setPrimaryItem(container, position, object);
//    }
}
