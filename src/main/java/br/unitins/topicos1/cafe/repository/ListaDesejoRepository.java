package br.unitins.topicos1.cafe.repository;

import br.unitins.topicos1.cafe.model.ListaDesejo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class ListaDesejoRepository implements PanacheRepository<ListaDesejo> {

    public Optional<ListaDesejo> findByUsuarioLogin(String login) {
        return find("usuario.login", login).firstResultOptional();
    }
}
