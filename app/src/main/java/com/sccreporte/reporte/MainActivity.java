package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView lastReportsTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastReportsTV = (TextView)findViewById(R.id.lastReportsTextView);

        lastReportsTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = MainActivity.this;
                Class destinationActivity = LastReportsActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startActivity(startChildActivityIntent);
            }
        });
    }
}
