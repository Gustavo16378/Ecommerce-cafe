package br.unitins.topicos1.cafe.mapper;

import br.unitins.topicos1.cafe.dto.ProdutoEcommerceResponseDTO;
import br.unitins.topicos1.cafe.dto.ProdutoRequestDTO;
import br.unitins.topicos1.cafe.dto.ProdutoResponseDTO;
import br.unitins.topicos1.cafe.model.Categoria;
import br.unitins.topicos1.cafe.model.LoteEstoque;
import br.unitins.topicos1.cafe.model.Produto;
import br.unitins.topicos1.cafe.model.ProdutoCafe;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProdutoMapper {

    public Produto toModel(ProdutoRequestDTO dto) {
        if (dto.getTipoCafe() != null) {
            ProdutoCafe p = new ProdutoCafe();
            p.setNome(dto.getNome());
            p.setDescricao(dto.getDescricao());
            p.setPreco(dto.getPreco());
            p.setAtivo(dto.getAtivo());
            p.setTipoCafe(dto.getTipoCafe());
            p.setTipoMoagem(dto.getTipoMoagem());
            return p;
        }
        Produto p = new Produto();
        p.setNome(dto.getNome());
        p.setDescricao(dto.getDescricao());
        p.setPreco(dto.getPreco());
        p.setAtivo(dto.getAtivo());
        return p;
    }

    public ProdutoResponseDTO toResponseDTO(Produto produto) {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setPreco(produto.getPreco());
        dto.setAtivo(produto.getAtivo());
        dto.setTorra(produto.getTorra() != null ? produto.getTorra().getTipo().name() : null);
        dto.setTamanhoEmbalagem(produto.getTamanhoEmbalagem() != null ? produto.getTamanhoEmbalagem().getGramas() + "g" : null);
        dto.setMaterialEmbalagem(produto.getMaterialEmbalagem() != null ? produto.getMaterialEmbalagem().getNome() : null);
        List<String> categorias = produto.getCategorias() == null ? Collections.emptyList() :
                produto.getCategorias().stream().map(Categoria::getNome).collect(Collectors.toList());
        dto.setCategorias(categorias);
        dto.setFornecedor(produto.getFornecedor() != null ? produto.getFornecedor().getNome() : null);
        if (produto instanceof ProdutoCafe pc) {
            dto.setTipoCafe(pc.getTipoCafe() != null ? pc.getTipoCafe().name() : null);
            dto.setTipoMoagem(pc.getTipoMoagem() != null ? pc.getTipoMoagem().name() : null);
        }
        return dto;
    }

    public ProdutoEcommerceResponseDTO toEcommerceResponseDTO(Produto produto) {
        ProdutoEcommerceResponseDTO dto = new ProdutoEcommerceResponseDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setPreco(produto.getPreco());
        dto.setTorra(produto.getTorra() != null ? produto.getTorra().getTipo().name() : null);
        dto.setTamanhoEmbalagem(produto.getTamanhoEmbalagem() != null ? produto.getTamanhoEmbalagem().getGramas() + "g" : null);
        dto.setMaterialEmbalagem(produto.getMaterialEmbalagem() != null ? produto.getMaterialEmbalagem().getNome() : null);
        List<String> categorias = produto.getCategorias() == null ? Collections.emptyList() :
                produto.getCategorias().stream().map(Categoria::getNome).collect(Collectors.toList());
        dto.setCategorias(categorias);
        if (produto instanceof ProdutoCafe pc) {
            dto.setTipoCafe(pc.getTipoCafe() != null ? pc.getTipoCafe().name() : null);
            dto.setTipoMoagem(pc.getTipoMoagem() != null ? pc.getTipoMoagem().name() : null);
        }
        int estoque = produto.getLotesEstoque() == null ? 0 :
                produto.getLotesEstoque().stream().mapToInt(LoteEstoque::getQuantidade).sum();
        dto.setEstoqueDisponivel(estoque);
        return dto;
    }
}
