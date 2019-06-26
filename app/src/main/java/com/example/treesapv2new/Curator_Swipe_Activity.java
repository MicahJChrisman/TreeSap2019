package com.example.treesapv2new;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.treesapv2new.model.Tree;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.lorentzos.flingswipe.FlingCardListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
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
    CollectionReference treesRef;
    CollectionReference apprRef;
    SwipeFlingAdapterView flingContainer;
    Tree currentTree;
    Tree previousTree;
    Stack<DocSnap> previousTrees;
    FloatingActionButton undoButton;
    FloatingActionButton skipButton;
    FloatingActionButton rejectButton;
    FloatingActionButton acceptButton;
    FloatingActionButton mapButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntent().getExtras();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.curator_swipe);

        penTrees = new ArrayList<Tree>();
//                List<Tree> rowItems;
        //FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        treesRef = db.collection("pendingTrees");
        apprRef = db.collection("approvedTrees");

        previousTrees = new Stack<>();
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
                Toast.makeText(Curator_Swipe_Activity.this, "Rejected!", Toast.LENGTH_SHORT).show();
                rejectTree();
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                setCurrentTree();
                //removeFirstObjectInAdapter();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                acceptTree();
//                setCurrentTree();
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
        rejectButton = findViewById(R.id.reject_button);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectTree();
                Toast.makeText(Curator_Swipe_Activity.this, "Rejected!", Toast.LENGTH_SHORT).show();
//                FlingCardListener listener = flingContainer.getTopCardListener();
//                //flingContainer.dispatchNestedFling(400,400, false);
//                // Obtain MotionEvent object
//                long downTime = SystemClock.uptimeMillis();
//                long eventTime = SystemClock.uptimeMillis() + 100;
//                float x = 0.0f;
//                float y = -50.0f;
//// List of meta states found here:     developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
//                int metaState = 0;
//                MotionEvent motionEvent = MotionEvent.obtain(
//                        downTime,
//                        eventTime,
//                        MotionEvent.ACTION_MOVE,
//                        x,
//                        y,
//                        metaState
//                );

// Dispatch touch event to view
               //flingContainer.getSelectedView().dispatchTouchEvent(motionEvent);

                //flingContainer.getSelectedView().getRootView()..getDisplay();
                penTrees.remove(0);
                arrayAdapter.notifyDataSetChanged();
                if(penTrees.size()>0) {
                    arrayAdapter.getView(0, flingContainer.getSelectedView(), null);
                    setCurrentTree();
                }
//                penTrees.remove(0);
//                arrayAdapter.notifyDataSetChanged();
                //listener.onTouch(flingContainer.getSelectedView(), MotionEvent.obtain);
//                flingContainer.dispatchNestedFling(Float.valueOf(10000), Float.valueOf(10000), true);
            }
        });

        acceptButton = findViewById(R.id.accept_button);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptTree();
                Toast.makeText(Curator_Swipe_Activity.this, "Accepted!", Toast.LENGTH_SHORT).show();
                penTrees.remove(0);
                arrayAdapter.notifyDataSetChanged();
                if(penTrees.size()>0) {
                    arrayAdapter.getView(0, flingContainer.getSelectedView(), null);
                    setCurrentTree();
                }
//
//                penTrees.remove(0);
//                flingContainer.getSelectedView().setVisibility(View.GONE);
//                arrayAdapter.notifyDataSetChanged();
            }
        });

        undoButton = findViewById(R.id.undo_button);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!previousTrees.isEmpty()) {
                    DocSnap document = previousTrees.pop();
                    if(document.wasDeleted()) {
                        DocumentReference docum = db.collection("pendingTrees").document();
                        docum.set(document.getDoc().getData()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                if (document.wasApproved()) {
                                    DocumentReference doc = apprRef.document(document.getId());
                                    if (doc != null) {
                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("commonName", FieldValue.delete());
                                        updates.put("dbhArray", FieldValue.delete());
                                        updates.put("images", FieldValue.delete());
                                        updates.put("latitude", FieldValue.delete());
                                        updates.put("longitude", FieldValue.delete());
                                        updates.put("otherInfo", FieldValue.delete());
                                        updates.put("scientificName", FieldValue.delete());
                                        updates.put("timestamp", FieldValue.delete());
                                        updates.put("userID", FieldValue.delete());
                                        doc.update(updates);
                                        doc.delete();
                                    }
                                }
                                penTrees.add(0, makeTree(document.getDoc()));
                                penTrees.get(0).setID(docum.getId());
                                arrayAdapter.notifyDataSetChanged();
                                arrayAdapter.getView(0, flingContainer.getSelectedView(), null);
                                setCurrentTree();
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                previousTrees.push(document);
                                Log.w("error", "Error writing document. Tree file has been pushed back on stack", e);
                            }
                        });
                    }else{
                        penTrees.add(0, makeTree(document.getDoc()));
//                        penTrees.get(0).setID(docum.getId());
                        arrayAdapter.notifyDataSetChanged();
                        arrayAdapter.getView(0, flingContainer.getSelectedView(), null);
                        setCurrentTree();
                    }
                } else {
                    Toast.makeText(Curator_Swipe_Activity.this, "No previous trees", Toast.LENGTH_SHORT).show();
                }
            }
        });


        skipButton = findViewById(R.id.skip_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (penTrees.size() > 1){
                    DocumentReference doc = treesRef.document(currentTree.getID());
                    doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        DocSnap docSnap = new DocSnap(false, documentSnapshot.getId(), documentSnapshot, false);
                        previousTrees.push(docSnap);
                        penTrees.remove(0);
                        arrayAdapter.notifyDataSetChanged();
                        arrayAdapter.getView(0, flingContainer.getSelectedView(), null);
                        setCurrentTree();
                    }
                    });
                }else{
                    Toast.makeText(Curator_Swipe_Activity.this, "No more trees", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void setCurrentTree(){
        if(penTrees.size() > 0) {
            currentTree = penTrees.get(0);
        }
    }

    public void rejectTree(){
        DocumentReference doc = treesRef.document(currentTree.getID());
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        previousTrees.push(new DocSnap(false, doc.getId(), document, true));
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("commonName", FieldValue.delete());
                        updates.put("dbhArray", FieldValue.delete());
                        updates.put("images", FieldValue.delete());
                        updates.put("latitude", FieldValue.delete());
                        updates.put("longitude", FieldValue.delete());
                        updates.put("otherInfo", FieldValue.delete());
                        updates.put("scientificName", FieldValue.delete());
                        updates.put("timestamp", FieldValue.delete());
                        updates.put("userID", FieldValue.delete());
                        doc.update(updates);
                        doc.delete();
                        arrayAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("problem:", "No such document in rejectTree()");
                    }
                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
//        flingContainer.
        //setCurrentTree();
        });
    }

    public void acceptTree(){
        DocumentReference doc = treesRef.document(currentTree.getID());
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        db.collection("acceptedTrees").document().set(document.getData())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Log.d(TAG, "DocumentSnapshot successfully written!");
//                                        rejectTree();//just to delete currentTree
                                        DocumentSnapshot document = task.getResult();
                                        previousTrees.push(new DocSnap(true, document.getId(), document, true));
                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("commonName", FieldValue.delete());
                                        updates.put("dbhArray", FieldValue.delete());
                                        updates.put("images", FieldValue.delete());
                                        updates.put("latitude", FieldValue.delete());
                                        updates.put("longitude", FieldValue.delete());
                                        updates.put("otherInfo", FieldValue.delete());
                                        updates.put("scientificName", FieldValue.delete());
                                        updates.put("timestamp", FieldValue.delete());
                                        updates.put("userID", FieldValue.delete());
                                        doc.update(updates);
                                        doc.delete();
                                        arrayAdapter.notifyDataSetChanged();
                                        setCurrentTree();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
//                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
                    } else {
//                        Log.d(TAG, "No such document");
                    }
                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public Tree makeTree(DocumentSnapshot document){
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
        tree.setID(document.getId());
        ArrayList<String> notes = (ArrayList<String>) document.get("notes");
        if(notes != null && !notes.isEmpty()) {
            String finalNote = "";
            for (String note : notes) {
                finalNote += note + "\n";
            }
            //String note = otherInfo.get("Notes");
//          while(i>=0) {
//          String notes = (String) tree.getInfo("Notes");
//          notes = notes + "/n" + otherInfo[i];
//          }
            if (!finalNote.equals("")) {
                tree.addInfo("Notes", finalNote);
            }
        }

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
//                                    byte[] encodeByte = Base64.decode(pic, Base64.DEFAULT);
//                                    InputStream is = new ByteArrayInputStream(encodeByte);
//
//                                    Bitmap bmp = BitmapFactory.decodeStream(is);
//                                    BitmapDrawable dBmp = new BitmapDrawable(getResources(), bmp);

                if(pic != null) {
                    tree.addPics(key, stringPics.get(i));
                }
                i++;
            }
        }
        return tree;
    }

    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
        @Override
        protected Long doInBackground(URL... urls) {
//            FirebaseFirestore db = (FirebaseFirestore) objects[0];
            db.collection("pendingTrees").orderBy("timestamp", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Tree tree = makeTree(document);
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
            //setCurrentTree();
        }
    }
}
