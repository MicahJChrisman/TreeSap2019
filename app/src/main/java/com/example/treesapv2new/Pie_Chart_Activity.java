package com.example.treesapv2new;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.treesapv2new.control.PrefManager;
import com.example.treesapv2new.control.Transform;
import com.example.treesapv2new.display.AddNotesActivity;
import com.example.treesapv2new.display.PieChartDisplay;
import com.example.treesapv2new.model.Tree;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.graphics.Paint.Align.CENTER;

public class Pie_Chart_Activity extends AppCompatActivity {

    private GestureDetectorCompat gestureObject;

    Dialog myDialog;

    private static final int[] COLORFUL_COLORS = {
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53), Color.rgb(55, 55, 200)
    };
    private PopupWindow popupWindow;
    private String commonName = MainActivity.banana.getCommonName();
    private String allInfo;
    private PieChart pieChart;
    private PieDataSet dataset;
    private PieData data;
    private double total;
    private boolean hasValues = false;
    private Tree tree;




    static final int STORM_WATER = 9;
    static final int POLLUTION = 13;
    static final int CO2 = 7;
    static final int TOTAL = 15;
    static final int CARBON_LBS = 6;
    static final int WATER_GAL = 8;
    static final int POLLUTION_OZ = 12;






    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_pie_chart);
//        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

//        ImageButton button = (ImageButton) findViewById(R.id.add2);
//        button.setOnClickListener(new AddNotesEvent());

        myDialog = new Dialog(this);

        tree = MainActivity.banana;
        try {
            findInfo(tree);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.activity_pie_chart, null);

        gestureObject = new GestureDetectorCompat(customView.getContext(), new LearnGesture());

        ImageButton button = (ImageButton) customView.findViewById(R.id.add2);
        button.setOnClickListener(new AddNotesEvent());

        pieChart = (PieChart) customView.findViewById(R.id.chart);
        //PieChartDisplay pieChartDisplay = new PieChartDisplay(getApplicationContext());
        //pieChartDisplay.display(tree);
        //noData = (TextView) customView.findViewById(R.id.no_data);
        //noData.setVisibility(View.GONE);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Description description = new Description();
                description.setTextSize(12);
                PieEntry selected = (PieEntry) e.copy();
                String category = selected.getLabel();
                Log.i("thing", "description set: " + description.getPosition());
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                description.setPosition(width/2, height /4*3);
                description.setTextAlign(CENTER);
                switch (category){
                    case("Storm Water"):
                        description.setText("Trees act as mini-reservoirs, controlling runoff at the source.");
                        pieChart.setDescription(description);
                        break;
                    case("Energy"):
                        description.setText("Strategically placed trees can increase home energy efficiency.");
                        pieChart.setDescription(description);
                        break;
                    case("Air Quality"):
                        description.setText("Urban forest can mitigate the health effects of pollution.");
                        pieChart.setDescription(description);
                        break;
                    case("Property Value"):
                        description.setText("");
                        pieChart.setDescription(description);
                        break;
                    case("Natural Gas"):
                        description.setText("");
                        pieChart.setDescription(description);
                        break;
                    case("CO2"):
                        description.setText("Trees sequester CO2 in their roots, trunks, stems and leaves");
                        pieChart.setDescription(description);
                        break;
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        ArrayList<PieEntry> entries = new ArrayList<>();
        if(!tree.getDataSource().equals("ExtendedCoH")){
            float stormWater;
            if (hasValues) {
                stormWater = Float.valueOf(allInfo.split(",")[STORM_WATER]);
                entries.add(new PieEntry(stormWater, "Storm Water"));
            } else {
                stormWater = 5f;
            }
            //entries.add(new PieEntry(stormWater, "Storm Water"));
            float electricity;
            //if(hasValues) { electricity = Float.valueOf(allInfo.split(",")[14]); }
            //else{electricity = 2.54f;}
            //entries.add(new PieEntry(2.54f, "Energy"));
            final float pollution;
            if (hasValues) {
                pollution = Float.valueOf(allInfo.split(",")[POLLUTION]);
                entries.add(new PieEntry(pollution, "Air Quality"));

            } else {
                pollution = 5;
            }
//            entries.add(new PieEntry(pollution, "Air Quality"));
            //entries.add(new PieEntry(1.23f, "Property Value"));
            //entries.add(new PieEntry(0.45f, "Natural Gas"));
            float co2;
            if (hasValues) {
                co2 = Float.valueOf(allInfo.split(",")[CO2]);
                entries.add(new PieEntry(co2, "CO2"));

            } else {
                co2 = 5f;
            }
//            entries.add(new PieEntry(co2, "CO2"));

        }else{
            float stormWater;
            if (hasValues) {
                stormWater = Float.valueOf(allInfo.split(",")[22]);
            } else {
                stormWater = 5f;
            }
            if(stormWater != 0) {
                entries.add(new PieEntry(stormWater, "Storm Water"));
            }

            float energy;
            if (hasValues) {
                energy = Float.valueOf(allInfo.split(",")[28]);
            } else {
                energy = 5f;
            }
            if(energy>0) {
                entries.add(new PieEntry(energy, "Energy"));
            }

            final float pollution;
            if (hasValues) {
                pollution = Float.valueOf(allInfo.split(",")[26]);
            } else {
                pollution = 5;
            }
            if(pollution!=0) {
                entries.add(new PieEntry(pollution, "Air Quality"));
            }

            float co2;
            if (hasValues) {
                co2 = Float.valueOf(allInfo.split(",")[20]);
            } else {
                co2 = 5f;
            }
            if(co2!=0) {
                entries.add(new PieEntry(co2, "CO2"));
            }
        }
        dataset = new PieDataSet(entries, "");
        data = new PieData(dataset);
        IValueFormatter iValueFormatter = new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                DecimalFormat mFormat = new DecimalFormat("###,###,##0.00");
                return  "$" + mFormat.format(value);
            }
        };
        for(PieEntry pieEntry: entries){
            total+=pieEntry.getValue();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        String entry = "$"+df.format(total);
        data.setValueFormatter(iValueFormatter);
        data.setValueTextSize(20);
        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.setCenterText(commonName +" Annual Benefits equals "+ entry);
        pieChart.setCenterTextSize(20);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        dataset.setColors(COLORFUL_COLORS);
        pieChart.getLegend().setWordWrapEnabled(true);
//        if ( PrefManager.getInteger("lastIDMethod", -1)==3) {
//
//        }
//        else {
            pieChart.animateY(2000);
//        }


        setContentView(customView);
        gestureObject = new GestureDetectorCompat(customView.getContext(), new LearnGesture());
        if(!hasValues){
            pieChart.setVisibility(View.GONE);
            TextView noData = (TextView) findViewById(R.id.no_data_1);
            noData.setVisibility(View.VISIBLE);
            noData.setText("There is no data to display on this "+ commonName);
        }
        //customView/setContentView();








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
//        final View rootView = this.getWindow().getDecorView().findViewById(R.id.masterLayout);
//        rootView.post(new Runnable() {
//            @Override
//            public void run() {
//                popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 400);
//
//            }
//        });

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            if (event2.getX() > event1.getX()) {
                //left to right swipe
                Intent intent1 = new Intent(Pie_Chart_Activity.this, Cereal_Box_Activity.class);
                finish();
                startActivity(intent1);
            } else if (event2.getX() < event1.getX()) {
                //right to left swipe
                Intent intent2 = new Intent(Pie_Chart_Activity.this, Tree_Info_First.class);
                finish();
                startActivity(intent2);
            }
            return true;
        }
    }

    private class AddNotesEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
//            Intent intentA = new Intent(Cereal_Box_Activity.this, AddNotesActivity.class);
//            startActivity(intentA);
            ShowPopup(v);
        }
    }

    public void ShowPopup(View v){
        TextView txtclose;
        Button buttonSubmit;
        myDialog.setContentView(R.layout.add_notes_display);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        buttonSubmit = (Button) myDialog.findViewById(R.id.add_notes_button);
        txtclose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                myDialog.dismiss();
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void findInfo(Tree tree) throws IOException {
        total = 0.0;
        InputStream input;
        if(tree.getDataSource().equals("ExtendedCoH")) {
            input = getResources().openRawResource(R.raw.katelyns_database);
        }else{
            input = getResources().openRawResource(R.raw.individual_tree_benefits_18july18);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        commonName = Transform.ChangeName(tree.getCommonName());
        allInfo = "";
        hasValues = false;
        double treeDbh = tree.getCurrentDBH();
        BigDecimal bd = new BigDecimal(treeDbh);
        //bd = bd.setScale(0, RoundingMode.DOWN);
        while ((line = reader.readLine()) != null) {
            String treeName = line.split(",")[1];
            if (treeName.equals(commonName)) {
                String dbh = line.split(",")[2];
                Double diameter = Double.parseDouble(dbh);
                BigDecimal bd1 = new BigDecimal(diameter);
                //bd1 = bd1.setScale(0, RoundingMode.DOWN);
                if(bd1.doubleValue()==0){bd1 = BigDecimal.valueOf(1);}
                if (bd1.doubleValue() == bd.doubleValue()) {
                    allInfo = line;
                    hasValues = true;
                    break;
                }
            }
        }
        if (allInfo.equals("")) {
            allInfo = "N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A";
        }
//        total = 0.0;
//        hasValues = false;
//        //InputStream inputStream = parent.getResources().openRawResource(R.raw.individual_tree_tenefits_18july18);
//        InputStream input;
//        if(tree.getDataSource().equals("ExtendedCoH")) {
//            input = getResources().openRawResource(R.raw.katelyns_database);
//        }else{
//            input = getResources().openRawResource(R.raw.individual_tree_benefits_18july18);
//        }
//        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//        String line;
//        commonName = Transform.ChangeName(tree.getCommonName());
//        allInfo = "";
//        hasValues = false;
//        double treeDbh = tree.getCurrentDBH();
//        BigDecimal bd = new BigDecimal(treeDbh);
//        //bd = bd.setScale(0, RoundingMode.DOWN);
//        int treeID= Integer.parseInt(tree.getID());
//        while ((line = reader.readLine()) != null) {
//            String treeName = line.split(",")[1];
//            if (treeName.equals(commonName)) {
////
////                String tID = line.split(",")[0];
////                int trees = Integer.parseInt(tID);
////                if(trees == treeID){
////                    allInfo = line;
////                    hasValues = true;
////                    break;
////                }
//
//
//
//
//                String dbh = line.split(",")[2];
//                Double diameter = Double.parseDouble(dbh);
//                BigDecimal bd1 = new BigDecimal(diameter);
//                //bd1 = bd1.setScale(0, RoundingMode.DOWN);
//                if(bd1.doubleValue()==0){bd1 = BigDecimal.valueOf(1);}
//                if (bd1.doubleValue() == bd.doubleValue()) {
//                    allInfo = line;
//                    hasValues = true;
//                    break;
//                }
//            }
//        }
//        if (allInfo.equals("")) {
//            allInfo = "N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A";
//        }
    }
}
