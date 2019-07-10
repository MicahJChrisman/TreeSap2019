package com.example.treesapv2new;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class changePassword extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.change_password);
        boolean isConnectedToFirebase = ConnectionCheck.isConnectedToFirebase();
        ((Button) findViewById(R.id.button_change_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnectedToFirebase) {
                    FirebaseUser user = Login_Activity.mAuth.getCurrentUser();
                    final String email = user.getEmail();
                    String oldpass = ((EditText) findViewById(R.id.old_password)).getText().toString();
                    String newPass = ((EditText) findViewById(R.id.new_password)).getText().toString();
                    if (newPass.equals(((EditText) findViewById(R.id.confirm_new_password)).getText().toString())) {
                        AuthCredential credential = EmailAuthProvider.getCredential(email, oldpass);
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {
                                                try {
                                                    throw task.getException();
                                                } catch (FirebaseAuthWeakPasswordException e) {
                                                    Toast toast = Toast.makeText(changePassword.this, "Password must be at least 6 characters.", Toast.LENGTH_LONG);
                                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                                    toast.show();
                                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                                    Toast toast = Toast.makeText(changePassword.this, "Please enter a valid email.", Toast.LENGTH_LONG);
                                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                                    toast.show();
                                                } catch (FirebaseAuthUserCollisionException e) {
                                                    Toast toast = Toast.makeText(changePassword.this, "This email already exists.", Toast.LENGTH_LONG);
                                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                                    toast.show();
                                                } catch (Exception e) {
                                                    Toast toast = Toast.makeText(changePassword.this, "An error occurred.", Toast.LENGTH_LONG);
                                                    toast.show();
                                                }
                                            } else {
                                                Toast.makeText(changePassword.this, "Password updated", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(changePassword.this, "Authentication failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(changePassword.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast toast = Toast.makeText(changePassword.this, "No internet, cannot change password", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }
}
