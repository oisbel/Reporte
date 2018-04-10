package com.sccreporte.reporte;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sccreporte.reporte.data.Biblical;
import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.utilities.DataUtils;
import com.sccreporte.reporte.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateBiblicalActivity extends AppCompatActivity {

    private ImageButton backBT;
    private ImageButton doneBT;
    private ProgressBar mLoadingIndicator;
    private User mUser;
    private Biblical mBiblical;
    private TextView nameTV;
    private TextView lugarTV;
    private TextView fechaTV;

    private EditText nombreET;
    private EditText direccionET;

    private Date dateToday;
    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_biblical);

        // Back button click
        backBT = findViewById(R.id.backButton);
        backBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        doneBT = findViewById(R.id.doneButton);
        mLoadingIndicator = findViewById(R.id.loadingIndicatorProgressBar);

        nameTV = findViewById(R.id.nameTextView);
        lugarTV = findViewById(R.id.lugarTextView);
        fechaTV = findViewById(R.id.fechaTextView);

        nombreET = findViewById(R.id.nombreEditText);
        direccionET = findViewById(R.id.direccionEditText);

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

        // click para guardar el estudio biblico en el servidor
        doneBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allFieldEmpty())
                    return;
                new CreateBiblicalQueryTask().execute(makeBiblicalData());
            }
        });
    }

    /**
     * Crea un objeto object con los valores del estudio biblico que se quiere crear
     * @return
     */
    private JSONObject makeBiblicalData(){
        JSONObject result = new JSONObject();
        try {
            result.put("year", year);
            result.put("month", month);
            result.put("day", day);

            result.put("nombre", nombreET.getText());
            result.put("direccion", direccionET.getText());

        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        return result;
    }

    private void showLoading(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
        //reportSV.setAlpha(.5f);
    }

    private void hideLoading(){
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        //reportSV.setAlpha(0);
    }

    private void ShowErrorMessage(){
        Toast toast = Toast.makeText(this, R.string.create_biblical_error_message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void ShowSuccessMessage(){
        Toast toast = Toast.makeText(this, R.string.create_biblical_success_message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private boolean allFieldEmpty(){

        if(nombreET.getText().toString().isEmpty() && direccionET.getText().toString().isEmpty())
            return true;
        return false;
    }

    public class CreateBiblicalQueryTask extends AsyncTask<JSONObject, Void, JSONObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... jsonObjects) {
            if(jsonObjects == null || jsonObjects.length == 0) return null;
            JSONObject jsonData = jsonObjects[0];
            URL createBiblicalUrl = NetworkUtils.buildCreateBiblicalUrl();
            String createBiblicalJSONResult = null;
            JSONObject result = null;
            try {
                createBiblicalJSONResult = NetworkUtils.geCreateBiblicalFromHttpUrl(
                        createBiblicalUrl, jsonData, mUser.email, mUser.password);
            }catch (IOException e){
                e.printStackTrace();
                return result;
            }
            try {
                result = new JSONObject(createBiblicalJSONResult);
            }catch (JSONException e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            hideLoading();
            if(jsonObject != null){
                if(jsonObject.has("biblical")){
                    //success
                    ShowSuccessMessage();
                    Intent intent = new Intent(getApplicationContext(), BiblicalActivity.class);
                    startActivity(intent);
                    finish();
                }
            }else{
                ShowErrorMessage();
            }
        }
    }
}
