package pedidos.herprogramacion.com.pedidos;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import pedidos.herprogramacion.com.pedidos.modelo.CabeceraPedido;
import pedidos.herprogramacion.com.pedidos.modelo.DetallePedido;
import pedidos.herprogramacion.com.pedidos.sqlite.ContratoPedidos;
import pedidos.herprogramacion.com.pedidos.sqlite.OperacionesBaseDatos;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.icu.text.RelativeDateTimeFormatter.Direction.THIS;

public class ProductosActivity extends AppCompatActivity {

    OperacionesBaseDatos datos;
    DetallePedido det;
    List<DetallePedido> carrito = new LinkedList<DetallePedido>();
    CabeceraPedido cab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        Intent intent = getIntent();
        cab = (CabeceraPedido) intent.getSerializableExtra("CabeceraPedido");
        datos = OperacionesBaseDatos.obtenerInstancia(getApplicationContext());

        Spinner spinner = (Spinner) findViewById(R.id.planets_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.products_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                String nombre = arg0.getSelectedItem().toString();
                String id_producto = "";
                String nombre_producto = "";
                float precio = 0;
                Cursor producto = datos.getProducto(nombre);

                if (producto != null && producto.moveToFirst()) {
                    nombre_producto = producto.getString(producto.getColumnIndex(ContratoPedidos.Productos.NOMBRE));
                    precio = producto.getFloat(producto.getColumnIndex(ContratoPedidos.Productos.PRECIO));
                    id_producto = producto.getString(producto.getColumnIndex(ContratoPedidos.Productos.ID));
                    producto.close();
                }

                displayPrice((int)precio);
                displayTotal((int)precio);
                det = new DetallePedido(cab.idCabeceraPedido, (int)(Math.random() + 1), id_producto, 0, precio);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                displayPrice(0);
            }
        });
    }

    int quantity = 1;
    float price = 1;
    int total = 0;

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        TextView mensaje = (TextView) findViewById(R.id.mensaje);
        mensaje.setText("");

        if (carrito.size() == 0 ) {
            mensaje.setText("Carrito de Compras vacio");
        }
        else {
            String priceMessage = "Total: $" + quantity * det.precio + "\nGracias por su compra!";
            TextView quantityTextView = (TextView) findViewById(
                    R.id.quantity_text_view);
            display(this.quantity);
            displayMessage(priceMessage);
            Intent intent = new Intent(this, CarritoActivity.class);
            intent.putExtra("DetallePedido", (Serializable) carrito);
            intent.putExtra("CabeceraPedido", cab);
            startActivity(intent);
        }
    }

    public void agregarProducto(View view) {
        TextView mensaje = (TextView) findViewById(R.id.mensaje);
        mensaje.setText("");

        det.setCantidad(this.quantity);
        if (carrito.contains(det)) {
            carrito.remove(det);
        }
        carrito.add(det);
        this.quantity = 1;
        this.price = det.precio;
        display(this.quantity);
        displayTotal(this.quantity * this.price);
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.planets_spinner);
        String producto = spinner.getSelectedItem().toString();

        this.quantity = this.quantity + 1;
        display(this.quantity);
        displayTotal(this.quantity * det.precio);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        this.quantity = quantity - 1;
        if (this.quantity == 0) {
            this.quantity = 1;
        }
        display(quantity);
        displayTotal(quantity * det.precio);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given price on the screen.
     */
    private void displayPrice(int number) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }

    /**
     * This method displays the given total on the screen.
     */
    private void displayTotal(float number) {
        TextView priceTextView = (TextView) findViewById(R.id.total_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(message);
    }
}
