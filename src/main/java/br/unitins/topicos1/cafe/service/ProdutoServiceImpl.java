package br.unitins.topicos1.cafe.service;

import br.unitins.topicos1.cafe.dto.ProdutoRequestDTO;
import br.unitins.topicos1.cafe.dto.ProdutoResponseDTO;
import br.unitins.topicos1.cafe.mapper.ProdutoMapper;
import br.unitins.topicos1.cafe.model.*;
import br.unitins.topicos1.cafe.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProdutoServiceImpl {
    @Inject ProdutoRepository produtoRepository;
    @Inject TorraRepository torraRepository;
    @Inject TamanhoEmbalagemRepository tamanhoEmbalagemRepository;
    @Inject MaterialEmbalagemRepository materialEmbalagemRepository;
    @Inject CategoriaRepository categoriaRepository;
    @Inject FornecedorRepository fornecedorRepository;
    @Inject ProdutoMapper mapper;

    public List<ProdutoResponseDTO> listar() {
        return produtoRepository.listAll().stream()
            .map(mapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    public List<ProdutoResponseDTO> buscarPorNome(String nome) {
        return produtoRepository.list("nome like ?1", "%" + nome + "%")
            .stream()
            .map(mapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public ProdutoResponseDTO salvar(ProdutoRequestDTO dto) {
        Produto produto = mapper.toModel(dto);
        produto.setTorra(buscarTorra(dto.getTorraId()));
        produto.setTamanhoEmbalagem(buscarTamanho(dto.getTamanhoEmbalagemId()));
        produto.setMaterialEmbalagem(buscarMaterial(dto.getMaterialEmbalagemId()));
        produto.setFornecedor(buscarFornecedor(dto.getFornecedorId()));
        if (dto.getCategoriasIds() != null) {
            produto.setCategorias(dto.getCategoriasIds().stream().map(this::buscarCategoria).collect(Collectors.toList()));
        }
        produtoRepository.persist(produto);
        return mapper.toResponseDTO(produto);
    }

    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id);
        if (produto == null) throw new NotFoundException("Produto não encontrado");
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setAtivo(dto.getAtivo());
        produto.setTorra(buscarTorra(dto.getTorraId()));
        produto.setTamanhoEmbalagem(buscarTamanho(dto.getTamanhoEmbalagemId()));
        produto.setMaterialEmbalagem(buscarMaterial(dto.getMaterialEmbalagemId()));
        produto.setFornecedor(buscarFornecedor(dto.getFornecedorId()));
        if (dto.getCategoriasIds() != null) {
            produto.setCategorias(dto.getCategoriasIds().stream().map(this::buscarCategoria).collect(Collectors.toList()));
        }
        return mapper.toResponseDTO(produto);
    }

    @Transactional
    public void excluir(Long id) {
        produtoRepository.deleteById(id);
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id);
        if (produto == null) throw new NotFoundException("Produto não encontrado");
        return mapper.toResponseDTO(produto);
    }

    private Torra buscarTorra(Long id) {
        Torra torra = torraRepository.findById(id);
        if (torra == null) throw new NotFoundException("Torra não encontrada");
        return torra;
    }

    private TamanhoEmbalagem buscarTamanho(Long id) {
        TamanhoEmbalagem t = tamanhoEmbalagemRepository.findById(id);
        if (t == null) throw new NotFoundException("Tamanho de embalagem não encontrado");
        return t;
    }

    private MaterialEmbalagem buscarMaterial(Long id) {
        MaterialEmbalagem m = materialEmbalagemRepository.findById(id);
        if (m == null) throw new NotFoundException("Material de embalagem não encontrado");
        return m;
    }

    private Fornecedor buscarFornecedor(Long id) {
        Fornecedor f = fornecedorRepository.findById(id);
        if (f == null) throw new NotFoundException("Fornecedor não encontrado");
        return f;
    }

    private Categoria buscarCategoria(Long id) {
        Categoria c = categoriaRepository.findById(id);
        if (c == null) throw new NotFoundException("Categoria não encontrada");
        return c;
    }
}
