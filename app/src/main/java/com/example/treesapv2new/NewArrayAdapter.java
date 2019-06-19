package com.example.treesapv2new;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.treesapv2new.model.Tree;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.List;


public class NewArrayAdapter extends ArrayAdapter<Tree>{

    Context context;

    public NewArrayAdapter(Context context, int resourceId, List<Tree> trees){
        super(context, resourceId, trees);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Tree tree = this.getItem(position);


        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

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

}
