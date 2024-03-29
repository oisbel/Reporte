package com.sccreporte.reporte.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.JsonReader;

import com.sccreporte.reporte.data.Biblical;
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

    /**
     * Acorta el str para que sea menor que limitLenght
     * @param str
     * @param limitLenght
     * @return
     */
    public static String shortTheString(String str, int limitLenght)
    {
        String result = str;
        if(result.length()>limitLenght){
            String[] fullName = str.split("\\s+");

            if(fullName.length>0) {
                result = fullName[0];
            }else
                return str.substring(0,limitLenght);
            if(result.length()>limitLenght)
                return str.substring(0,limitLenght);

            for(int i=1; i<fullName.length; i++)
            {
               String temp = result + " " + fullName[i];

               if(temp.length()>limitLenght)
                   break;
               result = temp;
            }
        }
        return  result;
    }

    /**
     * Guarda en shared preferences los datos del reporte que no se ha terminado de llenar
     * por lo que no se ha salvado al servidor y necesita una salva local temporal
     * @param context
     * @param jsonObject
     */
    public static void saveReportData(Context context, JSONObject jsonObject){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("reportInfo",jsonObject.toString());
        editor.apply();
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
     * Guarda en shared preferences los datos del reporte editado para mostrarlo en report activitiy
     * @param context
     * @param jsonObject
     */
    public static void saveReportDataJustEdited(Context context, JSONObject jsonObject){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("reportDataJustEdited",jsonObject.toString());
        editor.apply();
    }

    /**
     * Get the report data jst edited to show it in report activity, then erase it
     */
    public static JSONObject loadReportDataJustEdited(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try{
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("reportDataJustEdited",""));
            if(jsonObject.toString() != ""){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("reportDataJustEdited","");
                editor.apply();
                return jsonObject;
            } else return null;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Guarda en shared preferences los datos del ultimo reporte que no se quiere editar porque se
     * quiso create report pero no es no es tiempo de crear reporte
     * @param context
     * @param jsonObject
     */
    public static void saveReportDataForeEdit(Context context, JSONObject jsonObject){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("reportInfoForEdit",jsonObject.toString());
        editor.apply();
    }

    /**
     * Para guardar temporalmente si es tiempo de crear un nuevo reporte
     * @param context
     * @param itIs
     */
    public static void saveItIsTimeToNewReport(Context context, boolean itIs){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("itIsTimeToNewReport", itIs);
        editor.apply();
    }

    /**
     * Para guardar temporalmente es estudio biblico creado
     * de manera que lo pueda agregar al recycler view adapter de la vista del  biblical fragment
     * @param context
     * @param biblical
     */
    public static void saveCreatedBiblical(Context context, JSONObject biblical){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("biblicalFragmentInfo", biblical.toString());
        editor.apply();
    }

    /**
     * Get the recent created biblical to show it in the biblical fragment, and then erase it.
     * @param context
     * @return
     */
    public static Biblical loadCreatedBiblical(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try{
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("biblicalFragmentInfo",""));
            if(jsonObject.toString() != ""){
                // Limpiar los datos para que no los vuelva  recuperar
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("biblicalFragmentInfo", "");
                editor.apply();

                return new Biblical(jsonObject);
            } else return null;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Guarda en shared preferences los datos del nuevo reporte que se ha guardado al servidor
     * para mostrarlo en el recycler view del reports fragment
     * @param context
     * @param jsonObject
     */
    public static void saveCreatedReport(Context context, JSONObject jsonObject){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("reportFragmentInfo",jsonObject.toString());
        editor.apply();
    }

    /**
     * Get the saved report data form SharePreferences to show it in reports fragment view
     * then erased
     */
    public static Report loadCreatedReport(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try{
            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("reportFragmentInfo",""));
            if(jsonObject.toString() != ""){
                // Limpiar los datos para que no los vuelva  recuperar
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("reportFragmentInfo", "");
                editor.apply();
                return new Report(jsonObject);
            } else return null;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Devuelve y guarda si recientemente se ha editado un reporte para cargarlo en el reports fragment
     * @param context
     * @param edited
     * @return
     */
    public static boolean statusEditedReport(Context context, boolean edited){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean result = sharedPreferences.getBoolean("editedReport", false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("editedReport", edited);
        editor.apply();
        return result;
    }


    /**
     * Get the user data form SharePreferences
     */
    public static User loadUserData(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new User(sharedPreferences.getString("lugar",""),
                sharedPreferences.getString("pastor",""),
                sharedPreferences.getInt("id",-1),
                sharedPreferences.getString("nombre",""),
                sharedPreferences.getString("email",""),
                sharedPreferences.getString("phone",""),
                sharedPreferences.getInt("year",-1),
                sharedPreferences.getInt("month",-1),
                sharedPreferences.getInt("day",-1),
                sharedPreferences.getString("direccion",""),
                sharedPreferences.getString("nombre_conyuge",""),
                sharedPreferences.getString("fecha_casamiento",""),
                sharedPreferences.getString("grado",""),
                sharedPreferences.getString("ministerio",""),
                sharedPreferences.getString("responsabilidad",""),
                sharedPreferences.getString("password",""));
    }

    public static void clearUserData(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("lugar","");
        editor.putString("pastor","");
        editor.putInt("id",-1);
        editor.putString("nombre","");
        editor.putString("email","");
        editor.putString("phone","");
        editor.putInt("year",-1);
        editor.putInt("month",-1);
        editor.putInt("day",-1);
        editor.putString("direccion","");
        editor.putString("nombre_conyuge","");
        editor.putString("fecha_casamiento","");
        editor.putString("grado","");
        editor.putString("ministerio","");
        editor.putString("responsabilidad","");
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

            editor.putString("lugar", jsonData.getString("lugar"));

            String temp = jsonData.getString("pastor");
            editor.putString("pastor", temp.isEmpty() ? "" : temp);

            editor.putInt("id", user_id);
            editor.putString("nombre", jsonData.getString("nombre"));
            editor.putString("email", jsonData.getString("email"));

            temp = jsonData.getString("phone");
            editor.putString("phone", temp.isEmpty() ? "No" : temp);

            editor.putInt("year", jsonData.getInt("year"));
            editor.putInt("month", jsonData.getInt("month"));
            editor.putInt("day", jsonData.getInt("day"));

            temp = jsonData.getString("direccion");
            editor.putString("direccion", temp.isEmpty() ? "No" : temp);

            temp = jsonData.getString("nombre_conyuge");
            editor.putString("nombre_conyuge", temp.isEmpty() ? "No" : temp);

            temp = jsonData.getString("fecha_casamiento");
            editor.putString("fecha_casamiento", temp.isEmpty() ? "No" : temp);

            temp = jsonData.getString("grado");
            editor.putString("grado", temp.isEmpty() ? "No" : temp);

            temp = jsonData.getString("ministerio");
            editor.putString("ministerio", temp.isEmpty() ? "No" : temp);

            temp = jsonData.getString("responsabilidad");
            editor.putString("responsabilidad", temp.isEmpty() ? "No" : temp);

            editor.putString("password", jsonData.getString("password"));
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * guarda los datos de usuario que se acaba de logear en el servidor
     * @param context
     * @param jsonData
     * @param passNoHash
     */
    public static void SaveUserData(Context context, JSONObject jsonData, String passNoHash){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString("lugar", jsonData.getString("lugar"));

            String temp = jsonData.getString("pastor");
            editor.putString("pastor", temp.isEmpty() ? "" : temp);

            editor.putInt("id", jsonData.getInt("id"));
            editor.putString("nombre", jsonData.getString("nombre"));
            editor.putString("email", jsonData.getString("email"));

            temp = jsonData.getString("phone");
            editor.putString("phone", temp.isEmpty() ? "No" : temp);

            editor.putInt("year", jsonData.getInt("year"));
            editor.putInt("month", jsonData.getInt("month"));
            editor.putInt("day", jsonData.getInt("day"));

            temp = jsonData.getString("direccion");
            editor.putString("direccion", temp.isEmpty() ? "No" : temp);

            temp = jsonData.getString("nombre_conyuge");
            editor.putString("nombre_conyuge", temp.isEmpty() ? "No" : temp);

            temp = jsonData.getString("fecha_casamiento");
            editor.putString("fecha_casamiento", temp.isEmpty() ? "No" : temp);

            temp = jsonData.getString("grado");
            editor.putString("grado", temp.isEmpty() ? "No" : temp);

            temp = jsonData.getString("ministerio");
            editor.putString("ministerio", temp.isEmpty() ? "No" : temp);

            temp = jsonData.getString("responsabilidad");
            editor.putString("responsabilidad", temp.isEmpty() ? "No" : temp);

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
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Guarda en shared preferences los links de las redes sociales y website
     * @param context
     * @param jsonObject
     */
    public static void saveSocialMediaLinks(Context context, JSONObject jsonObject){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("socialMediaLinks",jsonObject.toString());
        editor.apply();
    }

    /**
     * Get the social media links
     */
    public static JSONObject loadSocialMediaLinks(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        JSONObject jsonObjectDefault = null;
        try{
            String socialMediaDefaults = "{" +
                    "  \"tutorial\": \"063V0DnwpFM\"," +
                    "  \"website\": \"https://www.soldadosdelacruz.org\"," +
                    "  \"radio\": \"https://tunein.com/radio/Radio-Directo-Al-Corazon-s249347/\"," +
                    "  \"facebook\": \"https://www.facebook.com/radiodirectoalcorazon\"," +
                    "  \"facebook_page_id\": \"886750211342788\"," +
                    "  \"youtube\": \"https://www.youtube.com/channel/UCJcClh8xBY3MT1UZY3pR3Dg/\"," +
                    "  \"twitter\": \"https://twitter.com/SCCMIAMI\"," +
                    "  \"instagram\": \"https://www.instagram.com/iglesiasoldadosdelacruz/\"," +
                    "  \"otros\": \"\"" +
                    "}";
            jsonObjectDefault = new JSONObject(socialMediaDefaults);

            JSONObject jsonObject = new JSONObject(sharedPreferences.getString("socialMediaLinks",
                    ""));

            if(jsonObject.toString() != ""){
                return jsonObject;
            } else {
                return jsonObjectDefault;
            }
        }catch (JSONException e){
            return jsonObjectDefault;
        }
    }

}

