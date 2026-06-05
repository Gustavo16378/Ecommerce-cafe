package br.unitins.topicos1.cafe.repository;

import br.unitins.topicos1.cafe.model.MaterialEmbalagem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MaterialEmbalagemRepository implements PanacheRepository<MaterialEmbalagem> {
}
