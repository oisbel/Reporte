package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.utilities.DataUtils;

public class MainActivity extends AppCompatActivity {

    private ImageButton menuBT;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuBT = (ImageButton) findViewById(R.id.menuButton);
        imageUserBT = findViewById(R.id.imageUserButton);
        addReportBT = findViewById(R.id.imageButtonAddReport);
        biblicalBT = findViewById(R.id.imageButtonBiblical);
        lastReportsTV = (TextView)findViewById(R.id.lastReportsTextView);
        userDataTV = findViewById(R.id.myDataTextView);
        emailTV = (TextView)findViewById(R.id.emailTextView);
        sccBT = findViewById(R.id.sccImageButton);
        radioBT = findViewById(R.id.radioImageButton);
        facebookBT = findViewById(R.id.faceBookImageButton);

        // pop up the menu for logout
        menuBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                // Agregarle estilo al contenido del popup menu
                ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(
                        MainActivity.this, R.style.wraperPopupMenuStyle);

                PopupMenu popup = new PopupMenu(contextThemeWrapper, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.main, popup.getMenu());
                popup.show();

                // set the click for the items on the popout menu
                // logout
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.action_logout){
                            // Clear user data from sharepreferences
                            DataUtils.clearUserData(MainActivity.this);

                            // open splash activity
                            Intent intent = new Intent(getApplicationContext(),SplashActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        return true;
                    }
                });
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
                Context context = MainActivity.this;
                Class destinationActivity = CreateReportActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startActivity(startChildActivityIntent);
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
