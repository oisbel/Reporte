package com.sccreporte.reporte;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;


public class TutorialActivity extends AppCompatActivity {

    private TextView explicadosTV;
    private ImageButton closeBT;

    ViewPager viewPager;
    SpringDotsIndicator circleIndicator3;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        explicadosTV = findViewById(R.id.explicadosTextView);
        closeBT = findViewById(R.id.closeButton);

        viewPager = findViewById(R.id.view_pager);
        circleIndicator3 = findViewById(R.id.indicator);

        viewPagerAdapter = new ViewPagerAdapter(this);

        Integer[] images = {R.drawable.tutorial_create_report, R.drawable.tutorial_edit_report,
                R.drawable.tutorial_edit_biblical, R.drawable.tutorial_delete_biblical};
        String[] titles = {getResources().getString(R.string.topic_create_report),
                getResources().getString(R.string.topic_edit_report),
                getResources().getString(R.string.topic_add_biblical),
                getResources().getString(R.string.topic_delete_biblical)};
        String[] details = {getResources().getString(R.string.details_create_report_help),
                getResources().getString(R.string.details_edit_report_help),
                getResources().getString(R.string.details_add_biblical_help),
                getResources().getString(R.string.details_delete_biblical_help)};
        viewPagerAdapter.FillData(images,titles,details);

        viewPager.setAdapter(viewPagerAdapter);
        circleIndicator3.setViewPager(viewPager);

        //Click para abrir explicados activity
        explicadosTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class destinationActivity = ExplicadosActivity.class;
                Intent startChildActivityIntent = new Intent(getApplicationContext(), destinationActivity);
                startActivity(startChildActivityIntent);
            }
        });

        closeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
