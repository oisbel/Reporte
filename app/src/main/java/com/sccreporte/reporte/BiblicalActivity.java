package com.sccreporte.reporte;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sccreporte.reporte.data.Biblical;
import com.sccreporte.reporte.data.BiblicalsData;
import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.utilities.DataUtils;
import com.sccreporte.reporte.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class BiblicalActivity extends AppCompatActivity
    implements BiblicalsAdapter.ListItemClickListener{

    private TextView nameUserTV;
    private TextView lugarUserTV;

    //cantidad de elementos del recycler view
    private static final int NUM_LIST_ITEMS = 20;
    private BiblicalsAdapter mBiblicalAdapter;
    private RecyclerView mBiblicalList;
    private TextView mErrorMessageDisplay;
    private TextView mEmptyMessageDisplay;
    // Create a ProgressBar variable to store a reference to the ProgressBar
    private ProgressBar mLoadingIndicator;

    private Toast mToast;

    private ImageButton backBT;

    // Lista de los estudios biblicos
    List<Biblical> mBiblicalsData;

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

        // Get a reference to the error TextView using findViewById
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        // Get a reference to the empty TextView using findViewById
        mEmptyMessageDisplay = findViewById(R.id.emptyMessageTextView);

        // Get a reference to the ProgressBar using findViewById
        mLoadingIndicator =  findViewById(R.id.pb_loading_indicator);

        // Wiring up RecycerView
        mBiblicalList = findViewById(R.id.biblicalRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBiblicalList.setLayoutManager(layoutManager);
        mBiblicalList.setHasFixedSize(true);
        mBiblicalAdapter = new BiblicalsAdapter(this);
        mBiblicalList.setAdapter(mBiblicalAdapter);

        // agregar el nombre y el lugar del usuario
        mUser = DataUtils.loadUserData(this);
        if(mUser.id == -1)
            finish();
        nameUserTV.setText(mUser.nombre);
        lugarUserTV.setText(mUser.lugar);

         /* Once all of our views are setup, we can load the reports data. */
         loadBiblicalData();
    }

    private void loadBiblicalData()
    {
        showBiblicalRecyclerView();
        makeBiblicalsQuery();
    }

    /**
     * Establece el id de usuario, y ejecuta el hilo para descargar sus reportes
     */
    private void makeBiblicalsQuery(){
        new BiblicalsQueryTask().execute(Integer.toString(mUser.id));
    }

    // Helper methods
    /**
     * This method will make the RecyclerView data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showBiblicalRecyclerView() {
        // First, make sure the error is invisible
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // Then, make sure the RecyclerView data is visible
        mBiblicalList.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the Recycler View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        // First, hide the currently visible data
        mBiblicalList.setVisibility(View.INVISIBLE);
        // Then, show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the empty message visible and hide the Recycler View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showEmptyMessage() {
        // First, hide the currently visible data
        mBiblicalList.setVisibility(View.INVISIBLE);
        // Then, show the error
        mEmptyMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
    }

    public class BiblicalsQueryTask extends AsyncTask<String, Void, List<Biblical>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Biblical> doInBackground(String... params) {
            if(params.length == 0) return null;

            String user_id = params[0];
            URL biblicalUrl = NetworkUtils.buildBiblicalsUrl(user_id);
            // Para guardar la respuesta string en formato JSON
            String biblicalsJSONResult = null;
            try {
                biblicalsJSONResult = NetworkUtils.getBiblicalsFromHttpUrl(biblicalUrl, mUser.email, mUser.password);
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }
            BiblicalsData biblicalsData = null;

            try {
                biblicalsData =  new BiblicalsData(biblicalsJSONResult);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (biblicalsData != null){
                return biblicalsData.getData();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Biblical> biblicals) {
            // As soon as the loading is complete, hide the loading indicator
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if(biblicals != null && biblicals.size() > 0){
                showBiblicalRecyclerView();
                // Guardo la referecia de la lista de reportes
                mBiblicalsData = biblicals;
                // Mando los datos al adaptador para que los muestre en el recyclerView
                mBiblicalAdapter.setBiblicalData(biblicals);
            }else if (biblicals != null && biblicals.size() == 0){
                showEmptyMessage();
            }
            else{
                showErrorMessage();
            }
        }
    }
}
