package com.example.treesapv2new.identify;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treesapv2new.R;
import com.example.treesapv2new.model.GPSCoordinates;
import com.example.treesapv2new.model.TreeLocation;

public class SimpleIdentifier extends IdentificationMethod {

    TreeLocation location;

    @Override
    public String getMethodName() { return "GPS Manual Coordinate Entry"; }

    @Override
    public String getDescription() {
        return "Identify a tree by entering it's GPS coordinates manually.";
    }

    @Override
    public void identify() {
        if (parent == null) {
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(parent);
        final View dialog = (View)inflater.inflate(R.layout.manual_gps_entry, null);

        final EditText input = new EditText(parent);
        location = new TreeLocation();
        new AlertDialog.Builder(parent)
            .setTitle("Enter GPS Coordinates")
            .setMessage("Enter GPS Coordinates of the Tree. In order to receive the best results, it is important to enter 11 significant digits.")
            .setView(dialog)
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogint, int whichButton) {
                    TextView latTV = (TextView)dialog.findViewById(R.id.treeLatitude);
                    String latNum = latTV.getText().toString();
                    TextView longTV = (TextView)dialog.findViewById(R.id.treeLongitude);
                    String longNum = longTV.getText().toString();
                    if (latNum.length() > 0 && longNum.length() > 0) {
                        GPSCoordinates coords = new GPSCoordinates();
                        coords.parse(latNum+","+longNum);
                        location.setLatitude(coords.getLatitude());
                        location.setLongitude(coords.getLongitude());
                        callbackClass.idCompleted();
                    } else {
                        Toast.makeText(parent, "ERROR: bad GPS coordinates", Toast.LENGTH_LONG).show();
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {/* Do nothing. */}
        }).show();

    }

    @Override
    public TreeLocation getLocation() {
        return location;
    }

    public void setLocation(TreeLocation location) {
        this.location = location;
    }

}
