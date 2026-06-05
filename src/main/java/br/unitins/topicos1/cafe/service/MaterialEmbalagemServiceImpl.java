package br.unitins.topicos1.cafe.service;

import br.unitins.topicos1.cafe.dto.MaterialEmbalagemRequestDTO;
import br.unitins.topicos1.cafe.dto.MaterialEmbalagemResponseDTO;
import br.unitins.topicos1.cafe.mapper.MaterialEmbalagemMapper;
import br.unitins.topicos1.cafe.model.MaterialEmbalagem;
import br.unitins.topicos1.cafe.repository.MaterialEmbalagemRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MaterialEmbalagemServiceImpl {

    @Inject
    MaterialEmbalagemRepository repository;

    @Inject
    MaterialEmbalagemMapper mapper;

    public List<MaterialEmbalagemResponseDTO> listar() {
        return repository.listAll().stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    public MaterialEmbalagemResponseDTO buscarPorId(Long id) {
        MaterialEmbalagem m = repository.findById(id);
        if (m == null) throw new NotFoundException("Material de embalagem não encontrado");
        return mapper.toResponseDTO(m);
    }

    @Transactional
    public MaterialEmbalagemResponseDTO salvar(MaterialEmbalagemRequestDTO dto) {
        MaterialEmbalagem m = mapper.toModel(dto);
        repository.persist(m);
        return mapper.toResponseDTO(m);
    }

    @Transactional
    public MaterialEmbalagemResponseDTO atualizar(Long id, MaterialEmbalagemRequestDTO dto) {
        MaterialEmbalagem m = repository.findById(id);
        if (m == null) throw new NotFoundException("Material de embalagem não encontrado");
        m.setNome(dto.getNome());
        return mapper.toResponseDTO(m);
    }

    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
