package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.sccreporte.reporte.data.Church;
import com.sccreporte.reporte.utilities.DataUtils;
import com.sccreporte.reporte.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    EditText emailET;
    EditText passwordET;
    ProgressBar loadingProgressBar;
    private EditText nombreET;
    private EditText ministerioET;
    private EditText responsabilidadET;
    private Button registerButton;
    private Spinner gradoSpinner;
    private Spinner churchSpinner;

    private List<Church> churchs;

    // Objeto JSON con los datos del nuevo usuario a crear
    JSONObject userDataJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        nombreET = (EditText) findViewById(R.id.nombreEditText);
        churchSpinner = findViewById(R.id.churchSpinner);
        gradoSpinner = findViewById(R.id.gradoSpinner);
        ministerioET = (EditText) findViewById(R.id.ministerioEditText);
        responsabilidadET = (EditText) findViewById(R.id.responsabilidadEditText);
        registerButton = (Button) findViewById(R.id.registerButton);

        // Obtener el string pasado de la activity anterior
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            String churchsJSONString = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            churchs = CreateChurchsList(churchsJSONString);
            List<String> listChurchs = new ArrayList<>();
            for (int i =0; i<churchs.size();i++){
                listChurchs.add(churchs.get(i).nombre);
            }

            // Agregar un spinner para las iglesias
            ArrayAdapter<String> adapterChurchs = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item,listChurchs);
            // Specify the layout to use when the list of choices appears
            adapterChurchs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            churchSpinner.setAdapter(adapterChurchs);
        }

        // Agregar un spinner para el grado
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.grado, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gradoSpinner.setAdapter(adapter);

        userDataJSON = new JSONObject();

        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCreateUserQuery();
            }
        });
    }

    /**
     * Crea una lista de objects church a partir del string pasado de la activity anterior
     * @param churchsJSONString
     * @return
     */
    private List<Church> CreateChurchsList(String churchsJSONString){
        List<Church> result= new ArrayList<>();
        try{
            JSONObject data = new JSONObject(churchsJSONString);
            JSONArray churchsList = data.getJSONArray("list");
            for (int i = 0; i<churchsList.length();i++){
                result.add(new Church(churchsList.getJSONObject(i)));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Crea el objeto JSON que se va a mandar con el request de crear
     * el usuario y ejecuta el asynctask
     */
    private void makeCreateUserQuery() {
        if (!checkEntries()) return;
        try {
            userDataJSON.put("email", emailET.getText());
            userDataJSON.put("password", passwordET.getText());
            userDataJSON.put("nombre",nombreET.getText());
            userDataJSON.put("grado",gradoSpinner.getSelectedItem().toString());
            userDataJSON.put("ministerio",ministerioET.getText());
            userDataJSON.put("responsabilidad",responsabilidadET.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new CreateUserQueryTask().execute(userDataJSON);
    }

    /**
     * Guarda el usuario creado , usando SharePreferences
     * Ademas abre la main activity
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

    /**
     * Verifica que se hayan entrado los campos obligatorios(nombre
     * @return
     */
    private boolean checkEntries(){
        if(!DataUtils.validateEmail(emailET.getText().toString())){
            emailET.setError(getResources().getString(R.string.invalid_email));
            return false;
        }
        if(!DataUtils.validateName(nombreET.getText().toString())){
            nombreET.setError(getResources().getString(R.string.invalid_name));
            return false;
        }
        if(passwordET.getText().toString().equalsIgnoreCase("")){
            passwordET.setError(getResources().getString(R.string.mandatory_error));
            return false;
        }
        if(gradoSpinner.getSelectedItem().toString().equals(getResources().getString(R.string.grado_label))){
            Toast.makeText(this, R.string.mandatory_grado, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void ShowNullMessage(){
        Toast toast = Toast.makeText(this, R.string.null_response_error_message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void ShowServerMessage(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void ShowCreatedUserMessage(){
        Toast toast = Toast.makeText(this, R.string.user_created, Toast.LENGTH_LONG);
        toast.show();
    }

    private void showLoading(){
        loadingProgressBar.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.INVISIBLE);
    }

    private void showLogin(){
        loadingProgressBar.setVisibility(View.INVISIBLE);
        registerButton.setVisibility(View.VISIBLE);
    }

    /**
     * Ejecuta el pedido de crear un usuario nuevo pasandole el objeto json
     * con toda la informacion del usuario
     */
    public class CreateUserQueryTask extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... jsonObjects) {
            if(jsonObjects == null || jsonObjects.length == 0) return null;
            JSONObject jsonData = jsonObjects[0];
            URL createUserUrl = NetworkUtils.buildCreateUserUrl();
            String createUserJSONResult = null;
            JSONObject result = null;
            try {
                createUserJSONResult = NetworkUtils.geCreateUserFromHttpUrl(createUserUrl,jsonData);
            }catch (IOException e){
                e.printStackTrace();
                return result;
            }
            try {
                result = new JSONObject(createUserJSONResult);
            }catch (JSONException e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            showLogin();
            if(jsonObject != null){
                if(jsonObject.has("message")) {
                    try {
                        ShowServerMessage(jsonObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                        OpenMainActivity();
                    }
                }
            } else {
                ShowNullMessage();
            }
        }
    }
}
