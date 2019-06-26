package com.example.treesapv2new;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treesapv2new.control.Transform;
import com.example.treesapv2new.datasource.CityOfHollandDataSource;
import com.example.treesapv2new.datasource.DataSource;
import com.example.treesapv2new.datasource.ExtendedCoHDataSource;
import com.example.treesapv2new.datasource.HopeCollegeDataSource;
import com.example.treesapv2new.datasource.ITreeDataSource;
import com.example.treesapv2new.model.Tree;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

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
    private static final int REQUEST_IMGAGE_CAPTURE = 101;
    private byte[] byteArray;
    ImageView picAppear;

    Float testY; //for testing

    private static final String[] PERMS = {
            Manifest.permission.CAMERA,
    };
    private static final int REQUEST_ID = 1;
    private static final int[] PERMISSION_ALL = new int[0];






//    public Cereal_Box_Activity(String sentString){
//        this.sentString = sentString;
//    }

    private GestureDetectorCompat gestureObject;
//    private ActivitySwipeDetector activitySwipeDetector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cereal_box);
        setContentView(R.layout.cereal_box_new);

//        activitySwipeDetector = new ActivitySwipeDetector(this);
//        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        ScrollView scrollView = (ScrollView)findViewById(R.id.scrollView1);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            float downX, downY, upX, upY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                downX =event.getX();
                switch(event.getAction()){

                    case MotionEvent.ACTION_DOWN:{
                        downX = event.getX();
                        downY = event.getY();
                    }
                    case MotionEvent.ACTION_UP:{
                        upX = event.getX();
                        upY = event.getY();

                        float deltaX = downX - upX;
                        float deltaY = downY - upY;

                        if(Math.abs(deltaX)>100 && Math.abs(deltaX) > Math.abs(deltaY)){
                            if(deltaX<=0){

                                Intent intent1 = new Intent(Cereal_Box_Activity.this, Tree_Info_First.class);
                                finish();
                                startActivity(intent1);

                                return true;
                            }else{
                                //right to left swipe
                                Intent intent2 = new Intent(Cereal_Box_Activity.this, Pie_Chart_Activity.class);
                                finish();
                                startActivity(intent2);
                                return  true;
                            }
                        }
                    }
                }

                return false;
            }

        });














        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_brb:
                        Intent intent1 = new Intent(Cereal_Box_Activity.this, Big_Red_Button.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_home:
                        Intent intent2 = new Intent(Cereal_Box_Activity.this, MainActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_coordinates:
                        Intent intent3 = new Intent(Cereal_Box_Activity.this, Coordinates_View_Activity.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_map:
                        Intent intent4 = new Intent(Cereal_Box_Activity.this, Maps_Activity.class);
                        startActivity(intent4);
                        break;
                    case R.id.navigation_qr:
                        Intent intent5 = new Intent(Cereal_Box_Activity.this, QR_Code_Activity.class);
                        startActivity(intent5);
                        break;
                }
                return false;
            }
        };

//        TextView eventer = (TextView) findViewById(R.id.treeNamePlease);
//        eventer.setText(MainActivity.banana.getCommonName());
//
//        TextView eventer1 = (TextView) findViewById(R.id.latitudetree);
//        eventer1.setText(String.valueOf(MainActivity.banana.getLocation().getLatitude()));
//
//        TextView eventer2= (TextView) findViewById(R.id.longitudetree);
//        eventer2.setText(String.valueOf(MainActivity.banana.getLocation().getLongitude()));


        myDialog = new Dialog(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Set<String> sources = prefs.getStringSet("databasesUsedSelector",new HashSet<String>());
        if(sources.size()>0) {
            //patchTreeData();
            display(MainActivity.banana);
        }else{
            finish();
            Toast.makeText(getBaseContext(), "Select a database!", Toast.LENGTH_LONG).show();
        }
        //patchTreeData();

//        ImageButton button = (ImageButton) findViewById(R.id.add_notes);
//        button.setOnClickListener(new AddNotesEvent());

        BottomNavigationView navView = findViewById(R.id.cereal_box_menu_1);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.cereal_box_menu_1);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.cereal_box_menu_1);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem1 = menu.getItem(0);
        menuItem1.setCheckable(false);
    }

    public void display(Tree tree){
        String COMMON_NAME;
        String SCIENTIFIC_NAME;
        String DBH;
        String NATIVE;
        String ANNUAL_BENEFITS;
        String CO2_SEQUESTERED_$;
        String CO2_SEQUESTERED_lbs;
        String STORM_WATER_RUNOFF_AVOIDED;
        String RUNOFF_AVOIDED;
        String RAINFALL_INTERCEPTED;
        String AIR_POLLUTION_ANNUAL;
        String CARBON_MONOXIDE;
        String OZONE;
        String NITROGEN_DIOXIDE;
        String SULFUR_DIOXIDE;
        String PARTICULATE_MATTER;
        String ENERGY_USAGE_ANNUAL;
        String ELECTRICITY_SAVINGS;
        String FUEL_SAVINGS;
        String AVOIDED_EMISSIONS;
        String CARBON_DIOXIDE_EMISSIONS;
        String CARBON_MONOXIDE_EMISSIONS;
        String NITROGEN_DIOXIDE_EMISSIONS;
        String SULFUR_DIOXIDE_EMISSIONS;
        String PARTICULATE_MATTER_EMISSIONS;
        String CO2_TO_DATE;


        if(!tree.getDataSource().equals("ExtendedCoH")){
            ((TextView) findViewById(R.id.not_exact_text)).setVisibility(View.VISIBLE);
            ExtendedCoHDataSource extendedCoHDataSource = new ExtendedCoHDataSource();
            extendedCoHDataSource.initialize(this, null);
            Tree bestMatchTree = extendedCoHDataSource.bestMatchFinder(MainActivity.banana.getCurrentDBH(),MainActivity.banana.getLocation(),MainActivity.banana.getCommonName());
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




        String ds = tree.getDataSource();
        Benefits_Columns_Enum benefits_columns_enum = new Benefits_Columns_Enum(Benefits_Columns_Enum.database.valueOf(ds));


//        if(tree.getDataSource().equals("ExtendedCoH")) {
            try {
                COMMON_NAME = allInfo.split(",")[benefits_columns_enum.commonNameIndex()];
                ((TextView) findViewById(R.id.common_name_cereal)).setText(COMMON_NAME);
            } catch (Exception e) {
                try {
                    ((TextView) findViewById(R.id.common_name_cereal)).setText(tree.getCommonName());
                } catch (Exception i) {
                    ((TextView) findViewById(R.id.common_name_cereal)).setText("Tree Type Unknown");
                }
            }

            try {
                SCIENTIFIC_NAME = allInfo.split(",")[benefits_columns_enum.scientificNameIndex()];
                ((TextView) findViewById(R.id.scientific_name_cereal)).setText(SCIENTIFIC_NAME);
            } catch (Exception e) {
                ((TextView) findViewById(R.id.scientific_name_cereal)).setVisibility(View.GONE);
            }

            try {
                DBH = allInfo.split(",")[benefits_columns_enum.dbhIndex()];
                ((TextView) findViewById(R.id.dbh_meas_cereal)).setText(DBH + "in.");
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.dbh_linear_layout)).setVisibility(View.GONE);
            }

            try {
                NATIVE = allInfo.split(",")[benefits_columns_enum.nativeIndex()];
                ((TextView) findViewById(R.id.native_yn_cereal)).setText(NATIVE);
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.native_linear_layout)).setVisibility(View.GONE);
            }

            try {
                ANNUAL_BENEFITS = allInfo.split(",")[benefits_columns_enum.totalAnnualBenefitsDollarsIndex()];
                if(ANNUAL_BENEFITS.length() < 4){
                    ANNUAL_BENEFITS += "0";
                }
                ((TextView) findViewById(R.id.annual_benefits_val)).setText("$" + ANNUAL_BENEFITS);
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.annual_linear_layout)).setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.thick_bar_2)).setVisibility(View.GONE);
            }

            try {
                CO2_SEQUESTERED_$ = allInfo.split(",")[benefits_columns_enum.carbonSequestrationDollarsIndex()];
                ((TextView) findViewById(R.id.carbon_dioxide_sequestered_val)).setText("$" + CO2_SEQUESTERED_$);
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.carbon_dioxide_sequestered)).setVisibility(View.GONE);
            }

            try {
                CO2_SEQUESTERED_lbs = allInfo.split(",")[benefits_columns_enum.carbonSequestrationPoundsIndex()];
                ((TextView) findViewById(R.id.annual_co2_val)).setText(CO2_SEQUESTERED_lbs + " lbs");
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.annual_co2_layout)).setVisibility(View.GONE);
            }

            try {
                STORM_WATER_RUNOFF_AVOIDED = allInfo.split(",")[benefits_columns_enum.aintedRunoffDollarsIndex()];
                ((TextView) findViewById(R.id.storm_water_runoff_val)).setText("$" + STORM_WATER_RUNOFF_AVOIDED);
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.water_runnoff_avoided)).setVisibility(View.GONE);
            }

            try {
                RUNOFF_AVOIDED = allInfo.split(",")[benefits_columns_enum.aintedRunoffCubicFeetIndex()];
                ((TextView) findViewById(R.id.runoff_val)).setText(RUNOFF_AVOIDED + " gal");
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.runoff_layout)).setVisibility(View.GONE);
            }

            try {
                RAINFALL_INTERCEPTED = allInfo.split(",")[benefits_columns_enum.waterInterceptedCubicFeetIndex()];
                ((TextView) findViewById(R.id.rainfall_val)).setText(RAINFALL_INTERCEPTED + " gal");
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.rainfall_layout)).setVisibility(View.GONE);
            }

            try {
                AIR_POLLUTION_ANNUAL = allInfo.split(",")[benefits_columns_enum.pollutionRemovalDollarsIndex()];
                ((TextView) findViewById(R.id.air_pollution_val)).setText("$" + AIR_POLLUTION_ANNUAL);
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.air_pollution_removed)).setVisibility(View.GONE);
            }

            try {
                CARBON_MONOXIDE = allInfo.split(",")[benefits_columns_enum.coOuncesIndex()];
                ((TextView) findViewById(R.id.carbon_monoxide_val)).setText(CARBON_MONOXIDE + " oz");
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.co_layout)).setVisibility(View.GONE);
            }

            try {
                OZONE = allInfo.split(",")[benefits_columns_enum.o3OuncesIndex()];
                ((TextView) findViewById(R.id.ozone_val)).setText(OZONE + " oz");
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.ozone_layout)).setVisibility(View.GONE);
            }

            try {
                NITROGEN_DIOXIDE = allInfo.split(",")[benefits_columns_enum.no2OuncesIndex()];
                ((TextView) findViewById(R.id.nitrogen_dioxide_val)).setText(NITROGEN_DIOXIDE + " oz");
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.no2_layout)).setVisibility(View.GONE);
            }

            try {
                SULFUR_DIOXIDE = allInfo.split(",")[benefits_columns_enum.so2OuncesIndex()];
                ((TextView) findViewById(R.id.sulfur_dioxide_val)).setText(SULFUR_DIOXIDE + " oz");
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.so2_layout)).setVisibility(View.GONE);
            }

            try {
                PARTICULATE_MATTER = allInfo.split(",")[benefits_columns_enum.pm25OuncesIndex()];
                ((TextView) findViewById(R.id.particulate_matter_val)).setText(PARTICULATE_MATTER + " oz");
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.pm_layout)).setVisibility(View.GONE);
            }

            try {
                ENERGY_USAGE_ANNUAL = allInfo.split(",")[benefits_columns_enum.energySavingsDollarsIndex()];
                ((TextView) findViewById(R.id.energy_usage_val)).setText("$" + ENERGY_USAGE_ANNUAL);
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.energy_usage_annual)).setVisibility(View.GONE);
            }

            try {
                ELECTRICITY_SAVINGS = allInfo.split(",")[benefits_columns_enum.coolingKWHIndex()];
                ((TextView) findViewById(R.id.electricity_savings_val)).setText(ELECTRICITY_SAVINGS + " kWh");
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.electricity_layout)).setVisibility(View.GONE);
            }

            try {
                FUEL_SAVINGS = allInfo.split(",")[benefits_columns_enum.heatingMBTUIndex()];
                ((TextView) findViewById(R.id.fuel_savings_val)).setText(FUEL_SAVINGS + " MMBtu");
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.fuel_layout)).setVisibility(View.GONE);
            }

            try {
                AVOIDED_EMISSIONS = allInfo.split(",")[benefits_columns_enum.carbonAintedDollarsIndex()];
                ((TextView) findViewById(R.id.energy_emissions_val)).setText("$" + AVOIDED_EMISSIONS);
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.avoided_energy_emissions)).setVisibility(View.GONE);
            }

            try {
                CARBON_DIOXIDE_EMISSIONS = allInfo.split(",")[benefits_columns_enum.carbonAintedPoundsIndex()];
                ((TextView) findViewById(R.id.carbon_dioxide_val)).setText(CARBON_DIOXIDE_EMISSIONS + " lbs");
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.co2_emissions_layout)).setVisibility(View.GONE);
            }

            try {
                CARBON_MONOXIDE_EMISSIONS = allInfo.split(",")[benefits_columns_enum.thingsThatDoNotExist()];
                ((TextView) findViewById(R.id.carbon_monoxide_emissions_val)).setText(CARBON_MONOXIDE_EMISSIONS + " lbs");
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.co_emissions_layout)).setVisibility(View.GONE);
            }

            try {
                NITROGEN_DIOXIDE_EMISSIONS = allInfo.split(",")[benefits_columns_enum.thingsThatDoNotExist()];
                ((TextView) findViewById(R.id.nitrogen_dioxide_emissions_val)).setText(NITROGEN_DIOXIDE_EMISSIONS + " lbs");
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.no2_emissions_layout)).setVisibility(View.GONE);
            }

            try {
                SULFUR_DIOXIDE_EMISSIONS = allInfo.split(",")[benefits_columns_enum.thingsThatDoNotExist()];
                ((TextView) findViewById(R.id.sulfur_dioxide_emissions_val)).setText(SULFUR_DIOXIDE_EMISSIONS + " lbs");
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.so2_emissions_layout)).setVisibility(View.GONE);
            }
            try {
                PARTICULATE_MATTER_EMISSIONS = allInfo.split(",")[benefits_columns_enum.thingsThatDoNotExist()];
                ((TextView) findViewById(R.id.particulate_matter_emissions_val)).setText(PARTICULATE_MATTER_EMISSIONS + " lbs");
            } catch (Exception e) {
                ((LinearLayout) findViewById(R.id.pm_emissions_layout)).setVisibility(View.GONE);
            }

            try {
                CO2_TO_DATE = allInfo.split(",")[benefits_columns_enum.thingsThatDoNotExist()];
                ((TextView) findViewById(R.id.co2_total_val)).setText("$" + CO2_TO_DATE);
            } catch (Exception e) {
                ((ImageView) findViewById(R.id.thick_bar_3)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.co2_stored_to_date)).setVisibility(View.GONE);
            }
//        }else{
//            if(bestMatchTree != null) {
//                ((TextView) findViewById(R.id.common_name_cereal)).setText(bestMatchTree.getCommonName());
//                ((TextView) findViewById(R.id.dbh_meas_cereal)).setText(String.valueOf(bestMatchTree.getCurrentDBH()));
//            }

//            try {
//                COMMON_NAME = allInfo.split(",")[1];
//                ((TextView) findViewById(R.id.common_name_cereal)).setText(COMMON_NAME);
//            } catch (Exception e) {
//                try {
//                    ((TextView) findViewById(R.id.common_name_cereal)).setText(tree.getCommonName());
//                } catch (Exception i) {
//                    ((TextView) findViewById(R.id.common_name_cereal)).setText("Tree Type Unknown");
//                }
//            }
//
//                ((TextView) findViewById(R.id.scientific_name_cereal)).setVisibility(View.GONE);
//
//            try {
//                DBH = allInfo.split(",")[2];
//                ((TextView) findViewById(R.id.dbh_meas_cereal)).setText(DBH + "in.");
//            } catch (Exception e) {
//                ((LinearLayout) findViewById(R.id.dbh_linear_layout)).setVisibility(View.GONE);
//            }

//        }
    }

    public void patchTreeData(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Set<String> sources = prefs.getStringSet("databasesUsedSelector",new HashSet<String>());
        // sources.remove(MainActivity.banana.getDataSource());
        for (String source : sources) {
            Log.d("MainActivity", "Searching.  Trying: "+source);
            DataSource ds;
            if(source.equals("HopeCollegeDataSource")){
                ds = new HopeCollegeDataSource();
            }else if(source.equals("CityOfHollandDataSource")) {
                ds = new CityOfHollandDataSource();
            }else if(source.equals("ExtendedCoHDataSource")){
                ds = new ExtendedCoHDataSource();
            }else{
                ds = new ITreeDataSource();
            }
            ds.initialize(Cereal_Box_Activity.this,null);
            Tree tree = ds.search(MainActivity.banana.getLocation());

            if(tree != null && tree.isFound()){
                ds.patchData(tree);
            }
        }

    }

    public Tree estimateTreeNearest(Double maxDistance, float maxDBHDifference){
        Tree bestTree = new Tree();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Set<String> sources = prefs.getStringSet("databasesUsedSelector",new HashSet<String>());
        // sources.remove(MainActivity.banana.getDataSource());
        for (String source : sources) {
            DataSource ds;
            if(source.equals("HopeCollegeDataSource")){
                ds = new HopeCollegeDataSource();
            }else if(source.equals("CityOfHollandDataSource")) {
                ds = new CityOfHollandDataSource();
            }else if(source.equals("ExtendedCoHDataSource")){
                ds = new ExtendedCoHDataSource();
            }else{
                ds = new ITreeDataSource();
            }

        }
        return bestTree;
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
        if(tree.getCommonName() != null) {
            commonName = Transform.ChangeName(tree.getCommonName());
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
        if(tree.getDataSource().equals("ExtendedCoH")) {
            bd = bd.setScale(2, RoundingMode.HALF_UP);
        }else{
            bd = bd.setScale(0, RoundingMode.DOWN);
        }
        while ((line = reader.readLine()) != null) {
            String treeName = line.split(",")[1];
            if (treeName.equals(commonName)) {
                String dbh = line.split(",")[2];
                Double diameter = Double.parseDouble(dbh);
                BigDecimal bd1 = new BigDecimal(diameter);
                if(tree.getDataSource().equals("ExtendedCoH")) {
                    bd1 = bd1.setScale(2, RoundingMode.HALF_UP);
                }else{
                    bd1 = bd1.setScale(0, RoundingMode.DOWN);
                }
                if(bd1.doubleValue()==0){bd1 = BigDecimal.valueOf(1);}
                if (bd1.doubleValue() == bd.doubleValue()) {
                    allInfo = line;
                    hasValues = true;
                    break;
                }
            }
        }
//        if (allInfo.equals("")) {
//            allInfo = "N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A,N/A";
//        }
    }

//    public void display(Tree tree) {
//        try {
//            findInfo(tree);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
//        wm.getDefaultDisplay().getMetrics(displayMetrics);
//
//        cerealBoxWidth = displayMetrics.widthPixels+125;
//        right = cerealBoxWidth - 90;
//        cerealBoxHeight = displayMetrics.heightPixels+250;
//        Bitmap bitmap = Bitmap.createBitmap(cerealBoxWidth, cerealBoxHeight, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        final Paint paint = new Paint();
//
//        DisplayMetrics displaymetrics = this.getResources().getDisplayMetrics();
//        Paint textPaint = new Paint();
//        Paint rightPaint = new Paint();
//        rightPaint.setTextAlign(Paint.Align.RIGHT);
//        rightPaint.setColor(Color.BLACK);
//        rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//        rightPaint.setTextSize(dpToPx(subtitleTextSize, displaymetrics));
//
//        String str;
//        float y;
//
//        // Compute real width and height
//
//
//        // Draw on the canvas
//        paint.setColor(Color.BLACK);
//        //canvas.drawRect(boxMargin, boxMargin, cerealBoxWidth-2*boxMargin, cerealBoxHeight-2*boxMargin, paint);
//        canvas.drawRect(boxMargin, boxMargin, cerealBoxWidth-2*boxMargin, cerealBoxHeight-200, paint);
//        paint.setColor(Color.WHITE);
//        //canvas.drawRect(boxMargin+boxLineWidth, boxMargin+boxLineWidth, cerealBoxWidth-2*boxMargin-boxLineWidth, cerealBoxHeight-2*boxMargin-boxLineWidth, paint);
//        canvas.drawRect(boxMargin+boxLineWidth, boxMargin+boxLineWidth, cerealBoxWidth-2*boxMargin-boxLineWidth, cerealBoxHeight-200-boxLineWidth, paint);
//        // Title
//        textPaint.setColor(Color.BLACK);
//        textPaint.setTextSize(dpToPx(titleTextSize, displaymetrics));
//        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        canvas.drawText(commonName,
//                boxMargin + boxLineWidth + 10, boxMargin + boxLineWidth + dpToPx(titleTextSize, displaymetrics) + 10,
//                textPaint);
//        if(tree.getScientificName()==null||tree.getScientificName().equals("")){
//            y = boxMargin+boxLineWidth+dpToPx(titleTextSize, displaymetrics)+10;
//        }
//        else {
//            y = boxMargin+boxLineWidth+dpToPx(titleTextSize, displaymetrics)+90;
//            canvas.drawText("(" + tree.getScientificName() + ")",
//                    boxMargin + boxLineWidth + 10, y,
//                    textPaint);
//        }
//        testY = y;
//
//
//        // Subtitle
//        if (tree.getCurrentDBH() != null) {
//            str = "Serving size: " + ((int) (tree.getCurrentDBH() * 100) / 100.0) + "\" DBH";
//        }else{
//            str = ("Serving size: N/A"+ "\" DBH");
//        }
//        String condition;
//        if(tree.getInfo("Condition") != null) {
//            condition = (String) tree.getInfo("Condition");
//        }else{
//            condition = "N/A";
//        }
//        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//        textPaint.setTextSize(dpToPx(subtitleTextSize, displaymetrics));
//        y += dpToPx(subtitleTextSize, displaymetrics) + 15;
//        if (condition!=null) {
//            canvas.drawText(str+", "+condition+" condition", boxMargin + boxLineWidth + 10, y, textPaint);
//        }
//        else {
//            canvas.drawText(str, boxMargin + boxLineWidth + 10, y, textPaint);
//        }
//        // Thick line
//        y += 30;
//        paint.setColor(Color.BLACK);
//        paint.setStrokeWidth(20);
//        canvas.drawLine(boxMargin+boxLineWidth+10, y,
//                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
//                paint);
//        y += 15;
//
//        // Make some computations
//
//
//
//        if(!tree.getDataSource().equals("ExtendedCoH")) {
//
//
//            // And report the computations
//            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//            y += dpToPx(subtitleTextSize, displaymetrics) + 15;
//            canvas.drawText("Total benefits for this year:",
//                    boxMargin + boxLineWidth + 10, y,
//                    textPaint);
//            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            canvas.drawText("$" + allInfo.split(",")[TOTAL], right, y, rightPaint);
//            y += dpToPx(subtitleTextSize, displaymetrics) - 10;
//
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(5);
//            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
//                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
//                    paint);
//
//            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//            canvas.drawText("Carbon Dioxide Sequestered",
//                    boxMargin + boxLineWidth + 10, y,
//                    textPaint);
//            canvas.drawText("$" + allInfo.split(",")[CO2], right, y, rightPaint);
//
//            y += dpToPx(subtitleTextSize, displaymetrics) - 5;
//
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(5);
//            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
//                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
//                    paint);
//
//            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//            canvas.drawText("CO2 absorbed each year",
//                    boxMargin + boxLineWidth + 70, y,
//                    textPaint);
//            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            canvas.drawText(allInfo.split(",")[CARBON_LBS] + " " +
//                    "lbs", right, y, rightPaint);
//            y += dpToPx(subtitleTextSize, displaymetrics) - 5;
//
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(5);
//            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
//                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
//                    paint);
//
//            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//            canvas.drawText("Storm Water",
//                    boxMargin + boxLineWidth + 10, y,
//                    textPaint);
//            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            canvas.drawText("$" + allInfo.split(",")[STORM_WATER], right, y, rightPaint);
//
//            y += dpToPx(subtitleTextSize, displaymetrics) - 5;
//
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(5);
//            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
//                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
//                    paint);
//
//            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//            canvas.drawText("Rainfall intercepted each year",
//                    boxMargin + boxLineWidth + 70, y,
//                    textPaint);
//            String ft = allInfo.split(",")[WATER_GAL];
//            if (!ft.equals("N/A")) {
//                double gal = Double.parseDouble(ft) * 7.48052;
//                DecimalFormat df = new DecimalFormat("0.00");
//                ft = df.format(gal);
//            }
//            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            canvas.drawText(ft + " gal.", right, y, rightPaint);
//            y += dpToPx(subtitleTextSize, displaymetrics) - 5;
//
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(5);
//            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
//                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
//                    paint);
//
//            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//            canvas.drawText("Pollution",
//                    boxMargin + boxLineWidth + 10, y,
//                    textPaint);
//            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            canvas.drawText("$" + allInfo.split(",")[POLLUTION], right, y, rightPaint);
//
//            y += dpToPx(subtitleTextSize, displaymetrics) - 5;
//
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(5);
//            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
//                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
//                    paint);
//
//            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//            canvas.drawText("Air Pollution removed each year",
//                    boxMargin + boxLineWidth + 70, y,
//                    textPaint);
//            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            canvas.drawText(allInfo.split(",")[POLLUTION_OZ] + " oz", right, y, rightPaint);
//
//            y += dpToPx(subtitleTextSize, displaymetrics) - 5;
//
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(15);
//            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
//                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
//                    paint);
//        }else{
//            // And report the computations
//            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//            y += dpToPx(subtitleTextSize, displaymetrics) + 15;
//            canvas.drawText("Total benefits for this year:",
//                    boxMargin + boxLineWidth + 10, y,
//                    textPaint);
//            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            canvas.drawText("$" + allInfo.split(",")[28], right, y, rightPaint);
//            y += dpToPx(subtitleTextSize, displaymetrics) - 10;
//
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(5);
//            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
//                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
//                    paint);
//
//            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//            canvas.drawText("Carbon Dioxide Sequestered",
//                    boxMargin + boxLineWidth + 10, y,
//                    textPaint);
//            canvas.drawText("$" + allInfo.split(",")[20], right, y, rightPaint);
//
//            y += dpToPx(subtitleTextSize, displaymetrics) - 5;
//
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(5);
//            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
//                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
//                    paint);
//
//            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//            canvas.drawText("CO2 absorbed each year",
//                    boxMargin + boxLineWidth + 70, y,
//                    textPaint);
//            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            canvas.drawText(allInfo.split(",")[19] + " " +
//                    "lbs", right, y, rightPaint);
//            y += dpToPx(subtitleTextSize, displaymetrics) - 5;
//
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(5);
//            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
//                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
//                    paint);
//
//            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//            canvas.drawText("Storm Water",
//                    boxMargin + boxLineWidth + 10, y,
//                    textPaint);
//            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            canvas.drawText("$" + allInfo.split(",")[22], right, y, rightPaint);
//
//            y += dpToPx(subtitleTextSize, displaymetrics) - 5;
//
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(5);
//            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
//                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
//                    paint);
//
//            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//            canvas.drawText("Rainfall intercepted each year",
//                    boxMargin + boxLineWidth + 70, y,
//                    textPaint);
//            String ft = allInfo.split(",")[21];
//            if (!ft.equals("N/A")) {
//                double gal = Double.parseDouble(ft) * 7.48052;
//                DecimalFormat df = new DecimalFormat("0.00");
//                ft = df.format(gal);
//            }
//            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            canvas.drawText(ft + " gal.", right, y, rightPaint);
//            y += dpToPx(subtitleTextSize, displaymetrics) - 5;
//
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(5);
//            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
//                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
//                    paint);
//
//            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//            canvas.drawText("Pollution",
//                    boxMargin + boxLineWidth + 10, y,
//                    textPaint);
//            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            canvas.drawText("$" + allInfo.split(",")[26], right, y, rightPaint);
//
//            y += dpToPx(subtitleTextSize, displaymetrics) - 5;
//
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(5);
//            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
//                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
//                    paint);
//
//            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//            canvas.drawText("Air Pollution removed each year",
//                    boxMargin + boxLineWidth + 70, y,
//                    textPaint);
//            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            canvas.drawText(allInfo.split(",")[25] + " oz", right, y, rightPaint);
//
//            y += dpToPx(subtitleTextSize, displaymetrics) - 5;
//
////            paint.setColor(Color.BLACK);
////            paint.setStrokeWidth(15);
////            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
////                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
////                    paint);
//
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(5);
//            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
//                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
//                    paint);
//
//
//            //Energy
//            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//            canvas.drawText("Energy",
//                    boxMargin + boxLineWidth + 10, y,
//                    textPaint);
//            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            canvas.drawText("$" + allInfo.split(",")[59], right, y, rightPaint);
//
//            y += dpToPx(subtitleTextSize, displaymetrics) - 5;
//
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(5);
//            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
//                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
//                    paint);
//            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//            canvas.drawText("Heating costs saved this year",
//                    boxMargin + boxLineWidth + 70, y,
//                    textPaint);
//            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            canvas.drawText(allInfo.split(",")[55] + " Kwh.", right, y, rightPaint);
//
//            y += dpToPx(subtitleTextSize, displaymetrics) - 5;
//
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(5);
//            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
//                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
//                    paint);
//            textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//            canvas.drawText("Cooling costs saved this year",
//                    boxMargin + boxLineWidth + 70, y,
//                    textPaint);
//            rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            canvas.drawText(allInfo.split(",")[57] + " Kwh.", right, y, rightPaint);
//            y += dpToPx(subtitleTextSize, displaymetrics) - 5;
//
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(15);
//            canvas.drawLine(boxMargin + boxLineWidth + 10, y,
//                    cerealBoxWidth - 2 * boxMargin - boxLineWidth - 20, y,
//                    paint);
//        }
//
//        /*textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//        canvas.drawText("Energy Usage each year",
//                boxMargin+boxLineWidth+10, y,
//                textPaint);
//        rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//        canvas.drawText("$" + allInfo.split(",")[14], right, y, rightPaint);
//
//        y += dpToPx(subtitleTextSize, displaymetrics)-5;
//
//        paint.setColor(Color.BLACK);
//        paint.setStrokeWidth(5);
//        canvas.drawLine(boxMargin+boxLineWidth+10, y,
//                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
//                paint);
//
//        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//        canvas.drawText("Electricity savings (A/C)",
//                boxMargin+boxLineWidth+70, y,
//                textPaint);
//        rightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//        canvas.drawText( "21.95"+ " kWh", right, y, rightPaint);
//       // canvas.drawText(allInfo.split(",")[6]+"lbs", boxMargin+boxLineWidth+760, y, textPaint);
//
//        y += dpToPx(subtitleTextSize, displaymetrics)-5;
//
//        paint.setColor(Color.BLACK);
//        paint.setStrokeWidth(5);
//        canvas.drawLine(boxMargin+boxLineWidth+10, y,
//                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
//                paint);
//
//        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//        canvas.drawText("Fuel savings (NG, Oil)",
//                boxMargin+boxLineWidth+70, y,
//                textPaint);
//
//        canvas.drawText( "-2.74"+ " therms", right, y, rightPaint);
//        // canvas.drawText(allInfo.split(",")[6]+"lbs", boxMargin+boxLineWidth+760, y, textPaint);
//
//        y += dpToPx(subtitleTextSize, displaymetrics)-5;
//
//        paint.setColor(Color.BLACK);
//        paint.setStrokeWidth(5);
//        canvas.drawLine(boxMargin+boxLineWidth+10, y,
//                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
//                paint);
//
//        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//        canvas.drawText("Avoided Emissions",
//                boxMargin+boxLineWidth+10, y,
//                textPaint);
//        y += dpToPx(subtitleTextSize, displaymetrics)-5;
//
//        paint.setColor(Color.BLACK);
//        paint.setStrokeWidth(5);
//        canvas.drawLine(boxMargin+boxLineWidth+10, y,
//                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
//                paint);
//
//        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//        canvas.drawText("Carbon dioxide",
//                boxMargin+boxLineWidth+70, y,
//                textPaint);
//        canvas.drawText( "-17.36"+ " lbs", right, y, rightPaint);
//
//        y += dpToPx(subtitleTextSize, displaymetrics)-5;
//
//        paint.setColor(Color.BLACK);
//        paint.setStrokeWidth(5);
//        canvas.drawLine(boxMargin+boxLineWidth+10, y,
//                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
//                paint);
//
//
//        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//        canvas.drawText("Nitrogen dioxide",
//                boxMargin+boxLineWidth+70, y,
//                textPaint);
//        canvas.drawText( "-0.06"+ " oz", right, y, rightPaint);
//
//        y += dpToPx(subtitleTextSize, displaymetrics)-5;
//
//        paint.setColor(Color.BLACK);
//        paint.setStrokeWidth(5);
//        canvas.drawLine(boxMargin+boxLineWidth+10, y,
//                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
//                paint);
//
//
//        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//        canvas.drawText("Sulfur dioxide",
//                boxMargin+boxLineWidth+70, y,
//                textPaint);
//        canvas.drawText( "-1.02"+ " oz", right, y, rightPaint);
//
//        y += dpToPx(subtitleTextSize, displaymetrics)-5;
//
//        paint.setColor(Color.BLACK);
//        paint.setStrokeWidth(5);
//        canvas.drawLine(boxMargin+boxLineWidth+10, y,
//                cerealBoxWidth-2*boxMargin-boxLineWidth-20, y,
//                paint);
//
//
//        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//        y += dpToPx(subtitleTextSize, displaymetrics) + 20;
//        canvas.drawText("Large particulate matter",
//                boxMargin+boxLineWidth+70, y,
//                textPaint);
//        canvas.drawText( "<0.1"+ " oz", right, y, rightPaint);*/
//
//        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View customView = inflater.inflate(R.layout.activity_cereal_box, null);
//
//
//        ImageButton button = (ImageButton) customView.findViewById(R.id.add_notes);
//        button.setOnClickListener(new AddNotesEvent());
////        TextView okay = (TextView) customView.findViewById(R.id.okay_cereal);
////        okay.bringToFront();
////        okay.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                popupWindow.dismiss();
////                popupWindow = null;
////            }
////        });
//
//
//
//        ImageView imageView = (ImageView) customView.findViewById(R.id.cereal_image);
//        imageView.setImageBitmap(bitmap);
//        noData = (TextView) customView.findViewById(R.id.no_ben);
//        noData.setVisibility(View.GONE);
//        if(!hasValues){
//            imageView.setVisibility(View.GONE);
//            noData.setVisibility(View.VISIBLE);
//            noData.setText("There is no data to display on this "+ commonName);
//        }
//
//
//        setContentView(customView);
//        gestureObject = new GestureDetectorCompat(customView.getContext(), new LearnGesture());
////        if (popupWindow != null) {
////            popupWindow.dismiss();
////        }
////        popupWindow = new PopupWindow(
////                customView,
////                ViewGroup.LayoutParams.MATCH_PARENT,
////                ViewGroup.LayoutParams.MATCH_PARENT
////        );
////
////        if (Build.VERSION.SDK_INT >= 21) {
////            popupWindow.setElevation(5.0f);
////        }
//////        View rootView = ((Activity)getParent()).getWindow().getDecorView().findViewById(R.id.drawer_layout);
//////        View rootView = findViewById(R.layout.activity_cereal_box)
//////        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 400);
////        final View rootView = this.getWindow().getDecorView().findViewById(R.id.drawer_layout);
////        gestureObject = new GestureDetectorCompat(rootView.getContext(), new LearnGesture());
////        rootView.post(new Runnable() {
////            @Override
////            public void run() {
////                popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 400);
////
////            }
////        });
//
//    }


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
//        this.gestureObject.onTouchEvent(event);
//        this.activitySwipeDetector.onTouch(findViewById(android.R.id.content), event);
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

//    class ActivitySwipeDetector implements View.OnTouchListener {
//
//        private AppCompatActivity activity;
//        static final int MIN_DISTANCE = 100;
//        private float downX, downY, upX, upY;
//
//        public ActivitySwipeDetector(final AppCompatActivity activity) {
//            this.activity = activity;
//        }
//
//        public final void onRightToLeftSwipe() {
//            Intent intent2 = new Intent(Cereal_Box_Activity.this, Pie_Chart_Activity.class);
//            finish();
//            startActivity(intent2);
//        }
//
//        public void onLeftToRightSwipe(){
//            Intent intent1 = new Intent(Cereal_Box_Activity.this, Tree_Info_First.class);
//            finish();
//            startActivity(intent1);
//        }
//
//        public void onTopToBottomSwipe(){
//
//        }
//
//        public void onBottomToTopSwipe(){
//
//        }
//
//        public boolean onTouch(View v, MotionEvent event) {
//
//            v.getParent().requestDisallowInterceptTouchEvent(false);
//
//
//            switch(event.getAction()){
//                case MotionEvent.ACTION_MOVE:
//                    if (Math.abs(downX - upX) > Math.abs(downY - upY))
//                        v.getParent().requestDisallowInterceptTouchEvent(true);
//                    break;
//
//                case MotionEvent.ACTION_DOWN: {
//                    downX = event.getX();
//                    downY = event.getY();
//                    //   return true;
//                }
//                case MotionEvent.ACTION_UP: {
//                    upX = event.getX();
//                    upY = event.getY();
//
//                    float deltaX = downX - upX;
//                    float deltaY = downY - upY;
//
//                    // swipe horizontal?
//                    if(Math.abs(deltaX) > MIN_DISTANCE){
//                        // left or right
//                        if(deltaX < 0) {
////                            v.getParent().requestDisallowInterceptTouchEvent(true);
//                            this.onLeftToRightSwipe();
//                            return true;
//                        }
//                        if(deltaX > 0) {
////                            v.getParent().requestDisallowInterceptTouchEvent(true);
//                            this.onRightToLeftSwipe();
//                            return true;
//                        }
//                    }
//
//                    // swipe vertical?
//                    if(Math.abs(deltaY) < MIN_DISTANCE){
//                        // top or down
//                        if(deltaY < 0) { this.onTopToBottomSwipe(); return true; }
//                        if(deltaY > 0) { this.onBottomToTopSwipe(); return true; }
//                    }
//                }
//            }
//            return false;
//        }
//    }






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

    public class addImageEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){
            picAppear = findViewById(R.id.user_add_tree_pic_appear_cereal);
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
            ImageView image = (ImageView) findViewById(R.id.user_add_tree_pic_appear_cereal);
            //image.setImageBitmap(bmp);
            MainActivity.banana.addPics("User pic", Base64.encodeToString(byteArray, Base64.DEFAULT));
            myDialog.dismiss();
            Toast.makeText(getBaseContext(), "Photo added. ", Toast.LENGTH_LONG).show();
        }
    }


}