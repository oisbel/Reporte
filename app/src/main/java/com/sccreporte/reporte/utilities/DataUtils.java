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

    public static final String[]
            MonthsShort = new String[]{"Ene", "Feb", "Mar", "Abr", "May", "Jun",
            "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern VALID_NAME_REGEX =
            Pattern.compile("^[\\p{L} .'-]+$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    public static boolean validateName(String name) {
        Matcher matcher = VALID_NAME_REGEX .matcher(name);
        return matcher.find();
    }

    /**
     * Get the not save report data form SharePreferences
     */
    public static Report loadReportData(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try{
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("reportInfo",""));
            if(jsonObject.toString() != ""){
                return new Report(jsonObject);
            } else return null;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Limplia los datos del reporte temporal guardado
     * @param context
     */
    public static void clearReportData(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("reportInfo","{}");
        editor.apply();
    }

    /**
     * Get the user data form SharePreferences
     */
    public static User loadUserData(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new User(sharedPreferences.getInt("id",-1),
                sharedPreferences.getString("nombre",""),
                sharedPreferences.getString("email",""),
                sharedPreferences.getString("grado",""),
                sharedPreferences.getString("ministerio",""),
                sharedPreferences.getString("responsabilidad",""),
                sharedPreferences.getString("lugar",""),
                sharedPreferences.getString("pastor",""),
                sharedPreferences.getString("numero",""),
                sharedPreferences.getString("password",""));
    }

    public static void clearUserData(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id","-1");
        editor.putString("nombre","");
        editor.putString("email","");
        editor.putString("grado","");
        editor.putString("ministerio","");
        editor.putString("responsabilidad","");
        editor.putString("lugar","");
        editor.putString("pastor","");
        editor.putString("numero","");
        editor.putString("password","");
        editor.apply();
    }

    /**
     * Guarda los datos de usuario que se acaba de crear en el servidor
     * @param context
     * @param jsonData
     * @param user_id
     */
    public static void SaveUserData(Context context, JSONObject jsonData, int user_id){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putInt("id", user_id);
            editor.putString("nombre", jsonData.getString("nombre"));
            editor.putString("email", jsonData.getString("email"));

            String temp = jsonData.getString("grado").toString();
            editor.putString("grado", temp.isEmpty() ? "No" : temp);

            temp = jsonData.getString("ministerio").toString();
            editor.putString("ministerio", temp.isEmpty() ? "No" : temp);

            temp = jsonData.getString("responsabilidad").toString();
            editor.putString("responsabilidad", temp.isEmpty() ? "No" : temp);

            editor.putString("lugar", jsonData.getString("lugar"));

            temp = jsonData.getString("pastor").toString();
            editor.putString("pastor", temp.isEmpty() ? "No" : temp);

            temp = jsonData.getString("numero").toString();
            editor.putString("numero", temp.isEmpty() ? "No" : temp);

            editor.putString("password", jsonData.getString("password"));
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * uarda los datos de usuario que se acaba de logear en el servidor
     * @param context
     * @param jsonData
     * @param passNoHash
     */
    public static void SaveUserData(Context context, JSONObject jsonData, String passNoHash){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putInt("id", jsonData.getInt("id"));
            editor.putString("nombre", jsonData.getString("nombre"));
            editor.putString("email", jsonData.getString("email"));
            editor.putString("grado", jsonData.getString("grado"));
            editor.putString("ministerio", jsonData.getString("ministerio"));
            editor.putString("responsabilidad", jsonData.getString("responsabilidad"));
            editor.putString("lugar", jsonData.getString("lugar"));
            editor.putString("pastor", jsonData.getString("pastor"));
            editor.putString("numero", jsonData.getString("numero"));
            editor.putString("password", passNoHash);
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda los datos de usuarios que se acaban de cambiar en el servidor
     * @param context
     * @param jsonData
     */
    public static void SaveUserData(Context context, JSONObject jsonData){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString("grado", jsonData.getString("grado"));
            editor.putString("ministerio", jsonData.getString("ministerio"));
            editor.putString("responsabilidad", jsonData.getString("responsabilidad"));
            editor.putString("lugar", jsonData.getString("lugar"));
            editor.putString("pastor", jsonData.getString("pastor"));
            editor.putString("numero", jsonData.getString("numero"));
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

