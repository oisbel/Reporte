package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sccreporte.reporte.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    Button loginBT;
    TextView registerTV;
    ProgressBar loadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loginBT = (Button) findViewById(R.id.loginBT);
        registerTV = (TextView) findViewById(R.id.registerTV);
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        // Check if there is a user registered, load the user email
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // temp delete sharepreferences
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.clear();
        //editor.apply();

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
                new GetChurchsQueryTask().execute();
            }
        });
    }

    /**
     *  Pasa la lista de iglesias a register activity
     * @param churchs lista de igleias json obtenidas del servidor
     */
    public void GotoRegisterActivity(JSONObject churchs){
        Intent startChildActivityIntent = new Intent(getApplicationContext(),RegisterActivity.class);
        // Pasar a la otra activity el String Json de la lista de iglesias
        startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, churchs.toString());
        startActivity(startChildActivityIntent);
        finish();
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

    private void showLoading(){
        loginBT.setVisibility(View.INVISIBLE);
        registerTV.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    private void ShowNullMessage(){
        Toast toast = Toast.makeText(this, R.string.null_response_error_message, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Ejecuta el pedido de obtener la lista de iglesias
     */
    public class GetChurchsQueryTask extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }

        @Override
        protected JSONObject doInBackground(Void... Objects) {
            URL getChurchsUrl = NetworkUtils.buildGetchurchsUrl();
            String churchsJSONResult = null;
            JSONObject result = null;
            try {
                churchsJSONResult = NetworkUtils.getChurchsFromHttpUrl(getChurchsUrl);
            }catch (IOException e){
                e.printStackTrace();
                return result;
            }
            try {
                result = new JSONObject(churchsJSONResult);
            }catch (JSONException e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(jsonObject != null){
                String status="";
                    try{
                        status = jsonObject.getString("status");
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    if(status.equals("ok")){
                        GotoRegisterActivity(jsonObject);
                    }else ShowNullMessage();
            } else {
                ShowNullMessage();
            }
        }
    }
}
