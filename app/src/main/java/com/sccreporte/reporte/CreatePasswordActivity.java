package com.sccreporte.reporte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class CreatePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        String mUserDataJSONString="";

        // Obtener el string pasado de la activity anterior
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            mUserDataJSONString = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }

        if(mUserDataJSONString != ""){
            Toast toast = Toast.makeText(this,mUserDataJSONString,Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
