package esideal.station.servico;

public interface IServico {
    void agendarServico();
    void pedirServico();
    void iniciarServico();
    void finalizarServico();
    void servExiste();
    void notificarCliente();
}
