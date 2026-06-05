package br.unitins.topicos1.cafe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EnderecoClienteRequestDTO(
        @NotBlank String rua,
        @NotBlank String cidade,
        @NotBlank String uf,
        @NotBlank @Pattern(regexp = "\\d{8}") String cep,
        String complemento) {
}
