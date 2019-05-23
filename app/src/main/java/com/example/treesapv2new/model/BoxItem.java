package com.example.treesapv2new.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class BoxItem extends View {

    View box;
    String title;
    String subtitle;
    View button;
    View content;
    private LayoutInflater mInflater;
    Context parent;

    public BoxItem(Context context) {
        super(context);

        parent = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        title = subtitle = null;
        button = null;
        content = null;
    }

    public void setTitle(String aTitle) {
        this.title = aTitle;
    }

    public void setSubtitle(String aSubtitle) {
        this.subtitle = aSubtitle;
    }

    public void setButton(View button) {
        this.button = button;
    }

    public void setContent(View content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public View getButton() {
        return button;
    }

    public View getContent() {
        return content;
    }

    public void redisplay() {
        if (content != null) content.invalidate();
    }
}
