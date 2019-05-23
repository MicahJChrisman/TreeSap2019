package com.example.treesapv2new.display;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import java.util.HashMap;

import com.example.treesapv2new.R;
import com.example.treesapv2new.control.PrefManager;
import com.example.treesapv2new.control.Transform;
import com.example.treesapv2new.model.SettingsOption;
import com.example.treesapv2new.model.Tree;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PieChartDisplay extends DisplayMethod implements OnChartValueSelectedListener{

    public PieChartDisplay() {
        super();
    }

    public PieChartDisplay(Context context) {
        super(context);
    }

    @Override
    public void setParent(Context context) {
        super.setParent(context);
    }

    private static final int[] COLORFUL_COLORS = {
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53), Color.rgb(55, 55, 200)
    };
    private PopupWindow popupWindow;
    private String commonName = "";
    private String allInfo;
    private PieChart pieChart;
    private PieDataSet dataset;
    private PieData data;
    private double total;
    private boolean hasValues = false;
    private TextView noData;
    private Tree trees;

    @Override
    public String getMethodName() {
        return "Chart Benefit Display";
    }

    public String getDescription() {
        return "Display method that displays tree benefits in a chart";
    }

    private void findInfo(Tree tree) throws IOException {
        total = 0.0;
        hasValues = false;
        InputStream inputStream = parent.getResources().openRawResource(R.raw.individual_tree_tenefits_18july18);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        commonName = Transform.ChangeName(tree.getCommonName());
        allInfo = "";
        hasValues = false;
            double treeDbh = tree.getCurrentDBH();
            BigDecimal bd = new BigDecimal(treeDbh);
            bd = bd.setScale(0, RoundingMode.DOWN);
            while ((line = reader.readLine()) != null) {
                String treeName = line.split(",")[1];
                if (treeName.equals(commonName)) {
                    String dbh = line.split(",")[2];
                    Double diameter = Double.parseDouble(dbh);
                    BigDecimal bd1 = new BigDecimal(diameter);
                    bd1 = bd1.setScale(0, RoundingMode.DOWN);
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
        }

    public void display(Tree tree) {
        trees = tree;
        if (parent == null) {
            return;
        }
        try {
            findInfo(tree);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LayoutInflater inflater = (LayoutInflater) parent.getSystemService(LAYOUT_INFLATER_SERVICE);
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
        pieChart.setOnChartValueSelectedListener(this);
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
        if ( PrefManager.getInteger("lastIDMethod", -1)==3) {

        }
        else {
            pieChart.animateY(5000);
        }
        if(!hasValues){
            pieChart.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
            noData.setText("There is no data to display on this "+ commonName);
        }

        TextView benefit = (TextView) customView.findViewById(R.id.goto_ben);
        benefit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                CerealBoxDisplay cerealBoxDisplay = new CerealBoxDisplay(parent);
                cerealBoxDisplay.display(trees);
            }
        });

        TextView notes1 = (TextView) customView.findViewById(R.id.add_notes1);
        notes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                AddNotesDisplay addNotesDisplay1 = new AddNotesDisplay(parent);
                addNotesDisplay1.display(trees);
                //startActivity(new Intent(CerealBoxDisplay.this, AddNotesActivity.class));
            }
        });

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
        View rootView = ((Activity)parent).getWindow().getDecorView().findViewById(R.id.drawer_layout);
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 400);
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

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Description description = new Description();
        description.setTextSize(12);
        PieEntry selected = (PieEntry) e.copy();
        String category = selected.getLabel();
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

    @Override
    public HashMap<String, SettingsOption> getPreferences()  {
        preferences = new HashMap<>();
        return preferences;
    }
}
