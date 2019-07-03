package com.example.treesapv2new;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class Login_Activity extends AppCompatActivity {
    public static FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener mAuthListener;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(null);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.button_login_first);
        loginButton.setOnClickListener(new onLoginClick());
        Button registerButton = ((Button) findViewById(R.id.button_register_first));
        registerButton.setOnClickListener(new onRegisterClick());

        ((TextView) findViewById(R.id.textview_login)).setOnClickListener(new onLoginClick());
        ((TextView) findViewById(R.id.textview_register)).setOnClickListener(new onRegisterClick());
        ((Button) findViewById(R.id.button_register)).setOnClickListener(new register());
        ((Button) findViewById(R.id.button_login)).setOnClickListener(new login());
        ((TextView) findViewById(R.id.cancel_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView) findViewById(R.id.forgot_password_link)).setOnClickListener(new forgotPassword());
        ((TextView) findViewById(R.id.back_to_login)).setOnClickListener( new onLoginClick());
        ((TextView) findViewById(R.id.button_forgot_password)).setOnClickListener(new submitForgotPassword());



        //Authorization Stuff
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@Nonnull FirebaseAuth firebaseAuth1){
                FirebaseUser user = firebaseAuth1.getCurrentUser();
                if(user!=null){
                    //user is signed in
                    finish();
                }else{
                    //user is signed out
                }
            }
        };

    }

    private class submitForgotPassword implements View.OnClickListener{
        private FirebaseAuth mAuth;

        public void onClick(View v){
            String emailForgot = ((TextView) findViewById(R.id.forgot_password_email)).getText().toString();
            mAuth = FirebaseAuth.getInstance();
            if(emailForgot.equals("")){
                Toast.makeText(Login_Activity.this, "Please provide an email", Toast.LENGTH_SHORT).show();
            }else{
                mAuth.sendPasswordResetEmail(emailForgot).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login_Activity.this, "Please check your email", Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            String message = task.getException().getMessage();
                            Toast.makeText(Login_Activity.this,"Error occured: "+ message, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }



    private class forgotPassword implements View.OnClickListener{
        public void onClick(View v){
            ((LinearLayout)findViewById(R.id.login_first_menu)).setVisibility(View.GONE);
            ((LinearLayout)findViewById(R.id.register_container)).setVisibility(View.GONE);
            ((LinearLayout)findViewById(R.id.login_container)).setVisibility(View.GONE);
            ((LinearLayout)findViewById(R.id.forgot_password_container)).setVisibility(View.VISIBLE);
        }
    }

    private class login implements View.OnClickListener{
        public void onClick(View v){
            String email = ((TextView)findViewById(R.id.edittext_username)).getText().toString();
            String password = ((TextView)findViewById(R.id.edittext_password)).getText().toString();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast toast = Toast.makeText(Login_Activity.this, "You are logged in.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }else{
                        Toast toast = Toast.makeText(Login_Activity.this, "Invalid login.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0,0);
                        toast.show();
                    }
                }
            });

        }
    }

    private class register implements View.OnClickListener{
        public void onClick(View v){
            String email = ((TextView)findViewById(R.id.edittext_username_reg)).getText().toString();
            String password = ((TextView)findViewById(R.id.edittext_password_reg)).getText().toString();
            String passwordCnf = ((TextView)findViewById(R.id.edittext_cnf_password)).getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()) {
                        Log.i("Failed Task", "signinwithemail:failed ", task.getException());
                        try {
                            throw task.getException();
                        } catch(FirebaseAuthWeakPasswordException e) {
                            Toast toast = Toast.makeText(Login_Activity.this, "Password must be at least 6 characters.", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0,0);
                            toast.show();
                        } catch(FirebaseAuthInvalidCredentialsException e) {
                            Toast toast = Toast.makeText(Login_Activity.this, "Please enter a valid email.", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0,0);
                            toast.show();
                        } catch(FirebaseAuthUserCollisionException e) {
                            Toast toast = Toast.makeText(Login_Activity.this, "This email already exists.", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0,0);
                            toast.show();
                        } catch(Exception e) {
                            Toast toast = Toast.makeText(Login_Activity.this, "Registration failed.", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }else {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(((EditText) findViewById(R.id.edittext_displayname_reg)).getText().toString()).build();
                        user.updateProfile(profileChangeRequest);


                        HashMap<String, String> tempHash = new HashMap<String, String>();
                        tempHash.put("userID", mAuth.getUid().toString());
                        tempHash.put("email",email);
                        db.collection("users").document().set(tempHash);
//                        emailToUID.document().set(tempHash);

                    }

                }
            });
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private class onLoginClick implements View.OnClickListener{
        public void onClick(View v){
            ((LinearLayout)findViewById(R.id.login_first_menu)).setVisibility(View.GONE);
            ((LinearLayout)findViewById(R.id.register_container)).setVisibility(View.GONE);
            ((LinearLayout)findViewById(R.id.login_container)).setVisibility(View.VISIBLE);
        }
    }
    private class onRegisterClick implements View.OnClickListener{
        public void onClick(View v){
            ((LinearLayout)findViewById(R.id.login_first_menu)).setVisibility(View.GONE);
            ((LinearLayout)findViewById(R.id.login_container)).setVisibility(View.GONE);
            ((LinearLayout)findViewById(R.id.register_container)).setVisibility(View.VISIBLE);
        }
    }
}
