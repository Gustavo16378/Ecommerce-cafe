package br.unitins.topicos1.cafe.dto;

import jakarta.validation.constraints.NotBlank;

public record AlterarSenhaRequestDTO(
        @NotBlank String senhaAtual,
        @NotBlank String novaSenha) {
}
