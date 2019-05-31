package com.example.treesapv2new;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treesapv2new.control.Transform;
import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.HopeCollegeDataSource;
import com.example.treesapv2new.display.AddNotesActivity;
import com.example.treesapv2new.display.AddNotesDisplay;
import com.example.treesapv2new.display.DisplayMethod;
import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.model.TreeLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import static com.example.treesapv2new.Pie_Chart_Activity.CARBON_LBS;
import static com.example.treesapv2new.Pie_Chart_Activity.CO2;
import static com.example.treesapv2new.Pie_Chart_Activity.POLLUTION;
import static com.example.treesapv2new.Pie_Chart_Activity.POLLUTION_OZ;
import static com.example.treesapv2new.Pie_Chart_Activity.STORM_WATER;
import static com.example.treesapv2new.Pie_Chart_Activity.TOTAL;
import static com.example.treesapv2new.Pie_Chart_Activity.WATER_GAL;


public class Cereal_Box_Activity extends AppCompatActivity {
//    TreeLocation testing = new TreeLocation(42.7878,-86.1057);
    String sentString;
    Dialog myDialog;

//    public Cereal_Box_Activity(String sentString){
//        this.sentString = sentString;
//    }

    private GestureDetectorCompat gestureObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_cereal_box);

//        TextView eventer = (TextView) findViewById(R.id.treeNamePlease);
//        eventer.setText(MainActivity.banana.getCommonName());
//
//        TextView eventer1 = (TextView) findViewById(R.id.latitudetree);
//        eventer1.setText(String.valueOf(MainActivity.banana.getLocation().getLatitude()));
//
//        TextView eventer2= (TextView) findViewById(R.id.longitudetree);
//        eventer2.setText(String.valueOf(MainActivity.banana.getLocation().getLongitude()));


        myDialog = new Dialog(this);

        display(MainActivity.banana);


//        ImageButton button = (ImageButton) findViewById(R.id.add_notes);
//        button.setOnClickListener(new AddNotesEvent());
    }



    private String allInfo;
    private String commonName;
    private int right;
    private PopupWindow popupWindow;
    private Tree trees;
    private boolean hasValues = false;
    private  TextView noData;

//    public CerealBoxDisplay() {
//        super();
//    }
//
//    public CerealBoxDisplay(Context context) {
//        super(context);
//    }

//    @Override
//    public void setParent(Context context) {
////        super.setParent(context);
////    }

//    @Override
//    public String getMethodName() {
//        return "Benefit Information Display";
//    }
//
//    @Override
//    public String getDescription() {
//        return "Display that has information about tree benefits";
//    }

    // This "cerealbox" method will draw to a canvas and create a bitmap from it.

    private int cerealBoxWidth = 1000;
    private int cerealBoxHeight = 1800;
    private final int boxMargin = 20;
    private final int boxLineWidth = 20;
    private final int titleTextSize = 22;
    private final int subtitleTextSize = 15;
    private final int categoryTextSize = 20;

    public int dpToPx(int dp, DisplayMetrics dm)
    {
        return (int) (dp * dm.density);
    }

    public int pxToDp(int px, DisplayMetrics dm)
    {
        return (int) (px / dm.density);
    }

    public void findInfo(Tree tree) throws IOException {

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
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        while ((line = reader.readLine()) != null) {
            String treeName = line.split(",")[1];
            if (treeName.equals(commonName)) {
                String dbh = line.split(",")[2];
                Double diameter = Double.parseDouble(dbh);
                BigDecimal bd1 = new BigDecimal(diameter);
                bd1 = bd1.setScale(2, RoundingMode.HALF_UP);
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

        try {
            findInfo(tree);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        cerealBoxWidth = displayMetrics.widthPixels+125;
        right = cerealBoxWidth - 90;
        cerealBoxHeight = displayMetrics.heightPixels+250;
        Bitmap bitmap = Bitmap.createBitmap(cerealBoxWidth, cerealBoxHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        final Paint paint = new Paint();

        DisplayMetrics displaymetrics = this.getResources().getDisplayMetrics();
        Paint textPaint = new Paint();
        Paint rightPaint = new Paint();
        rightPaint.setTextAlign(Paint.Align.RIGHT);
        rightPaint.setColor(Color.BLACK);
        rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        rightPaint.setTextSize(dpToPx(subtitleTextSize, displaymetrics));

        String str;
        float y;

        // Compute real width and height


        // Draw on the canvas
        paint.setColor(Color.BLACK);
        canvas.drawRect(boxMargin, boxMargin,
                cerealBoxWidth-2*boxMargin, cerealBoxHeight-2*boxMargin,
                paint);
        paint.setColor(Color.WHITE);
        canvas.drawRect(boxMargin+boxLineWidth, boxMargin+boxLineWidth,
                cerealBoxWidth-2*boxMargin-boxLineWidth, cerealBoxHeight-2*boxMargin-boxLineWidth,
                paint);
        // Title
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(dpToPx(titleTextSize, displaymetrics));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(commonName,
                boxMargin + boxLineWidth + 10, boxMargin + boxLineWidth + dpToPx(titleTextSize, displaymetrics) + 10,
                textPaint);
        if(tree.getScientificName()==null||tree.getScientificName().equals("")){
            y = boxMargin+boxLineWidth+dpToPx(titleTextSize, displaymetrics)+10;
        }
        else {
            y = boxMargin+boxLineWidth+dpToPx(titleTextSize, displaymetrics)+90;
            canvas.drawText("(" + tree.getScientificName() + ")",
                    boxMargin + boxLineWidth + 10, y,
                    textPaint);
        }


        // Subtitle
        str = "Serving size: "+((int)(tree.getCurrentDBH()*100)/100.0)+"\" DBH";
        String condition = (String) tree.getInfo("Condition");
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        textPaint.setTextSize(dpToPx(subtitleTextSize, displaymetrics));
        y += dpToPx(subtitleTextSize, displaymetrics) + 15;
        if (condition!=null) {
            canvas.drawText(str+", "+condition+" condition", boxMargin + boxLineWidth + 10, y, textPaint);
        }
        else {
            canvas.drawText(str, boxMargin + boxLineWidth + 10, y, textPaint);
        }
        // Thick line
        y += 30;
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(20);
        canvas.drawLine(boxMargin+boxLineWidth+10, y,
                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
                paint);
        y += 15;

        // Make some computations



        if(!tree.getDataSource().equals("ExtendedCoH")) {


            // And report the computations
            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            y += dpToPx(subtitleTextSize, displaymetrics) + 15;
            canvas.drawText("Total benefits for this year:",
                    boxMargin + boxLineWidth + 10, y,
                    textPaint);
            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText("$" + allInfo.split(",")[TOTAL], right, y, rightPaint);
            y += dpToPx(subtitleTextSize, displaymetrics) - 10;

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
                    paint);

            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
            canvas.drawText("Carbon Dioxide Sequestered",
                    boxMargin + boxLineWidth + 10, y,
                    textPaint);
            canvas.drawText("$" + allInfo.split(",")[CO2], right, y, rightPaint);

            y += dpToPx(subtitleTextSize, displaymetrics) - 5;

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
                    paint);

            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
            canvas.drawText("CO2 absorbed each year",
                    boxMargin + boxLineWidth + 70, y,
                    textPaint);
            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText(allInfo.split(",")[CARBON_LBS] + " " +
                    "lbs", right, y, rightPaint);
            y += dpToPx(subtitleTextSize, displaymetrics) - 5;

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
                    paint);

            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
            canvas.drawText("Storm Water",
                    boxMargin + boxLineWidth + 10, y,
                    textPaint);
            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText("$" + allInfo.split(",")[STORM_WATER], right, y, rightPaint);

            y += dpToPx(subtitleTextSize, displaymetrics) - 5;

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
                    paint);

            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
            canvas.drawText("Rainfall intercepted each year",
                    boxMargin + boxLineWidth + 70, y,
                    textPaint);
            String ft = allInfo.split(",")[WATER_GAL];
            if (!ft.equals("N/A")) {
                double gal = Double.parseDouble(ft) * 7.48052;
                DecimalFormat df = new DecimalFormat("0.00");
                ft = df.format(gal);
            }
            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText(ft + " gal.", right, y, rightPaint);
            y += dpToPx(subtitleTextSize, displaymetrics) - 5;

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
                    paint);

            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
            canvas.drawText("Pollution",
                    boxMargin + boxLineWidth + 10, y,
                    textPaint);
            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText("$" + allInfo.split(",")[POLLUTION], right, y, rightPaint);

            y += dpToPx(subtitleTextSize, displaymetrics) - 5;

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
                    paint);

            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
            canvas.drawText("Air Pollution removed each year",
                    boxMargin + boxLineWidth + 70, y,
                    textPaint);
            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText(allInfo.split(",")[POLLUTION_OZ] + " oz", right, y, rightPaint);

            y += dpToPx(subtitleTextSize, displaymetrics) - 5;

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(15);
            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
                    paint);
        }else{


            // And report the computations
            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            y += dpToPx(subtitleTextSize, displaymetrics) + 15;
            canvas.drawText("Total benefits for this year:",
                    boxMargin + boxLineWidth + 10, y,
                    textPaint);
            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText("$" + allInfo.split(",")[28], right, y, rightPaint);
            y += dpToPx(subtitleTextSize, displaymetrics) - 10;

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
                    paint);

            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
            canvas.drawText("Carbon Dioxide Sequestered",
                    boxMargin + boxLineWidth + 10, y,
                    textPaint);
            canvas.drawText("$" + allInfo.split(",")[21], right, y, rightPaint);

            y += dpToPx(subtitleTextSize, displaymetrics) - 5;

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
                    paint);

            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
            canvas.drawText("CO2 absorbed each year",
                    boxMargin + boxLineWidth + 70, y,
                    textPaint);
            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText(allInfo.split(",")[20] + " " +
                    "lbs", right, y, rightPaint);
            y += dpToPx(subtitleTextSize, displaymetrics) - 5;

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
                    paint);

            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
            canvas.drawText("Storm Water",
                    boxMargin + boxLineWidth + 10, y,
                    textPaint);
            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText("$" + allInfo.split(",")[23], right, y, rightPaint);

            y += dpToPx(subtitleTextSize, displaymetrics) - 5;

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
                    paint);

            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
            canvas.drawText("Rainfall intercepted each year",
                    boxMargin + boxLineWidth + 70, y,
                    textPaint);
            String ft = allInfo.split(",")[22];
            if (!ft.equals("N/A")) {
                double gal = Double.parseDouble(ft) * 7.48052;
                DecimalFormat df = new DecimalFormat("0.00");
                ft = df.format(gal);
            }
            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText(ft + " gal.", right, y, rightPaint);
            y += dpToPx(subtitleTextSize, displaymetrics) - 5;

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
                    paint);

            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
            canvas.drawText("Pollution",
                    boxMargin + boxLineWidth + 10, y,
                    textPaint);
            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText("$" + allInfo.split(",")[27], right, y, rightPaint);

            y += dpToPx(subtitleTextSize, displaymetrics) - 5;

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
                    paint);

            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
            canvas.drawText("Air Pollution removed each year",
                    boxMargin + boxLineWidth + 70, y,
                    textPaint);
            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText(allInfo.split(",")[26] + " oz", right, y, rightPaint);

            y += dpToPx(subtitleTextSize, displaymetrics) - 5;

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(15);
            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
                    paint);
        }

        /*textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
        canvas.drawText("Energy Usage each year",
                boxMargin+boxLineWidth+10, y,
                textPaint);
        rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("$" + allInfo.split(",")[14], right, y, rightPaint);

        y += dpToPx(subtitleTextSize, displaymetrics)-5;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        canvas.drawLine(boxMargin+boxLineWidth+10, y,
                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
                paint);

        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
        canvas.drawText("Electricity savings (A/C)",
                boxMargin+boxLineWidth+70, y,
                textPaint);
        rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText( "21.95"+ " kWh", right, y, rightPaint);
       // canvas.drawText(allInfo.split(",")[6]+"lbs", boxMargin+boxLineWidth+760, y, textPaint);

        y += dpToPx(subtitleTextSize, displaymetrics)-5;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        canvas.drawLine(boxMargin+boxLineWidth+10, y,
                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
                paint);

        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
        canvas.drawText("Fuel savings (NG, Oil)",
                boxMargin+boxLineWidth+70, y,
                textPaint);

        canvas.drawText( "-2.74"+ " therms", right, y, rightPaint);
        // canvas.drawText(allInfo.split(",")[6]+"lbs", boxMargin+boxLineWidth+760, y, textPaint);

        y += dpToPx(subtitleTextSize, displaymetrics)-5;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        canvas.drawLine(boxMargin+boxLineWidth+10, y,
                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
                paint);

        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
        canvas.drawText("Avoided Emissions",
                boxMargin+boxLineWidth+10, y,
                textPaint);
        y += dpToPx(subtitleTextSize, displaymetrics)-5;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        canvas.drawLine(boxMargin+boxLineWidth+10, y,
                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
                paint);

        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
        canvas.drawText("Carbon dioxide",
                boxMargin+boxLineWidth+70, y,
                textPaint);
        canvas.drawText( "-17.36"+ " lbs", right, y, rightPaint);

        y += dpToPx(subtitleTextSize, displaymetrics)-5;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        canvas.drawLine(boxMargin+boxLineWidth+10, y,
                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
                paint);


        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
        canvas.drawText("Nitrogen dioxide",
                boxMargin+boxLineWidth+70, y,
                textPaint);
        canvas.drawText( "-0.06"+ " oz", right, y, rightPaint);

        y += dpToPx(subtitleTextSize, displaymetrics)-5;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        canvas.drawLine(boxMargin+boxLineWidth+10, y,
                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
                paint);


        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
        canvas.drawText("Sulfur dioxide",
                boxMargin+boxLineWidth+70, y,
                textPaint);
        canvas.drawText( "-1.02"+ " oz", right, y, rightPaint);

        y += dpToPx(subtitleTextSize, displaymetrics)-5;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        canvas.drawLine(boxMargin+boxLineWidth+10, y,
                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
                paint);


        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
        canvas.drawText("Large particulate matter",
                boxMargin+boxLineWidth+70, y,
                textPaint);
        canvas.drawText( "<0.1"+ " oz", right, y, rightPaint);*/

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.activity_cereal_box, null);


        ImageButton button = (ImageButton) customView.findViewById(R.id.add_notes);
        button.setOnClickListener(new AddNotesEvent());
//        TextView okay = (TextView) customView.findViewById(R.id.okay_cereal);
//        okay.bringToFront();
//        okay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//                popupWindow = null;
//            }
//        });



        ImageView imageView = (ImageView) customView.findViewById(R.id.cereal_image);
        imageView.setImageBitmap(bitmap);
        noData = (TextView) customView.findViewById(R.id.no_ben);
        noData.setVisibility(View.GONE);
        if(!hasValues){
            imageView.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
            noData.setText("There is no data to display on this "+ commonName);
        }


        setContentView(customView);
        gestureObject = new GestureDetectorCompat(customView.getContext(), new LearnGesture());
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
////        View rootView = ((Activity)getParent()).getWindow().getDecorView().findViewById(R.id.drawer_layout);
////        View rootView = findViewById(R.layout.activity_cereal_box)
////        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 400);
//        final View rootView = this.getWindow().getDecorView().findViewById(R.id.drawer_layout);
//        gestureObject = new GestureDetectorCompat(rootView.getContext(), new LearnGesture());
//        rootView.post(new Runnable() {
//            @Override
//            public void run() {
//                popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 400);
//
//            }
//        });

    }


    public PopupWindow getPopupWindow(){
        return popupWindow;
    }

    public void dismiss(){
        popupWindow.dismiss();
        popupWindow=null;
    }

//    @Override
//    public HashMap<String, SettingsOption> getPreferences()  {
//        preferences = new HashMap<>();
//        return preferences;
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY){
            if(event2.getX()>event1.getX()){
                //left to right swipe
                Intent intent1 = new Intent(Cereal_Box_Activity.this, Tree_Info_First.class);
                finish();
                startActivity(intent1);
            }else if(event2.getX()<event1.getX()){
                //right to left swipe
                Intent intent2 = new Intent(Cereal_Box_Activity.this, Pie_Chart_Activity.class);
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
        gestureObject = new GestureDetectorCompat(this, new LearnGesture());
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
}