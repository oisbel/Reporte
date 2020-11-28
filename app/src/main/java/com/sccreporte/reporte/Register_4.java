package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

public class Register_4 extends AppCompatActivity {

    Button nextButton;
    ImageButton backButton;
    EditText addressEditText;
    TextView messageTV;
    // Objeto JSON con los datos del nuevo usuario a iniciar
    JSONObject userDataJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_4);

        addressEditText = findViewById(R.id.addressEditText);
        nextButton = findViewById(R.id.nextButton);
        userDataJSON = new JSONObject();
        backButton = findViewById(R.id.backIB);
        messageTV = findViewById(R.id.messageTV);

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
                if(addressEditText.getText().length() == 0)
                {
                    messageTV.setVisibility(View.VISIBLE);
                    return;
                }
                try {
                    userDataJSON.put("direccion", addressEditText.getText());
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                Context context = Register_4.this;
                Class destinationActivity = Register_5.class;
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
