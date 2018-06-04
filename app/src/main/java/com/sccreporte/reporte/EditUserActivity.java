package com.sccreporte.reporte;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.utilities.DataUtils;

public class EditUserActivity extends AppCompatActivity {

    ImageButton closeBT;
    User mUser;
    TextView nameTV;
    TextView lugarTV;
    private Spinner gradoSpinner;
    EditText ministerioET;
    EditText responsabilidadET;
    EditText pastorET;
    EditText numeroET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        closeBT = findViewById(R.id.closeButton);
        nameTV = findViewById(R.id.nameTextView);
        lugarTV = findViewById(R.id.lugarTextView);
        gradoSpinner = findViewById(R.id.gradoSpinner);
        ministerioET = findViewById(R.id.ministerioEditText);
        responsabilidadET = findViewById(R.id.responsabilidadEditText);
        pastorET = findViewById(R.id.pastorEditText);
        numeroET = findViewById(R.id.numeroEditText);
        mUser = DataUtils.loadUserData(this);
        if(mUser.id == -1)
            finish();
        nameTV.setText(mUser.nombre);
        lugarTV.setText(mUser.lugar);

        // Agregar un spinner para el grado
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.grado, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gradoSpinner.setAdapter(adapter);

        int spinnerPosition = adapter.getPosition(mUser.grado);
        gradoSpinner.setSelection(spinnerPosition);


        ministerioET.setText(mUser.ministerio);
        responsabilidadET.setText(mUser.responsabilidad);
        pastorET.setText(mUser.pastor);
        numeroET.setText(mUser.numero);

        closeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
