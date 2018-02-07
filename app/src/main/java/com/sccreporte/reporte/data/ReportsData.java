package com.sccreporte.reporte.data;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    /**
     *
     * @return Lista de reportes del usuario
     */
    public List<Report> getData(){
        return Reports;
    }

    public class Report implements Comparable<Report>{
        public int avivamientos;
        public int ayunos;
        public int biblias;
        public int cultos;
        public int devocionales;
        public int enfermos;
        public int estudios_asistidos;
        public int estudios_establecidos;
        public int estudios_realizados;
        public Date fecha;
        public int hogares;
        public int horas_ayunos;
        public int id;
        public int mensajeros;
        public int mensajes;
        public String otros;
        public int porciones;
        public int sanidades;
        public int user_id;
        public int visitas;

        public Report(JSONObject report){

            String tempFecha = "04/04/1988";
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "EEE, dd MMM yyyy HH:mm:ss zzz", Locale.getDefault());
            try {
                avivamientos = report.getInt("avivamientos");
                ayunos = report.getInt("ayunos");
                biblias = report.getInt("biblias");
                cultos = report.getInt("cultos");
                devocionales = report.getInt("devocionales");
                enfermos = report.getInt("enfermos");
                estudios_asistidos = report.getInt("estudios_asistidos");
                estudios_establecidos = report.getInt("estudios_establecidos");
                estudios_realizados = report.getInt("estudios_realizados");
                tempFecha = report.getString("fecha");
                hogares = report.getInt("hogares");
                horas_ayunos = report.getInt("horas_ayunos");
                id = report.getInt("id");
                mensajeros = report.getInt("mensajeros");
                mensajes = report.getInt("mensajes");
                otros = report.getString("otros");
                porciones = report.getInt("porciones");
                sanidades = report.getInt("sanidades");
                user_id = report.getInt("user_id");
                visitas = report.getInt("visitas");

            }catch (JSONException e){
                e.printStackTrace();
            }

            try {
                fecha = formatter.parse(tempFecha);
            } catch (ParseException e){
                e.printStackTrace();
            }
        }

        @Override
        public int compareTo(@NonNull Report anotherReport) {
            return fecha.compareTo(anotherReport.fecha);
        }
    }
}
