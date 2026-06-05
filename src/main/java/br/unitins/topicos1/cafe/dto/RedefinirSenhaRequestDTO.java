package br.unitins.topicos1.cafe.dto;

import jakarta.validation.constraints.NotBlank;

public record RedefinirSenhaRequestDTO(
        @NotBlank String token,
        @NotBlank String novaSenha) {
}
