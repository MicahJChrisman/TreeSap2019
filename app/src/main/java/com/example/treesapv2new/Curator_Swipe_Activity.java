package com.example.treesapv2new;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;




/**
 * https://www.youtube.com/watch?v=Q2FPDI99-as
 */



public class Curator_Swipe_Activity extends AppCompatActivity {
    private NewArrayAdapter arrayAdapter;
    private int i;


    ListView listView;
    List<Tree> rowItems;
    List<Tree> penTrees;
    FirebaseFirestore db;
    SwipeFlingAdapterView flingContainer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntent().getExtras();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.curator_swipe);

        penTrees = new ArrayList<Tree>();
//                List<Tree> rowItems;
        //FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        CollectionReference treesRef = db.collection("pendingTrees");


//        Tree tree = new Tree();
//        tree.setCommonName("Test tree");
//        tree.setScientificName("Testus treeus");
//        tree.setCurrentDBH(50.0);
//        penTrees.add(tree);
        //Object[] objects = {db};
        //new DownloadFilesTask();
        new DownloadFilesTask().execute();
        try {
            Thread.sleep(1600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

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

//            @Override
//            public void onCardExited(){
//
//            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(Curator_Swipe_Activity.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

       //arrayAdapter = new NewArrayAdapter(this, R.layout.item, penTrees);

    }

    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
        @Override
        protected Long doInBackground(URL... urls) {
//            FirebaseFirestore db = (FirebaseFirestore) objects[0];
            db.collection("pendingTrees").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Tree tree = new Tree();
                            String commonName = (String) document.get("commonName");
                            if(commonName == null || commonName.equals("")){
                                tree.setCommonName("N/A");
                            }else {
                                tree.setCommonName(commonName);
                            }
                            String scientificName = (String) document.get("scientificName");
                            if(scientificName == null || scientificName.equals("")){
                                tree.setScientificName("N/A");
                            }else {
                                tree.setScientificName(scientificName);
                            }
                            ArrayList<Number> dbhList = ((ArrayList<Number>) document.get("dbhArray"));
//                            Number dbh = ((ArrayList<Number>) document.get("dbhArray")).get(0);
                            if(dbhList == null || dbhList.isEmpty()){
                                tree.setCurrentDBH(0.0);
                            }else{
//                                tree.setCurrentDBHetCurrentDBH(new Double(dbh));
//                                tree.setCurrentDBH(dbh);
                                tree.setCurrentDBH(dbhList.get(0).doubleValue());
                            }
//     TODO                       String[] otherInfo = document.get("otherInfo").get("notes");
//                            if(otherInfo != null){
//                                int i = (otherInfo.length-1);
//                                String notes = "";
//                                while(i>=0) {
////    TODO                            String notes = (String) tree.getInfo("Notes");
//                                    notes = notes + "/n" + otherInfo[i];
//                                }
//                                if(!notes.equals("")){
//                                    tree.addInfo("Notes", notes);
//                                }
//                            }else{
//                                otherInfo = new String[1];
//                                otherInfo[0] = "";
//                            }

                            //TODO Put pics in trees

                            ArrayList<String> stringPics = (ArrayList<String>) document.get("images");
                            //String[] pics = stringPics.split("\n?\t.*: ");
                            int i = 0;
                            if(stringPics!=null) {
                                while(i<stringPics.size()){
                                    String key;
                                    if(i == 0){
                                        key = "Image Bark";
                                    }else if(i == 1){
                                        key = "Image Leaf";
                                    }else{
                                        key = "Image Tree";
                                    }
                                    String pic = stringPics.get(i);
                                    if(pic != null) {
                                        tree.addPics(key, stringPics.get(i));
                                    }
                                    i++;
                                }
                            }
                            penTrees.add(tree);
                        }
                    } else {
                        Toast toast = Toast.makeText(Curator_Swipe_Activity.this, "Unable to load trees.", Toast.LENGTH_LONG);
                    }
                }
            });
            return new Long(1);
        }

        protected void onPostExecute(Long result) {
            arrayAdapter = new NewArrayAdapter(Curator_Swipe_Activity.this, R.layout.item, penTrees, getSupportFragmentManager());
            flingContainer.setAdapter(arrayAdapter);
        }
    }
}
