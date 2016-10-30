package com.inge2.gestorventas.gestordeventas.ui;

/**
 * Created by ADMIN on 17/10/2016.
 */

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.View;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.inge2.gestorventas.gestordeventas.modelo.table.ClienteTableAdapter;
import com.inge2.gestorventas.gestordeventas.sqlite.OperacionesBaseDatos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class MostrarCliente extends AppCompatActivity {
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
                .setName("MostrarCliente Page") // TODO: Define a title for the content shown.
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

            try {

                datos.getDb().beginTransaction();

                return null;
            }finally {
                datos.getDb().endTransaction();
            }
        }
    }



    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_cliente);
        TableView table = (TableView) findViewById(R.id.tableView);
        //setSupportActionBar(toolbar);
        this.setTitle("             Mis Clientes                ");


        //getApplicationContext().deleteDatabase("pedidos.db");
        datos = OperacionesBaseDatos
                .obtenerInstancia(getApplicationContext());


       new TareaPruebaDatos().execute();

        Cursor cursor = datos.obtenerClientes();
        List<Cliente> clientes = new ArrayList<>();
        while (cursor.moveToNext()) {
            Cliente cliente = new Cliente(cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5));
            clientes.add(cliente);
        }
        table.addDataClickListener(new TableDataClickListener<Cliente>() {
            @Override
            public void onDataClicked(int rowIndex, Cliente clickedCliente) {
                String clickedClienteString = clickedCliente.apellidos + ", " + clickedCliente.nombres;
                Toast.makeText(MostrarCliente.this , clickedClienteString, Toast.LENGTH_SHORT).show();
                Cliente.clienteSeleccionado = clickedCliente;
                Intent intent = new Intent(MostrarCliente.this, LevantarPedido.class);
                MostrarCliente.this.startActivity(intent);
            }
        });
        table.setDataAdapter(new ClienteTableAdapter(this, clientes));
        table.setHeaderAdapter(new SimpleTableHeaderAdapter(this, "Nombre"
                , "Direccion", "Telefono"));
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


}

