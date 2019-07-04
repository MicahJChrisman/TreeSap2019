package com.example.treesapv2new;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

//import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 1/1/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<String> imageNames, ArrayList<String> images ) {
        mImageNames = imageNames;
        mImages = images;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, "onClick: clicked on: " + mImageNames.get(position));
//
//                Toast.makeText(mContext, mImageNames.get(position), Toast.LENGTH_LONG).show();
                TextView index = v.findViewById(R.id.index);
                Intent intent = new Intent(mContext, CuratorActivity.class);
                intent.putExtra("index", Integer.parseInt(index.getText().toString())-1);
//                Intent intent = new Intent(mContext, Curator_Swipe_Activity.class);
//                intent.putExtra("image_url", mImages.get(position));
//                intent.putExtra("image_name", mImageNames.get(position));
                mContext.startActivity(intent);

            }
        });

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

//    public class Glider extends AppGlideModule{
//        @GlideModule
//        public void gliderMove(ViewHolder holder, final int position){
//            Glide.with(mContext)
//                    .asBitmap()
//                    .load(mImages.get(position))
//                    .into(holder.image);
//        }
//    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        int a = mImageNames.size();
        int b = mImages.size();

        if(mImages.size()>position) {
            byte[] encodeByte = Base64.decode(mImages.get(position), Base64.DEFAULT);

            InputStream inputStream = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if(bitmap == null){

            }else {
                GlideApp.with(mContext)
                        .asBitmap()
                        .load(bitmap)
                        .into(holder.image);
            }
        }

        holder.imageName.setText(mImageNames.get(position));
        holder.index.setText(String.valueOf(position+1));

//                        ImageView image = (ImageView) findViewById(R.id.set_image_curator);
//                        image.setImageBitmap(bitmap);







//        Glide.with(mContext)
//                .asBitmap()
//                .load(mImages.get(position))
//                .into(holder.image);



//        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: clicked on: " + mImageNames.get(position));
//
//                Toast.makeText(mContext, mImageNames.get(position), Toast.LENGTH_LONG).show();
//
//                Intent intent = new Intent(mContext, GalleryActivity.class);
//                intent.putExtra("image_url", mImages.get(position));
//                intent.putExtra("image_name", mImageNames.get(position));
//                mContext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView imageName;
        TextView index;
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.tree_image_recycler);
            imageName = itemView.findViewById(R.id.image_name);
            index = itemView.findViewById(R.id.index);
            parentLayout = itemView.findViewById(R.id.curator_constraint_layout);
        }
    }
}