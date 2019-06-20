package com.example.treesapv2new;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ScreenSlidePageFragment extends Fragment {
    private ViewGroup rootView;
    private ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.pic_screen_slide, container, false);
        this.rootView = rootView;
        ImageView image = rootView.findViewById(R.id.imageView2);
        this.image = image;
        return rootView;

    }

    public View getRootView(){
        return rootView;
    }
    public ImageView getImageView(){
        return image;
    }

}
