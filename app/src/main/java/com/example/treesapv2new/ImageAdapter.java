package com.example.treesapv2new;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private Object[] dBmpList;
    NewArrayAdapter arrayAdapter;

    ImageAdapter(Context context, ArrayList<BitmapDrawable>  dBmpList, NewArrayAdapter arrayAdapter){
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
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageDrawable((BitmapDrawable) dBmpList[position]);
        imageView.getDrawable().setBounds(0,0,-1242, 1242);
       // imageView.getDrawable().setColorFilter(Color.CYAN, PorterDuff.Mode.ADD);
        BitmapDrawable dBmp = (BitmapDrawable) dBmpList[position];
        dBmp.setBounds(0,0,-1242,1242);
        //imageView.setBackgroundColor(Color.CYAN);
        //container.setBackground(dBmp);
        imageView.setVisibility(View.VISIBLE);
        container.addView(imageView);
        container.setVisibility(View.VISIBLE);
        container.setBackgroundColor(Color.YELLOW);
        //arrayAdapter.notifyDataSetChanged();
        //notifyDataSetChanged();
//        startUpdate(container);
        //otifyDataSetChanged();

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
