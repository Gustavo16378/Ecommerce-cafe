package br.unitins.topicos1.cafe.dto;

import jakarta.validation.constraints.NotBlank;

public record CadastroClienteSimplesRequestDTO(
        @NotBlank String login,
        @NotBlank String senha) {
}
