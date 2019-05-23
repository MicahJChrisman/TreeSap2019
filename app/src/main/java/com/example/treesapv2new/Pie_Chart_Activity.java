package com.example.treesapv2new;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.treesapv2new.control.PrefManager;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Pie_Chart_Activity extends AppCompatActivity {

    private GestureDetectorCompat gestureObject;





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
    private  TextView noData;
    private Tree trees;




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
        gestureObject = new GestureDetectorCompat(this, new LearnGesture());












        trees = MainActivity.banana;
//        try {
//            findInfo(tree);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.piechart_display, null);
        TextView okay = (TextView) customView.findViewById(R.id.okay_pie);
        okay.bringToFront();
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                popupWindow = null;
            }
        });
        pieChart = (PieChart) customView.findViewById(R.id.chart);
        noData = (TextView) customView.findViewById(R.id.no_data);
        noData.setVisibility(View.GONE);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });
        ArrayList<PieEntry> entries = new ArrayList<>();
        float stormWater;
        if(hasValues){ stormWater = Float.valueOf(allInfo.split(",")[STORM_WATER]); }
        else{ stormWater = 5f; }
        entries.add(new PieEntry(stormWater, "Storm Water"));
        float electricity;
        //if(hasValues) { electricity = Float.valueOf(allInfo.split(",")[14]); }
        //else{electricity = 2.54f;}
        //entries.add(new PieEntry(2.54f, "Energy"));
        final float pollution;
        if(hasValues) { pollution = Float.valueOf(allInfo.split(",")[POLLUTION]); }
        else{pollution = 5;}
        entries.add(new PieEntry(pollution, "Air Quality"));
        //entries.add(new PieEntry(1.23f, "Property Value"));
        //entries.add(new PieEntry(0.45f, "Natural Gas"));
        float co2;
        if(hasValues) {co2 = Float.valueOf(allInfo.split(",")[CO2]);}
        else{co2=5f;}
        entries.add(new PieEntry(co2, "CO2"));
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
        String enrty = "$"+df.format(total);
        data.setValueFormatter(iValueFormatter);
        data.setValueTextSize(20);
        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.setCenterText(commonName +" Annual Benefits equals "+ enrty);
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
            pieChart.animateY(5000);
//        }


//        if(!hasValues){
//            pieChart.setVisibility(View.GONE);
//            noData.setVisibility(View.VISIBLE);
//            noData.setText("There is no data to display on this "+ commonName);
//        }


        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        popupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }
//        View rootView = this.getWindow().getDecorView().findViewById(R.id.drawer_layout);
//        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 400);

















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
}
