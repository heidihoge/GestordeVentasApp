package com.inge2.gestorventas.gestordeventas.modelo;

/**
 * Created by ADMIN on 17/10/2016.
 */

public class CabeceraPedido {

    public String idCabeceraPedido;

    public String fecha;

    public String idCliente;

    public String idFormaPago;

    public String direccion;

    public CabeceraPedido(String idCabeceraPedido, String fecha,
                          String idCliente, String idFormaPago, String direccion) {
        this.idCabeceraPedido = idCabeceraPedido;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.idFormaPago = idFormaPago;
        this.direccion = direccion;
    }
}
