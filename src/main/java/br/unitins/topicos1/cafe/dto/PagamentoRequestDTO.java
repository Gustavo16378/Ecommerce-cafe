package br.unitins.topicos1.cafe.dto;

import br.unitins.topicos1.cafe.model.FormaPagamento;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PagamentoRequestDTO(
        @NotNull FormaPagamento formaPagamento,
        @Min(1) @Max(12) Integer parcelas) {
}
