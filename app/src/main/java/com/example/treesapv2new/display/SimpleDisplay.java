package com.example.treesapv2new.display;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.HashMap;

import com.example.treesapv2new.R;
import com.example.treesapv2new.control.Transform;
import com.example.treesapv2new.model.SettingsOption;
import com.example.treesapv2new.model.Tree;

public class SimpleDisplay extends DisplayMethod {

    public String getMethodName() {
        return "Simple tree info display";
    }

    public String getDescription() {
        return "Simple display method that displays a tree";
    }

    public void display(Tree tree) {
        if (parent == null) {
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(parent);
        final View layout = (View)inflater.inflate(R.layout.simple_tree_display, null);

        TextView tv;
        tv = (TextView) layout.findViewById(R.id.simple_view_tree_common_name);
        tv.setText(Transform.ChangeName(tree.getCommonName()));
        tv = (TextView) layout.findViewById(R.id.simple_view_tree_scientific_name);
        tv.setText(tree.getScientificName());
        tv = (TextView) layout.findViewById(R.id.simple_view_tree_location);
        tv.setText(tree.getLocation().toString());
        tv = (TextView) layout.findViewById(R.id.simple_view_tree_id);
        tv.setText(""+tree.getID());
        tv.setOnLongClickListener(new LongClickAction(tree));
        tv = (TextView) layout.findViewById(R.id.simple_view_tree_dbh);
        DecimalFormat df = new DecimalFormat("0.0#");
        tv.setText(df.format(tree.getCurrentDBH()));
        if (tree.isOtherInfoPresent()) {
            LinearLayout ll = (LinearLayout) layout.findViewById(R.id.simple_tree_view_other_info_layout);
            ll.setVisibility(View.VISIBLE);
            tv = (TextView) layout.findViewById(R.id.simple_view_tree_other_info);
            tv.setText(tree.getAllInfo());
        }

        new AlertDialog.Builder(parent)
                .setTitle(Transform.ChangeName(tree.getCommonName()))
                .setMessage("Simple Look at Tree Information")
                .setView(layout)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogint, int whichButton) {
                    }
                }).show();

    }

    private class LongClickAction implements View.OnLongClickListener {

        Tree saved;

        public LongClickAction(Tree tree) {
            saved = tree;
        }

        @Override
        public boolean onLongClick(View v) {

            String msg =
                    "Tree id found: "+saved.getID() + "\n"
                    + "Search for coords: " + saved.getSearchedfor().toString() + "\n"
                    + "Returned coords: " + saved.getLocation().toString() + "\n"
                    + "Is found: " + (saved.isFound()?"Yes":"No") + "\n"
                    + "Is closest: " + (saved.isClosest()?"Yes":"No") + "\n"
                    ;

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"jipping@hope.edu"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Tree Debugging Info");
            i.putExtra(Intent.EXTRA_TEXT   , msg);
            try {
                ((Activity)parent).startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(parent, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    @Override
    public HashMap<String, SettingsOption> getPreferences()  {
        preferences = new HashMap<>();
        return preferences;
    }
}
