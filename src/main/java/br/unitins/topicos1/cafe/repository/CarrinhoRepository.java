package br.unitins.topicos1.cafe.repository;

import br.unitins.topicos1.cafe.model.Carrinho;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class CarrinhoRepository implements PanacheRepository<Carrinho> {

    public Optional<Carrinho> findByUsuarioLogin(String login) {
        return find("usuario.login", login).firstResultOptional();
    }
}
