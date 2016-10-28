package com.inge2.gestorventas.gestordeventas.ui;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.inge2.gestorventas.gestordeventas.R;
import com.inge2.gestorventas.gestordeventas.modelo.Cliente;
import com.inge2.gestorventas.gestordeventas.modelo.Producto;
import com.inge2.gestorventas.gestordeventas.sqlite.OperacionesBaseDatos;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;

public class LevantarPedido extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levantar_pedido);
        this.setTitle(Cliente.clienteSeleccionado.apellidos + ", " + Cliente.clienteSeleccionado.nombres);
        OperacionesBaseDatos datos = OperacionesBaseDatos
                .obtenerInstancia(getApplicationContext());

        Spinner productoSpinner = (Spinner) findViewById(R.id.Producto);

        TableView tablaProductos = (TableView) findViewById(R.id.productos);

        Button agregar = (Button) findViewById(R.id.Agregar);

        EditText cantidad = (EditText) findViewById(R.id.Cantidad);

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
