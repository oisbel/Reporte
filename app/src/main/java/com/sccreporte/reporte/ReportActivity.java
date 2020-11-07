package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.sccreporte.reporte.data.Report;
import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.databinding.ActivityReportBinding;
import com.sccreporte.reporte.utilities.DataUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class ReportActivity extends AppCompatActivity {

    // Create a data binding instance
    ActivityReportBinding mBinding;
    String mReportJSONString;
    private User mUser;
    Report report;
    int clickedItemIndex; // Representa el indice del reporte en el recycler view de reportsFragment

    ImageButton backBT;
    Button editBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Set the content view to the layout
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_report);

        // Load the user data
        mUser = DataUtils.loadUserData(this);

        // Create the report
        // Obtener el string pasado de la activity anterior
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            mReportJSONString = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            report = createReport(mReportJSONString);
        }

        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_INDEX)){
            clickedItemIndex = intentThatStartedThisActivity.getIntExtra(Intent.EXTRA_INDEX,0);
        }
        // Bind the data with the layout
        if (report != null){
            displayReportInfo(report);
        }

        // Back button click
        backBT = (ImageButton)findViewById(R.id.backButton);
        backBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Edit button
        editBT = findViewById(R.id.editButton);
        editBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = ReportActivity.this;
                Class destinationActivity = EditReportActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, report.reportJSON.toString());
                startChildActivityIntent.putExtra(Intent.EXTRA_INDEX, clickedItemIndex);
                startActivity(startChildActivityIntent);
                //finish();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        JSONObject jsonObject = DataUtils.loadReportDataJustEdited(getApplicationContext());
        if (jsonObject == null)
                return;
        report = new Report(jsonObject);
        // Bind the data with the layout
        if (report != null){
            displayReportInfo(report);
        }
    }

    /**
     * Crea el reporte a partir del String JSON
     * @param reportJSONString
     * @return
     */
    private Report createReport(String reportJSONString){
        Report result = null;
        try {
            JSONObject data = new JSONObject(reportJSONString);
            result = new Report(data);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return  result;
    }

    /**
     * Bind each attribute in the views to the corresponding data
     * @param report
     */
    private void displayReportInfo(Report report){
        mBinding.nameTextView.setText(String.valueOf(mUser.nombre));

        if(report.month > 0 && report.month <= 12) {
            // Mes al que corresponde el reporte
            int month = 11;// Correspondiente a Diciembre
            if(report.month > 1)
                month = report.month - 2;
            mBinding.fechaTextView.setText("LLenado " + report.day + "/" +
                    DataUtils.Months[report.month - 1] + "/" + report.year);

            mBinding.monthTextView.setText(DataUtils.Months[month]);
        }else{
            mBinding.fechaTextView.setText("-");
            mBinding.monthTextView.setText("-");
        }
        mBinding.lugarTextView.setText(String.valueOf(mUser.lugar));

        mBinding.avivamientosTextView.setText(String.valueOf(report.avivamientos));
        mBinding.ayunosTextView.setText(String.valueOf(report.ayunos));
        mBinding.bibliasTextView.setText(String.valueOf(report.biblias));
        mBinding.horasAyunosTextView.setText(String.valueOf(report.horas_ayunos));
        mBinding.cultosTextView.setText(String.valueOf(report.cultos));
        mBinding.devocionalesTextView.setText(String.valueOf(report.devocionales));
        mBinding.enfermosTextView.setText(String.valueOf(report.enfermos));
        mBinding.estudiosAsistidosTextView.setText(String.valueOf(report.estudios_asistidos));
        mBinding.estudiosEstablecidosTextView.setText(String.valueOf(report.estudios_establecidos));
        mBinding.estudiosRealizadosTextView.setText(String.valueOf(report.estudios_realizados));
        mBinding.hogaresTextView.setText(String.valueOf(report.hogares));
        mBinding.mensajerosTextView.setText(String.valueOf(report.mensajeros));
        mBinding.mensajesTextView.setText(String.valueOf(report.mensajes));
        mBinding.porcionesTextView.setText(String.valueOf(report.porciones));
        mBinding.sanidadesTextView.setText(String.valueOf(report.sanidades));
        mBinding.visitasTextView.setText(String.valueOf(report.visitas));
        mBinding.horasTrabajoTextView.setText(String.valueOf(report.horas_trabajo));
        mBinding.otrosTextView.setText(report.otros);
    }
}
