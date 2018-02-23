package com.sccreporte.reporte.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sccreporte.reporte.data.Report;
import com.sccreporte.reporte.data.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by simpson on 2/8/2018.
 */

public class DataUtils {
    public static final String[]
            Months = new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    /**
     * Get the user data form SharePreferences
     */
    public static Report loadUserData(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try{
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("reportTemp",""));
            return new Report(jsonObject);
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the not save report data form SharePreferences
     */
    public static User loadReportData(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new User(sharedPreferences.getInt("id",-1),
                sharedPreferences.getString("nombre",""),
                sharedPreferences.getString("email",""),
                sharedPreferences.getString("grado",""),
                sharedPreferences.getString("ministerio",""),
                sharedPreferences.getString("responsabilidad",""),
                sharedPreferences.getString("lugar",""),
                sharedPreferences.getString("pastor",""),
                sharedPreferences.getString("password",""));
    }
}

