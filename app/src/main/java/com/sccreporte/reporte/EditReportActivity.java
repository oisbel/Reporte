package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.sccreporte.reporte.data.Report;
import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.databinding.ActivityEditReportBinding;
import com.sccreporte.reporte.utilities.DataUtils;
import com.sccreporte.reporte.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class EditReportActivity extends AppCompatActivity {

    // Create a data binding instance
    ActivityEditReportBinding mBinding;
    String mReportJSONString;
    private User mUser;
    Report report;
    int report_id; // para guardar el id del reporte y luego poder enviarlo en la solicitud de edit
    int year, month, day; // para guardar la fecha en share preferences dado que la fecha del reporte editado no cambia


    ImageButton backBT;
    ImageButton doneBT;
    private ProgressBar mLoadingIndicator;
    private ScrollView reportSV;

    private JSONObject reportEdited; // To save it in sharepreferences and then load it from home fragment

    int clickedItemIndex; // Representa el indice del reporte en el recycler view de reportsFragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_report);

        // Set the content view to the layout
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_report);
        backBT = findViewById(R.id.backButton);
        doneBT = findViewById(R.id.doneButton);
        mLoadingIndicator = findViewById(R.id.loadingIndicatorProgressBar);
        reportSV = findViewById(R.id.reportScrollView);

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
            report_id = report.id;
            year = report.year;
            month = report.month;
            day = report.day;
            displayReportInfo(report);
        }

        // Back button click
        backBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // click para editar el reporte en el servidor
        doneBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toLongInt(3))
                    return;
                reportEdited = makeReportData();
                new EditReportQueryTask().execute(reportEdited);
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
        Toast toast = Toast.makeText(this, R.string.edit_report_error_message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void ShowSuccessMessage(){
        Toast toast = Toast.makeText(this, R.string.edit_report_success_message, Toast.LENGTH_SHORT);
        toast.show();
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

            int month = 11;// Correspondiente a Diciembre
            if (report.month > 1)
                month = report.month - 2;
            mBinding.monthTextView.setText(DataUtils.Months[month]);
            mBinding.fechaTextView.setText("Llenado " + report.day + "/" +
                    DataUtils.Months[report.month - 1] + "/" +
                    report.year);
        }else{
            mBinding.fechaTextView.setText("-");
            mBinding.monthTextView.setText("-");
        }
        mBinding.lugarTextView.setText(String.valueOf(mUser.lugar));

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

    /**
     * check for all the field of report to see if they have to long integer
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
        if(mBinding.horasTrabajoEditText.getText().toString().length()>max)
            return true;
        return false;
    }

    /**
     * Crea un objeto object con los valores del reporte que se quiere mandar
     * @return
     */
    private JSONObject makeReportData(){
        JSONObject result = new JSONObject();
        String temp = "";
        try {
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

            temp = mBinding.horasTrabajoEditText.getText().toString();
            result.put("horas_trabajo", temp.isEmpty() ? "0" : temp);

            result.put("otros", mBinding.otrosEditText.getText());


        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * Clase que manda los datos del reporte a editar al servidor, y maneja el resultado devuelto
     * usando otro hilo
     */
    public class EditReportQueryTask extends AsyncTask<JSONObject, Void, JSONObject>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }
        @Override
        protected JSONObject doInBackground(JSONObject... jsonObjects) {
            if(jsonObjects == null || jsonObjects.length == 0) return null;
            JSONObject jsonData = jsonObjects[0];
            URL editReportUrl = NetworkUtils.buildEditReportUrl(report_id);
            String editReportJSONResult;
            JSONObject result = null;
            try {
                editReportJSONResult = NetworkUtils.getEditReportFromHttpUrl(
                        editReportUrl, jsonData, mUser.email, mUser.password);
            }catch (IOException e){
                e.printStackTrace();
                return result;
            }
            try {
                result = new JSONObject(editReportJSONResult);
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
                    Context context = getApplicationContext();
                    try {
                        reportEdited.put("id", jsonObject.getInt("report"));
                        reportEdited.put("year", year);
                        reportEdited.put("month", month);
                        reportEdited.put("day", day);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(clickedItemIndex == 0) {
                        // Guardar el reporte en sharepreferences para cargarlo en edit report desde el home fragment
                        DataUtils.saveReportDataForeEdit(context, reportEdited);
                    }
                    DataUtils.saveReportDataJustEdited(context,reportEdited);
                    DataUtils.statusEditedReport(context, true);
                    ShowSuccessMessage();
                    //Intent intent = new Intent(getApplicationContext(), LastReportsActivity.class);
                    //startActivity(intent);
                    finish();
                }
            }else{
                ShowErrorMessage();
            }
        }
    }
}
