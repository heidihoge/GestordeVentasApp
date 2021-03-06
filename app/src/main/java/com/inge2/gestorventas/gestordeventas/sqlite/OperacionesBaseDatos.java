package com.inge2.gestorventas.gestordeventas.sqlite;

/**
 * Created by ADMIN on 17/10/2016.
 */

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.DatabaseUtils;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteQueryBuilder;
    import android.provider.BaseColumns;
    import android.util.Log;

    import com.inge2.gestorventas.gestordeventas.modelo.CabeceraPedido;
    import com.inge2.gestorventas.gestordeventas.modelo.Cliente;
    import com.inge2.gestorventas.gestordeventas.modelo.DetallePedido;
    import com.inge2.gestorventas.gestordeventas.modelo.FormaPago;
    import com.inge2.gestorventas.gestordeventas.modelo.Producto;
    import com.inge2.gestorventas.gestordeventas.sqlite.BaseDatosPedidos.Tablas;
    import com.inge2.gestorventas.gestordeventas.sqlite.ContratoPedidos.CabecerasPedido;
    import com.inge2.gestorventas.gestordeventas.sqlite.ContratoPedidos.Clientes;
    import com.inge2.gestorventas.gestordeventas.sqlite.ContratoPedidos.DetallesPedido;
    import com.inge2.gestorventas.gestordeventas.sqlite.ContratoPedidos.FormasPago;
    import com.inge2.gestorventas.gestordeventas.sqlite.ContratoPedidos.Productos;

    import java.util.Calendar;

/**
     * Clase auxiliar que implementa a {@link BaseDatosPedidos} para llevar a cabo el CRUD
     * sobre las entidades existentes.
     */
    public final class OperacionesBaseDatos {

        private static BaseDatosPedidos baseDatos;

        private static OperacionesBaseDatos instancia = new OperacionesBaseDatos();
    private static volatile Boolean queryExecuted = false;


    private OperacionesBaseDatos() {
        }

        public static OperacionesBaseDatos obtenerInstancia(Context contexto) {
            if (baseDatos == null) {
                baseDatos = new BaseDatosPedidos(contexto);
            }
            return instancia;
        }

        // [OPERACIONES_CABECERA_PEDIDO]
        public Cursor obtenerCabecerasPedidos() {
            SQLiteDatabase db = baseDatos.getReadableDatabase();

            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

            builder.setTables(CABECERA_PEDIDO_JOIN_CLIENTE_Y_FORMA_PAGO);

            return builder.query(db, proyCabeceraPedido, null, null, null, null, null);
        }

        public Cursor obtenerCabacerasPedidosRaw(){
            SQLiteDatabase db = baseDatos.getReadableDatabase();
            String sql = String.format("SELECT * FROM %s", Tablas.CABECERA_PEDIDO);
            return db.rawQuery(sql, null);
        }

        public Cursor obtenerCabeceraPorId(String id) {
            SQLiteDatabase db = baseDatos.getWritableDatabase();

            String selection = String.format("%s=?", CabecerasPedido.ID);
            String[] selectionArgs = {id};

            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(CABECERA_PEDIDO_JOIN_CLIENTE_Y_FORMA_PAGO);

            String[] proyeccion = {
                    Tablas.CABECERA_PEDIDO + "." + CabecerasPedido.ID,
                    CabecerasPedido.FECHA,
                    Clientes.NOMBRES,
                    Clientes.APELLIDOS,
                    FormasPago.NOMBRE};

            return builder.query(db, proyeccion, selection, selectionArgs, null, null, null);
        }

        public String insertarCabeceraPedido(CabeceraPedido pedido) {
            SQLiteDatabase db = baseDatos.getWritableDatabase();

            // Generar Pk
            String idCabeceraPedido = CabecerasPedido.generarIdCabeceraPedido();

            ContentValues valores = new ContentValues();
            valores.put(CabecerasPedido.ID, idCabeceraPedido);
            valores.put(CabecerasPedido.FECHA, pedido.fecha);
            valores.put(CabecerasPedido.ID_CLIENTE, pedido.idCliente);
            valores.put(CabecerasPedido.ID_FORMA_PAGO, pedido.idFormaPago);

            // Insertar cabecera
            db.insertOrThrow(Tablas.CABECERA_PEDIDO, null, valores);

            return idCabeceraPedido;
        }

        public boolean actualizarCabeceraPedido(CabeceraPedido pedidoNuevo) {
            SQLiteDatabase db = baseDatos.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put(CabecerasPedido.FECHA, pedidoNuevo.fecha);
            valores.put(CabecerasPedido.ID_CLIENTE, pedidoNuevo.idCliente);
            valores.put(CabecerasPedido.ID_FORMA_PAGO, pedidoNuevo.idFormaPago);

            String whereClause = String.format("%s=?", CabecerasPedido.ID);
            String[] whereArgs = {pedidoNuevo.idCabeceraPedido};

            int resultado = db.update(Tablas.CABECERA_PEDIDO, valores, whereClause, whereArgs);

            return resultado > 0;
        }

        public boolean eliminarCabeceraPedido(String idCabeceraPedido) {
            SQLiteDatabase db = baseDatos.getWritableDatabase();

            String whereClause = CabecerasPedido.ID + "=?";
            String[] whereArgs = {idCabeceraPedido};

            int resultado = db.delete(Tablas.CABECERA_PEDIDO, whereClause, whereArgs);

            return resultado > 0;
        }
        // [/OPERACIONES_CABECERA_PEDIDO]

        // [OPERACIONES_DETALLE_PEDIDO]
        public Cursor obtenerDetallesPedido() {
            SQLiteDatabase db = baseDatos.getReadableDatabase();

            String sql = String.format("SELECT * FROM %s", Tablas.DETALLE_PEDIDO);

            return db.rawQuery(sql, null);
        }

        public Cursor obtenerDetallesPorIdPedido(String idCabeceraPedido) {
            SQLiteDatabase db = baseDatos.getReadableDatabase();

            String sql = String.format("SELECT * FROM %s WHERE %s=?",
                    Tablas.DETALLE_PEDIDO, CabecerasPedido.ID);

            String[] selectionArgs = {idCabeceraPedido};

            return db.rawQuery(sql, selectionArgs);

        }

        public String insertarDetallePedido(DetallePedido detalle) {
            SQLiteDatabase db = baseDatos.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put(DetallesPedido.ID, detalle.idCabeceraPedido);
            valores.put(DetallesPedido.SECUENCIA, detalle.secuencia);
            valores.put(DetallesPedido.ID_PRODUCTO, detalle.idProducto);
            valores.put(DetallesPedido.CANTIDAD, detalle.cantidad);
            valores.put(DetallesPedido.PRECIO, detalle.precio);

            db.insertOrThrow(Tablas.DETALLE_PEDIDO, null, valores);

            return String.format("%s#%d", detalle.idCabeceraPedido, detalle.secuencia);

        }

        public boolean actualizarDetallePedido(DetallePedido detalle) {
            SQLiteDatabase db = baseDatos.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put(DetallesPedido.SECUENCIA, detalle.secuencia);
            valores.put(DetallesPedido.CANTIDAD, detalle.cantidad);
            valores.put(DetallesPedido.PRECIO, detalle.precio);

            String selection = String.format("%s=? AND %s=?",
                    DetallesPedido.ID, DetallesPedido.SECUENCIA);
            final String[] whereArgs = {detalle.idCabeceraPedido, String.valueOf(detalle.secuencia)};

            int resultado = db.update(Tablas.DETALLE_PEDIDO, valores, selection, whereArgs);

            return resultado > 0;
        }

        public boolean eliminarDetallePedido(String idCabeceraPedido, int secuencia) {
            SQLiteDatabase db = baseDatos.getWritableDatabase();

            String selection = String.format("%s=? AND %s=?",
                    DetallesPedido.ID, DetallesPedido.SECUENCIA);
            String[] whereArgs = {idCabeceraPedido, String.valueOf(secuencia)};

            int resultado = db.delete(Tablas.DETALLE_PEDIDO, selection, whereArgs);

            return resultado > 0;
        }
        // [/OPERACIONES_DETALLE_PEDIDO]

        // [OPERACIONES_PRODUCTO]
        public Cursor obtenerProductos() {
            SQLiteDatabase db = baseDatos.getReadableDatabase();

            String sql = String.format("SELECT * FROM %s", Tablas.PRODUCTO);
            return db.rawQuery(sql, null);
        }

        public String insertarProducto(Producto producto) {
            SQLiteDatabase db = baseDatos.getWritableDatabase();

            ContentValues valores = new ContentValues();
            // Generar Pk
            String idProducto = Productos.generarIdProducto();
            valores.put(Productos.ID, idProducto);
            valores.put(Productos.NOMBRE, producto.nombre);
            valores.put(Productos.PRECIO, producto.precio);
            valores.put(Productos.EXISTENCIAS, producto.existencias);

            db.insertOrThrow(Tablas.PRODUCTO, null, valores);

            return idProducto;

        }

        public boolean actualizarProducto(Producto producto) {
            SQLiteDatabase db = baseDatos.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put(Productos.NOMBRE, producto.nombre);
            valores.put(Productos.PRECIO, producto.precio);
            valores.put(Productos.EXISTENCIAS, producto.existencias);

            String whereClause = String.format("%s=?", Productos.ID);
            String[] whereArgs = {producto.idProducto};

            int resultado = db.update(Tablas.PRODUCTO, valores, whereClause, whereArgs);

            return resultado > 0;
        }

        public boolean eliminarProducto(String idProducto) {
            SQLiteDatabase db = baseDatos.getWritableDatabase();

            String whereClause = String.format("%s=?", Productos.ID);
            String[] whereArgs = {idProducto};

            int resultado = db.delete(Tablas.PRODUCTO, whereClause, whereArgs);

            return resultado > 0;
        }
        // [/OPERACIONES_PRODUCTO]

        // [OPERACIONES_CLIENTE]
        public Cursor obtenerClientes() {
            SQLiteDatabase db = baseDatos.getReadableDatabase();

            String sql = String.format("SELECT * FROM %s", Tablas.CLIENTE);

            return db.rawQuery(sql, null);
        }

        public Cliente obtenerClientesPorId(String id){
            SQLiteDatabase db = baseDatos.getReadableDatabase();

            String sql = String.format("SELECT * FROM %s WHERE %s = '%s'",
                    Tablas.CLIENTE, Clientes.ID, id);

            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToNext();
            Cliente cliente = new Cliente(cursor.getString(1), cursor.getString(2),
                    cursor.getString(3),cursor.getString(4), cursor.getString(5));
            return cliente;
        }

        public String insertarCliente(Cliente cliente) {
            SQLiteDatabase db = baseDatos.getWritableDatabase();

            // Generar Pk
            String idCliente = Clientes.generarIdCliente();

            ContentValues valores = new ContentValues();
            valores.put(Clientes.ID, idCliente);
            valores.put(Clientes.NOMBRES, cliente.nombres);
            valores.put(Clientes.APELLIDOS, cliente.apellidos);
            valores.put(Clientes.TELEFONO, cliente.telefono);
            valores.put(Clientes.DIRECCION, cliente.direccion);

            return db.insertOrThrow(Tablas.CLIENTE, null, valores) > 0 ? idCliente : null;
        }

        public boolean actualizarCliente(Cliente cliente) {
            SQLiteDatabase db = baseDatos.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put(Clientes.NOMBRES, cliente.nombres);
            valores.put(Clientes.APELLIDOS, cliente.apellidos);
            valores.put(Clientes.TELEFONO, cliente.telefono);

            String whereClause = String.format("%s=?", Clientes.ID);
            final String[] whereArgs = {cliente.idCliente};

            int resultado = db.update(Tablas.CLIENTE, valores, whereClause, whereArgs);

            return resultado > 0;
        }

        public boolean eliminarCliente(String idCliente) {
            SQLiteDatabase db = baseDatos.getWritableDatabase();

            String whereClause = String.format("%s=?", Clientes.ID);
            final String[] whereArgs = {idCliente};

            int resultado = db.delete(Tablas.CLIENTE, whereClause, whereArgs);

            return resultado > 0;
        }
        // [/OPERACIONES_CLIENTE]

        // [OPERACIONES_FORMA_PAGO]
        public Cursor obtenerFormasPago() {
            SQLiteDatabase db = baseDatos.getReadableDatabase();

            String sql = String.format("SELECT * FROM %s", Tablas.FORMA_PAGO);

            return db.rawQuery(sql, null);
        }

        public String insertarFormaPago(FormaPago formaPago) {
            SQLiteDatabase db = baseDatos.getWritableDatabase();

            // Generar Pk
            String idFormaPago = FormasPago.generarIdFormaPago();

            ContentValues valores = new ContentValues();
            valores.put(FormasPago.ID, idFormaPago);
            valores.put(FormasPago.NOMBRE, formaPago.nombre);

            return db.insertOrThrow(Tablas.FORMA_PAGO, null, valores) > 0 ? idFormaPago : null;
        }

        public boolean actualizarFormaPago(FormaPago formaPago) {
            SQLiteDatabase db = baseDatos.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put(FormasPago.NOMBRE, formaPago.nombre);

            String whereClause = String.format("%s=?", FormasPago.ID);
            String[] whereArgs = {formaPago.idFormaPago};

            int resultado = db.update(Tablas.FORMA_PAGO, valores, whereClause, whereArgs);

            return resultado > 0;
        }

        public boolean eliminarFormaPago(String idFormaPago) {
            SQLiteDatabase db = baseDatos.getWritableDatabase();

            String whereClause = String.format("%s=?", FormasPago.ID);
            String[] whereArgs = {idFormaPago};

            int resultado = db.delete(Tablas.FORMA_PAGO, whereClause, whereArgs);

            return resultado > 0;
        }

        public SQLiteDatabase getDb() {
            return baseDatos.getWritableDatabase();
        }


        // [/OPERACIONES_FORMA_PAGO]


        private static final String CABECERA_PEDIDO_JOIN_CLIENTE_Y_FORMA_PAGO = "cabecera_pedido " +
                "INNER JOIN cliente " +
                "ON cabecera_pedido.id_cliente = cliente.id " +
                "INNER JOIN forma_pago " +
                "ON cabecera_pedido.id_forma_pago = forma_pago.id";


        private final String[] proyCabeceraPedido = new String[]{
                Tablas.CABECERA_PEDIDO + "." + CabecerasPedido.ID,
                CabecerasPedido.FECHA,
                Tablas.CLIENTE + "." + Clientes.ID,
                Clientes.NOMBRES,
                Clientes.APELLIDOS,
                FormasPago.NOMBRE};

        public void bootstrap(){
            if(queryExecuted){
                return;
            }
            queryExecuted = Boolean.TRUE;
            // [INSERCIONES]
            String fechaActual = Calendar.getInstance().getTime().toString();
            OperacionesBaseDatos datos = this;
            try {

                datos.getDb().beginTransaction();
                String sql = String.format("SELECT * FROM %s", Tablas.PRODUCTO);
                Cursor cursor = datos.getDb().rawQuery(sql, null);
                cursor.moveToNext();
                if(cursor.getCount() > 0){
                    return;
                }






                // Inserción Clientes
                String cliente1 = datos.insertarCliente(new Cliente(null, "Heidi", "Hoge", "501476", "Leopardi 1912"));
                String cliente2 = datos.insertarCliente(new Cliente(null, "Laura", "Calderon", "500147", "E.Ayala 123"));
                String cliente3 = datos.insertarCliente(new Cliente(null, "Marilia", "Sanchez", "521474", "Mcal.Lopez 455"));

                // Inserción Formas de pago
                String formaPago1 = datos.insertarFormaPago(new FormaPago(null, "Efectivo"));
                String formaPago2 = datos.insertarFormaPago(new FormaPago(null, "Efectivo"));


                // Inserción Productos
                String producto1 = datos.insertarProducto(new Producto(null, "Jugo De Naranja", 8000,250));
                String producto2 = datos.insertarProducto(new Producto(null, "Jugo De Pera", 5000, 250));
                String producto3 = datos.insertarProducto(new Producto(null, "Pan de Gluten", 14000, 250));
                String producto4 = datos.insertarProducto(new Producto(null, "Mermelada de Frutilla",6500, 250));

                // Inserción Pedidos
                String pedido1 = datos.insertarCabeceraPedido(
                        new CabeceraPedido(null, fechaActual, cliente1, formaPago1));
                String pedido2 = datos.insertarCabeceraPedido(
                        new CabeceraPedido(null, fechaActual, cliente2, formaPago2));

                // Inserción Detalles
                datos.insertarDetallePedido(new DetallePedido(pedido1, 1, producto1, 5, 2));
                datos.insertarDetallePedido(new DetallePedido(pedido1, 2, producto2, 10, 3));
                datos.insertarDetallePedido(new DetallePedido(pedido2, 1, producto3, 30, 5));
                datos.insertarDetallePedido(new DetallePedido(pedido2, 2, producto4, 20, 3.6f));

                // Eliminación Pedido
                datos.eliminarCabeceraPedido(pedido1);

                // Actualización Cliente
                //datos.actualizarCliente(new Cliente(cliente2, "Laurita", "Calderon", "3333333", "abcd"));

                datos.getDb().setTransactionSuccessful();
            } finally {
                datos.getDb().endTransaction();
            }
//
//            // [QUERIES]
//            Log.d("Clientes", "Clientes");
//            DatabaseUtils.dumpCursor(datos.obtenerClientes());
//            Log.d("Formas de pago", "Formas de pago");
//            DatabaseUtils.dumpCursor(datos.obtenerFormasPago());
//            Log.d("Productos", "Productos");
//            DatabaseUtils.dumpCursor(datos.obtenerProductos());
//            Log.d("Cabeceras de pedido", "Cabeceras de pedido");
//            DatabaseUtils.dumpCursor(datos.obtenerCabecerasPedidos());
//            Log.d("Detalles de pedido", "Detalles de pedido");
//            DatabaseUtils.dumpCursor(datos.obtenerDetallesPedido());

        }

    }


