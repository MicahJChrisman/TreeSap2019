package com.example.treesapv2new;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;

public class changeUsername extends AppCompatActivity {
    public static String newUsername = Login_Activity.mAuth.getCurrentUser().getDisplayName();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.change_username);
        ((Button) findViewById(R.id.button_change_username)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isConnectedToFirebase = ConnectionCheck.isConnectedToFirebase();
                if(isConnectedToFirebase) {
                    FirebaseUser user = Login_Activity.mAuth.getCurrentUser();
                    String a = (((EditText) findViewById(R.id.old_username)).getText().toString());
                    String b = user.getDisplayName().toString();
                    if (a.equals(b)) {
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(((EditText) findViewById(R.id.new_username)).getText().toString()).build();
                        user.updateProfile(profileChangeRequest);
                        newUsername = ((EditText) findViewById(R.id.new_username)).getText().toString();
                        Toast.makeText(changeUsername.this, "Username updated", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(changeUsername.this, "Old username does not match", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast toast = Toast.makeText(changeUsername.this, "No internet, cannot change username", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }
}
