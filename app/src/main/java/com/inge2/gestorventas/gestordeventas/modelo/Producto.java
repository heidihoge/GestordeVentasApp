package com.inge2.gestorventas.gestordeventas.modelo;

/**
 * Created by ADMIN on 17/10/2016.
 */

public class Producto {

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
}
