package com.example.treesapv2new.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.treesapv2new.R;
import com.example.treesapv2new.model.BoxItem;

public class UserViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<BoxItem> boxItems;

    public UserViewAdapter(Context context) {
        mContext = context;
        boxItems = new ArrayList<BoxItem>();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return boxItems.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return boxItems.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    public View getView(int position, View convertView, ViewGroup parent) {
        View boxView = convertView;
        // Get view for row item
        if (boxView == null) {
            boxView = mInflater.inflate(R.layout.box_item, null);
        }

        TextView tv = (TextView) boxView.findViewById(R.id.box_item_title);
        tv.setText(boxItems.get(position).getTitle());
        View v = (View) boxView.findViewById(R.id.box_item_content);
        if (boxItems.get(position).getSubtitle() != null) {
            tv = (TextView) boxView.findViewById(R.id.box_item_subtitle);
            tv.setVisibility(View.VISIBLE);
            tv.setText(boxItems.get(position).getSubtitle());
        }
        if (boxItems.get(position).getButton() != null) {
            RelativeLayout rl = (RelativeLayout) boxView.findViewById(R.id.box_item_titlebar);
            View button = boxItems.get(position).getButton();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_BASELINE, RelativeLayout.TRUE);
            button.setLayoutParams(params);
            if (button.getParent() != null) {
                ((ViewGroup) button.getParent()).removeView(button);
            }
            rl.addView(button);
        }
        if (boxItems.get(position).getContent() != null) {
            Log.d("UserViewAdapter", "Generating content box");
            View view = boxItems.get(position).getContent();
            LinearLayout ll = (LinearLayout) boxView.findViewById(R.id.box_item_content);
            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) ll.getLayoutParams();
            //ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            if (boxItems.get(position).getTitle().equals("Tree Identification")) {
                lp.height = 700;
            } else {
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            ll.setLayoutParams(lp);
//            if (view.getParent() != null) {
//                Log.d("UserViewAdapter", "Removing old content box");
//                ((ViewGroup) view.getParent()).removeView(view);
//            }
            if (view.getParent() == null) ll.addView(view);
            view.invalidate();
            ll.invalidate();


        }

        return boxView;
    }

    public void addBoxItem(BoxItem aBoxItem) {
        if (boxItems == null) boxItems = new ArrayList<BoxItem>();
        boxItems.add(aBoxItem);
    }

    public void setBoxItems(ArrayList<BoxItem> bis) {
        boxItems = bis;
    }

    public void replaceBoxItem(int position, BoxItem bi) {
        boxItems.set(position, bi);
    }

//    @Override
//    public void notifyDataSetChanged() {
//        super.notifyDataSetChanged();
//
//        for (BoxItem bi : boxItems) bi.redisplay();
//    }
}
