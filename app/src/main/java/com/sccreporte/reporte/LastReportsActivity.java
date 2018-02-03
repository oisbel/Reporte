package com.sccreporte.reporte;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class LastReportsActivity extends AppCompatActivity {

    //cantidad de elementos del recycler view
    private static final int NUM_LIST_ITEMS = 100;
    private LastReportsAdapter mAdapter;
    private RecyclerView mReportList;
    ImageButton backBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_reports);

        // Wiring up RecycerView
        mReportList = (RecyclerView)findViewById(R.id.reportsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mReportList.setLayoutManager(layoutManager);
        mReportList.setHasFixedSize(true);
        mAdapter = new LastReportsAdapter(NUM_LIST_ITEMS);
        mReportList.setAdapter(mAdapter);

        backBT = (ImageButton)findViewById(R.id.backButton);
        backBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
