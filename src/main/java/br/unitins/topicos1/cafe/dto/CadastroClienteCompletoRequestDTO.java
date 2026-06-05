package br.unitins.topicos1.cafe.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CadastroClienteCompletoRequestDTO(
        @NotBlank String login,
        @NotBlank String senha,
        @NotBlank String nome,
        @NotBlank String cpf,
        @NotBlank String email,
        @NotBlank String telefone,
        @NotNull @Valid List<EnderecoClienteRequestDTO> enderecos) {
}
