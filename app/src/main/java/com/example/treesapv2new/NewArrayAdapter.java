package com.example.treesapv2new;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;


import com.example.treesapv2new.model.Tree;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;
//import static android.support.v4.graphics.drawable.IconCompat.getResources;

public class NewArrayAdapter extends ArrayAdapter<Tree>{
    Curator_Swipe_Activity context;
    View convertView;
    private static ViewPager mPager;
//    private PagerAdapter pagerAdapter;
//    private static int currentPage = 0;
    //private static int NUM_PAGES = 1;
    //private ArrayList<ImageModel> imageModelArrayList;
    private Tree tree;
    FragmentManager fm;

    private ArrayList<BitmapDrawable> dBmpList;


    private BitmapDrawable[] myImageList = new BitmapDrawable[10];
//            {R.drawable.harley2, R.drawable.benz2,
//            R.drawable.vecto,R.drawable.webshots
//            R.drawable.bikess,R.drawable.img1
//    };

    public NewArrayAdapter(Context context, int resourceId, List<Tree> trees, FragmentManager fm){
        super(context, resourceId, trees);
        this.context = (Curator_Swipe_Activity) context;
        this.fm = fm;
        //this.context.setCurrentTree();
    }

    public View getView(int position, View convertView, ViewGroup parent){
        tree = this.getItem(position);
        context.setCurrentTree();
        //imageModelArrayList = new ArrayList<>();
        dBmpList = new ArrayList<>();
        //imageModelArrayList = populateList();
        dBmpList = new ArrayList<BitmapDrawable>();
        dBmpList = populateList();
//        if(dBmpList.size()!=0) {
//            NUM_PAGES = dBmpList.size();
//        }


        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
            this.convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        else{
            this.convertView = convertView;
        }
        //init();
        mPager = (ViewPager) convertView.findViewById(R.id.pager);
//        mPager.setBackgroundColor(Color.YELLOW);
       // pagerAdapter = new ScreenSlidePagerAdapter(fm, dBmpList);
//        mPager.setAdapter(pagerAdapter);
//        mPager.setCurrentItem(pagerAdapter.getItem());

        ImageAdapter adapter = new ImageAdapter(context, dBmpList, this);
        mPager.setAdapter(adapter);
        mPager.setOffscreenPageLimit(2);
        TextView noPicsMessage = convertView.findViewById(R.id.no_pics_message);
        if(dBmpList.size()==0) {
            //noPicsMessage.setVisibility(View.VISIBLE);
            mPager.setBackgroundResource(R.drawable.new_logo);
        }else{
            //noPicsMessage.setVisibility(View.GONE);
            mPager.setBackground(null);
        }

        TextView commonName = (TextView) convertView.findViewById(R.id.common_name);
        TextView scientificName = (TextView) convertView.findViewById(R.id.scientific_name);
        TextView dbh = (TextView) convertView.findViewById(R.id.dbh);
        TextView notes = (TextView) convertView.findViewById(R.id.notes);
        //ImageView fullPic = (ImageView) convertView.findViewById(R.id.full_pic);

        commonName.setText(tree.getCommonName());
        scientificName.setText(tree.getScientificName());
        Double treeDbh = tree.getCurrentDBH();
        if(treeDbh.equals(0.0)){
            dbh.setText("N/A");
        }else{
            dbh.setText(treeDbh.toString());
        }
        notes.setText((String) tree.getInfo("Notes"));





//        byte[] byteArrayFull = Base64.decode((String) tree.getPics("full_pic_byte_array"), Base64.DEFAULT);
//        Bitmap bmp = BitmapFactory.decodeByteArray(byteArrayFull, 0, byteArrayFull.length);
//        fullPic.setImageBitmap(bmp);

        return convertView;
    }

    private ArrayList<BitmapDrawable> populateList(){

//        ArrayList<ImageModel> picList = new ArrayList<>();
        ArrayList<BitmapDrawable> dBmpList = new ArrayList<>();

        String picsString = tree.getAllPics();
        if(picsString != null) {
            String[] pics = picsString.split("\n?\t.*: ");
            int picsLength = pics.length;
            int i = 0;
            while(i<picsLength && pics[i]!=null){
                if(!pics[i].equals("")) {
                    byte[] encodeByte = Base64.decode(pics[i], Base64.DEFAULT);
                    InputStream is = new ByteArrayInputStream(encodeByte);

//                    InputStream is = new ByteArrayInputStream(pics[i].getBytes());
                    Bitmap bmp = BitmapFactory.decodeStream(is);
//                    ImageModel imageModel = new ImageModel();
                    BitmapDrawable dBmp = new BitmapDrawable(getContext().getResources(), bmp);
                    dBmpList.add(dBmp);
//                    imageModel.setImage_drawable(dBmp);
//                    picList.add(imageModel);
                }
                i++;
            }
        }

//        for(int i = 0; i < 6; i++){
//            ImageModel imageModel = new ImageModel();
//            imageModel.setImage_drawable(myImageList[i]);
//            list.add(imageModel);
//        }

        return dBmpList;
    }

//    private void init() {
//
//        mPager = (ViewPager) convertView.findViewById(R.id.pager);
//        //mPager.setAdapter(new SlidingImage_Adapter(context, imageModelArrayList));
//        mPager.setAdapter(new SlidingImage_Adapter(context, dBmpList));
//
//        CirclePageIndicator indicator = (CirclePageIndicator) convertView.findViewById(R.id.indicator);
//
//        indicator.setViewPager(mPager);
//
//        final float density = convertView.getResources().getDisplayMetrics().density;
//
////Set circle indicator radius
//        indicator.setRadius(5 * density);
//
//        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentPage == NUM_PAGES) {
//                    currentPage = 0;
//                }
//                mPager.setCurrentItem(currentPage++, true);
//            }
//        };
//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 3000, 3000);
//
//        // Pager listener over indicator
//        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int position) {
//                currentPage = position;
//
//            }
//
//            @Override
//            public void onPageScrolled(int pos, float arg1, int arg2) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int pos) {
//
//            }
//        });
//
//    }
//    private void initRecyclerView(){
//        Log.d(TAG, "initRecyclerView: init recyclerview.");
//        RecyclerView recyclerView = convertView.findViewById(R.id.recycler_view_curator);
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//    }
    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        private ArrayList<BitmapDrawable> dBmpList;

        public ScreenSlidePagerAdapter(FragmentManager fm, ArrayList<BitmapDrawable> dBmpList) {
            super(fm);
            this.dBmpList = dBmpList;
        }
        @Override
        public Fragment getItem(int i) {
            i--;
            ScreenSlidePageFragment screen = new ScreenSlidePageFragment();
            if (i >= 0) {
                ImageView pic = screen.getImageView();
                pic.setImageDrawable(dBmpList.get(i));
            }
            return screen;
        }
        @Override
        public int getCount() {
            return dBmpList.size();
        }
    }
}
