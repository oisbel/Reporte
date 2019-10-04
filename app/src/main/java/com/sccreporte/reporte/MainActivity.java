package com.sccreporte.reporte;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNav;

    // Para guardar todos los fragment en memoria
    final Fragment homeFragment = new HomeFragment();
    final Fragment reportsFragment = new ReportsFragment();
    final Fragment biblicalsFragment = new BiblicalsFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment activeFragment = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNav = findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        fragmentManager.beginTransaction().add(R.id.container, biblicalsFragment,"3")
                .hide(biblicalsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container, reportsFragment,"2")
                .hide(reportsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container, homeFragment,"1")
                .commit();
    }

    private void selectFragment(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                activeFragment = homeFragment;
                break;
            case R.id.navigation_reports:
                fragmentManager.beginTransaction().hide(activeFragment).show(reportsFragment).commit();
                activeFragment = reportsFragment;
                break;
            case R.id.navigation_biblicals:
                fragmentManager.beginTransaction().hide(activeFragment).show(biblicalsFragment).commit();
                activeFragment = biblicalsFragment;
                break;
        }
    }

    @Override
    public void onBackPressed() {}

}
