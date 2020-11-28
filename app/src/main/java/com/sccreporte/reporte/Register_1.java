package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Register_1 extends AppCompatActivity {

    EditText nameEditText;
    TextView churchTextView;
    TextView cancelTextView;
    Button nextButton;
    // Objeto JSON con los datos del nuevo usuario a iniciar
    JSONObject userDataJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_1);

        nameEditText = findViewById(R.id.nameEditText);
        churchTextView = findViewById(R.id.churchTextView);
        cancelTextView = findViewById(R.id.cancelTextView);
        nextButton = findViewById(R.id.nextButton);

        userDataJSON = new JSONObject();

        // Obtener el string pasado de la activity anterior
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            String userDataJSONString = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            try {
                userDataJSON = new JSONObject(userDataJSONString);
                churchTextView.setText(userDataJSON.getString("lugar"));
                nameEditText.setText(userDataJSON.getString("nombre"));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    userDataJSON.put("nombre", nameEditText.getText());
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                Context context = Register_1.this;
                Class destinationActivity = Register1_1.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, userDataJSON.toString());
                startActivity(startChildActivityIntent);
            }
        });

    }
}
