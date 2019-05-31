package com.example.treesapv2new;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

public class Tree_Other_Info_Activity extends AppCompatActivity {

    private static final String[] treeOptions = new String[]{
        "Oak","Maple","Mulberry"
    };


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.add_tree_other_info);

        Button sumbitButton = (Button) findViewById(R.id.add_tree_submit);
        sumbitButton.setOnClickListener(new SubmitEvent());

        TextView txtclose = (TextView) findViewById(R.id.other_info_close);
        txtclose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(Tree_Other_Info_Activity.this);
                builder.setCancelable(true);
                builder.setTitle("Discard your tree?");
                builder.setMessage("This will get rid of the data you entered.");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Discard Tree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intentA = new Intent(Tree_Other_Info_Activity.this, MainActivity.class);
                        startActivity(intentA);
                    }
                });
                builder.show();
            }
        });

        String[] treeTypes = getResources().getStringArray(R.array.known_trees);

        AutoCompleteTextView editTrees = findViewById(R.id.common_name);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,treeTypes);
        editTrees.setAdapter(adapter);
    }

    private class SubmitEvent implements View.OnClickListener{
        @Override
        public void onClick(View v){

        }
    }
}
