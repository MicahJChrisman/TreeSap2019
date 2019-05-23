package com.example.treesapv2new.display;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.PopupWindow;

import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.MainActivity;

public class AddNotesDisplay extends DisplayMethod {
    private Tree trees;
    private PopupWindow popupWindow;

    public AddNotesDisplay(){
        super();
    }

    public AddNotesDisplay(Context context) {
        super(context);
    }

    @Override
    public void setParent(Context context) {
        super.setParent(context);
    }

    @Override
    public String getMethodName() {
        return "Add Notes Display";
    }

    @Override
    public String getDescription() {
        return "Allow users to add notes about trees";
    }

    @Override
    public void display(Tree tree){
        trees = tree;
        if (parent == null) {
            return;
        }
//        try {
//            findInfo(tree);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        LayoutInflater inflater = (LayoutInflater) parent.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View layout = (View)inflater.inflate(R.layout.add_notes_display, null);
//        LinearLayout ll = (LinearLayout) layout.findViewById(R.id.thing_thing);
////        ll.setVisibility(View.VISIBLE);
//
//        LinearLayout layout1 = (LinearLayout) findViewById(R.id.thing_thing);

//        LayoutInflater inflater = LayoutInflater.from(parent);
//        final View layout = (View)inflater.inflate(R.layout.add_notes_display, null);
//
//        TextView tv;
//        tv = (TextView) layout.findViewById(R.id.addNotesAboutATree);
//        tv.setText("Notes");
//
//        tv.setOnLongClickListener(new LongClickAction(tree));
//        tv = (TextView) layout.findViewById(R.id.simple_view_tree_dbh);
//        DecimalFormat df = new DecimalFormat("0.0#");
//
//        if (tree.isOtherInfoPresent()) {
//            LinearLayout ll = (LinearLayout) layout.findViewById(R.id.thing_thing);
//            ll.setVisibility(View.VISIBLE);
//            tv = (TextView) layout.findViewById(R.id.simple_view_tree_other_info);
//        }


//        LayoutInflater inflater = (LayoutInflater) parent.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View customView = inflater.inflate(R.layout.add_notes_display, null);
//        TextView okay = (TextView) customView.findViewById(R.id.okay_addNotes);
//        okay.bringToFront();
//        okay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//                popupWindow = null;
//            }
//        });
//
//        if (popupWindow != null) {
//            popupWindow.dismiss();
//        }
//        popupWindow = new PopupWindow(
//                customView,
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//        );
//
//        if (Build.VERSION.SDK_INT >= 21) {
//            popupWindow.setElevation(5.0f);
//        }
//
//        View rootView = ((Activity)parent).getWindow().getDecorView().findViewById(R.id.drawer_layout);
//        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 400);


//        new AlertDialog.Builder(parent)
//                .setTitle("Add Notes")
//                .setMessage("Put an information about the tree")
//                .setView(layout).show();
////                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialogint, int whichButton) {
////                    }
////                }).show();



//        LayoutInflater inflater = (LayoutInflater) parent.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View customView = inflater.inflate(R.layout.add_notes_display, null);
//        TextView okay = (TextView) customView.findViewById(R.id.okay_cereal);
//        okay.bringToFront();
//        okay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//                popupWindow = null;
//            }
//        });
        Bundle bundle = new Bundle();
        bundle.putDouble("longitude", 0.0);
        bundle.putDouble("latitude", 0.0);
        Intent startDeviceIntent = new Intent(parent, AddNotesActivity.class);
        startDeviceIntent.putExtras(bundle);
        ((MainActivity)parent).startActivityForResult(startDeviceIntent, 1000);


    }

    @Override
    public PopupWindow getPopupWindow(){
        return popupWindow;
    }

    @Override
    public void dismiss(){
        popupWindow.dismiss();
        popupWindow=null;
    }

//    @Override
//    public PopupWindow getPopupWindow(){
//        return popupWindow;
//    }
//
//    @Override
//    public void dismiss(){
//        popupWindow.dismiss();
//        popupWindow=null;
//    }
}
