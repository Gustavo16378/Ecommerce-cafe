package br.unitins.topicos1.cafe.dto;

import br.unitins.topicos1.cafe.model.TipoCafe;
import br.unitins.topicos1.cafe.model.TipoMoagem;
import jakarta.validation.constraints.*;
import java.util.List;

public class ProdutoRequestDTO {
    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    @NotNull
    @Positive
    private Double preco;

    @NotNull
    private Boolean ativo;

    @NotNull
    private Long torraId;

    @NotNull
    private Long tamanhoEmbalagemId;

    @NotNull
    private Long materialEmbalagemId;

    @NotNull
    @Size(min = 1)
    private List<Long> categoriasIds;

    @NotNull
    private Long fornecedorId;

    private TipoCafe tipoCafe;

    private TipoMoagem tipoMoagem;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Long getTorraId() {
        return torraId;
    }

    public void setTorraId(Long torraId) {
        this.torraId = torraId;
    }

    public Long getTamanhoEmbalagemId() {
        return tamanhoEmbalagemId;
    }

    public void setTamanhoEmbalagemId(Long tamanhoEmbalagemId) {
        this.tamanhoEmbalagemId = tamanhoEmbalagemId;
    }

    public Long getMaterialEmbalagemId() {
        return materialEmbalagemId;
    }

    public void setMaterialEmbalagemId(Long materialEmbalagemId) {
        this.materialEmbalagemId = materialEmbalagemId;
    }

    public List<Long> getCategoriasIds() {
        return categoriasIds;
    }

    public void setCategoriasIds(List<Long> categoriasIds) {
        this.categoriasIds = categoriasIds;
    }

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public TipoCafe getTipoCafe() {
        return tipoCafe;
    }

    public void setTipoCafe(TipoCafe tipoCafe) {
        this.tipoCafe = tipoCafe;
    }

    public TipoMoagem getTipoMoagem() {
        return tipoMoagem;
    }

    public void setTipoMoagem(TipoMoagem tipoMoagem) {
        this.tipoMoagem = tipoMoagem;
    }

}
