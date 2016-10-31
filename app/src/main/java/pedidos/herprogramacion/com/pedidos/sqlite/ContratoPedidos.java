package pedidos.herprogramacion.com.pedidos.sqlite;

import java.util.UUID;

/**
 * Clase que establece los nombres a usar en la base de datos
 */
public class ContratoPedidos {

    interface ColumnasCabeceraPedido {
        String ID = "id";
        String FECHA = "fecha";
        String ID_CLIENTE = "id_cliente";
    }

    interface ColumnasDetallePedido {
        String ID = "id";
        String SECUENCIA = "secuencia";
        String ID_PRODUCTO = "id_producto";
        String CANTIDAD = "cantidad";
        String PRECIO = "precio";
    }

    interface ColumnasProducto {
        String ID = "id";
        String NOMBRE = "nombre";
        String PRECIO = "precio";
        String EXISTENCIAS = "existencias";
    }

    interface ColumnasCliente {
        String ID = "id";
        String NOMBRES = "nombres";
        String APELLIDOS = "apellidos";
        String TELEFONO = "telefono";
        String DIRECCION = "direccion";
    }

    public static class CabecerasPedido implements ColumnasCabeceraPedido {
        public static String generarIdCabeceraPedido() {
            return "CP-" + UUID.randomUUID().toString();
        }
    }

    public static class DetallesPedido implements ColumnasDetallePedido {
        public static String generarIdDetallePedido() {
            return "DP-" + UUID.randomUUID().toString();
        }
    }

    public static class Productos implements ColumnasProducto{
        public static String generarIdProducto() {
            return "PRO-" + UUID.randomUUID().toString();
        }
    }

    public static class Clientes implements ColumnasCliente{
        public static String generarIdCliente() {
            return "CLI-" + UUID.randomUUID().toString();
        }
    }

    private ContratoPedidos() {
    }

}