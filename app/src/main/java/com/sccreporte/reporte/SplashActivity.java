package com.sccreporte.reporte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    Button loginBT;
    TextView registerTV;
    ProgressBar loadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loginBT = findViewById(R.id.loginBT);
        registerTV =  findViewById(R.id.registerTV);
        loadingIndicator = findViewById(R.id.loading_indicator);

        // Check if there is a user registered, load the user email
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Load user data to see if is already register
        final String user_email = sharedPreferences.getString("email", "");


        if (user_email.equals("")) {
            // No hay nadie registrado o no se ha logeado en este dispositivo
            showLoginRegister();
        } else {
            // if there is data from existing user: log in
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }


        loginBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startChildActivityIntent = new Intent(getApplicationContext(),Register_0.class);
                startActivity(startChildActivityIntent);
                finish();
            }
        });
    }

    /**
     * Muestra los links para entrar y registrase(estan invisibles)
     * y hide el progress loading bar
     */
    private void showLoginRegister(){
        loginBT.setVisibility(View.VISIBLE);
        registerTV.setVisibility(View.VISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
    }
}
