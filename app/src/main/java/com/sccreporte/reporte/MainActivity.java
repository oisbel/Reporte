package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.sccreporte.reporte.data.Report;
import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.sync.ReminderTasks;
import com.sccreporte.reporte.sync.ReminderUtilities;
import com.sccreporte.reporte.utilities.DataUtils;
import com.sccreporte.reporte.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageButton helpBT;
    private ImageButton imageUserBT;
    private ImageButton addReportBT;
    private ImageButton biblicalBT;
    private TextView lastReportsTV;
    private TextView userDataTV;
    private TextView emailTV; // El email del usuario
    private ImageButton sccBT;
    private ImageButton radioBT;
    private ImageButton facebookBT;
    private User mUser;

    private AsyncTask mBackgroundTask; // para llamar a ItIsTimeToNewReport

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helpBT = (ImageButton) findViewById(R.id.helpButton);
        imageUserBT = findViewById(R.id.imageUserButton);
        addReportBT = findViewById(R.id.imageButtonAddReport);
        biblicalBT = findViewById(R.id.imageButtonBiblical);
        lastReportsTV = (TextView)findViewById(R.id.lastReportsTextView);
        userDataTV = findViewById(R.id.myDataTextView);
        emailTV = (TextView)findViewById(R.id.emailTextView);
        sccBT = findViewById(R.id.sccImageButton);
        radioBT = findViewById(R.id.radioImageButton);
        facebookBT = findViewById(R.id.faceBookImageButton);

        // pop up the menu for help
        helpBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),HelpActivity.class);
                startActivity(intent);

            }
        });

        // establecer el click listener para ultimos reportes
        lastReportsTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Class destinationActivity = LastReportsActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startActivity(startChildActivityIntent);
            }
        });

        // establecer el click listener para mis datos
        userDataTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Class destinationActivity = UserDataActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startActivity(startChildActivityIntent);
            }
        });
        imageUserBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Class destinationActivity = UserDataActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startActivity(startChildActivityIntent);
            }
        });

        // establecer el click para ir a los estudios biblicos
        biblicalBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Class destinationActivity = BiblicalActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startActivity(startChildActivityIntent);
            }
        });

        // establecer el click para crear reporte
        addReportBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrEditReport();
            }
        });

        // Abrir el sitio web de scc
        sccBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openSCCWebPage(getString(R.string.scc_website_url));
            }
        });

        // Abrir el sitio web de la radio
        radioBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openSCCWebPage(getString(R.string.radio_website_url));
            }
        });

        // Abrir el facebook
        facebookBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openSCCWebPage(getFacebookPageURL());
            }
        });

        // cargar los datos del usuario desde share preferences
        mUser = DataUtils.loadUserData(this);
        if(mUser.email!=""){
            emailTV.setText(mUser.email);
        }

        // Schedule the create report reminder
        ReminderUtilities.scheduleCreateReportReminder(this);


    }

    /**
     * Abre create report activity
     */
    private void openCreateReport() {
        Context context = MainActivity.this;
        Class destinationActivity = CreateReportActivity.class;
        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        startActivity(startChildActivityIntent);
    }
    /**
     * Abre edit report activity
     */
    private void openEditReport(JSONObject report) {
        Context context = MainActivity.this;
        Class destinationActivity = EditReportActivity.class;
        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, report.toString());
        startActivity(startChildActivityIntent);
    }

    /**
     * Se encarga de preguntar si es tiempo de crear un nuevo reporte y abrir create report activity
     * o simplemente abrir la activity para editar el ultimo reporte
     */
    private void createOrEditReport(){
        mBackgroundTask = new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... voids) {
                URL itIsTimeUrl = NetworkUtils.buildItIsTimeUrl();
                String lastReportJSONResult;
                JSONObject result = null;
                try{
                    lastReportJSONResult = NetworkUtils.getItIsTimeFromHttpUrl(
                            itIsTimeUrl, mUser.email, mUser.password);
                }catch (IOException e){
                    e.printStackTrace();
                    return result;
                }
                try {
                    result = new JSONObject(lastReportJSONResult);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                if(jsonObject != null){
                    if(jsonObject.has("id")){
                        // solo editar
                        openEditReport(jsonObject);
                    }
                    else{
                        //crear nuevo
                        openCreateReport();
                    }
                }
            }
        }.execute();
    }

    private void openSCCWebPage(String url){
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    /**
     * Devuelve el valor url en dependencia de si facebook app esta
     * instalado o no (para que se abra el browser o la app)
     * @return
     */
    public String getFacebookPageURL(){
        if(appInstalled("com.facebook.katana")){
            return "fb://page/" + getString(R.string.facebook_page_id);
        }else {
            return getString(R.string.facebook_url);
        }
    }

    /**
     * To know if facebook app is installed
     * @param uri
     * @return
     */
    private boolean appInstalled(String uri)
    {
        PackageManager packageManager = getPackageManager();
        try
        {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            boolean activated =  packageManager.getApplicationInfo(uri, 0).enabled;
            return activated;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
