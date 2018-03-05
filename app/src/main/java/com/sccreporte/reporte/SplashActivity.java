package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    TextView loginTB;
    Button registerBT;
    ProgressBar loadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loginTB = (TextView)findViewById(R.id.loginTextView);
        registerBT = (Button)findViewById(R.id.registerButton);
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        // Check if there is a user registered, load the user email
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // temp delete sharepreferences
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.clear();
        //editor.apply();

        // Load user data to see if is already register
        final String user_email = sharedPreferences.getString("email","");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(user_email.equals("")){
                    // No hay nadie registrado
                    showLoginRegister();
                } else {
                    // if there is data from existing user: log in
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);

        loginTB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = SplashActivity.this;
                Class destinationActivity = CreatePasswordActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startActivity(startChildActivityIntent);
                finish();
            }
        });

        registerBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Muestra los links para entrar y registrase(estan invisibles)
     * y hide el progress loading bar
     */
    private void showLoginRegister(){
        loginTB.setVisibility(View.VISIBLE);
        registerBT.setVisibility(View.VISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
    }
}
