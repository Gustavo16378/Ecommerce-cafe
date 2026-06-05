package br.unitins.topicos1.cafe.service;

import java.util.List;

import br.unitins.topicos1.cafe.model.Categoria;
import br.unitins.topicos1.cafe.repository.CategoriaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CategoriaService {
    
    @Inject
    CategoriaRepository repository;

    public List<Categoria> listar() {
        return repository.listAll();
    }

    @Transactional
    public void salvar(Categoria categoria) {
        repository.persist(categoria);
    }

    @Transactional
    public void atualizar(Long id, Categoria categoria) {
        Categoria existente = repository.findById(id);
        if (existente == null) {
        throw new RuntimeException("Categoria não encontrada");
    }
    existente.setNome(categoria.getNome());
    }

    @Transactional
    public void excluir (Long id) {
        repository.deleteById(id);
    }
}
