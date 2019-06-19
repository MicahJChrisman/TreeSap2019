package com.example.treesapv2new;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class RecyclerViewTreeOtherInfo extends RecyclerView.Adapter<RecyclerViewTreeOtherInfo.ViewHolder>{
    private Context mContext;

    public RecyclerViewTreeOtherInfo(Context context) {
        mContext = context;
    }

    public RecyclerViewTreeOtherInfo.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tree_other_info_for_recycle, parent, false);

        ((ImageButton) view.findViewById(R.id.add_more_dbh_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) view.findViewById(R.id.dbh_circum_add_2)).setVisibility(View.VISIBLE);
                ((ImageButton) view.findViewById(R.id.add_more_dbh_button)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout) view.findViewById(R.id.dbh_circum_add_3)).setVisibility(View.VISIBLE);
                        ((ImageButton) view.findViewById(R.id.add_more_dbh_button)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((LinearLayout) view.findViewById(R.id.dbh_circum_add_4)).setVisibility(View.VISIBLE);
                                ((ImageButton) view.findViewById(R.id.add_more_dbh_button)).setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        });


//        Button sumbitButton = (Button) view.findViewById(R.id.add_tree_submit);
//        sumbitButton.setOnClickListener(new SubmitEvent());

        ImageButton circumButton = (ImageButton) view.findViewById(R.id.circum_info_button);
        circumButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("This is necessary to calculate the benefits of a tree.");
                builder.setMessage("Please measure the circumference at chest height.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

//        TextView txtclose = (TextView) view.findViewById(R.id.other_info_close);
//        txtclose.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setCancelable(true);
//                builder.setTitle("Discard your tree?");
//                builder.setMessage("This will get rid of the data you entered.");
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.setPositiveButton("Discard Tree", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        Intent intentA = new Intent(mContext, MainActivity.class);
//                        mContext.startActivity(intentA);
//                        //finish();
//                    }
//                });
//                builder.show();
//            }
//        });

        String[] treeTypes = view.getResources().getStringArray(R.array.known_trees);
        //String[] treeTypes = getResources().getStringArray(R.array.known_trees_full);

        AutoCompleteTextView editTrees = view.findViewById(R.id.common_name);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,treeTypes);
//        editTrees.setAdapter(adapter);

        ((EditText) view.findViewById(R.id.dbh_edit)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String circumString = ((EditText) view.findViewById(R.id.dbh_edit)).getText().toString();
                if(!circumString.equals("")) {
                    Double circumValue = Double.valueOf(circumString);
                    Double dbhValue = circumValue / 3.1415;
                    dbhValue = Math.round(dbhValue * 100.0) / 100.0;
                    ((TextView) view.findViewById(R.id.dbh_textView)).setText(dbhValue.toString());
                }else{
                    ((TextView) view.findViewById(R.id.dbh_textView)).setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        RecyclerViewTreeOtherInfo.ViewHolder holder = new RecyclerViewTreeOtherInfo.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewTreeOtherInfo.ViewHolder holder, final int position) {
            GlideApp.with(mContext);
    }


    @Override
    public int getItemCount(){
        return 1;
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.tree_other_info_for_recycle);
        }
    }



}
