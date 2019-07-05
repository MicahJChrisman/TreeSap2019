package com.example.treesapv2new.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.treesapv2new.CuratorActivity;
import com.example.treesapv2new.R;

public class CuratorMessage extends AppCompatActivity {
    Button cancelButton;
    Button saveButton;
    EditText messageBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.curator_message);

        cancelButton = findViewById(R.id.cancel_button);
        saveButton = findViewById(R.id.submit_button);
        messageBox = findViewById(R.id.message_box);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CuratorMessage.super.onBackPressed();
            }
        });

        String title = "";
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder a_builder = new AlertDialog.Builder(CuratorMessage.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                a_builder.setCancelable(true)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                if(((boolean) getIntent().getExtras().get("accepted")) == true ){
                    a_builder.setMessage("This tree will be added to the online database for everyone to see.")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //TODO accept tree
                        }
                    });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Accept tree?");
                    alert.show();
                }else{
                    a_builder.setMessage("This tree will be permanently removed from the database.")
                            .setPositiveButton("Reject", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    //TODO rejectTree
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("Reject tree?");
                    alert.show();
                }
            }
        });
    }

}
