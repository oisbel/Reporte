package com.sccreporte.reporte;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.utilities.DataUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateReportActivity extends AppCompatActivity {

    private ImageButton backBT;

    private User mUser;
    private TextView nameTV;
    private TextView lugarTV;
    private TextView fechaTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);

        backBT = findViewById(R.id.backButton);
        nameTV = findViewById(R.id.nameTextView);
        lugarTV = findViewById(R.id.lugarTextView);
        fechaTV = findViewById(R.id.fechaTextView);

        // Set the user data
        mUser = DataUtils.loadUserData(this);
        lugarTV.setText(mUser.lugar);
        nameTV.setText(mUser.nombre);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        fechaTV.setText(dateFormat.format(date));

        // Back button click
        backBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // cargar los datos del usuario desde share preferences
        mUser = DataUtils.loadUserData(this);
        if(mUser.id!=-1){

        }
    }
}
