package com.sccreporte.reporte;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class LoginActivity extends AppCompatActivity {

    TextView messageTV; // Para mostrar errores
    ImageButton backIB;
    ProgressBar loadingPB;
    EditText emailET;
    EditText passwordET;
    Button loginBT;
    String passNoHash; // Para Guardar la clave cuando se logea el usuario yno el hash del pass de viene del server

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        messageTV = findViewById(R.id.messageTV);
        backIB = findViewById(R.id.backIB);
        loadingPB = findViewById(R.id.loadingProgressBar);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        loginBT = findViewById(R.id.loginButton);

        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeGetUserQuery();
            }
        });


        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * Crea el objeto JSON con email y pass del
     * usuario que se intenta logear, y ejecuta el asynctask
     */
    private void makeGetUserQuery(){
        if(!checkEntries()) return;
        JSONObject userDataJSON = new JSONObject();
        passNoHash = passwordET.getText().toString();
        try{
            userDataJSON.put("email", emailET.getText());
            userDataJSON.put("password", passwordET.getText());
            new GetUserQueryTask().execute(userDataJSON);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void ShowErrorMessage(String message){
        messageTV.setText(message);
        messageTV.setVisibility(View.VISIBLE);
    }

    private void HideErrorMessage(){
        messageTV.setVisibility(View.INVISIBLE);
    }

    private void showLoading(){
        loadingPB.setVisibility(View.VISIBLE);
        loginBT.setText("");
    }

    private void showLogin(){
        loadingPB.setVisibility(View.INVISIBLE);
        loginBT.setText(getString(R.string.loginLabel));
    }
    /**
     * Guarda el usuario logeado usando SharePreferences
     * @param userDataJSON es null si se esta creando el usuario, sino seria la respuesta desde el servidor
     */
    private void SaveUser(JSONObject userDataJSON) {
        if(userDataJSON != null)
            DataUtils.SaveUserData(this, userDataJSON, passNoHash);
    }

    private void goMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
    /**
     * Verifica que se hayan entrado los campos obligatorios(nombre
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

    /**
     * Ejecuta el pedido de logear un usuario pasandole el header authorization
     * que seria el objeto json con email y password
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
            if(jsonObjects.length == 0) return null;
            JSONObject result = null;

            JSONObject jsonData = jsonObjects[0];
            URL getUserUrl = NetworkUtils.buildGetUserUrl();
            String getUserJSONResult = null;
            String user_name = "";
            String pass = "";

            try{
                user_name = jsonData.getString("email");
                pass = jsonData.getString("password");
            }catch (JSONException e){
                e.printStackTrace();
            }

            try {
                getUserJSONResult = NetworkUtils.getUserFromHttpUrl(getUserUrl, user_name, pass);
            }catch (IOException e){
                e.printStackTrace();
                return result;
            }

            try {
                result = new JSONObject(getUserJSONResult);
            }catch (JSONException e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            showLogin();
            if(jsonObject != null){
                String status = "fail";
                boolean profile_complete = false;
                try{
                    status = jsonObject.getString("status");
                    profile_complete = jsonObject.getBoolean("profile_complete");
                    if(status == "fail"){
                        ShowErrorMessage(getString(R.string.invalid_user_credential));
                    }else if(!profile_complete){
                        ShowErrorMessage(getString(R.string.user_must_register));
                    }else{
                        // sucess
                        SaveUser(jsonObject);
                        goMainActivity();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    ShowErrorMessage(getString(R.string.create_user_error_message));
                }
            } else {
                ShowErrorMessage(getString(R.string.create_user_error_message));
            }
        }
    }

}
