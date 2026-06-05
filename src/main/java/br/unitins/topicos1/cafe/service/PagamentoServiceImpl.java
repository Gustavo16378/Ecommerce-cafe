package br.unitins.topicos1.cafe.service;

import br.unitins.topicos1.cafe.dto.PagamentoRequestDTO;
import br.unitins.topicos1.cafe.dto.PagamentoResponseDTO;
import br.unitins.topicos1.cafe.exception.ValidationException;
import br.unitins.topicos1.cafe.model.*;
import br.unitins.topicos1.cafe.repository.PagamentoRepository;
import br.unitins.topicos1.cafe.repository.PedidoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class PagamentoServiceImpl {

    @Inject PagamentoRepository pagamentoRepository;
    @Inject PedidoRepository pedidoRepository;

    @Transactional
    public PagamentoResponseDTO pagar(String login, Long pedidoId, PagamentoRequestDTO dto) {
        Pedido pedido = pedidoRepository.findById(pedidoId);
        if (pedido == null || !pedido.getUsuario().getLogin().equals(login))
            throw new NotFoundException("Pedido não encontrado");

        if (pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO)
            throw new ValidationException("Este pedido não está aguardando pagamento", "pedidoId");

        double total = pedido.getItens().stream()
                .mapToDouble(i -> i.getPrecoUnitario() * i.getQuantidade()).sum();

        if (dto.formaPagamento() == FormaPagamento.CARTAO_DEBITO && dto.parcelas() != null && dto.parcelas() > 1)
            throw new ValidationException("Cartão de débito não permite parcelamento", "parcelas");

        int parcelas = 1;
        if (dto.formaPagamento() == FormaPagamento.CARTAO_CREDITO && dto.parcelas() != null)
            parcelas = dto.parcelas();

        double valorParcela = Math.round((total / parcelas) * 100.0) / 100.0;

        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setFormaPagamento(dto.formaPagamento());
        pagamento.setStatus(StatusPagamento.APROVADO);
        pagamento.setValor(total);
        pagamento.setParcelas(parcelas);
        pagamento.setValorParcela(valorParcela);
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setCodigoPagamento(gerarCodigo(dto.formaPagamento(), total));

        pagamentoRepository.persist(pagamento);

        pedido.setStatus(StatusPedido.CONFIRMADO);

        return toDTO(pagamento);
    }

    private String gerarCodigo(FormaPagamento forma, double valor) {
        return switch (forma) {
            case PIX -> gerarCodigoPix(valor);
            case BOLETO -> gerarCodigoBoleto();
            case CARTAO_CREDITO -> "CREDITO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            case CARTAO_DEBITO -> "DEBITO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        };
    }

    // Gera código PIX Copia e Cola seguindo padrão BACEN (EMV)
    private String gerarCodigoPix(double valor) {
        String chave = "ecommerce-cafe@pix.com.br";
        String nome = "Ecommerce Cafe";
        String cidade = "PALMAS";
        String valorStr = String.format("%.2f", valor).replace(",", ".");

        String payload =
            "000201"                                                        // formato
            + "010212"                                                      // ponto de iniciação
            + "26" + lpad("0014BR.GOV.BCB.PIX01" + lpad(chave, 2), 2)     // chave PIX
            + "52040000"                                                     // categoria
            + "5303986"                                                      // moeda BRL
            + "54" + lpad(valorStr, 2)                                      // valor
            + "5802BR"                                                       // país
            + "59" + lpad(nome, 2)                                          // nome
            + "60" + lpad(cidade, 2)                                        // cidade
            + "62070503***"                                                  // referência
            + "6304";                                                        // CRC placeholder

        return payload + calcularCRC16(payload);
    }

    private String lpad(String valor, int tamanhoBytes) {
        String tam = String.format("%02d", valor.length());
        return tam + valor;
    }

    private String calcularCRC16(String payload) {
        int crc = 0xFFFF;
        for (char c : payload.toCharArray()) {
            crc ^= (c << 8);
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) crc = (crc << 1) ^ 0x1021;
                else crc <<= 1;
                crc &= 0xFFFF;
            }
        }
        return String.format("%04X", crc);
    }

    private String gerarCodigoBoleto() {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        return "34191." + uuid.substring(0, 5) + " " +
               uuid.substring(5, 10) + "." + uuid.substring(10, 16) + " " +
               uuid.substring(16, 20) + "." + "000000" + " 1 " +
               System.currentTimeMillis();
    }

    private PagamentoResponseDTO toDTO(Pagamento p) {
        PagamentoResponseDTO dto = new PagamentoResponseDTO();
        dto.setId(p.getId());
        dto.setPedidoId(p.getPedido().getId());
        dto.setFormaPagamento(p.getFormaPagamento().name());
        dto.setStatus(p.getStatus().name());
        dto.setValor(p.getValor());
        dto.setParcelas(p.getParcelas());
        dto.setValorParcela(p.getValorParcela());
        dto.setCodigoPagamento(p.getCodigoPagamento());
        dto.setDataPagamento(p.getDataPagamento());
        return dto;
    }
}
