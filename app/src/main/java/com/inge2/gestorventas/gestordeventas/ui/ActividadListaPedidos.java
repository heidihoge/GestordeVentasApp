package com.inge2.gestorventas.gestordeventas.ui;

/**
 * Created by ADMIN on 17/10/2016.
 */

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.util.Log;


import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.inge2.gestorventas.gestordeventas.R;
import com.inge2.gestorventas.gestordeventas.modelo.CabeceraPedido;
import com.inge2.gestorventas.gestordeventas.modelo.Cliente;
import com.inge2.gestorventas.gestordeventas.modelo.DetallePedido;
import com.inge2.gestorventas.gestordeventas.modelo.FormaPago;
import com.inge2.gestorventas.gestordeventas.modelo.Producto;
import com.inge2.gestorventas.gestordeventas.sqlite.OperacionesBaseDatos;

import java.util.Calendar;

public class ActividadListaPedidos extends AppCompatActivity {


    private static Boolean queryExecuted = false;

    OperacionesBaseDatos datos;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ActividadListaPedidos Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    public class TareaPruebaDatos extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            if(queryExecuted){
                return null;
            }
            queryExecuted = Boolean.TRUE;
            // [INSERCIONES]
            String fechaActual = Calendar.getInstance().getTime().toString();

            try {

                datos.getDb().beginTransaction();

                // Inserción Clientes
                String cliente1 = datos.insertarCliente(new Cliente(null, "Heidi", "Hoge", "1111111", "abc"));
                String cliente2 = datos.insertarCliente(new Cliente(null, "Laura", "Calderon", "222222", "def"));
                String cliente3 = datos.insertarCliente(new Cliente(null, "Marilia", "Sanchez", "3333333", "ghi"));

                // Inserción Formas de pago
                String formaPago1 = datos.insertarFormaPago(new FormaPago(null, "Efectivo"));
                String formaPago2 = datos.insertarFormaPago(new FormaPago(null, "Efectivo"));


                // Inserción Productos
                String producto1 = datos.insertarProducto(new Producto(null, "Jugo De Naranja", 1, 8000));
                String producto2 = datos.insertarProducto(new Producto(null, "Jugo De Pera", 2, 5000));
                String producto3 = datos.insertarProducto(new Producto(null, "Pan de Gluten", 4, 14000));
                String producto4 = datos.insertarProducto(new Producto(null, "Mermelada de Frutilla", 3, 6500));

                // Inserción Pedidos
                String pedido1 = datos.insertarCabeceraPedido(
                        new CabeceraPedido(null, fechaActual, cliente1, formaPago1, null));
                String pedido2 = datos.insertarCabeceraPedido(
                        new CabeceraPedido(null, fechaActual, cliente2, formaPago2, null));

                // Inserción Detalles
                datos.insertarDetallePedido(new DetallePedido(pedido1, 1, producto1, 5, 2));
                datos.insertarDetallePedido(new DetallePedido(pedido1, 2, producto2, 10, 3));
                datos.insertarDetallePedido(new DetallePedido(pedido2, 1, producto3, 30, 5));
                datos.insertarDetallePedido(new DetallePedido(pedido2, 2, producto4, 20, 3.6f));

                // Eliminación Pedido
                datos.eliminarCabeceraPedido(pedido1);

                // Actualización Cliente
                datos.actualizarCliente(new Cliente(cliente2, "Laurita", "Calderon", "3333333", "abcd"));

                datos.getDb().setTransactionSuccessful();
            } finally {
                datos.getDb().endTransaction();
            }

            // [QUERIES]
            Log.d("Clientes", "Clientes");
            DatabaseUtils.dumpCursor(datos.obtenerClientes());
            Log.d("Formas de pago", "Formas de pago");
            DatabaseUtils.dumpCursor(datos.obtenerFormasPago());
            Log.d("Productos", "Productos");
            DatabaseUtils.dumpCursor(datos.obtenerProductos());
            Log.d("Cabeceras de pedido", "Cabeceras de pedido");
            DatabaseUtils.dumpCursor(datos.obtenerCabecerasPedidos());
            Log.d("Detalles de pedido", "Detalles de pedido");
            DatabaseUtils.dumpCursor(datos.obtenerDetallesPedido());

            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_lista_pedidos);
        ViewGroup l = (ViewGroup) findViewById(R.id.linear_layout_lista_pedidos);
        //setSupportActionBar(toolbar);


        getApplicationContext().deleteDatabase("pedidos.db");
        datos = OperacionesBaseDatos
                .obtenerInstancia(getApplicationContext());


        new TareaPruebaDatos().execute();

        Cursor cursor = datos.obtenerDetallesPedido();

        while (cursor.moveToNext()) {
            Button b = new Button(this);
            b.setText(cursor.getString(2));
            b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            l.addView(b);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


}
