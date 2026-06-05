package br.unitins.topicos1.cafe.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record PedidoRequestDTO(
        @NotNull @Size(min = 1) @Valid List<ItemPedidoRequestDTO> itens) {
}
