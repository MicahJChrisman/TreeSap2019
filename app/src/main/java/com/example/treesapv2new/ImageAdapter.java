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
            imageView = new TouchImageView(context);
        }else{
            imageView = new ImageView(context);
        }
        imageView.setImageDrawable((BitmapDrawable) dBmpList[position]);
        BitmapDrawable dBmp = (BitmapDrawable) dBmpList[position];
        container.addView(imageView);

       setPrimaryItem(container, position, imageView);

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
