package br.unitins.topicos1.cafe.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponseDTO {
    private Long id;
    private LocalDateTime dataPedido;
    private String status;
    private String enderecoEntrega;
    private LocalDate dataEntregaPrevista;
    private List<ItemPedidoResponseDTO> itens;
    private Double total;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(String enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }

    public LocalDate getDataEntregaPrevista() { return dataEntregaPrevista; }
    public void setDataEntregaPrevista(LocalDate dataEntregaPrevista) { this.dataEntregaPrevista = dataEntregaPrevista; }

    public List<ItemPedidoResponseDTO> getItens() { return itens; }
    public void setItens(List<ItemPedidoResponseDTO> itens) { this.itens = itens; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}
