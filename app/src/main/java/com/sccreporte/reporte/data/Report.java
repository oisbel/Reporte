package com.sccreporte.reporte.data;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by simpson on 2/9/2018.
 */

public class Report implements Comparable<Report>{
    public JSONObject reportJSON;
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
    // para especificar cuando se pudo parsear satisfactoriamente la fecha
    public boolean fechaOK;

    public Report(JSONObject report){
        reportJSON = report;
        String tempFecha = "04/04/1988";
        SimpleDateFormat formatter = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH );
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
            if(report.has("fecha")) {
                tempFecha = report.getString("fecha");
            }
            hogares = report.getInt("hogares");
            horas_ayunos = report.getInt("horas_ayunos");
            if(report.has("id")){
                id = report.getInt("id");
            }
            mensajeros = report.getInt("mensajeros");
            mensajes = report.getInt("mensajes");
            otros = report.getString("otros");
            porciones = report.getInt("porciones");
            sanidades = report.getInt("sanidades");
            if(report.has("user_id")){
                user_id = report.getInt("user_id");
            }
            visitas = report.getInt("visitas");

        }catch (JSONException e){
            e.printStackTrace();
        }

        try {
            fecha = formatter.parse(tempFecha);
            fechaOK =true;
        } catch (ParseException e){
            e.printStackTrace();
            fechaOK = false;
            fecha = new Date(2014,11,11);
        }
    }

    @Override
    public int compareTo(@NonNull Report anotherReport) {
        return fecha.compareTo(anotherReport.fecha);
    }
}
