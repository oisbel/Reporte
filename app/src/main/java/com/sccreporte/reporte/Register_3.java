package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;;

import org.json.JSONException;
import org.json.JSONObject;

public class Register_3 extends AppCompatActivity {

    Button nextButton;
    ImageButton backButton;
    DatePicker datePicker;
    // Objeto JSON con los datos del nuevo usuario a iniciar
    JSONObject userDataJSON;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_3);

        nextButton = findViewById(R.id.nextButton);
        datePicker = findViewById(R.id.datePicker);
        userDataJSON = new JSONObject();
        backButton = findViewById(R.id.backIB);

        datePicker.updateDate(1988,3,4);

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
                    userDataJSON.put("year", datePicker.getYear());
                    userDataJSON.put("month", datePicker.getMonth());
                    userDataJSON.put("day", datePicker.getDayOfMonth());
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                Context context = Register_3.this;
                Class destinationActivity = Register_4.class;
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
