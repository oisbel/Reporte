package com.sccreporte.reporte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.sccreporte.reporte.utilities.DataUtils;
import com.sccreporte.reporte.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class CreatePasswordActivity extends AppCompatActivity {

    // String extra pasado de la anterior activity register
    String mUserDataJSONString="";
    // Objeto JSON creado a partir de mUserDataJSONString
    JSONObject userDataJSON;
    EditText emailET;
    EditText passwordET;
    Button registerBT;
    ProgressBar loadingPB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        emailET = (EditText) findViewById(R.id.emailEditText);
        passwordET = (EditText) findViewById(R.id.passwordEditText);
        registerBT = (Button) findViewById(R.id.registerButton);
        loadingPB = (ProgressBar) findViewById(R.id.loadingProgressBar);

        // Obtener el string pasado de la activity anterior
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            mUserDataJSONString = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }
        // Crear el objeto JSON a partir del string pasado de la activity anterior
        if(mUserDataJSONString != ""){
            try{
                userDataJSON = new JSONObject(mUserDataJSONString);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        registerBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCreateUserQuery();
            }
        });
    }

    private void ShowErrorMessage(){
        Toast toast = Toast.makeText(this, R.string.create_user_error_message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void ShowUserExistMessage(){
        Toast toast = Toast.makeText(this, R.string.user_exist_message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void ShowCreatedUserMessage(){
        Toast toast = Toast.makeText(this, R.string.user_created, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Guarda el usuario creado (userDataJSON), usando SharePreferences
     * Ademas abre la main activity
     * @param user_id
     */
    private void SaveUser(int user_id){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putInt("id", user_id);
            editor.putString("nombre", userDataJSON.getString("nombre"));
            editor.putString("email", userDataJSON.getString("email"));
            editor.putString("grado", userDataJSON.getString("grado"));
            editor.putString("ministerio", userDataJSON.getString("ministerio"));
            editor.putString("responsabilidad", userDataJSON.getString("responsabilidad"));
            editor.putString("lugar", userDataJSON.getString("lugar"));
            editor.putString("pastor", userDataJSON.getString("pastor"));
            editor.putString("password", userDataJSON.getString("password"));
            editor.apply();

        }catch (JSONException e){
            e.printStackTrace();
        }
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();

    }

    /**
     * Verifica que se hayan entrado los campos obligatorios(nombre
     * @return
     */
    private boolean checkEntries(){
        if(passwordET.getText().toString().equalsIgnoreCase("")){
            passwordET.setError("Correo Inválido");
            return false;
        }
        if(!DataUtils.validateEmail(emailET.getText().toString())){
            emailET.setError("Obligatorio");
            return false;
        }
        return true;
    }

    /**
     * Crea el objeto JSON que se va a mandar con el rewuest de crear
     * el usuario y ejecuta el asynctask
     */
    private void makeCreateUserQuery(){
        if(!checkEntries()) return;
        if(userDataJSON != null){
            try {
                userDataJSON.put("email", emailET.getText());
                userDataJSON.put("password", passwordET.getText());
            }catch (JSONException e){
                e.printStackTrace();
            }
            new CreateUserQueryTask().execute(userDataJSON);
        }
    }

    public class CreateUserQueryTask extends AsyncTask<JSONObject, Void, JSONObject>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(JSONObject... jsonObjects) {
            if(jsonObjects.length == 0) return null;
            JSONObject jsonData = jsonObjects[0];
            URL createUserUrl = NetworkUtils.buildCreateUserUrl();
            String createUserJSONResult = null;
            try {
                createUserJSONResult = NetworkUtils.geCreateUserFromHttpUrl(createUserUrl,jsonData);
            }catch (IOException e){
                e.printStackTrace();
            }
            JSONObject result = null;
            try {
                result = new JSONObject(createUserJSONResult);
            }catch (JSONException e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            loadingPB.setVisibility(View.INVISIBLE);
            if(jsonObject != null){
                if(jsonObject.has("message")){
                    ShowUserExistMessage();
                }
                if(jsonObject.has("email")){
                    // sucess
                    ShowCreatedUserMessage();
                    int user_id = -1;
                    try{
                        user_id = jsonObject.getInt("id");
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    if(user_id != -1){
                        SaveUser(user_id);
                    }
                }
            } else {
                ShowErrorMessage();
            }
        }
    }
}
