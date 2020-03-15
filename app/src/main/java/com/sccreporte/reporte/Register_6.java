package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

public class Register_6 extends AppCompatActivity {

    Button nextButton;
    ImageButton backButton;
    DatePicker datePicker;
    EditText conyugeNameEditText;
    // Objeto JSON con los datos del nuevo usuario a iniciar
    JSONObject userDataJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_6);

        nextButton = findViewById(R.id.nextButton);
        datePicker = findViewById(R.id.datePicker);
        conyugeNameEditText = findViewById(R.id.conyugeNameEditText);
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

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(conyugeNameEditText.getText().toString().equalsIgnoreCase("")){
                    conyugeNameEditText.setError(getResources().getString(R.string.mandatory_error));
                    return;
                }
                try {
                    userDataJSON.put("nombre_conyuge", conyugeNameEditText.getText());
                    userDataJSON.put("fecha_casamiento", datePicker.getDayOfMonth() + "/" +
                            datePicker.getMonth() + "/" +
                            datePicker.getYear());
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                Context context = Register_6.this;
                Class destinationActivity = Register_7.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, userDataJSON.toString());
                startActivity(startChildActivityIntent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
