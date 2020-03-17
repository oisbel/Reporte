package com.sccreporte.reporte.data;

/**
 * Created by simpson on 2/19/2018.
 */

public class User {
    public String lugar;
    public String pastor;
    public int id;
    public String nombre;
    public String email;
    public String phone;
    public int year;
    public int month;
    public int day;
    public String direccion;
    public String nombre_conyuge;
    public String fecha_casamiento;
    public String grado;
    public String ministerio;
    public String responsabilidad;
    public String password;

    public User(String lugar, String pastor,int id, String nombre, String email, String phone,
                int year, int month, int day, String direccion, String nombre_conyuge, String fecha_casamiento,
                String grado, String ministerio, String responsabilidad, String password){
        this.lugar = lugar;
        this.pastor = pastor;
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.phone = phone;
        this.year = year;
        this.month = month;
        this.day = day;
        this.direccion = direccion;
        this.nombre_conyuge = nombre_conyuge;
        this.fecha_casamiento = fecha_casamiento;
        this.grado = grado;
        this.ministerio = ministerio;
        this.responsabilidad = responsabilidad;
        this.password = password;
    }
}
