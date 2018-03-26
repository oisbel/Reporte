package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText nombreET;
    private EditText ministerioET;
    private EditText responsabilidadET;
    private EditText pastorET;
    private EditText numeroET;
    private EditText lugarET;
    private Button registerB;
    private Spinner gradoSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nombreET = (EditText) findViewById(R.id.nombreEditText);
        gradoSpinner = findViewById(R.id.gradoSpinner);
        ministerioET = (EditText) findViewById(R.id.ministerioEditText);
        responsabilidadET = (EditText) findViewById(R.id.responsabilidadEditText);
        pastorET = (EditText) findViewById(R.id.pastorEditText);
        lugarET = (EditText) findViewById(R.id.lugardEditText);
        numeroET = (EditText) findViewById(R.id.numeroEditText);
        registerB = (Button) findViewById(R.id.registerButton);

        // Agregar un spinner para el grado
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.grado, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gradoSpinner.setAdapter(adapter);

        registerB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkEntries()){
                    return;
                }

                Context context = RegisterActivity.this;
                Class destinationActivity = CreatePasswordActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);

                JSONObject dataUserJSON = createPersonalData();
                startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, dataUserJSON.toString());

                startActivity(startChildActivityIntent);
                finish();
            }
        });
    }

    /**
     * Crea el objeto JSON que contiene los datos del registro del usuario
     * @return
     */
    private JSONObject createPersonalData(){
        JSONObject obj = new JSONObject();
        try{
            obj.put("nombre",nombreET.getText());
            obj.put("grado",gradoSpinner.getSelectedItem().toString());
            obj.put("ministerio",ministerioET.getText());
            obj.put("responsabilidad",responsabilidadET.getText());
            obj.put("pastor",pastorET.getText());
            obj.put("numero",numeroET.getText().toString().isEmpty() ? "0" : numeroET.getText());
            obj.put("lugar",lugarET.getText());
        }catch (JSONException e){
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * Verifica que se hayan entrado los campos obligatorios(nombre
     * @return
     */
    private boolean checkEntries(){
        if(nombreET.getText().toString().equalsIgnoreCase("")){
            nombreET.setError(getResources().getString(R.string.mandatory_error));
            return false;
        }
        if(pastorET.getText().toString().equalsIgnoreCase("")){
            pastorET.setError(getResources().getString(R.string.mandatory_error));
            return false;
        }
        if(lugarET.getText().toString().equalsIgnoreCase("")){
            lugarET.setError(getResources().getString(R.string.mandatory_error));
            return false;
        }
        if(gradoSpinner.getSelectedItem().toString().equals(getResources().getString(R.string.grado_label))){
            Toast.makeText(this, R.string.mandatory_grado, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
