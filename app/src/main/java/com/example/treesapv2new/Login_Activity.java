package com.example.treesapv2new;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Login_Activity extends AppCompatActivity {
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
    }

    private class login implements View.OnClickListener{
        public void onClick(View v){
            Add_Tree_Activity.loggedIn = true;
            finish();
        }
    }

    private class register implements View.OnClickListener{
        public void onClick(View v){
            Add_Tree_Activity.loggedIn = true;
            finish();
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
