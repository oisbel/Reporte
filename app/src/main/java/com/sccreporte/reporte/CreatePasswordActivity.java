package com.sccreporte.reporte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.View.OnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

public class CreatePasswordActivity extends AppCompatActivity {

    // String extra pasado de la activity register
    String mUserDataJSONString="";
    // Objeto JSON creado a partir de mUserDataJSONString
    JSONObject userDataJASON;
    EditText emailET;
    EditText passwordET;
    CardView registerCV;
    ProgressBar loadingPB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        emailET = (EditText) findViewById(R.id.emailEditText);
        passwordET = (EditText) findViewById(R.id.passwordEditText);
        registerCV = (CardView) findViewById(R.id.registerCardView);
        loadingPB = (ProgressBar) findViewById(R.id.loadingProgressBar);

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

        registerCV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
