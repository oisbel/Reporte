package com.sccreporte.reporte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class CreatePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        // String extra pasado de la activity register
        String mUserDataJSONString="";
        // Objeto JSON creado a partir de mUserDataJSONString
        JSONObject userDataJASON;

        // Obtener el string pasado de la activity anterior
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            mUserDataJSONString = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }
        // Crear el objeto JSON
        if(mUserDataJSONString != ""){
            try{
                userDataJASON = new JSONObject(mUserDataJSONString);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
