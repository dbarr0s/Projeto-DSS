package esideal.station.cliente;

public interface ICliente {
    boolean clienteValido(Integer NIF);
    boolean clienteTemVeiculos(Cliente c);
}
