package br.unitins.topicos1.cafe.repository;

import br.unitins.topicos1.cafe.model.Pedido;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PedidoRepository implements PanacheRepository<Pedido> {

    public List<Pedido> findByUsuarioLogin(String login) {
        return list("usuario.login", login);
    }
}
