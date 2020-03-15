package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

public class Register_8 extends AppCompatActivity {

    Button nextButton;
    ImageButton backButton;
    EditText ministerioEditText;
    // Objeto JSON con los datos del nuevo usuario a iniciar
    JSONObject userDataJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_8);

        nextButton = findViewById(R.id.nextButton);
        ministerioEditText = findViewById(R.id.ministerioEditText);
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
                try {
                    userDataJSON.put("ministerio", ministerioEditText.getText());
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                Context context = Register_8.this;
                Class destinationActivity = Register_9.class;
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
