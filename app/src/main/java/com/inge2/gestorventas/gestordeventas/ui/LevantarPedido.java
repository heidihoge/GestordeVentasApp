package com.inge2.gestorventas.gestordeventas.ui;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.inge2.gestorventas.gestordeventas.R;
import com.inge2.gestorventas.gestordeventas.modelo.Cliente;
import com.inge2.gestorventas.gestordeventas.modelo.Producto;
import com.inge2.gestorventas.gestordeventas.modelo.table.ProductoTableAdapter;
import com.inge2.gestorventas.gestordeventas.sqlite.OperacionesBaseDatos;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class LevantarPedido extends AppCompatActivity {
    static List <Producto> productos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levantar_pedido);
        this.setTitle(Cliente.clienteSeleccionado.apellidos + ", " + Cliente.clienteSeleccionado.nombres);
        OperacionesBaseDatos datos = OperacionesBaseDatos
                .obtenerInstancia(getApplicationContext());

        final Spinner productoSpinner = (Spinner) findViewById(R.id.Producto);
        final EditText cantidad =  (EditText) findViewById(R.id.Cantidad);
        final TableView tablaProductos = (TableView) findViewById(R.id.productos);

        Button agregar = (Button) findViewById(R.id.Agregar);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Producto p = (Producto)productoSpinner.getSelectedItem();
                int c = Integer.parseInt(String.valueOf(cantidad.getText()));
                p.cantidad = c;
                productos.add(p);

                tablaProductos.setDataAdapter(new ProductoTableAdapter(LevantarPedido.this , productos));
            }
        });


        tablaProductos.setHeaderAdapter(new SimpleTableHeaderAdapter(this, "Producto", "Precio", "Cantidad", "Total" ));

        List<Producto> productos = new ArrayList();
        ArrayAdapter productoAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, productos);
        Cursor cursor = datos.obtenerProductos();
        while(cursor.moveToNext()){
            Producto producto = new Producto(cursor.getString(1), cursor.getString(2),
                    cursor.getFloat(3), cursor.getInt(4));
            productos.add(producto);
        }
        productoSpinner.setAdapter(productoAdapter);

    }
}
