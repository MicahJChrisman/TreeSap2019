package com.example.treesapv2new.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import com.example.treesapv2new.MainActivity;
import com.example.treesapv2new.R;
import com.example.treesapv2new.control.PrefManager;
import com.example.treesapv2new.model.BulletedListItem;

public class BulletedWebView extends WebView {

    private Context parent;
    private List<BulletedListItem> stringList = new ArrayList<BulletedListItem>();
    private int background;

    public BulletedWebView(Context context, ArrayList<BulletedListItem> list) {
        super(context);
        parent = context;
        stringList = list;

        if(PrefManager.getBoolean("scheme", false)) {
            background = R.color.altColorPrimary;
        } else {
            background = R.color.colorPrimary;
        }

        clearCache(true);

        getSettings().setLoadsImagesAutomatically(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        getSettings().setAppCacheEnabled(false);

        setWebViewClient(new BulletedWebViewClient());
    }

   public String genData (String html, String mimeType, String encoding) {

        int color = ResourcesCompat.getColor(getResources(), background, null);
        String hex = Integer.toHexString(color).substring(2);
        String viewHTML = "<html><style>body {background-color: #"+hex+";} li {font-size: 18px;}</style><body>" + html;

        if (stringList.size() > 0) {
            viewHTML += "<ul>";
            for (BulletedListItem bli : stringList) {
                viewHTML += "<li>";
                int textColor = bli.getNameColor();
                textColor = textColor & 0xffffff;
                String textColorString = Integer.toHexString(textColor);
                if (textColorString.length() < 6) {
                    textColorString = "000000".substring(0, 6 - textColorString.length()) + textColorString;
                }
                viewHTML += "<font color=\"#" + textColorString +"\">";
                if (bli.getLink() == null) {
                    viewHTML += bli.getName();
                } else {
                    viewHTML += "<a ";
                    viewHTML += "href=\""+bli.getLink()+"\"";
                    if (bli.getNameColor() != Color.BLACK)
                        viewHTML += " style=\"color:#" + textColorString +"\" ";
                    viewHTML += ">" + bli.getName() + "</a>";
                }
                viewHTML += "</font></li>";
            }
            viewHTML += "</ul>";
            viewHTML += "</body></html>";
        }
        //loadUrl("about:blank");
        //loadDataWithBaseURL("same://ur/l/tat/does/not/work", viewHTML, "text/html", "utf-8", null);
        //loadData(viewHTML, mimeType, encoding);
        //super.loadData(viewHTML, mimeType, encoding);
        Log.d("BulletedWebView", "Reloading data; html = "+viewHTML);
        return viewHTML;

   }

   private class BulletedWebViewClient extends WebViewClient {
       @Override
       public boolean shouldOverrideUrlLoading(WebView view, String url){

           if (url.startsWith("display:")) {

               String msg = url.substring(url.indexOf(':') + 1);

               AlertDialog.Builder builder;
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                   builder = new AlertDialog.Builder(parent, android.R.style.Theme_Material_Dialog_Alert);
               } else {
                   builder = new AlertDialog.Builder(parent);
               }
               builder.setTitle("Identifier Description")
                       .setMessage(msg)
                       .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int which) {
                           }
                       }).setIcon(android.R.drawable.ic_dialog_info)
                       .show();
           } else if (url.startsWith("iddisplay:")) {

                   String msg = url.substring(url.indexOf(':')+1);

                   AlertDialog.Builder builder;
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                       builder = new AlertDialog.Builder(parent, android.R.style.Theme_Material_Dialog_Alert);
                   } else {
                       builder = new AlertDialog.Builder(parent);
                   }
                   builder.setTitle("Identifier Description")
                           .setMessage(msg)
                           .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   ((MainActivity)parent).removeSelectedIDMethod();
                               }
                           }).setPositiveButton("Close", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int which) {
                       }
                   }).setIcon(android.R.drawable.ic_dialog_info)
                           .show();

           } else if (url.startsWith("change:")) {
               String subject = url.substring(url.indexOf(':')+1);
               Dialog dialog = new Dialog(parent);


               if (subject.equals("id")) {
                   dialog.setContentView(R.layout.new_tree_id_dialog);
                   dialog.setTitle("Choose Tree Identifier");

                   Spinner choicesSpinner = (Spinner) dialog.findViewById(R.id.new_tree_id_list);
                   ArrayAdapter<String> cAdapter = new ArrayAdapter<String>(parent, R.layout.add_tree_id_choices_spinner);
                   cAdapter.setDropDownViewResource(R.layout.add_tree_id_choices_spinner);
                   if (stringList.size() > 0) {
                       for (BulletedListItem bli : stringList) {
                            cAdapter.add(bli.getName());
                       }
                   }
                   choicesSpinner.setAdapter(cAdapter);
               }

               Button button;
               button = (Button) dialog.findViewById(R.id.new_tree_id_choose_button);
               //button.setOnClickListener(new createNewTaskAction(dialog));
               button = (Button) dialog.findViewById(R.id.new_tree_id_cancel_button);
               button.setOnClickListener(new cancelDialogAction(dialog));

               dialog.show();
           } else if (url.startsWith("http:") || url.startsWith("https:")) {
               String subject = url.substring(url.indexOf(':') + 1);

               Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
               parent.startActivity(browserIntent);
           }

           return true;
       }

   }

    public class cancelDialogAction implements OnClickListener {
        Dialog parentDialog;
        public cancelDialogAction(Dialog dialog) {
            parentDialog = dialog;
        }
        public void onClick(View v) {
            parentDialog.dismiss();
        }
    }

//    protected void onDraw(Canvas canvas) {
//        this.genData("", "text/html", null);
//        Log.d("BulletedWebView", "REDRAWING: list is "+stringList.size()+" long.");
//        invalidate();
//        super.onDraw(canvas);
//    }

    public void setItems(List<BulletedListItem> blis) {
        stringList = blis;
        invalidate();
        loadData(this.genData("", "text/html", null), "text/html", null);
        Log.d("BulletedWebView", "RESETTING LIST ITEMS: list is "+stringList.size()+" long.");
    }
}
