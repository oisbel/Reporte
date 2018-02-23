package com.sccreporte.reporte;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sccreporte.reporte.data.Report;
import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.databinding.ActivityCreateReportBinding;
import com.sccreporte.reporte.utilities.DataUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateReportActivity extends AppCompatActivity {

    ActivityCreateReportBinding mBinding;

    private ImageButton backBT;

    private User mUser;
    private Report mReport;
    private TextView nameTV;
    private TextView lugarTV;
    private TextView fechaTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);

        // Set the content view to the layout
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_report);

        backBT = findViewById(R.id.backButton);
        nameTV = findViewById(R.id.nameTextView);
        lugarTV = findViewById(R.id.lugarTextView);
        fechaTV = findViewById(R.id.fechaTextView);

        // Set the user data
        mUser = DataUtils.loadUserData(this);
        lugarTV.setText(mUser.lugar);
        nameTV.setText(mUser.nombre);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        fechaTV.setText(dateFormat.format(date));

        // Back button click
        backBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // cargar los datos del usuario desde share preferences
        mUser = DataUtils.loadUserData(this);
        if(mUser.id!=-1){

        }

        // cargar los datos del reporte que no se ha terminado
        mReport = DataUtils.loadReportData(this);
        if(mReport != null){
            displayReportInfo(mReport);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SaveTempReportInfo();
    }

    /**
     * Bind each attribute in the views to the corresponding data
     * @param report
     */
    private void displayReportInfo(Report report){
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
        mBinding.otrosEditText.setText(report.otros);
    }

    /**
     * Guarda en shared preferences los datos del reporte que no se ha terminado
     */
    private void SaveTempReportInfo(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("avivamientos",
                    mBinding.avivamientosEditText.getText().length() != 0 ?
                            mBinding.avivamientosEditText.getText() : 0);
            jsonObject.put("ayunos",mBinding.ayunosEditText.getText().length() != 0 ?
                    mBinding.ayunosEditText.getText() : 0);
            jsonObject.put("biblias",mBinding.bibliasEditText.getText().length() != 0 ?
                    mBinding.bibliasEditText.getText() : 0);
            jsonObject.put("cultos",mBinding.cultosEditText.getText().length() != 0 ?
                    mBinding.cultosEditText.getText() : 0);
            jsonObject.put("devocionales",mBinding.devocionalesEditText.getText().length() != 0 ?
                    mBinding.devocionalesEditText.getText() : 0);
            jsonObject.put("enfermos",mBinding.enfermosEditText.getText().length() != 0 ?
                    mBinding.enfermosEditText.getText(): 0);
            jsonObject.put("estudios_asistidos",mBinding.asistidsosEEditText.getText().length() != 0 ?
                    mBinding.asistidsosEEditText.getText() : 0);
            jsonObject.put("estudios_establecidos",mBinding.establecidosEEditText.getText().length() != 0 ?
                    mBinding.establecidosEEditText.getText() : 0);
            jsonObject.put("estudios_realizados",mBinding.realizadosEEditText.getText().length() != 0 ?
                    mBinding.realizadosEEditText.getText() : 0);
            jsonObject.put("hogares",mBinding.hogaresEditText.getText().length() != 0 ?
                    mBinding.hogaresEditText.getText() : 0);
            jsonObject.put("horas_ayunos",mBinding.horasAyunosEditText.getText().length() != 0 ?
                    mBinding.horasAyunosEditText.getText() : 0);
            jsonObject.put("mensajeros",mBinding.mensajerosEditText.getText().length() != 0 ?
                    mBinding.mensajerosEditText.getText() : 0);
            jsonObject.put("mensajes",mBinding.mensajesEditText.getText().length() != 0 ?
                    mBinding.mensajesEditText.getText() : 0);
            jsonObject.put("otros",mBinding.otrosEditText.getText());
            jsonObject.put("porciones",mBinding.porcionesEditText.getText().length() != 0 ?
                    mBinding.porcionesEditText.getText() : 0);
            jsonObject.put("sanidades",mBinding.sanidadesEditText.getText().length() != 0 ?
                    mBinding.sanidadesEditText.getText() : 0);
            jsonObject.put("visitas",mBinding.visitasEditTExt.getText().length() != 0 ?
                    mBinding.visitasEditTExt.getText() : 0);

            editor.putString("reportInfo",jsonObject.toString());
            editor.apply();

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
