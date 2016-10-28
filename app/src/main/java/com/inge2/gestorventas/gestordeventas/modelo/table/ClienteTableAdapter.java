package com.inge2.gestorventas.gestordeventas.modelo.table;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.inge2.gestorventas.gestordeventas.R;
import com.inge2.gestorventas.gestordeventas.modelo.Cliente;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.listeners.TableDataClickListener;

/**
 * Created by HEIDI
 */

public class ClienteTableAdapter extends TableDataAdapter<Cliente> {
    public static final String[][] HEADERS = { { "Nombre", "Apellido", "Direccion", "Telefono" } };
    public ClienteTableAdapter(Context context, List<Cliente> cliente){
        super(context, cliente);
    }


    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Cliente cliente = getRowData(rowIndex);

        TextView textView = new TextView(this.getContext());
        textView.setTextAppearance(this.getContext(), android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Large);
        textView.setHeight(50);
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.END);

        switch (columnIndex){
            case 0:
                textView.setText(cliente.apellidos + ", " +cliente.nombres);
                break;
            case 1:
                textView.setText(cliente.direccion);
                break;
            case 2:
                textView.setText(cliente.telefono);
                break;
        }

        return textView;
    }
}
