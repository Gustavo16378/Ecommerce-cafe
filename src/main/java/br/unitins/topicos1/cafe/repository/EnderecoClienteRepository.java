package br.unitins.topicos1.cafe.repository;

import br.unitins.topicos1.cafe.model.EnderecoCliente;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class EnderecoClienteRepository implements PanacheRepository<EnderecoCliente> {

    public List<EnderecoCliente> findByUsuarioId(Long usuarioId) {
        return list("usuario.id", usuarioId);
    }
}
