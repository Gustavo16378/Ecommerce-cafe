package br.unitins.topicos1.cafe.dto;

import java.util.List;

public class CarrinhoResponseDTO {
    private Long id;
    private List<ItemCarrinhoResponseDTO> itens;
    private Double total;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public List<ItemCarrinhoResponseDTO> getItens() { return itens; }
    public void setItens(List<ItemCarrinhoResponseDTO> itens) { this.itens = itens; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}
