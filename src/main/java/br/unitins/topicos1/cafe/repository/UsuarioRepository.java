package br.unitins.topicos1.cafe.repository;

import br.unitins.topicos1.cafe.model.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {

    public Usuario findByLogin(String login) {
        return find("login", login).firstResult();
    }

    public Usuario findByEmail(String email) {
        return find("email", email).firstResult();
    }
}
