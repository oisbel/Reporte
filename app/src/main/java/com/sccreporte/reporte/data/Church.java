package com.sccreporte.reporte.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Church {
    public int id;
    public String nombre;
    public String direccion;
    public int feligresia;
    public int estudios_biblicos;
    public String pastor;
    public String picture;

    public Church(JSONObject church){
        try {
            id = church.getInt("id");
            nombre = church.getString("nombre");
            direccion = church.getString("direccion");
            feligresia = church.getInt("feligresia");
            estudios_biblicos = church.getInt("estudios_biblicos");
            pastor = church.getString("pastor");
            picture = church.getString("picture");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    @Override
    public String toString(){
        return nombre;
    }
}
