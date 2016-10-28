package com.inge2.gestorventas.gestordeventas.modelo;

/**
 * Created by ADMIN on 17/10/2016.
 */


public class Cliente {

    transient volatile public static Cliente clienteSeleccionado = null;

    public String direccion;

    public String idCliente;

    public String nombres;

    public String apellidos;

    public String telefono;

    public Cliente(String idCliente, String nombres, String apellidos, String telefono, String direccion) {
        this.idCliente = idCliente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.direccion = direccion;
    }
}
