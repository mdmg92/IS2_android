package pedidos.herprogramacion.com.pedidos;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import pedidos.herprogramacion.com.pedidos.modelo.CabeceraPedido;
import pedidos.herprogramacion.com.pedidos.modelo.Cliente;
import pedidos.herprogramacion.com.pedidos.modelo.DetallePedido;
import pedidos.herprogramacion.com.pedidos.modelo.FormaPago;
import pedidos.herprogramacion.com.pedidos.modelo.Producto;
import pedidos.herprogramacion.com.pedidos.sqlite.ContratoPedidos;
import pedidos.herprogramacion.com.pedidos.sqlite.OperacionesBaseDatos;

import java.util.Calendar;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class ClientesActivity extends AppCompatActivity {

    OperacionesBaseDatos datos;
    Cliente cliente;
    CabeceraPedido cab;

    public class TareaPruebaDatos extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            // [INSERCIONES]
            String fechaActual = Calendar.getInstance().getTime().toString();

            try {

                datos.getDb().beginTransaction();

                // Inserción Clientes
                String cliente1 = datos.insertarCliente(new Cliente(null, "Mathias", "de Mestral", "4552000", "Asuncion"));
                String cliente2 = datos.insertarCliente(new Cliente(null, "Fernando", "Barrios", "4440000", "Nemby"));
                String cliente3 = datos.insertarCliente(new Cliente(null, "Sergio", "Mendez", "4440000", "Luque"));

                // Inserción Productos
                String producto1 = datos.insertarProducto(new Producto(null, "Manzana unidad", 2, 20));
                String producto2 = datos.insertarProducto(new Producto(null, "Pera unidad", 3, 10));
                String producto3 = datos.insertarProducto(new Producto(null, "Guayaba unidad", 5, 5));
                String producto4 = datos.insertarProducto(new Producto(null, "Maní unidad", 3.6f, 3));

                // Inserción Pedidos
                String pedido1 = datos.insertarCabeceraPedido(
                        new CabeceraPedido(null, fechaActual, cliente1));
                String pedido2 = datos.insertarCabeceraPedido(
                        new CabeceraPedido(null, fechaActual,cliente2));

                // Inserción Detalles
                datos.insertarDetallePedido(new DetallePedido(pedido1, 1, producto1, 5, 2));
                datos.insertarDetallePedido(new DetallePedido(pedido1, 2, producto2, 10, 3));
                datos.insertarDetallePedido(new DetallePedido(pedido2, 1, producto3, 30, 5));
                datos.insertarDetallePedido(new DetallePedido(pedido2, 2, producto4, 20, 3.6f));

                // Eliminación Pedido
                datos.eliminarCabeceraPedido(pedido1);

                datos.getDb().setTransactionSuccessful();
            } finally {
                datos.getDb().endTransaction();
            }

            // [QUERIES]
            Log.d("Clientes","Clientes");
            DatabaseUtils.dumpCursor(datos.obtenerClientes());
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
        setContentView(R.layout.clientes_activity);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        getApplicationContext().deleteDatabase("pedidos.db");
        datos = OperacionesBaseDatos
                .obtenerInstancia(getApplicationContext());

        new TareaPruebaDatos().execute();

        Spinner spinner = (Spinner) findViewById(R.id.clientes_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.clientes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                String nombre = arg0.getSelectedItem().toString();
                String nombre_cliente = "";
                String apellido_cliente = "";
                String tel_cliente = "";
                String direccion_cliente = "";
                String id_cliente = "";
                Cursor cliente = datos.getCliente(nombre);
                if (cliente != null && cliente.moveToFirst()) {
                    nombre_cliente = cliente.getString(cliente.getColumnIndex(ContratoPedidos.Clientes.NOMBRES));
                    apellido_cliente = cliente.getString(cliente.getColumnIndex(ContratoPedidos.Clientes.APELLIDOS));
                    tel_cliente = cliente.getString(cliente.getColumnIndex(ContratoPedidos.Clientes.TELEFONO));
                    id_cliente = cliente.getString(cliente.getColumnIndex(ContratoPedidos.Clientes.ID));
                    direccion_cliente = cliente.getString(cliente.getColumnIndex(ContratoPedidos.Clientes.DIRECCION));
                    cliente.close();
                }
                displayCliente(nombre_cliente, apellido_cliente, direccion_cliente, tel_cliente);
                String fecha = Calendar.getInstance().getTime().toString();
                cab = new CabeceraPedido(ContratoPedidos.CabecerasPedido.generarIdCabeceraPedido(),
                        fecha,
                        id_cliente);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void displayCliente(String nombre, String apellido, String direccion, String telefono) {
        TextView nombreTextView = (TextView) findViewById(R.id.cliente_text);
        TextView telefonoTextView = (TextView) findViewById(R.id.cliente_telefono_text);
        TextView direccionTextView = (TextView) findViewById(R.id.cliente_direccion_text);
        nombreTextView.setText(nombre + ' ' + apellido);
        telefonoTextView.setText(telefono);
        direccionTextView.setText(direccion);
    }

    public void agregarCliente(View view) {
        if (cab.equals(null)) {
            //
        } else {
            Intent intent = new Intent(this, ProductosActivity.class);
            intent.putExtra("CabeceraPedido", cab);
            startActivity(intent);
        }
    }

}