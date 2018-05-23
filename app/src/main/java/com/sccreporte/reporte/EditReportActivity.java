package com.sccreporte.reporte;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.sccreporte.reporte.data.Report;
import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.databinding.ActivityEditReportBinding;
import com.sccreporte.reporte.utilities.DataUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class EditReportActivity extends AppCompatActivity {

    // Create a data binding instance
    ActivityEditReportBinding mBinding;
    String mReportJSONString;
    private User mUser;
    Report report;

    ImageButton backBT;
    ImageButton doneBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_report);

        // Set the content view to the layout
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_report);

        // Load the user data
        mUser = DataUtils.loadUserData(this);

        // Create the report
        // Obtener el string pasado de la activity anterior
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            mReportJSONString = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            report = createReport(mReportJSONString);
        }
        // Bind the data with the layout
        if (report != null){
            displayReportInfo(report);
        }

        // Back button click
        backBT = findViewById(R.id.backButton);
        backBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
        mBinding.lugarTextView.setText(String.valueOf(mUser.lugar));
        mBinding.nameTextView.setText(String.valueOf(mUser.nombre));

        mBinding.fechaTextView.setText(String.valueOf(report.day) + "/" +
                DataUtils.Months[report.month - 1] + "/" +
                String.valueOf(report.year));
        if(report.avivamientos != 0){
            mBinding.avivamientosEditText.setText(String.valueOf(report.avivamientos));
        }
        if(report.ayunos != 0){
            mBinding.ayunosEditText.setText(String.valueOf(report.ayunos));
        }
        if(report.biblias != 0){
            mBinding.bibliasEditText.setText(String.valueOf(report.biblias));
        }
        if(report.horas_ayunos != 0){
            mBinding.horasAyunosEditText.setText(String.valueOf(report.horas_ayunos));
        }
        if(report.cultos != 0){
            mBinding.cultosEditText.setText(String.valueOf(report.cultos));
        }
        if(report.devocionales != 0){
            mBinding.devocionalesEditText.setText(String.valueOf(report.devocionales));
        }
        if(report.enfermos != 0){
            mBinding.enfermosEditText.setText(String.valueOf(report.enfermos));
        }
        if(report.estudios_asistidos != 0){
            mBinding.asistidsosEEditText.setText(String.valueOf(report.estudios_asistidos));
        }
        if(report.estudios_establecidos != 0){
            mBinding.establecidosEEditText.setText(String.valueOf(report.estudios_establecidos));
        }
        if(report.estudios_realizados != 0){
            mBinding.realizadosEEditText.setText(String.valueOf(report.estudios_realizados));
        }
        if(report.hogares != 0){
            mBinding.hogaresEditText.setText(String.valueOf(report.hogares));
        }
        if(report.mensajeros != 0){
            mBinding.mensajerosEditText.setText(String.valueOf(report.mensajeros));
        }
        if(report.mensajes != 0){
            mBinding.mensajesEditText.setText(String.valueOf(report.mensajes));
        }
        if(report.porciones != 0){
            mBinding.porcionesEditText.setText(String.valueOf(report.porciones));
        }
        if(report.sanidades != 0){
            mBinding.sanidadesEditText.setText(String.valueOf(report.sanidades));
        }
        if(report.visitas != 0){
            mBinding.visitasEditTExt.setText(String.valueOf(report.visitas));
        }
        if(report.horas_trabajo != 0){
            mBinding.horasTrabajoEditText.setText(String.valueOf(report.horas_trabajo));
        }
        mBinding.otrosEditText.setText(report.otros);
    }
}
