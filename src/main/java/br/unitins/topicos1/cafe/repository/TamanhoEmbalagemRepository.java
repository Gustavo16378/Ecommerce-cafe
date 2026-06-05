package br.unitins.topicos1.cafe.repository;

import br.unitins.topicos1.cafe.model.TamanhoEmbalagem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TamanhoEmbalagemRepository implements PanacheRepository<TamanhoEmbalagem> {
}
