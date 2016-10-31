package pedidos.herprogramacion.com.pedidos.modelo;

public class Cliente {

    public String idCliente;

    public String nombres;

    public String apellidos;

    public String telefono;

    public String direccion;

    public Cliente(String idCliente, String nombres, String apellidos, String telefono, String direccion) {
        this.idCliente = idCliente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.direccion = direccion;
    }
}