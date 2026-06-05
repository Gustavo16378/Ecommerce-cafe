package br.unitins.topicos1.cafe.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EsqueceuSenhaRequestDTO(@NotBlank @Email String email) {
}
