package com.inge2.gestorventas.gestordeventas.modelo;

/**
 * Created by ADMIN on 17/10/2016.
 */

public class Producto {

    /** no se guarda en la base de datos **/
    public transient int cantidad;

    /** si se guarda en la base de datos **/

    public String idProducto;

    public String nombre;

    public float precio;

    public int existencias;

    public Producto(String idProducto, String nombre, float precio, int existencias) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.existencias = existencias;
    }

    @Override
    public String toString(){
        return this.nombre + "(" + this.precio + " Gs)";
    }
}
