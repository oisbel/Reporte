package com.sccreporte.reporte.data;

/**
 * Created by simpson on 2/19/2018.
 */

public class User {
    public int id;
    public String nombre;
    public String email;
    public String grado;
    public String ministerio;
    public String responsabilidad;
    public String password;

    public User(int id, String nombre, String email, String grado, String ministerio,
                String responsabilidad, String password){
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.grado = grado;
        this.ministerio = ministerio;
        this.responsabilidad = responsabilidad;
        this.password = password;
    }
}
