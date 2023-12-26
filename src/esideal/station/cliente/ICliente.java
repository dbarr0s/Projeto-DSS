package esideal.station.cliente;

import java.util.Map;

public interface ICliente {
    boolean clienteValido(String nome);
    boolean clienteTemVeiculos(Cliente c);
    Map<String, Cliente> getClientes();
}
