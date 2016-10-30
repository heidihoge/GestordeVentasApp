package com.inge2.gestorventas.gestordeventas.modelo.table;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inge2.gestorventas.gestordeventas.modelo.CabeceraPedido;
import com.inge2.gestorventas.gestordeventas.modelo.Cliente;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

/**
 * Created by HEIDI
 */

public class PedidosTableAdapter extends TableDataAdapter<CabeceraPedido> {
    public static final String[][] HEADERS = { { "Pedido", "Cliente", "Fecha" } };
    public PedidosTableAdapter(Context context, List<CabeceraPedido> pedidos){
        super(context, pedidos);
    }


    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
       CabeceraPedido cabeceraPedido = getRowData(rowIndex);

        TextView textView = new TextView(this.getContext());
        textView.setTextAppearance(this.getContext(), android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Large);
        textView.setHeight(50);
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.END);


        String cliente = cabeceraPedido.idCliente;

        if(cabeceraPedido.cliente != null){
            cliente = cabeceraPedido.cliente.apellidos + ", " + cabeceraPedido.cliente.nombres;
        }

        switch (columnIndex){
            case 0:
                textView.setText(cabeceraPedido.idCabeceraPedido);
                break;
            case 1:
                textView.setText(cliente);
                break;
            case 2:
                textView.setText(cabeceraPedido.fecha);
                break;
        }

        return textView;
    }
}
