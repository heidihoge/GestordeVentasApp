package com.inge2.gestorventas.gestordeventas.ui;

import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.inge2.gestorventas.gestordeventas.R;
import com.inge2.gestorventas.gestordeventas.modelo.CabeceraPedido;
import com.inge2.gestorventas.gestordeventas.modelo.Cliente;
import com.inge2.gestorventas.gestordeventas.modelo.DetallePedido;
import com.inge2.gestorventas.gestordeventas.modelo.FormaPago;
import com.inge2.gestorventas.gestordeventas.modelo.Producto;
import com.inge2.gestorventas.gestordeventas.modelo.table.ProductoTableAdapter;
import com.inge2.gestorventas.gestordeventas.sqlite.OperacionesBaseDatos;

import java.util.ArrayList;
import java.util.Calendar;
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
        final NumberPicker cantidad =  (NumberPicker) findViewById(R.id.Cantidad);
        final TableView tablaProductos = (TableView) findViewById(R.id.productos);
        cantidad.setMaxValue(5000);
        cantidad.setMinValue(1);

        Button agregar = (Button) findViewById(R.id.Agregar);
        Button guardar = (Button) findViewById(R.id.Guardar);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Producto pOriginal = (Producto)productoSpinner.getSelectedItem();

                Producto p = new Producto(pOriginal.idProducto, pOriginal.nombre, pOriginal.precio, pOriginal.existencias);
                int c = cantidad.getValue();
                p.cantidad = c;
                productos.add(p);

                tablaProductos.setDataAdapter(new ProductoTableAdapter(LevantarPedido.this , productos));

                cantidad.setValue(1);
                productoSpinner.setSelection(0);

                Toast.makeText(LevantarPedido.this, "Producto agregado", Toast.LENGTH_SHORT).show();
            }
        });


        tablaProductos.setHeaderAdapter(new SimpleTableHeaderAdapter(this, "Producto", "Precio", "Cantidad", "Total" ));

        final List<Producto> productosSpinner = new ArrayList();
        ArrayAdapter productoAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, productosSpinner);
        Cursor cursor = datos.obtenerProductos();
        while(cursor.moveToNext()){
            Producto producto = new Producto(cursor.getString(1), cursor.getString(2),
                    cursor.getFloat(3), cursor.getInt(4));
            productosSpinner.add(producto);
        }
        productoSpinner.setAdapter(productoAdapter);



        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productos.isEmpty()){
                    Toast.makeText(LevantarPedido.this, "Elija al menos 1 producto.", Toast.LENGTH_SHORT).show();
                    return;
                }
                OperacionesBaseDatos datos = OperacionesBaseDatos
                        .obtenerInstancia(LevantarPedido.this.getApplicationContext());
                datos.getDb().beginTransaction();
                try {
                    String fechaActual = Calendar.getInstance().getTime().toString();

                    String formaPago1 = datos.insertarFormaPago(new FormaPago(null, "Efectivo"));
                    String pedido1 = datos.insertarCabeceraPedido(
                            new CabeceraPedido(null, fechaActual, Cliente.clienteSeleccionado.idCliente, formaPago1));
                    int count = 1;
                    for (Producto p : productos) {
                        datos.insertarDetallePedido(new DetallePedido(pedido1, count++, p.idProducto, p.cantidad, p.precio));

                    }
                    datos.getDb().setTransactionSuccessful();
                }finally{
                    datos.getDb().endTransaction();
                }
                productos.clear();
                Toast.makeText(LevantarPedido.this, "Pedido Guardado", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LevantarPedido.this.finish();
                    }
                }, 2000);
            }
        });

    }
}
