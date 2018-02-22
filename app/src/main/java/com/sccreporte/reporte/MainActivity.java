package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private TextView lastReportsTV;
    private TextView emailTV; // El email del usuario
    private User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolsBT = (ImageButton) findViewById(R.id.toolsButton);
        lastReportsTV = (TextView)findViewById(R.id.lastReportsTextView);
        emailTV = (TextView)findViewById(R.id.emailTextView);

        // pop up the menu for logout
        toolsBT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                // Agregarle estilo al popup menu
                ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(
                        MainActivity.this, R.style.popupMenuStyle);

                PopupMenu popup = new PopupMenu(contextThemeWrapper,view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.main, popup.getMenu());
                popup.show();

                // set the click for the items on the popout menu
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

        // establecer el click listener para los ultimos reportes
        lastReportsTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Class destinationActivity = LastReportsActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startActivity(startChildActivityIntent);
            }
        });
        // cargar los datos del usuario desde share preferences
        mUser = DataUtils.loadUserData(this);
        emailTV.setText(mUser.email);
    }

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    //    getMenuInflater().inflate(R.menu.main,menu);
    //    return true;
    //}
}
