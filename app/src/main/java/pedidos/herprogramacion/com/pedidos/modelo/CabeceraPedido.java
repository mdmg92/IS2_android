package pedidos.herprogramacion.com.pedidos.modelo;

import java.io.Serializable;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class CabeceraPedido implements Serializable{

    public String idCabeceraPedido;

    public String fecha;

    public String idCliente;

    public CabeceraPedido(String idCabeceraPedido, String fecha,
                          String idCliente) {
        this.idCabeceraPedido = idCabeceraPedido;
        this.fecha = fecha;
        this.idCliente = idCliente;
    }
}