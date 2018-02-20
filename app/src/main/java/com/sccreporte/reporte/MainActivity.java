package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.utilities.DataUtils;

public class MainActivity extends AppCompatActivity {

    private TextView lastReportsTV;
    private TextView emailTV; // El email del usuario
    private User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastReportsTV = (TextView)findViewById(R.id.lastReportsTextView);
        emailTV = (TextView)findViewById(R.id.emailTextView);

        lastReportsTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Class destinationActivity = LastReportsActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startActivity(startChildActivityIntent);
            }
        });
        mUser = DataUtils.loadUserData(this);
        emailTV.setText(mUser.email);
    }
}
