package pedidos.herprogramacion.com.pedidos.modelo;

import java.io.Serializable;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class DetallePedido implements Serializable {

    public String idCabeceraPedido;

    public int secuencia;

    public String idProducto;

    public int cantidad;

    public float precio;

    public DetallePedido(String idCabeceraPedido, int secuencia,
                         String idProducto, int cantidad, float precio) {
        this.idCabeceraPedido = idCabeceraPedido;
        this.secuencia = secuencia;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public void setCantidad(int cantidad) {

        this.cantidad = cantidad;
    }

    public void setIdCabeceraPedido(String id) {
        this.idCabeceraPedido = id;
    }
}