package com.example.treesapv2new;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.treesapv2new.model.Tree;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class Curator_Swipe_Activity extends AppCompatActivity {
    private NewArrayAdapter arrayAdapter;
    private int i;


    ListView listView;
    List<Tree> rowItems;
    List<Tree> penTrees;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntent().getExtras();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.curator_swipe);

        penTrees = new ArrayList<Tree>();
//                List<Tree> rowItems;
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        //CollectionReference penTrees = db.collection("pendingTrees");
        db.collection("pendingTrees").orderBy("Date and time", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Tree tree = new Tree();
                        String commonName = (String) document.get("Common name");
                        if(commonName == null || commonName.equals("")){
                            tree.setCommonName("N/A");
                        }else {
                            tree.setCommonName(commonName);
                        }
                        String scientificName = (String) document.get("Scientific name");
                        if(scientificName == null || scientificName.equals("")){
                            tree.setCommonName("N/A");
                        }else {
                            tree.setScientificName(scientificName);
                        }
                        String dbh = (String) document.get("DBH");
                        if(dbh == null || dbh.equals("")){
                            tree.setCurrentDBH(0.0);
                        }else{
                            tree.setCurrentDBH(Double.parseDouble(dbh));
                        }
                        String[] otherInfo = (String[]) document.get("Other info");
                        if(otherInfo != null){
                            int i = (otherInfo.length-1);
                            String notes = "";
                            while(i>=0) {
//                                String notes = (String) tree.getInfo("Notes");
                                notes = notes + "/n" + otherInfo[i];
                            }
                            if(!notes.equals("")){
                                tree.addInfo("Notes", notes);
                            }
                        }
                        penTrees.add(tree);
                    }
                } else {
                    Toast toast = Toast.makeText(Curator_Swipe_Activity.this, "Unable to load trees.", Toast.LENGTH_LONG);
                }
            }
        });

//        Tree tree = new Tree();
//        tree.setCommonName("Test tree");
//        tree.setScientificName("Testus treeus");
//        tree.setCurrentDBH(50.0);
//        penTrees.add(new Tree());
        arrayAdapter = new NewArrayAdapter(this, R.layout.item, penTrees );

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                penTrees.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(Curator_Swipe_Activity.this, "Left!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(Curator_Swipe_Activity.this, "Right!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                //rowItems.add("XML ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
               //view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                //view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(Curator_Swipe_Activity.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
