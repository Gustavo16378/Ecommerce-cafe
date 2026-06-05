package br.unitins.topicos1.cafe.dto;

import br.unitins.topicos1.cafe.model.Perfil;

public record UsuarioResponseDTO(Long id, String login, Perfil perfil) {
}
