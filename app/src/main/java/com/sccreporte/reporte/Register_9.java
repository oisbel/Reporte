package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sccreporte.reporte.utilities.DataUtils;
import com.sccreporte.reporte.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class Register_9 extends AppCompatActivity {

    Button endButton;
    ImageButton backButton;
    EditText responsabilidadEditText;
    ProgressBar loadingProgressBar;
    // Objeto JSON con los datos del nuevo usuario a iniciar
    JSONObject userDataJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_9);

        endButton = findViewById(R.id.endButton);
        responsabilidadEditText = findViewById(R.id.responsabilidadEditText);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        userDataJSON = new JSONObject();
        backButton = findViewById(R.id.backIB);

        // Obtener el string pasado de la activity anterior
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            String userDataJSONString = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            try {
                userDataJSON = new JSONObject(userDataJSONString);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    userDataJSON.put("responsabilidad", responsabilidadEditText.getText());
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                new EditUserQueryTask().execute(userDataJSON);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Guarda el usuario creado , usando SharePreferences
     * @param user_id nuevo id del usuario que se registro
     */
    private void SaveUser(int user_id) {
        if(userDataJSON != null)
            DataUtils.SaveUserData(this, userDataJSON, user_id);
    }

    private void OpenMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void ShowNullMessage(){
        Toast toast = Toast.makeText(this, R.string.null_response_error_message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void ShowServerMessage(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void ShowSuccessMessage(){
        Toast toast = Toast.makeText(this, R.string.user_created, Toast.LENGTH_LONG);
        toast.show();
    }

    private void showLoading(){
        loadingProgressBar.setVisibility(View.VISIBLE);
        endButton.setVisibility(View.INVISIBLE);
    }

    private void hideLoading(){
        loadingProgressBar.setVisibility(View.INVISIBLE);
        endButton.setVisibility(View.VISIBLE);
    }

    /**
     * Clase que manda los datos del usuario a editar al servidor, y maneja el resultado devuelto
     * usando otro hilo
     */
    public class EditUserQueryTask extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }
        @Override
        protected JSONObject doInBackground(JSONObject... jsonObjects) {
            if(jsonObjects == null || jsonObjects.length == 0) return null;
            JSONObject jsonData = jsonObjects[0];

            String editReportJSONResult;
            JSONObject result = null;

            int id=-1;
            String email="";
            String password="";
            try{
                id = userDataJSON.getInt("id");
                email = userDataJSON.getString("email");
                password = userDataJSON.getString("password");
            } catch (JSONException e) {
                e.printStackTrace();
                return result;
            }
            URL editUserUrl = NetworkUtils.buildEditUserUrl(id);
            try {
                editReportJSONResult = NetworkUtils.getEditUserFromHttpUrl(
                        editUserUrl, jsonData, email, password);
            }catch (IOException e){
                e.printStackTrace();
                return result;
            }
            try {
                result = new JSONObject(editReportJSONResult);
            }catch (JSONException e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            hideLoading();
            if(jsonObject != null){
                if(jsonObject.has("message")) {
                    try {
                        ShowServerMessage(jsonObject.getString("message"));
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(jsonObject.has("user")){
                        // sucess
                        ShowSuccessMessage();
                        int user_id = -1;
                        try{
                            user_id = jsonObject.getInt("user");
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        if(user_id != -1){
                            SaveUser(user_id);
                            OpenMainActivity();
                            return;
                        }
                }else ShowNullMessage();
            }else{
                ShowNullMessage();
            }
        }
    }
}
