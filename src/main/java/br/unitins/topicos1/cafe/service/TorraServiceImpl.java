package br.unitins.topicos1.cafe.service;

import br.unitins.topicos1.cafe.dto.TorraRequestDTO;
import br.unitins.topicos1.cafe.dto.TorraResponseDTO;
import br.unitins.topicos1.cafe.mapper.TorraMapper;
import br.unitins.topicos1.cafe.model.Torra;
import br.unitins.topicos1.cafe.repository.TorraRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TorraServiceImpl {
    @Inject
    TorraRepository repository;

    @Inject
    TorraMapper mapper;

    public List<TorraResponseDTO> listar() {
        return repository.listAll().stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    public TorraResponseDTO buscarPorId(Long id) {
        Torra torra = repository.findById(id);
        if (torra == null) throw new NotFoundException("Torra não encontrada");
        return mapper.toResponseDTO(torra);
    }

    @Transactional
    public TorraResponseDTO salvar(TorraRequestDTO dto) {
        Torra torra = mapper.toModel(dto);
        repository.persist(torra);
        return mapper.toResponseDTO(torra);
    }

    @Transactional
    public TorraResponseDTO atualizar(Long id, TorraRequestDTO dto) {
        Torra torra = repository.findById(id);
        if (torra == null) throw new NotFoundException("Torra não encontrada");
        torra.setTipo(dto.getTipo());
        return mapper.toResponseDTO(torra);
    }

    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
