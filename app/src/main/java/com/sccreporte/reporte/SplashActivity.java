package com.sccreporte.reporte;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sccreporte.reporte.utilities.DataUtils;
import com.sccreporte.reporte.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private AsyncTask mBackgroundTask;
    SharedPreferences sharedPreferences;
    Context context;

    Button loginBT;
    TextView registerTV;
    ProgressBar loadingIndicator;

    FloatingActionButton fab; //boton flotante para agregar estudio biblico
    LinearLayout floatingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = getApplicationContext();

        loginBT = findViewById(R.id.loginBT);
        registerTV =  findViewById(R.id.registerTV);
        loadingIndicator = findViewById(R.id.loading_indicator);
        fab = findViewById(R.id.fab);
        floatingLayout = findViewById(R.id.floatingLayout);

        // Check if there is a user registered, load the user email
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Guardar los link de social media y demas en SharedPreferences
        getSocialMediaLinks();

        loginBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        registerTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startChildActivityIntent = new Intent(context,Register_0.class);
                startActivity(startChildActivityIntent);
                finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = DataUtils.loadSocialMediaLinks(context);
                if (jsonObject == null)
                        return;
                String id = "063V0DnwpFM";
                try{
                    id = jsonObject.getString("tutorial");
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                openYoutube(id);
            }
        });
    }

    /**
     * Verifica si hay un usuario logeado para ir a el amin activity, sino pone las opciones de registar y login
     */
    private void seeIfUserLogIn(){
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
    }

    /**
     * Muestra los links para entrar y registrase(estan invisibles)
     * y hide el progress loading bar
     */
    private void showLoginRegister(){
        loginBT.setVisibility(View.VISIBLE);
        registerTV.setVisibility(View.VISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
        floatingLayout.setVisibility(View.VISIBLE);
    }

    private void openYoutube(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    /**
     * Descarga los links del sitio web y redes sociales y guardarlos en SharedPreferences
     */
    private void getSocialMediaLinks(){
        mBackgroundTask = new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingIndicator.setVisibility(View.VISIBLE);
            }
            @Override
            protected JSONObject doInBackground(Void... voids) {
                URL socialMediaUrl = NetworkUtils.buildGetSocialMediaUrl();
                String socialMediaJSONResult = "";
                JSONObject result = null;
                try{
                    socialMediaJSONResult = NetworkUtils.getSocialMediaFromHttpUrl(
                            socialMediaUrl);

                }catch (IOException e){
                    e.printStackTrace();
                    return result;
                }
                try {
                    result = new JSONObject(socialMediaJSONResult);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(final JSONObject jsonObject) {
                if(jsonObject != null){
                    if(jsonObject.has("website")) {
                        DataUtils.saveSocialMediaLinks(getApplicationContext(), jsonObject);
                    }
                }
                loadingIndicator.setVisibility(View.INVISIBLE);
                seeIfUserLogIn();
            }
        }.execute();
    }
}
