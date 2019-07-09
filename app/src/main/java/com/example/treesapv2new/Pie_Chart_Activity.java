package com.example.treesapv2new;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treesapv2new.control.PrefManager;
import com.example.treesapv2new.control.Transform;
import com.example.treesapv2new.datasource.ExtendedCoHDataSource;
import com.example.treesapv2new.display.AddNotesActivity;
import com.example.treesapv2new.display.PieChartDisplay;
import com.example.treesapv2new.model.Tree;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
    private static final int REQUEST_IMGAGE_CAPTURE = 101;
    private static final String[] PERMS = {
            Manifest.permission.CAMERA,
    };
    private static final int REQUEST_ID = 1;
    private static final int[] PERMISSION_ALL = new int[0];
    private byte[] byteArray;

    private static final int[] COLORFUL_COLORS = {

            Color.parseColor("#F4CC70"), //Storm water, yellow
            Color.parseColor("#DE7A22"), //Energy, orange
            Color.parseColor("#20948B"), //Air quality, dark turquoise
            Color.parseColor("#6AB187"), //CO2, sea green







            //purplething
            //Color.rgb(193, 37, 82),
            //orange
            //Color.rgb(255, 102, 0),
            //yellow
            //Color.rgb(245, 199, 0),
            //beige
            Color.rgb(239, 201, 175),
            //dark blue
            Color.rgb(16, 76, 145),
            //turquiose
            Color.rgb(31, 138, 192),
            //green
            Color.rgb(106, 150, 31),

            Color.rgb(179, 100, 53),

            Color.rgb(55, 55, 200),
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



        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_brb:
                        Intent intent1 = new Intent(Pie_Chart_Activity.this, Big_Red_Button.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_home:
                        Intent intent2 = new Intent(Pie_Chart_Activity.this, MainActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_coordinates:
                        Intent intent3 = new Intent(Pie_Chart_Activity.this, Coordinates_View_Activity.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_map:
                        Intent intent4 = new Intent(Pie_Chart_Activity.this, Maps_Activity.class);
                        startActivity(intent4);
                        break;
                    case R.id.navigation_qr:
                        Intent intent5 = new Intent(Pie_Chart_Activity.this, QR_Code_Activity.class);
                        startActivity(intent5);
                        break;
                }
                return false;
            }
        };



        myDialog = new Dialog(this);

        tree = MainActivity.banana;
        String dsOrig = tree.getDataSource();

        if(!tree.getDataSource().equals("ExtendedCoH")){
//            ((TextView) findViewById(R.id.not_exact_text)).setVisibility(View.VISIBLE);
            ExtendedCoHDataSource extendedCoHDataSource = new ExtendedCoHDataSource();
            extendedCoHDataSource.initialize(this, null);
            Tree bestMatchTree = null;
            if(tree.getDataSource().equals("User") || tree.getDataSource().equals("AllUserDB")) {
                try {
                    bestMatchTree = extendedCoHDataSource.bestMatchFinder((Double) MainActivity.banana.getDBHArray().get(0), MainActivity.banana.getLocation(), MainActivity.banana.getCommonName());
                }catch (Exception e){

                }
            }else{
                try {
                    bestMatchTree = extendedCoHDataSource.bestMatchFinder(MainActivity.banana.getCurrentDBH(), MainActivity.banana.getLocation(), MainActivity.banana.getCommonName());
                }catch (Exception e){

                }
            }
            if(bestMatchTree == null){

            }else {
                tree = bestMatchTree;
                tree.setDataSource("ExtendedCoH");
            }
        }
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

        ArrayList<PieEntry> entries = new ArrayList<>();
        if(tree.getDataSource().equals("ExtendedCoH")){
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
                energy = Float.valueOf(allInfo.split(",")[59]);
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
        }else{
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
        }
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Description description = new Description();
                description.setTextSize(-12);
                PieEntry selected = (PieEntry) e.copy();
                String category = selected.getLabel();
                Log.i("thing", "description set: " + description.getPosition());
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                description.setPosition(width/2, height/4*3);
                description.setTextAlign(CENTER);
                TextView tv = (TextView) findViewById(R.id.pie_chart_text);
                ((LinearLayout) findViewById(R.id.bar_key)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.bar_chart)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.bar_chart_pol)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.bar_key_pol)).setVisibility(View.GONE);
                switch (category){
                    case("Storm Water"):
                        description.setText("Trees act as mini-reservoirs, controlling runoff at the source.");
                        pieChart.setDescription(description);
                        tv.setVisibility(View.VISIBLE);
                        tv.setText("Trees act as mini-reservoirs, controlling runoff at the source.");
                        break;
                    case("Energy"):
                        ((LinearLayout) findViewById(R.id.bar_chart)).setVisibility(View.VISIBLE);
                        description.setText("Strategically placed trees can increase home energy efficiency.");
                        pieChart.setDescription(description);

                        float coolingEnergy = Float.valueOf(allInfo.split(",")[58]);
                        float heatingEnergy = Float.valueOf(allInfo.split(",")[56]);
                        heatingEnergy = heatingEnergy + Float.valueOf(allInfo.split(",")[54]);
                        float totalEnergy = coolingEnergy+heatingEnergy;
                        if(totalEnergy>0) {
                            tv.setVisibility(View.GONE);
                            float coolPercent = coolingEnergy / totalEnergy;
                            float heatPercent = heatingEnergy / totalEnergy;

                            int linWidth = ((LinearLayout) findViewById(R.id.bar_chart)).getWidth();
                            int coolLength = Math.round(linWidth * coolPercent);
                            int heatLength = Math.round(linWidth * heatPercent);


                            ((ImageView) findViewById(R.id.red_rect)).setVisibility(View.VISIBLE);
                            ((ImageView) findViewById(R.id.blue_rect)).setVisibility(View.VISIBLE);
                            ((LinearLayout) findViewById(R.id.bar_key)).setVisibility(View.VISIBLE);
                            ImageView cooling = findViewById(R.id.blue_rect);
                            cooling.requestLayout();
                            cooling.getLayoutParams().width = coolLength;
                            ImageView heating = findViewById(R.id.red_rect);
                            heating.requestLayout();
                            heating.getLayoutParams().width = heatLength;
                        }else{
                            tv.setText("Strategically placed trees can increase home energy efficiency.");
                        }

                        break;
                    case("Air Quality"):
                        ((LinearLayout) findViewById(R.id.bar_chart_pol)).setVisibility(View.VISIBLE);
                        description.setText("Urban forest can mitigate the health effects of pollution.");
                        pieChart.setDescription(description);
                        float coRemoved;
                        float ozoneRemoved;
                        float noRemoved;
                        float soRemoved;
                        float pmRemoved;
                        if(tree.getDataSource().equals("ExtendedCoH")) {
                            coRemoved = Float.valueOf(allInfo.split(",")[41]);
                            ozoneRemoved = Float.valueOf(allInfo.split(",")[42]);
                            noRemoved = Float.valueOf(allInfo.split(",")[43]);
                            soRemoved = Float.valueOf(allInfo.split(",")[44]);
                            pmRemoved = Float.valueOf(allInfo.split(",")[45]);
                        }
                        else{
                            coRemoved = 0;
                            ozoneRemoved=0;
                            noRemoved = 0;
                            soRemoved = 0;
                            pmRemoved = 0;
                        }
                        float totalRemoved = coRemoved+ozoneRemoved+noRemoved+soRemoved+pmRemoved;
                        if(totalRemoved>0){
                            tv.setVisibility(View.GONE);
                            coRemoved = coRemoved/totalRemoved;
                            ozoneRemoved = ozoneRemoved/totalRemoved;
                            noRemoved = noRemoved/totalRemoved;
                            soRemoved = soRemoved/totalRemoved;
                            pmRemoved = pmRemoved/totalRemoved;

                            int linWidth = ((LinearLayout) findViewById(R.id.bar_chart_pol)).getWidth();
                            int coLength = Math.round(linWidth * coRemoved);
                            int ozoneLength = Math.round(linWidth * ozoneRemoved);
                            int noLength = Math.round(linWidth * noRemoved);
                            int soLength = Math.round(linWidth * soRemoved);
                            int pmLength = Math.round(linWidth * pmRemoved);



                            ((ImageView) findViewById(R.id.co_rect)).setVisibility(View.VISIBLE);
                            ((ImageView) findViewById(R.id.o3_rect)).setVisibility(View.VISIBLE);
                            ((ImageView) findViewById(R.id.no2_rect)).setVisibility(View.VISIBLE);
                            ((ImageView) findViewById(R.id.so_rect)).setVisibility(View.VISIBLE);
                            ((ImageView) findViewById(R.id.pm_rect)).setVisibility(View.VISIBLE);
                            ((LinearLayout) findViewById(R.id.bar_key_pol)).setVisibility(View.VISIBLE);
                            ImageView co = findViewById(R.id.co_rect);
                            co.requestLayout();
                            co.getLayoutParams().width = coLength;
                            ImageView ozone = findViewById(R.id.o3_rect);
                            ozone.requestLayout();
                            ozone.getLayoutParams().width = ozoneLength;
                            ImageView no = findViewById(R.id.no2_rect);
                            no.requestLayout();
                            no.getLayoutParams().width = noLength;
                            ImageView so = findViewById(R.id.so_rect);
                            so.requestLayout();
                            so.getLayoutParams().width = soLength;
                            ImageView pm = findViewById(R.id.pm_rect);
                            pm.requestLayout();
                            pm.getLayoutParams().width = pmLength;
                        }else {
                            tv.setText("Urban forest can mitigate the health effects of pollution.");
                        }
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
                        tv.setText("Trees sequester CO2 in their roots, trunks, stems and leaves");
                        tv.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected() {
                TextView tv = (TextView) findViewById(R.id.pie_chart_text);
                tv.setVisibility(View.VISIBLE);
                tv.setText("Select a category for more information.");
                ((LinearLayout) findViewById(R.id.bar_chart)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.bar_key)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.bar_chart_pol)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.bar_key_pol)).setVisibility(View.GONE);
            }
        });

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
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setDrawSliceText(false);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        dataset.setColors(COLORFUL_COLORS);
        Legend legend = pieChart.getLegend();
        legend.setTextSize(20);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setYOffset(30f);
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
            ((TextView) findViewById(R.id.pie_chart_text)).setVisibility(View.GONE);
        }else{
            if(!dsOrig.equals("ExtendedCoH")) {
                ((TextView) findViewById(R.id.not_exact_text)).setVisibility(View.VISIBLE);
            }
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

        BottomNavigationView navView = findViewById(R.id.pie_chart_menu);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.pie_chart_menu);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.pie_chart_menu);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem1 = menu.getItem(0);
        menuItem1.setCheckable(false);
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        MainActivity.banana.setIsNearbyTree(false);
    }

    private class AddNotesEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
//            Intent intentA = new Intent(Cereal_Box_Activity.this, AddNotesActivity.class);
//            startActivity(intentA);
            ShowPopup(v);
        }
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
        if(tree.getCommonName() != null) {
            commonName = Transform.ChangeName(tree.getCommonName());
            if (commonName != null) {
                String commonNameImage = commonName.replaceAll("\\s","_");
                commonNameImage = commonNameImage.toLowerCase();
                try{
                    int id = this.getResources().getIdentifier(commonNameImage, "mipmap", Pie_Chart_Activity.this.getPackageName());
                    Drawable d = getDrawable(id);
                    ((ImageView) findViewById(R.id.pie_chart_background)).setImageDrawable(d);
                }catch (Exception e){
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT);
                }
            } else {
                //commonNameText.setText("Common name: " + "Unavailable");
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT);
            }
        }else{
            commonName = "tree";
        }
        allInfo = "";
        hasValues = false;
        double treeDbh;
        if(tree.getCurrentDBH() != null) {
            treeDbh = tree.getCurrentDBH();
        }else{
            treeDbh = 0;
        }
        BigDecimal bd = new BigDecimal(treeDbh);
        if(!tree.getDataSource().equals("ExtendedCoH")) {
            bd = bd.setScale(0, RoundingMode.DOWN);
        }
        while ((line = reader.readLine()) != null) {
            String treeName = line.split(",")[1];
            if(!treeName.equals("")) {
                if (treeName.equals(commonName)) {
                    String dbh = line.split(",")[2];
                    Double diameter = Double.parseDouble(dbh);
                    BigDecimal bd1 = new BigDecimal(diameter);
                    if (!tree.getDataSource().equals("ExtendedCoH")) {
                        bd1 = bd1.setScale(0, RoundingMode.DOWN);
                    }
                    if (bd1.doubleValue() == 0) {
                        bd1 = BigDecimal.valueOf(1);
                    }
                    if (bd1.doubleValue() == bd.doubleValue()) {
                        allInfo = line;
                        hasValues = true;
                        break;
                    }
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
    public void ShowPopup(View v){
        gestureObject = new GestureDetectorCompat(this, new LearnGesture());
        TextView txtclose;
        Button buttonSubmit;
        ImageButton imageButton;
        myDialog.setContentView(R.layout.add_notes_display);
        imageButton = (ImageButton) myDialog.findViewById(R.id.user_add_tree_pic);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        buttonSubmit = (Button) myDialog.findViewById(R.id.add_notes_button);
        imageButton.setOnClickListener(new addImageEvent());
        //        ImageButton button = (ImageButton) findViewById(R.id.add_notes);
//        button.setOnClickListener(new AddNotesEvent());
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

    private class addImageEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            ImageView picAppear = findViewById(R.id.user_add_tree_pic_appear_pie);
            picAppear.setVisibility(View.VISIBLE);

            if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMS, REQUEST_ID);
                }
            }else{
                onRequestPermissionsResult(REQUEST_ID,PERMS,PERMISSION_ALL);
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (imageTakeIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(imageTakeIntent, REQUEST_IMGAGE_CAPTURE);
            }
        }else{
            Toast.makeText(getBaseContext(), "Permissions are not right", Toast.LENGTH_SHORT).show();
        }
    }

    public static Bitmap bmp;

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMGAGE_CAPTURE && resultCode==RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byteArray = stream.toByteArray();
            //   picAppear.setImageBitmap(imageBitmap);

            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            ImageView image = (ImageView) findViewById(R.id.user_add_tree_pic_appear_pie);
            //image.setImageBitmap(bmp);
            MainActivity.banana.addPics("User pic", Base64.encodeToString(byteArray, Base64.DEFAULT));
            myDialog.dismiss();
            Toast.makeText(getBaseContext(), "Photo added. ", Toast.LENGTH_LONG).show();
        }
    }
}
