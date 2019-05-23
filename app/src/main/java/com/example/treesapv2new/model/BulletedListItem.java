package com.example.treesapv2new.model;

import android.graphics.Color;

public class BulletedListItem {

    private String mName = null;
    private String mLink = null;
    private int nameColor;

    public BulletedListItem(String name) {
        this.mName = name;
        this.mLink = null;
        this.nameColor = Color.BLACK;
    }

    public BulletedListItem(String name, String link) {

        this.mName = name;
        this.mLink = link;
        this.nameColor = Color.BLACK;
    }

    public BulletedListItem(String name, String link, int color) {

        this.mName = name;
        this.mLink = link;
        this.nameColor = color;
    }

    public BulletedListItem(String name, int color) {

        this.mName = name;
        this.mLink = null;
        this.nameColor = color;
    }

    public String getLink() {

        return mLink;
    }

    public void setLink(String link) {

        this.mLink = link;
    }

    public String getName() {

        return mName;
    }

    public void setName(String name) {

        this.mName = name;
    }

    public int getNameColor() {
        return nameColor;
    }

    public void setNameColor(int nameColor) {
        this.nameColor = nameColor;
    }

    @Override
    public String toString() {

        return this.mName;
    }
}
