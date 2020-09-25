package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Register_7 extends AppCompatActivity {

    Button nextButton;
    ImageButton backButton;
    private Spinner gradoSpinner;
    // Objeto JSON con los datos del nuevo usuario a crear
    JSONObject userDataJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_7);

        nextButton = findViewById(R.id.nextButton);
        gradoSpinner = findViewById(R.id.gradoSpinner);
        backButton = findViewById(R.id.backIB);


        // Agregar un spinner para el grado
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.grado, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gradoSpinner.setAdapter(adapter);

        userDataJSON = new JSONObject();

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
            public void onClick(View v) {
                if(gradoSpinner.getSelectedItem().toString().equals(getResources().getString(R.string.grado_label))){
                    Toast.makeText(getApplicationContext(),R.string.mandatory_grado, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    userDataJSON.put("grado",gradoSpinner.getSelectedItem().toString());;
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                Context context = Register_7.this;
                Class destinationActivity = Register_8.class;
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
