package br.unitins.topicos1.cafe.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_produto", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("PRODUTO")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private Boolean ativo;

    @ManyToOne
    private Torra torra;

    @ManyToOne
    private TamanhoEmbalagem tamanhoEmbalagem;

    @ManyToOne
    private MaterialEmbalagem materialEmbalagem;

    @ManyToMany
    private List<Categoria> categorias;

    @ManyToOne
    private Fornecedor fornecedor;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoteEstoque> lotesEstoque;

    // Getters e setters

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

    public Torra getTorra() {
        return torra;
    }

    public void setTorra(Torra torra) {
        this.torra = torra;
    }

    public TamanhoEmbalagem getTamanhoEmbalagem() {
        return tamanhoEmbalagem;
    }

    public void setTamanhoEmbalagem(TamanhoEmbalagem tamanhoEmbalagem) {
        this.tamanhoEmbalagem = tamanhoEmbalagem;
    }

    public MaterialEmbalagem getMaterialEmbalagem() {
        return materialEmbalagem;
    }

    public void setMaterialEmbalagem(MaterialEmbalagem materialEmbalagem) {
        this.materialEmbalagem = materialEmbalagem;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public List<LoteEstoque> getLotesEstoque() {
        return lotesEstoque;
    }

    public void setLotesEstoque(List<LoteEstoque> lotesEstoque) {
        this.lotesEstoque = lotesEstoque;
    }

    
}
