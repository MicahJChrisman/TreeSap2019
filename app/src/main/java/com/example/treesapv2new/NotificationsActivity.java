package com.example.treesapv2new;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.notifications_activity);
        ScrollView scrollView = ((ScrollView) findViewById(R.id.notificaitons_scroller));
        LinearLayout linearLayout = ((LinearLayout) findViewById(R.id.notifications_linear_layout));

        LinearLayout textElement = new LinearLayout(this);
        textElement.setOrientation(LinearLayout.VERTICAL);

        TextView acceptedText = new TextView(this);
        acceptedText.setText("orange");
        textElement.addView(acceptedText);

        TextView treeMessage = new TextView(this);
        treeMessage.setText("bananan");
        textElement.addView(treeMessage);

        LinearLayout notificationElement = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        notificationElement.setOrientation(LinearLayout.HORIZONTAL);


        notificationElement.addView(textElement);

        ImageView nextArrow = new ImageView(this);
        nextArrow.setImageResource(R.drawable.next_icon);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        nextArrow.setLayoutParams(layoutParams);


        View spacer = new View(this);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        spacer.setLayoutParams(layoutParams1);

        notificationElement.addView(spacer);
        notificationElement.addView(nextArrow);


        linearLayout.addView(notificationElement, lp);



    }
}
