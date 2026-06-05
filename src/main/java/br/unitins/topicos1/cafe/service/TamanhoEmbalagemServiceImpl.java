package br.unitins.topicos1.cafe.service;

import br.unitins.topicos1.cafe.dto.TamanhoEmbalagemRequestDTO;
import br.unitins.topicos1.cafe.dto.TamanhoEmbalagemResponseDTO;
import br.unitins.topicos1.cafe.mapper.TamanhoEmbalagemMapper;
import br.unitins.topicos1.cafe.model.TamanhoEmbalagem;
import br.unitins.topicos1.cafe.repository.TamanhoEmbalagemRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TamanhoEmbalagemServiceImpl {

    @Inject
    TamanhoEmbalagemRepository repository;

    @Inject
    TamanhoEmbalagemMapper mapper;

    public List<TamanhoEmbalagemResponseDTO> listar() {
        return repository.listAll().stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    public TamanhoEmbalagemResponseDTO buscarPorId(Long id) {
        TamanhoEmbalagem t = repository.findById(id);
        if (t == null) throw new NotFoundException("Tamanho de embalagem não encontrado");
        return mapper.toResponseDTO(t);
    }

    @Transactional
    public TamanhoEmbalagemResponseDTO salvar(TamanhoEmbalagemRequestDTO dto) {
        TamanhoEmbalagem t = mapper.toModel(dto);
        repository.persist(t);
        return mapper.toResponseDTO(t);
    }

    @Transactional
    public TamanhoEmbalagemResponseDTO atualizar(Long id, TamanhoEmbalagemRequestDTO dto) {
        TamanhoEmbalagem t = repository.findById(id);
        if (t == null) throw new NotFoundException("Tamanho de embalagem não encontrado");
        t.setGramas(dto.getGramas());
        return mapper.toResponseDTO(t);
    }

    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
