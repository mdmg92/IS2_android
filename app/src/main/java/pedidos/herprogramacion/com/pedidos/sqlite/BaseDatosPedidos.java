package pedidos.herprogramacion.com.pedidos.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

import pedidos.herprogramacion.com.pedidos.sqlite.ContratoPedidos.CabecerasPedido;
import pedidos.herprogramacion.com.pedidos.sqlite.ContratoPedidos.DetallesPedido;
import pedidos.herprogramacion.com.pedidos.sqlite.ContratoPedidos.Productos;
import pedidos.herprogramacion.com.pedidos.sqlite.ContratoPedidos.Clientes;

/**
 * Clase que administra la conexión de la base de datos y su estructuración
 */
public class BaseDatosPedidos extends SQLiteOpenHelper {

    private static final String NOMBRE_BASE_DATOS = "pedidos.db";

    private static final int VERSION_ACTUAL = 1;

    private final Context contexto;

    interface Tablas {
        String CABECERA_PEDIDO = "cabecera_pedido";
        String DETALLE_PEDIDO = "detalle_pedido";
        String PRODUCTO = "producto";
        String CLIENTE = "cliente";
        String FORMA_PAGO = "forma_pago";
    }

    interface Referencias {

        String ID_CABECERA_PEDIDO = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                Tablas.CABECERA_PEDIDO, CabecerasPedido.ID);

        String ID_PRODUCTO = String.format("REFERENCES %s(%s)",
                Tablas.PRODUCTO, Productos.ID);

        String ID_CLIENTE = String.format("REFERENCES %s(%s)",
                Tablas.CLIENTE, Clientes.ID);
    }

    public BaseDatosPedidos(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL,%s DATETIME NOT NULL, %s TEXT NOT NULL)",
                Tablas.CABECERA_PEDIDO, BaseColumns._ID,
                CabecerasPedido.ID, CabecerasPedido.FECHA,
                CabecerasPedido.ID_CLIENTE, Referencias.ID_CLIENTE));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL %s,%s INTEGER NOT NULL CHECK (%s>0),%s INTEGER NOT NULL %s," +
                        "%s INTEGER NOT NULL,%s REAL NOT NULL )",
                Tablas.DETALLE_PEDIDO, BaseColumns._ID,
                DetallesPedido.ID, Referencias.ID_CABECERA_PEDIDO,
                DetallesPedido.SECUENCIA, DetallesPedido.SECUENCIA,
                DetallesPedido.ID_PRODUCTO, Referencias.ID_PRODUCTO,
                DetallesPedido.CANTIDAD, DetallesPedido.PRECIO));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s REAL NOT NULL," +
                        "%s INTEGER NOT NULL CHECK(%s>=0) )",
                Tablas.PRODUCTO, BaseColumns._ID,
                Productos.ID, Productos.NOMBRE, Productos.PRECIO,
                Productos.EXISTENCIAS, Productos.EXISTENCIAS));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE,%s TEXT NOT NULL,%s TEXT NOT NULL,%s TEXT NOT NULL, %s )",
                Tablas.CLIENTE, BaseColumns._ID,
                Clientes.ID, Clientes.NOMBRES, Clientes.APELLIDOS, Clientes.TELEFONO, Clientes.DIRECCION));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Tablas.CABECERA_PEDIDO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.DETALLE_PEDIDO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.PRODUCTO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.CLIENTE);

        onCreate(db);
    }


}