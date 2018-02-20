package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sccreporte.reporte.data.Report;
import com.sccreporte.reporte.data.ReportsData;
import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.utilities.DataUtils;
import com.sccreporte.reporte.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class LastReportsActivity extends AppCompatActivity
    implements LastReportsAdapter.ListItemClickListener{

    //cantidad de elementos del recycler view
    private static final int NUM_LIST_ITEMS = 20;
    private LastReportsAdapter mReportAdapter;
    private RecyclerView mReportList;
    private TextView mErrorMessageDisplay;
    // Create a ProgressBar variable to store a reference to the ProgressBar
    private ProgressBar mLoadingIndicator;

    private Toast mToast;

    ImageButton backBT;

    // Lista de los reportes
    List<Report> mReportsData;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_reports);

        // Back button click
        backBT = (ImageButton)findViewById(R.id.backButton);
        backBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Get a reference to the error TextView using findViewById
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        // Get a reference to the ProgressBar using findViewById
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        // Wiring up RecycerView
        mReportList = (RecyclerView)findViewById(R.id.reportsRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mReportList.setLayoutManager(layoutManager);
        mReportList.setHasFixedSize(true);
        mReportAdapter = new LastReportsAdapter(this);
        mReportList.setAdapter(mReportAdapter);

        mUser = DataUtils.loadUserData(this);
        /* Once all of our views are setup, we can load the reports data. */
        loadReportData();
    }

    private void loadReportData(){
        showReportRecyclerView();
        makeReportsQuery();
    }

    /**
     * Establece el id de usuario, y ejecuta el hilo para descargar sus reportes
     */
    private void makeReportsQuery(){
        new ReportsQueryTask().execute(Integer.toString(mUser.id));
    }

    // Helper methods
    /**
     * This method will make the RecyclerView data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showReportRecyclerView() {
        // First, make sure the error is invisible
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // Then, make sure the RecyclerView data is visible
        mReportList.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the Recycler View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        // First, hide the currently visible data
        mReportList.setVisibility(View.INVISIBLE);
        // Then, show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * To handle the click in the recycler view
     * @param clickedItemIndex The item position
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        // Ver el reporte seleccionado -  pasar a la otra activity usando Intent
        Context context = LastReportsActivity.this;
        Class destinationActivity = ReportActivity.class;
        Intent startChildActivityIntent = new Intent(context, destinationActivity);

        Report selectedReport = null;
        if(mReportsData != null && mReportsData.size() > 0){
            selectedReport = mReportsData.get(clickedItemIndex);
        }
        if(selectedReport != null) {
            // Pasar a la otra activity el String Json del reporte
            startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, selectedReport.reportJSON.toString());
        }

        if(mToast!=null){
            mToast.cancel();
        }
        String tempToastMessage = "Reporte # " + selectedReport.id;
        mToast = Toast.makeText(this, tempToastMessage,Toast.LENGTH_SHORT);
        mToast.show();

        startActivity(startChildActivityIntent);
    }

    public class ReportsQueryTask extends AsyncTask<String, Void, List<Report>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Report> doInBackground(String... params) {

            if(params.length == 0) return null;

            String user_id = params[0];
            URL reportsUrl = NetworkUtils.buildReportsUrl(user_id);
            // Para guardar la respuesta string en formato JSON
            String reportsJSONResult = null;
            try {
                reportsJSONResult = NetworkUtils.getReportsFromHttpUrl(reportsUrl, mUser.email, mUser.password);
            }catch (IOException e){
                e.printStackTrace();
            }
            ReportsData reportsData = null;
            try {
                reportsData= new ReportsData(reportsJSONResult);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(reportsData!=null) {
                return reportsData.getData();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Report> reportsDataResult) {
            // As soon as the loading is complete, hide the loading indicator
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if(reportsDataResult != null && reportsDataResult.size() > 0){
                showReportRecyclerView();
                // Guardo la referecia de la lista de reportes
                mReportsData = reportsDataResult;
                // Mando los datos al adaptador para que los muestre en el recyclerView
                mReportAdapter.setReportData(reportsDataResult);
            }else{
                showErrorMessage();
            }
        }
    }
}
