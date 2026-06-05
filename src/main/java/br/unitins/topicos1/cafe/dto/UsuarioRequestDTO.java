package br.unitins.topicos1.cafe.dto;

import br.unitins.topicos1.cafe.model.Perfil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequestDTO(
        @NotBlank String login,
        @NotBlank String senha,
        @NotNull Perfil perfil) {
}
