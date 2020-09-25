package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sccreporte.reporte.utilities.DataUtils;
import com.sccreporte.reporte.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class Register_0 extends AppCompatActivity {

    TextView messageTV; // Para mostrar errores
    EditText emailET;
    EditText passwordET;
    ProgressBar loadingProgressBar;
    Button initButton;
    ImageButton backButton;

    // Objeto JSON con los datos del nuevo usuario a iniciar
    JSONObject userDataJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_0);

        messageTV = findViewById(R.id.messageTV);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        initButton = findViewById(R.id.initButton);
        backButton = findViewById(R.id.backIB);

        userDataJSON = new JSONObject();

        initButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeInitUserQuery();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void GoToNextActivity(JSONObject userDataJSON){
        // Guardar el password
        try {
            userDataJSON.put("old_password", passwordET.getText());
        }catch (JSONException e){
            e.printStackTrace();
        }
        Context context = Register_0.this;
        Class destinationActivity = Register_1.class;
        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, userDataJSON.toString());
        startActivity(startChildActivityIntent);
    }

    /**
     * Crea el objeto JSON que se va a mandar con el request de iniciar
     * el usuario y ejecuta el asynctask
     */
    private void makeInitUserQuery() {
        if (!checkEntries()) return;
        try {
            userDataJSON.put("email", emailET.getText());
            userDataJSON.put("password", passwordET.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new GetUserQueryTask().execute(userDataJSON);
    }

    /**
     * Verifica que se hayan entrado los campos obligatorios y correctamente
     * @return
     */
    private boolean checkEntries(){
        if(!DataUtils.validateEmail(emailET.getText().toString())){
            emailET.setError(getResources().getString(R.string.invalid_email));
            return false;
        }
        if(passwordET.getText().toString().equalsIgnoreCase("")){
            passwordET.setError(getResources().getString(R.string.mandatory_error));
            return false;
        }
        return true;
    }

    private void showLoading(){
        loadingProgressBar.setVisibility(View.VISIBLE);
        initButton.setVisibility(View.INVISIBLE);
    }

    private void hideLoading(){
        loadingProgressBar.setVisibility(View.INVISIBLE);
        initButton.setVisibility(View.VISIBLE);
    }

    private void ShowErrorMessage(String message){
        messageTV.setText(message);
        messageTV.setVisibility(View.VISIBLE);
    }

    private void HideErrorMessage(){
        messageTV.setVisibility(View.INVISIBLE);
    }

    /**
     * Ejecuta el pedido de verificar que el email y pass provistos pertenezcan a un usuario y pasa los datos a
     * la siguiente activity
     */
    public class GetUserQueryTask extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            HideErrorMessage();
            showLoading();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... jsonObjects) {
            if(jsonObjects == null || jsonObjects.length == 0) return null;
            JSONObject jsonData = jsonObjects[0];
            URL initUserUrl = NetworkUtils.buildGetUserUrl();
            String initUserJSONResult = null;
            JSONObject result = null;
            String email="";
            String password="";
            try{
                email = jsonData.getString("email");
                password = jsonData.getString("password");
            }catch (JSONException e){
                e.printStackTrace();
                return result;
            }
            try {
                initUserJSONResult = NetworkUtils.getUserFromHttpUrl(initUserUrl,
                        email, password);
            }catch (IOException e){
                e.printStackTrace();
                return result;
            }
            try {
                result = new JSONObject(initUserJSONResult);
            }catch (JSONException e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(jsonObject != null){
                if(jsonObject.has("message")) {
                    try {
                        ShowErrorMessage(jsonObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(jsonObject.has("status")){
                    try{
                        if(jsonObject.getString("status").equals("fail"))
                        {
                            ShowErrorMessage("El usuario especificado no existe");
                            hideLoading();
                            return;
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                        ShowErrorMessage("Error recibiendo respuesta del servidor");
                        hideLoading();
                        return;
                    }
                    // sucess
                    boolean profile_complete = true;
                    boolean admin = true;
                    try{
                        profile_complete = jsonObject.getBoolean("profile_complete");
                        admin = jsonObject.getBoolean("admin");
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    if(profile_complete || admin){
                        ShowErrorMessage("Los datos de usuario especificados no pueden iniciar una cuenta");
                        hideLoading();
                        return;
                    }else {
                        GoToNextActivity(jsonObject);
                        hideLoading();
                    }
                }
            } else {
                ShowErrorMessage(getString(R.string.init_user_error_message));
                hideLoading();
            }
        }
    }
}
