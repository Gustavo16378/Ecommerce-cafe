package br.unitins.topicos1.cafe.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CheckoutResponseDTO {
    private Long pedidoId;
    private String statusPedido;
    private String statusPagamento;
    private String enderecoEntrega;
    private LocalDate dataEntregaPrevista;
    private String formaPagamento;
    private Double total;
    private Integer parcelas;
    private Double valorParcela;
    private String codigoPagamento;
    private LocalDateTime dataPagamento;

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public String getStatusPedido() { return statusPedido; }
    public void setStatusPedido(String statusPedido) { this.statusPedido = statusPedido; }

    public String getStatusPagamento() { return statusPagamento; }
    public void setStatusPagamento(String statusPagamento) { this.statusPagamento = statusPagamento; }

    public String getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(String enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }

    public LocalDate getDataEntregaPrevista() { return dataEntregaPrevista; }
    public void setDataEntregaPrevista(LocalDate dataEntregaPrevista) { this.dataEntregaPrevista = dataEntregaPrevista; }

    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public Integer getParcelas() { return parcelas; }
    public void setParcelas(Integer parcelas) { this.parcelas = parcelas; }

    public Double getValorParcela() { return valorParcela; }
    public void setValorParcela(Double valorParcela) { this.valorParcela = valorParcela; }

    public String getCodigoPagamento() { return codigoPagamento; }
    public void setCodigoPagamento(String codigoPagamento) { this.codigoPagamento = codigoPagamento; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }
}
