package com.sccreporte.reporte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sccreporte.reporte.data.Report;
import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.databinding.ActivityCreateReportBinding;
import com.sccreporte.reporte.utilities.DataUtils;
import com.sccreporte.reporte.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateReportActivity extends AppCompatActivity {

    ActivityCreateReportBinding mBinding;

    private ImageButton backBT;
    private ImageButton doneBT;
    private ProgressBar mLoadingIndicator;
    private ScrollView reportSV;

    private User mUser;
    private Report mReport;
    private TextView nameTV;
    private TextView lugarTV;
    private TextView fechaTV;

    private Date dateToday;
    private int year;
    private int month;
    private int day;

    // Indica si el reporte se ha salvado al servidor y no se necesita una salva local temporal
    private boolean reportSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);

        // Set the content view to the layout
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_report);

        backBT = findViewById(R.id.backButton);
        doneBT = findViewById(R.id.doneButton);
        mLoadingIndicator = findViewById(R.id.loadingIndicatorProgressBar);
        reportSV = findViewById(R.id.reportScrollView);

        nameTV = findViewById(R.id.nameTextView);
        lugarTV = findViewById(R.id.lugarTextView);
        fechaTV = findViewById(R.id.fechaTextView);

        // Set the user data
        mUser = DataUtils.loadUserData(this);
        lugarTV.setText(mUser.lugar);
        nameTV.setText(mUser.nombre);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateToday = new Date();
        String date = dateFormat.format(dateToday);

        year = Integer.parseInt(date.substring(6));
        month = Integer.parseInt(date.substring(3,5));
        day = Integer.parseInt(date.substring(0,2));

        fechaTV.setText(date);

        // Back button click
        backBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // cargar los datos del usuario desde share preferences
        mUser = DataUtils.loadUserData(this);

        // cargar los datos del reporte que no se ha terminado
        mReport = DataUtils.loadReportData(this);
        if(mReport != null){
            displayReportInfo(mReport);
        }

        // click para guardar el reporte en el servidor
        doneBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allFieldEmpty())
                    return;
                if(toLongInt(3))
                    return;
                new CreateReportQueryTask().execute(makeReportData());
            }
        });
    }

    private void showLoading(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
        reportSV.setAlpha(.5f);
    }

    private void hideLoading(){
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        reportSV.setAlpha(0);
    }

    private void ShowErrorMessage(){
        Toast toast = Toast.makeText(this, R.string.create_report_error_message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void ShowSuccessMessage(){
        Toast toast = Toast.makeText(this, R.string.create_report_success_message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!reportSaved){
            SaveTempReportInfo();
            reportSaved = false;
        }
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                CreateReportActivity.this);
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

    /**
     * Crea un objeto object con los valores del reporte que se quiere mandar
     * @return
     */
    private JSONObject makeReportData(){
        JSONObject result = new JSONObject();
        String temp = "";
        try {

            result.put("year", year);
            result.put("month", month);
            result.put("day", day);

            temp = mBinding.avivamientosEditText.getText().toString();
            result.put("avivamientos", temp.isEmpty() ? "0" : temp);

            temp = mBinding.bibliasEditText.getText().toString();
            result.put("biblias", temp.isEmpty() ? "0" : temp);

            temp = mBinding.ayunosEditText.getText().toString();
            result.put("ayunos", temp.isEmpty() ? "0" : temp);

            temp = mBinding.horasAyunosEditText.getText().toString();
            result.put("horas_ayunos", temp.isEmpty() ? "0" : temp);

            temp = mBinding.cultosEditText.getText().toString();
            result.put("cultos", temp.isEmpty() ? "0" : temp);

            temp = mBinding.devocionalesEditText.getText().toString();
            result.put("devocionales", temp.isEmpty() ? "0" : temp);

            temp = mBinding.enfermosEditText.getText().toString();
            result.put("enfermos", temp.isEmpty() ? "0" : temp);

            temp = mBinding.hogaresEditText.getText().toString();
            result.put("hogares", temp.isEmpty() ? "0" : temp);

            temp = mBinding.asistidsosEEditText.getText().toString();
            result.put("estudios_asistidos", temp.isEmpty() ? "0" : temp);

            temp = mBinding.establecidosEEditText.getText().toString();
            result.put("estudios_establecidos", temp.isEmpty() ? "0" : temp);

            temp = mBinding.realizadosEEditText.getText().toString();
            result.put("estudios_realizados", temp.isEmpty() ? "0" : temp);

            temp = mBinding.mensajerosEditText.getText().toString();
            result.put("mensajeros", temp.isEmpty() ? "0" : temp);

            temp = mBinding.mensajesEditText.getText().toString();
            result.put("mensajes", temp.isEmpty() ? "0" : temp);

            temp = mBinding.porcionesEditText.getText().toString();
            result.put("porciones", temp.isEmpty() ? "0" : temp);

            temp = mBinding.sanidadesEditText.getText().toString();
            result.put("sanidades", temp.isEmpty() ? "0" : temp);

            temp = mBinding.visitasEditTExt.getText().toString();
            result.put("visitas", temp.isEmpty() ? "0" : temp);

            result.put("otros", mBinding.otrosEditText.getText());


        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * Check for all the field of report to see if they are empty
     * @return true if not data enter
     */
    private boolean allFieldEmpty(){
        if(!mBinding.avivamientosEditText.getText().toString().isEmpty())
            return false;
        if(!mBinding.bibliasEditText.getText().toString().isEmpty())
            return false;
        if(!mBinding.ayunosEditText.getText().toString().isEmpty())
            return false;
        if(!mBinding.horasAyunosEditText.getText().toString().isEmpty())
            return false;
        if(!mBinding.cultosEditText.getText().toString().isEmpty())
            return false;
        if(!mBinding.devocionalesEditText.getText().toString().isEmpty())
            return false;
        if(!mBinding.enfermosEditText.getText().toString().isEmpty())
            return false;
        if(!mBinding.hogaresEditText.getText().toString().isEmpty())
            return false;
        if(!mBinding.asistidsosEEditText.getText().toString().isEmpty())
            return false;
        if(!mBinding.establecidosEEditText.getText().toString().isEmpty())
            return false;
        if(!mBinding.realizadosEEditText.getText().toString().isEmpty())
            return false;
        if(!mBinding.mensajerosEditText.getText().toString().isEmpty())
            return false;
        if(!mBinding.mensajesEditText.getText().toString().isEmpty())
            return false;
        if(!mBinding.porcionesEditText.getText().toString().isEmpty())
            return false;
        if(!mBinding.sanidadesEditText.getText().toString().isEmpty())
            return false;
        if(!mBinding.visitasEditTExt.getText().toString().isEmpty())
            return false;
        if(!mBinding.otrosEditText.getText().toString().isEmpty())
            return false;
        return true;
    }

    /**
     * heck for all the field of report to see if they have to long integer
     * @param max
     * @return true if any big big int
     */
    private boolean toLongInt(int max){
        if(mBinding.avivamientosEditText.getText().toString().length()>max)
            return true;
        if(mBinding.bibliasEditText.getText().toString().length()>max)
            return true;
        if(mBinding.ayunosEditText.getText().toString().length()>max)
            return true;
        if(mBinding.horasAyunosEditText.getText().toString().length()>max)
            return true;
        if(mBinding.cultosEditText.getText().toString().length()>max)
            return true;
        if(mBinding.devocionalesEditText.getText().toString().length()>max)
            return true;
        if(mBinding.enfermosEditText.getText().toString().length()>max)
            return true;
        if(mBinding.hogaresEditText.getText().toString().length()>max)
            return true;
        if(mBinding.asistidsosEEditText.getText().toString().length()>max)
            return true;
        if(mBinding.establecidosEEditText.getText().toString().length()>max)
            return true;
        if(mBinding.realizadosEEditText.getText().toString().length()>max)
            return true;
        if(mBinding.mensajerosEditText.getText().toString().length()>max)
            return true;
        if(mBinding.mensajesEditText.getText().toString().length()>max)
            return true;
        if(mBinding.porcionesEditText.getText().toString().length()>max)
            return true;
        if(mBinding.sanidadesEditText.getText().toString().length()>max)
            return true;
        if(mBinding.visitasEditTExt.getText().toString().length()>max)
            return true;
        return false;
    }

    public class CreateReportQueryTask extends AsyncTask<JSONObject, Void, JSONObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... jsonObjects) {
            if(jsonObjects == null || jsonObjects.length == 0) return null;
            JSONObject jsonData = jsonObjects[0];
            URL createReportUrl = NetworkUtils.buildCreateReportUrl();
            String createReportJSONResult = null;
            JSONObject result = null;
            try {
                createReportJSONResult = NetworkUtils.geCreateReportFromHttpUrl(
                        createReportUrl, jsonData, mUser.email, mUser.password);
            }catch (IOException e){
                e.printStackTrace();
                return result;
            }
            try {
                result = new JSONObject(createReportJSONResult);
            }catch (JSONException e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            hideLoading();
            if(jsonObject != null){
                if(jsonObject.has("report")){
                    //success
                    ShowSuccessMessage();

                    reportSaved = true;

                    // Remove report data from sharepreferences
                    DataUtils.clearReportData(CreateReportActivity.this);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }else{
                ShowErrorMessage();
            }
        }
    }
}
