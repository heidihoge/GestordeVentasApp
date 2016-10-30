package com.inge2.gestorventas.gestordeventas.modelo;

import java.util.List;

/**
 * Created by ADMIN on 17/10/2016.
 */

public class CabeceraPedido {

    public transient Cliente cliente;
    public transient List<DetallePedido> detallePedidoList;

    public String idCabeceraPedido;

    public String fecha;

    public String idCliente;

    public String idFormaPago;

    public CabeceraPedido(String idCabeceraPedido, String fecha,
                          String idCliente, String idFormaPago) {
        this.idCabeceraPedido = idCabeceraPedido;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.idFormaPago = idFormaPago;
    }
}
