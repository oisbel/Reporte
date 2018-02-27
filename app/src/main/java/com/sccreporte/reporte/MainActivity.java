package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.PopupMenu;
import android.widget.TextView;

import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.utilities.DataUtils;

public class MainActivity extends AppCompatActivity {

    private ImageButton toolsBT;
    private ImageButton addReportBT;
    private TextView lastReportsTV;
    private TextView emailTV; // El email del usuario
    private ImageButton sccBT;
    private ImageButton radioBT;
    private ImageButton facebookBT;
    private User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolsBT = (ImageButton) findViewById(R.id.toolsButton);
        addReportBT = findViewById(R.id.imageButtonAddReport);
        lastReportsTV = (TextView)findViewById(R.id.lastReportsTextView);
        emailTV = (TextView)findViewById(R.id.emailTextView);
        sccBT = findViewById(R.id.sccImageButton);
        radioBT = findViewById(R.id.radioImageButton);
        facebookBT = findViewById(R.id.faceBookImageButton);

        // pop up the menu for logout
        toolsBT.setOnClickListener(new OnClickListener() {
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
                            // Remove user data from sharepreferences
                            SharedPreferences sharedPreferences =
                                    PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();

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
                openSCCWebPage(getString(R.string.facebook_url));
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
}
