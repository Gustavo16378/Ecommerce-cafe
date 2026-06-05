package br.unitins.topicos1.cafe.dto;

import java.util.List;

public class ListaDesejoResponseDTO {
    private Long id;
    private List<ProdutoEcommerceResponseDTO> produtos;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public List<ProdutoEcommerceResponseDTO> getProdutos() { return produtos; }
    public void setProdutos(List<ProdutoEcommerceResponseDTO> produtos) { this.produtos = produtos; }
}
