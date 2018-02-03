package com.sccreporte.reporte;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sccreporte.reporte.data.ReportsData;

public class LastReportsActivity extends AppCompatActivity
    implements LastReportsAdapter.ListItemClickListener{

    //cantidad de elementos del recycler view
    private static final int NUM_LIST_ITEMS = 20;
    private LastReportsAdapter mAdapter;
    private RecyclerView mReportList;

    private Toast mToast;

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
        mAdapter = new LastReportsAdapter(new ReportsData().getData(),this);
        mReportList.setAdapter(mAdapter);

        backBT = (ImageButton)findViewById(R.id.backButton);
        backBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if(mToast!=null){
            mToast.cancel();
        }
        String tempToastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(this, tempToastMessage,Toast.LENGTH_LONG);

        mToast.show();
    }
}
