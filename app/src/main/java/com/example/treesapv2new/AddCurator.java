package com.example.treesapv2new;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class AddCurator extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.add_curator_activity);

        findViewById(R.id.add_curator_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.add_curator_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isConnectedToFirebase = ConnectionCheck.isConnectedToFirebase();
                if(isConnectedToFirebase) {
                    String email = ((EditText) findViewById(R.id.add_curator_email)).getText().toString().toLowerCase();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (email.equals(document.get("email"))) {
                                        String userID = document.get("userID").toString();
                                        db.collection("curators").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                        String abc = documentSnapshot.getId();
                                                        if (userID.equals(documentSnapshot.getId())) {
                                                            break;
                                                        }
                                                    }
                                                    HashMap<Object, Object> tempHash = new HashMap<>();
                                                    db.collection("curators").document(userID).set(tempHash);
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT);
                                                    finish();

                                                }
                                            }
                                        });
                                    }else{
                                        Toast toast = Toast.makeText(AddCurator.this, "No user with that email found", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }
                                }
                            }
                        }
                    });
                }else{
                    Toast toast = Toast.makeText(AddCurator.this, "No internet, cannot add curator", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }
}
