package com.example.treesapv2new.display;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;

import com.example.treesapv2new.R;
import com.example.treesapv2new.control.Transform;
import com.example.treesapv2new.model.SettingsOption;
import com.example.treesapv2new.model.Tree;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CerealBoxDisplay extends DisplayMethod {

    private String allInfo;
    private String commonName;
    private int right;
    private PopupWindow popupWindow;
    private Tree trees;
    private boolean hasValues = false;
    private TextView noData;

    public CerealBoxDisplay() {
        super();
    }

    public CerealBoxDisplay(Context context) {
        super(context);
    }

    @Override
    public void setParent(Context context) {
        super.setParent(context);
    }

    @Override
    public String getMethodName() {
        return "Benefit Information Display";
    }

    @Override
    public String getDescription() {
        return "Display that has information about tree benefits";
    }

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

    @Override
    public void display(Tree tree) {
        trees = tree;
        try {
            findInfo(tree);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) parent.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        cerealBoxWidth = displayMetrics.widthPixels+125;
        right = cerealBoxWidth - 90;
        cerealBoxHeight = displayMetrics.heightPixels+250;
        Bitmap bitmap = Bitmap.createBitmap(cerealBoxWidth, cerealBoxHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        final Paint paint = new Paint();

        DisplayMetrics displaymetrics = parent.getResources().getDisplayMetrics();
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


        // And report the computations
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        y += dpToPx(subtitleTextSize, displaymetrics) + 15;
        canvas.drawText("Total benefits for this year:",
                boxMargin+boxLineWidth+10, y,
                textPaint);
        rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("$" + allInfo.split(",")[TOTAL], right, y, rightPaint);
        y += dpToPx(subtitleTextSize, displaymetrics) - 10;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        canvas.drawLine(boxMargin+boxLineWidth+10, y,
                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
                paint);

        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
        canvas.drawText("Carbon Dioxide Sequestered",
                boxMargin+boxLineWidth+10, y,
                textPaint);
        canvas.drawText("$" + allInfo.split(",")[CO2], right, y, rightPaint);

        y += dpToPx(subtitleTextSize, displaymetrics)-5;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        canvas.drawLine(boxMargin+boxLineWidth+10, y,
                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
                paint);

        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
        canvas.drawText("CO2 absorbed each year",
                boxMargin+boxLineWidth+70, y,
                textPaint);
        rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(allInfo.split(",")[CARBON_LBS]+" " +
                "lbs", right, y, rightPaint);
        y += dpToPx(subtitleTextSize, displaymetrics)-5;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        canvas.drawLine(boxMargin+boxLineWidth+10, y,
                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
                paint);

        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
        canvas.drawText("Storm Water",
                boxMargin+boxLineWidth+10, y,
                textPaint);
        rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("$" + allInfo.split(",")[STORM_WATER], right, y, rightPaint);

        y += dpToPx(subtitleTextSize, displaymetrics)-5;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        canvas.drawLine(boxMargin+boxLineWidth+10, y,
                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
                paint);

        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
        canvas.drawText("Rainfall intercepted each year",
                boxMargin+boxLineWidth+70, y,
                textPaint);
        String ft = allInfo.split(",")[WATER_GAL];
        if(!ft.equals("N/A")) {
            double gal = Double.parseDouble(ft) * 7.48052;
            DecimalFormat df = new DecimalFormat("0.00");
            ft = df.format(gal);
        }
        rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText( ft+ " gal.", right, y, rightPaint);
        y += dpToPx(subtitleTextSize, displaymetrics)-5;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        canvas.drawLine(boxMargin+boxLineWidth+10, y,
                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
                paint);

        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
        canvas.drawText("Pollution",
                boxMargin+boxLineWidth+10, y,
                textPaint);
        rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("$" + allInfo.split(",")[POLLUTION], right, y, rightPaint);

        y += dpToPx(subtitleTextSize, displaymetrics)-5;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        canvas.drawLine(boxMargin+boxLineWidth+10, y,
                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
                paint);

        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
        canvas.drawText("Air Pollution removed each year",
                boxMargin+boxLineWidth+70, y,
                textPaint);
        rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText(allInfo.split(",")[POLLUTION_OZ] +" oz", right, y, rightPaint);

        y += dpToPx(subtitleTextSize, displaymetrics)-5;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(15);
        canvas.drawLine(boxMargin+boxLineWidth+10, y,
                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
                paint);

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

        LayoutInflater inflater = (LayoutInflater) parent.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.cereal_box, null);
        TextView okay = (TextView) customView.findViewById(R.id.okay_cereal);
        okay.bringToFront();
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                popupWindow = null;
            }
        });

        TextView notes = (TextView) customView.findViewById(R.id.add_notes);
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                AddNotesDisplay addNotesDisplay = new AddNotesDisplay(parent);
                addNotesDisplay.display(trees);
                //startActivity(new Intent(CerealBoxDisplay.this, AddNotesActivity.class));
            }
        });



        TextView chart = (TextView) customView.findViewById(R.id.goto_chart);
        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                PieChartDisplay pieChartDisplay = new PieChartDisplay(parent);
                pieChartDisplay.display(trees);
            }
        });





        ImageView imageView = (ImageView) customView.findViewById(R.id.cereal_image);
        imageView.setImageBitmap(bitmap);
        noData = (TextView) customView.findViewById(R.id.no_ben);
        noData.setVisibility(View.GONE);
        if(!hasValues){
            imageView.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
            noData.setText("There is no data to display on this "+ commonName);
        }

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
    public HashMap<String, SettingsOption> getPreferences()  {
        preferences = new HashMap<>();
        return preferences;
    }
}
