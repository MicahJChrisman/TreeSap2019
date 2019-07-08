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
    Button sendButton;
    EditText messageBox;
    boolean wasAccepted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        wasAccepted = (boolean) getIntent().getExtras().get("accepted");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.curator_message);

        cancelButton = findViewById(R.id.cancel_button);
        sendButton = findViewById(R.id.send_button);
        messageBox = findViewById(R.id.message_box);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CuratorMessage.super.onBackPressed();
            }
        });

        String title = "";
        sendButton.setOnClickListener(new View.OnClickListener() {
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
                if(wasAccepted){
                    a_builder.setMessage("This tree will be added to the online database for everyone to see.")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent();
                            intent.putExtra("message", messageBox.getText().toString());
                            intent.putExtra("accepted", wasAccepted);
                            setResult(RESULT_OK, intent);
                            onBackPressed();
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
                                    Intent intent = new Intent();
                                    intent.putExtra("message", messageBox.getText().toString());
                                    intent.putExtra("accepted", wasAccepted);
                                    setResult(RESULT_OK, intent);
                                    onBackPressed();
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

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(CuratorMessage.this, CuratorActivity.class);
//        intent.putExtra("accepted", wasAccepted);
//        setResult(RESULT_CANCELED, null);
//        super.onBackPressed();
//    }
}
