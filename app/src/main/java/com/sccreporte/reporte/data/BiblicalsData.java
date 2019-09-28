package com.sccreporte.reporte.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simpson on 4/6/2018.
 */

public class BiblicalsData {

    public String Status;
    // Id del usuario a quien pertenecen los reportes
    int UserId;
    List<Biblical> Biblicals;

    public BiblicalsData(String biblicalsJSONString){
        Biblicals = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(biblicalsJSONString);
            JSONObject biblicals = data.getJSONObject("Biblicals");
            Status = biblicals.getString("status");
            if(Status.equals("ok")) {
                JSONArray biblicalsList = biblicals.getJSONArray("list");
                for (int i = 0; i<biblicalsList.length();i++){
                    Biblicals.add(new Biblical(biblicalsList.getJSONObject(i)));
                }
                if(Biblicals.size()>0){
                    UserId = Biblicals.get(0).user_id;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @return Lista de estudios biblicos del usuario
     * Deberia crear metodos getter and setter pero lo dejo como variables publicas por ahora por simplicidad
     */
    public List<Biblical> getData(){
        return Biblicals;
    }
}
