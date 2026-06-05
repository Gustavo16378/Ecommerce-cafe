package br.unitins.topicos1.cafe.service;

import br.unitins.topicos1.cafe.dto.FornecedorRequestDTO;
import br.unitins.topicos1.cafe.dto.FornecedorResponseDTO;
import br.unitins.topicos1.cafe.mapper.FornecedorMapper;
import br.unitins.topicos1.cafe.model.Fornecedor;
import br.unitins.topicos1.cafe.repository.FornecedorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class FornecedorServiceImpl {

    @Inject
    FornecedorRepository repository;

    @Inject
    FornecedorMapper mapper;

    public List<FornecedorResponseDTO> listar() {
        return repository.listAll().stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    public FornecedorResponseDTO buscarPorId(Long id) {
        Fornecedor f = repository.findById(id);
        if (f == null) throw new NotFoundException("Fornecedor não encontrado");
        return mapper.toResponseDTO(f);
    }

    @Transactional
    public FornecedorResponseDTO salvar(FornecedorRequestDTO dto) {
        Fornecedor f = mapper.toModel(dto);
        repository.persist(f);
        return mapper.toResponseDTO(f);
    }

    @Transactional
    public FornecedorResponseDTO atualizar(Long id, FornecedorRequestDTO dto) {
        Fornecedor f = repository.findById(id);
        if (f == null) throw new NotFoundException("Fornecedor não encontrado");
        f.setNome(dto.getNome());
        f.setCnpj(dto.getCnpj());
        f.setContato(dto.getContato());
        f.setEndereco(mapper.toEnderecoModel(dto.getEndereco()));
        return mapper.toResponseDTO(f);
    }

    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
