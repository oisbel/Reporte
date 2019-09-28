package com.sccreporte.reporte.data;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by simpson on 2/3/2018.
 * Representa los reportes del usuario
 */

public class ReportsData {
    public String Status;
    // Id del usuario a quien pertenecen los reportes
    int UserId;
    List<Report> Reports;

    /**
     *
     * @param reportsJSONString reportes en formato JSON
     */
    public ReportsData(String reportsJSONString){
        Reports = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(reportsJSONString);
            JSONObject reports = data.getJSONObject("Reports");
            Status = reports.getString("status");
            if(Status.equals("ok")) {
                JSONArray reportsList = reports.getJSONArray("list");
                for (int i = 0; i<reportsList.length();i++){
                    Reports.add(new Report(reportsList.getJSONObject(i)));
                }
                if(Reports.size()>0){
                    UserId = Reports.get(0).user_id;
                    // reverse para tener el ultimo reporte arriba
                    //Collections.reverse(Reports);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    /**
     *
     * @return Lista de reportes del usuario
     * Deberia crear metodos getter and setter pero lo dejo como variables publicas por ahora por simplicidad
     */
    public List<Report> getData(){
        return Reports;
    }
}
