package br.unitins.topicos1.cafe.repository;

import br.unitins.topicos1.cafe.model.LoteEstoque;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoteEstoqueRepository implements PanacheRepository<LoteEstoque> {
}
