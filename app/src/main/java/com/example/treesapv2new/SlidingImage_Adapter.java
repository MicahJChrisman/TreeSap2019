package com.example.treesapv2new;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Parsania Hardik on 23/04/2016.
 */
public class SlidingImage_Adapter extends PagerAdapter {


    private ArrayList<ImageModel> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<BitmapDrawable> dBmpList;


//    public SlidingImage_Adapter(Context context, ArrayList<ImageModel> imageModelArrayList) {
//        this.context = context;
//        this.imageModelArrayList = imageModelArrayList;
//        inflater = LayoutInflater.from(context);
//        dBmpList = new ArrayList<>();
//    }

    public SlidingImage_Adapter(Context context, ArrayList<BitmapDrawable> dBmpList){
        this.context = context;
        this.imageModelArrayList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        this.dBmpList = dBmpList;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);


        //imageView.setImageDrawable(imageModelArrayList.get(position).getImage_drawable());
        imageView.setImageDrawable(dBmpList.get(position));

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}