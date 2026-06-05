package br.unitins.topicos1.cafe.dto;

import java.time.LocalDateTime;

public class PagamentoResponseDTO {
    private Long id;
    private Long pedidoId;
    private String formaPagamento;
    private String status;
    private Double valor;
    private Integer parcelas;
    private Double valorParcela;
    private String codigoPagamento;
    private LocalDateTime dataPagamento;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public Integer getParcelas() { return parcelas; }
    public void setParcelas(Integer parcelas) { this.parcelas = parcelas; }

    public Double getValorParcela() { return valorParcela; }
    public void setValorParcela(Double valorParcela) { this.valorParcela = valorParcela; }

    public String getCodigoPagamento() { return codigoPagamento; }
    public void setCodigoPagamento(String codigoPagamento) { this.codigoPagamento = codigoPagamento; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }
}
