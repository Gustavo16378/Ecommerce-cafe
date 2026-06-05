package br.unitins.topicos1.cafe.service;

import br.unitins.topicos1.cafe.dto.LoteEstoqueRequestDTO;
import br.unitins.topicos1.cafe.dto.LoteEstoqueResponseDTO;
import br.unitins.topicos1.cafe.mapper.LoteEstoqueMapper;
import br.unitins.topicos1.cafe.model.LoteEstoque;
import br.unitins.topicos1.cafe.model.Produto;
import br.unitins.topicos1.cafe.repository.LoteEstoqueRepository;
import br.unitins.topicos1.cafe.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LoteEstoqueServiceImpl {

    @Inject
    LoteEstoqueRepository repository;

    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    LoteEstoqueMapper mapper;

    public List<LoteEstoqueResponseDTO> listar() {
        return repository.listAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public LoteEstoqueResponseDTO buscarPorId(Long id) {
        LoteEstoque l = repository.findById(id);
        if (l == null) throw new NotFoundException("Lote de estoque não encontrado");
        return mapper.toResponseDTO(l);
    }

    @Transactional
    public LoteEstoqueResponseDTO salvar(LoteEstoqueRequestDTO dto) {
        Produto produto = produtoRepository.findById(dto.getProdutoId());
        if (produto == null) throw new NotFoundException("Produto não encontrado");
        LoteEstoque l = mapper.toModel(dto);
        l.setProduto(produto);
        repository.persist(l);
        return mapper.toResponseDTO(l);
    }

    @Transactional
    public LoteEstoqueResponseDTO atualizar(Long id, LoteEstoqueRequestDTO dto) {
        LoteEstoque l = repository.findById(id);
        if (l == null) throw new NotFoundException("Lote de estoque não encontrado");
        Produto produto = produtoRepository.findById(dto.getProdutoId());
        if (produto == null) throw new NotFoundException("Produto não encontrado");
        l.setProduto(produto);
        l.setQuantidade(dto.getQuantidade());
        l.setDataValidade(dto.getDataValidade());
        return mapper.toResponseDTO(l);
    }

    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
