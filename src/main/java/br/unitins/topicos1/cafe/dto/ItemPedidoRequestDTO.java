package br.unitins.topicos1.cafe.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPedidoRequestDTO(
        @NotNull Long produtoId,
        @NotNull @Positive Integer quantidade) {
}
