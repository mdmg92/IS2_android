package pedidos.herprogramacion.com.pedidos;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import pedidos.herprogramacion.com.pedidos.modelo.CabeceraPedido;
import pedidos.herprogramacion.com.pedidos.modelo.DetallePedido;
import pedidos.herprogramacion.com.pedidos.sqlite.ContratoPedidos;
import pedidos.herprogramacion.com.pedidos.sqlite.OperacionesBaseDatos;

import static android.R.attr.id;
import static android.R.interpolator.linear;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class CarritoActivity extends AppCompatActivity {

    OperacionesBaseDatos datos;
    CabeceraPedido cab;
    List<DetallePedido> carrito = new LinkedList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());

        Intent intent = getIntent();
        cab = (CabeceraPedido) intent.getSerializableExtra("CabeceraPedido");
        carrito = (List<DetallePedido>) intent.getSerializableExtra("DetallePedido");
        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());

        // Guardar Cabecera
        String id_cabecera = datos.insertarCabeceraPedido(cab);
        Cursor cabecera = datos.obtenerCabeceraPorId(id_cabecera);
        String fecha = "", nombre = "", apellido = "";
        if (cabecera != null && cabecera.moveToFirst()) {
            fecha = cabecera.getString(cabecera.getColumnIndex(ContratoPedidos.CabecerasPedido.FECHA));
            nombre = cabecera.getString(cabecera.getColumnIndex(ContratoPedidos.Clientes.NOMBRES));
            apellido = cabecera.getString(cabecera.getColumnIndex(ContratoPedidos.Clientes.APELLIDOS));
        }
        // Mostrar Cabecera
        TextView fecha_cab = new TextView(this);
        TextView cliente_cab = new TextView(this);

        fecha_cab.setText("Fecha: " + fecha);
        cliente_cab.setText("Cliente: " + apellido + ", " + nombre);

        final TableLayout tabla = (TableLayout) getLayoutInflater().inflate(R.layout.tabla_template, null);
        setContentView(tabla);
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 4.0f);

        tabla.addView(fecha_cab);
        tabla.addView(cliente_cab);

        TableRow row = new TableRow(this);
        TextView row_producto = (TextView) getLayoutInflater().inflate(R.layout.fila_template, null);
        TextView row_precio = (TextView) getLayoutInflater().inflate(R.layout.fila_template, null);
        TextView row_cantidad = (TextView) getLayoutInflater().inflate(R.layout.fila_template, null);
        TextView row_total = (TextView) getLayoutInflater().inflate(R.layout.fila_template, null);

        row_producto.setText("Producto");
        row_total.setText("Total");
        row_cantidad.setText("Cantidad");
        row_precio.setText("Precio");

        row.addView(row_producto);
        row.addView(row_precio);
        row.addView(row_cantidad);
        row.addView(row_total);
        tabla.addView(row);

        float total_tabla = 0;
        // Mostrar detalles
        for (int i = 0; i < carrito.size(); i++) {
            TableRow fila = new TableRow(this);
            fila.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 4.0f));

            TextView producto = (TextView) getLayoutInflater().inflate(R.layout.fila_template, null);
            TextView precio = (TextView) getLayoutInflater().inflate(R.layout.fila_template, null);
            TextView cantidad = (TextView) getLayoutInflater().inflate(R.layout.fila_template, null);
            TextView total = (TextView) getLayoutInflater().inflate(R.layout.fila_template, null);
            DetallePedido det = carrito.get(i);
            Cursor detalle = datos.getProducto(det.idProducto);
            if (detalle != null && detalle.moveToFirst()) {
                producto.setText(detalle.getString(detalle.getColumnIndex(ContratoPedidos.Productos.NOMBRE)));
                precio.setText(detalle.getString(detalle.getColumnIndex(ContratoPedidos.Productos.PRECIO)));
                cantidad.setText(String.valueOf(det.cantidad));
                float price = detalle.getFloat(detalle.getColumnIndex(ContratoPedidos.Productos.PRECIO));
                float total_aux = price * det.cantidad;
                total.setText(String.valueOf(total_aux));
                total_tabla += total_aux;
            }
            fila.addView(producto);
            fila.addView(precio);
            fila.addView(cantidad);
            fila.addView(total);
            tabla.addView(fila);
        }

        TableRow footer = new TableRow(this);
        TextView row_total_tabla = (TextView) getLayoutInflater().inflate(R.layout.fila_template, null);
        row_total_tabla.setText("Total: $" + String.valueOf(total_tabla));
        footer.addView(row_total_tabla);
        tabla.addView(footer);

        final Button compra = new Button(this); int id_compra = 0;
        compra.setText("Realizar compra"); compra.setId(id_compra);
        compra.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (compra.getId() == ((Button) v).getId()) {
                    // Guardar Detalles

                    String id_cabecera = datos.insertarCabeceraPedido(cab);
                    Cursor cabecera = datos.obtenerCabeceraPorId(id_cabecera);
                    String fecha = "", nombre = "", apellido = "";
                    if (cabecera != null && cabecera.moveToFirst()) {
                        Log.d(cabecera.getString(cabecera.getColumnIndex(ContratoPedidos.Clientes.APELLIDOS)),"Clientes");
                    }

                    for (int i = 0; i < carrito.size(); i++) {
                        DetallePedido det = carrito.get(i);
                        det.setIdCabeceraPedido(id_cabecera);
                        datos.insertarDetallePedido(det);
                    }
                }
            }
        });

        final Button cancelar = new Button(this);
        cancelar.setText("Cancelar"); int id_cancelar = 1;
        cancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (cancelar.getId() == ((Button) v).getId()) {
                    Intent mStartActivity = new Intent(getApplicationContext(), ClientesActivity.class);
                    int mPendingIntentId = 123456;
                    PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                    System.exit(0);
                }
            }
        });

        tabla.addView(compra);
        tabla.addView(cancelar);
    }
}
