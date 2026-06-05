package br.unitins.topicos1.cafe.dto;

import java.util.List;

public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private Boolean ativo;
    private String torra;
    private String tamanhoEmbalagem;
    private String materialEmbalagem;
    private List<String> categorias;
    private String fornecedor;
    private String tipoCafe;
    private String tipoMoagem;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getTorra() {
        return torra;
    }

    public void setTorra(String torra) {
        this.torra = torra;
    }

    public String getTamanhoEmbalagem() {
        return tamanhoEmbalagem;
    }

    public void setTamanhoEmbalagem(String tamanhoEmbalagem) {
        this.tamanhoEmbalagem = tamanhoEmbalagem;
    }

    public String getMaterialEmbalagem() {
        return materialEmbalagem;
    }

    public void setMaterialEmbalagem(String materialEmbalagem) {
        this.materialEmbalagem = materialEmbalagem;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getTipoCafe() {
        return tipoCafe;
    }

    public void setTipoCafe(String tipoCafe) {
        this.tipoCafe = tipoCafe;
    }

    public String getTipoMoagem() {
        return tipoMoagem;
    }

    public void setTipoMoagem(String tipoMoagem) {
        this.tipoMoagem = tipoMoagem;
    }

}
