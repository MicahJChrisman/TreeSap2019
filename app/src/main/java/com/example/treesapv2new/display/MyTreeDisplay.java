package com.example.treesapv2new.display;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.treesapv2new.model.Tree;
import com.example.treesapv2new.MainActivity;

public class MyTreeDisplay extends DisplayMethod {

    public String getMethodName() {
        return "iTree MyTree info display";
    }

    public String getDescription() {
        return "Resource usage tree info display method from iTree.";
    }

    public void display(Tree tree) {
        if (parent == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putDouble("longitude", 0.0);
        bundle.putDouble("latitude", 0.0);
        Intent startDeviceIntent = new Intent(parent, MyTreeDisplayView.class);
        startDeviceIntent.putExtras(bundle);
        ((MainActivity)parent).startActivityForResult(startDeviceIntent, 1000);


    }

    public class HelloWebViewClient extends WebViewClient {

        int timesFinished = 0;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            // This call inject JavaScript into the page which just finished loading.
            //wvbrowser.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            Log.d("MyTreeDisplay", "Page Finished.");

            timesFinished++;
            if (timesFinished >= 1) {

                new AlertDialog.Builder(parent)
                        .setTitle("Tree Display")
                        .setMessage("Simple Look at Tree Information")
                        .setView(view)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogint, int whichButton) {
                                    }
                                }
                        ).show();
            }
            view.loadUrl("javascript: "+
                          //  "window.HtmlViewer.showHTML("+
                         "(function() {"+
                               "document.getElementById(\"address\").value=\"19 East 34th Street\"; progress();}) ();"
                    //+");"
            );




        }

    }

    class MyJavaScriptInterface {

        @JavascriptInterface
        public void showHTML(String html) {
            new AlertDialog.Builder(parent).setTitle("HTML").setMessage(html)
                    .setPositiveButton(android.R.string.ok, null).setCancelable(false).create().show();
        }

    }

}
