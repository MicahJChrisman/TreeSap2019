package com.example.treesapv2new;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
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

    Context context;
    View convertView;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;
    private Tree tree;

    private BitmapDrawable[] myImageList = new BitmapDrawable[10];
//            {R.drawable.harley2, R.drawable.benz2,
//            R.drawable.vecto,R.drawable.webshots
//            R.drawable.bikess,R.drawable.img1
//    };

    public NewArrayAdapter(Context context, int resourceId, List<Tree> trees){
        super(context, resourceId, trees);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        tree = this.getItem(position);

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();



        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
            this.convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        else{
            this.convertView = convertView;
        }
        init();

        TextView commonName = (TextView) convertView.findViewById(R.id.common_name);
        TextView scientificName = (TextView) convertView.findViewById(R.id.scientific_name);
        TextView dbh = (TextView) convertView.findViewById(R.id.dbh);
        TextView notes = (TextView) convertView.findViewById(R.id.notes);
        ImageView fullPic = (ImageView) convertView.findViewById(R.id.full_pic);

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

    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> picList = new ArrayList<>();

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
                    ImageModel imageModel = new ImageModel();
                    BitmapDrawable dBmp = new BitmapDrawable(getContext().getResources(), bmp);
                    imageModel.setImage_drawable(new BitmapDrawable(getContext().getResources(), bmp));
                    picList.add(imageModel);
                }
                i++;
            }
        }
        else{

        }

//        for(int i = 0; i < 6; i++){
//            ImageModel imageModel = new ImageModel();
//            imageModel.setImage_drawable(myImageList[i]);
//            list.add(imageModel);
//        }

        return picList;
    }

    private void init() {

        mPager = (ViewPager) convertView.findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(context, imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator) convertView.findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = convertView.getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

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

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
//    private void initRecyclerView(){
//        Log.d(TAG, "initRecyclerView: init recyclerview.");
//        RecyclerView recyclerView = convertView.findViewById(R.id.recycler_view_curator);
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//    }

}
