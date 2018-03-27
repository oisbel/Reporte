package com.sccreporte.reporte.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by simpson on 3/27/2018.
 */

public class Biblical {
    public JSONObject biblicalJSON;
    public int id;
    public String nombre;
    public int year;
    public int month;
    public int day;
    public String direccion;
    public int user_id;

    public Biblical(JSONObject biblical){
        try {
            year = biblical.getInt("year");
            month = biblical.getInt("month");
            day = biblical.getInt("day");
            nombre = biblical.getString("nombre");
            direccion = biblical.getString("direccion");

            if(biblical.has("id")){
                id = biblical.getInt("id");
            }
            if(biblical.has("user_id")){
                user_id = biblical.getInt("user_id");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
