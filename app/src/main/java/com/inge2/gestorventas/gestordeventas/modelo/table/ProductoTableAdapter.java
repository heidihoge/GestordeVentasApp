package com.inge2.gestorventas.gestordeventas.modelo.table;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inge2.gestorventas.gestordeventas.modelo.Cliente;
import com.inge2.gestorventas.gestordeventas.modelo.Producto;

import java.util.List;
import java.util.Locale;

import de.codecrafters.tableview.TableDataAdapter;

/**
 * Created by HEIDI
 */

public class ProductoTableAdapter extends TableDataAdapter<Producto> {
    public static final String[][] HEADERS = { { "Producto", "Precio", "Cantidad", "Total" } };
    public ProductoTableAdapter(Context context, List<Producto> producto){
        super(context, producto);
    }


    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Producto producto = getRowData(rowIndex);

        TextView textView = new TextView(this.getContext());
        textView.setTextAppearance(this.getContext(), android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Large);
        textView.setHeight(50);
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.END);

        switch (columnIndex){
            case 0:
                textView.setText(producto.nombre);
                break;
            case 1:
                textView.setText(String.format(Locale.US, "%2.2f", producto.precio));
                break;
            case 2:
                textView.setText(String.valueOf(producto.cantidad));
                break;
            case 3:
                textView.setText(String.format(Locale.US, "%2.2f", ((float)producto.cantidad) * producto.precio));
                break;
        }

        return textView;
    }
}
