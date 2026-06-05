package br.unitins.topicos1.cafe.dto;

import jakarta.validation.constraints.NotBlank;

public record EsqueceuSenhaRequestDTO(
        @NotBlank String login,
        @NotBlank String novaSenha) {
}
