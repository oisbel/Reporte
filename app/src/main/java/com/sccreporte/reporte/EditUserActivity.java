package com.sccreporte.reporte;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sccreporte.reporte.data.User;
import com.sccreporte.reporte.utilities.DataUtils;
import com.sccreporte.reporte.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class EditUserActivity extends AppCompatActivity {

    ImageButton closeBT;
    ImageButton doneBT;
    private ProgressBar mLoadingIndicator;
    private ScrollView userSV;
    User mUser;
    TextView nameTV;
    TextView lugarTV;
    private Spinner gradoSpinner;
    EditText ministerioET;
    EditText responsabilidadET;
    EditText pastorET;
    EditText numeroET;

    JSONObject jsonChangeUserData; // los datos a cambiar del usuario
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        closeBT = findViewById(R.id.closeButton);
        doneBT = findViewById(R.id.doneButton);
        mLoadingIndicator = findViewById(R.id.loadingIndicatorProgressBar);
        userSV = findViewById(R.id.userScrollView);
        nameTV = findViewById(R.id.nameTextView);
        lugarTV = findViewById(R.id.lugarTextView);
        gradoSpinner = findViewById(R.id.gradoSpinner);
        ministerioET = findViewById(R.id.ministerioEditText);
        responsabilidadET = findViewById(R.id.responsabilidadEditText);
        mUser = DataUtils.loadUserData(this);
        if(mUser.id == -1)
            finish();
        nameTV.setText(mUser.nombre);
        lugarTV.setText(mUser.lugar);

        // Agregar un spinner para el grado
        // Create an ArrayAdapter using the string array and a default spinner layout
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        //        R.array.grado, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        //gradoSpinner.setAdapter(adapter);

        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.custom_spinner,
                getResources().getStringArray(R.array.grado));

        gradoSpinner.setAdapter(adapter);

        int spinnerPosition = adapter.getPosition(mUser.grado);
        gradoSpinner.setSelection(spinnerPosition);
        ministerioET.setText(mUser.ministerio);
        responsabilidadET.setText(mUser.responsabilidad);

        closeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditUserActivity.this, UserDataActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // click para editar el usuario en el servidor
        doneBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonChangeUserData = makeUserData();
                new EditUserQueryTask().execute(jsonChangeUserData);
            }
        });
    }

    // Helper methods
    private void showLoading(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
        userSV.setAlpha(.5f);
    }

    private void hideLoading(){
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        userSV.setAlpha(0);
    }

    private void ShowErrorMessage(){
        Toast toast = Toast.makeText(this, R.string.edit_user_error_message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void ShowSuccessMessage(){
        Toast toast = Toast.makeText(this, R.string.edit_user_success_message, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Crea un objeto object con los valores del usuario que se quiere mandar
     * @return
     */
    private JSONObject makeUserData(){
        JSONObject result = new JSONObject();
        String temp = "";
        try {
            temp = gradoSpinner.getSelectedItem().toString();
            result.put("grado", temp.isEmpty() ? "No" : temp);

            temp = ministerioET.getText().toString();
            result.put("ministerio", temp.isEmpty() ? "No" : temp);

            temp = responsabilidadET.getText().toString();
            result.put("responsabilidad", temp.isEmpty() ? "No" : temp);

        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * Clase que manda los datos del usuario a editar al servidor, y maneja el resultado devuelto
     * usando otro hilo
     */
    public class EditUserQueryTask extends AsyncTask<JSONObject, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }
        @Override
        protected JSONObject doInBackground(JSONObject... jsonObjects) {
            if(jsonObjects == null || jsonObjects.length == 0) return null;
            JSONObject jsonData = jsonObjects[0];
            URL editUserUrl = NetworkUtils.buildEditUserUrl(mUser.id);
            String editReportJSONResult;
            JSONObject result = null;
            try {
                editReportJSONResult = NetworkUtils.getEditUserFromHttpUrl(
                        editUserUrl, jsonData, mUser.email, mUser.password);
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
                if(jsonObject.has("user")){
                    //success
                    DataUtils.SaveUserData(EditUserActivity.this, jsonChangeUserData);
                    ShowSuccessMessage();
                    Intent intent = new Intent(getApplicationContext(), UserDataActivity.class);
                    startActivity(intent);
                    finish();
                }
            }else{
                ShowErrorMessage();
            }
        }
    }

    public class SpinnerAdapter extends ArrayAdapter<String> {
        private String[] objects;

        public SpinnerAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
            this.objects = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        private View getCustomView(final int position, View convertView, ViewGroup parent) {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_spinner, parent, false);
            final TextView label = (TextView) row.findViewById(R.id.tv_spinnervalue);
            label.setText(objects[position]);
            return row;
        }
    }
}
