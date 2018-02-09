package com.sccreporte.reporte;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sccreporte.reporte.databinding.ActivityReportBinding;

public class ReportActivity extends AppCompatActivity {

    // Create a data binding instance
    ActivityReportBinding mBinding;

    ImageButton backBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Back button click
        backBT = (ImageButton)findViewById(R.id.backButton);
        backBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
