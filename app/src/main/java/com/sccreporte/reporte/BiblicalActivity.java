package com.sccreporte.reporte;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.utilities.DataUtils;

public class BiblicalActivity extends AppCompatActivity {
    private TextView nameUserTV;
    private TextView lugarUserTV;

    private ImageButton backBT;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblical);

        nameUserTV = findViewById(R.id.nameUserTextView);
        lugarUserTV = findViewById(R.id.lugarUserTextView);

        // Back button click
        backBT = (ImageButton)findViewById(R.id.backButton);
        backBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // agregar el nombre y el lugar del usuario
        mUser = DataUtils.loadUserData(this);
        if(mUser.id == -1)
            finish();
        nameUserTV.setText(mUser.nombre);
        lugarUserTV.setText(mUser.lugar);
    }
}
