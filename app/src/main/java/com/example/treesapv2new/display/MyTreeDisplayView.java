package com.example.treesapv2new.display;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.treesapv2new.R;

public class MyTreeDisplayView extends AppCompatActivity {

    Context parent;

    String mprovider;
    Double longitude, latitude;
    LocationManager locationManager;

    WebView mytreeview;

    final long LOCATION_REFRESH_TIME = 1;     // 1 minute
    final long LOCATION_REFRESH_DISTANCE = 1; // 10 meters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_tree_display_view);

        this.parent = this;

        mytreeview = (WebView) findViewById(R.id.myTreeWebView);
        mytreeview.setWebViewClient(new HelloWebViewClient());
        //mytreeview.addJavascriptInterface(new MyJavaScriptInterface(), "HtmlViewer");


        mytreeview.getSettings().setJavaScriptEnabled(true);
        mytreeview.getSettings().setLoadsImagesAutomatically(true);
        mytreeview.getSettings().setDomStorageEnabled(true);
        //mytreeview.getSettings().setDatabaseEnabled(true);
        //mytreeview.getSettings().setPluginsEnabled(true);

        mytreeview.setWebChromeClient(new MyTreeChromeCLient());

        mytreeview.loadUrl("http://mytree.itreetools.org");



    }

    public class HelloWebViewClient extends WebViewClient {

        int timesFinished = 0;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Log.d("MyTreeDisplay", "Javascript loading. URL = "+url);
            view.loadUrl(url);

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // This call inject JavaScript into the page which just finished loading.
            //wvbrowser.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            Log.d("MyTreeDisplay", "Page Finished. URL = "+url);

            //timesFinished++;
            //if (timesFinished >= 1) {
//                try {Thread.sleep(2000);} catch (Exception e) {}
//                view.loadUrl("javascript: " +
//                                //  "window.HtmlViewer.showHTML("+
//                                "(function() {" +
//                                "loadpage('dashboard')" +
//                                "}) ();"
//                        //+");"
//                );
                mytreeview.loadUrl("javascript: "+
                                //  "window.HtmlViewer.showHTML("+
                                //"(function() {"+
                        "proceed();"
                                //"document.getElementById('address').value=\"19 East 34th Street\";"
                                //+ "}) ();"
                        //+");"
                );

            //}


        }
    }

    public class MyTreeChromeCLient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView wv, int newProgress) {
            Log.d("MyTreeChromeCLient", "Page progress = "+newProgress);
//            if (newProgress == 100)
//            wv.evaluateJavascript("javascript: "+
//                            //  "window.HtmlViewer.showHTML("+
//                            "(function() {"+
//                            "document.getElementById('address').value=\"19 East 34th Street\"; proceed();}) ();"
//                    //+");"
//                    , null );

        }
    }

}
