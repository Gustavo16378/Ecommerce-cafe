package br.unitins.topicos1.cafe.mapper;

import br.unitins.topicos1.cafe.dto.UsuarioRequestDTO;
import br.unitins.topicos1.cafe.dto.UsuarioResponseDTO;
import br.unitins.topicos1.cafe.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioMapper {

    public Usuario toModel(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setLogin(dto.login());
        usuario.setPerfil(dto.perfil());
        return usuario;
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioResponseDTO(usuario.getId(), usuario.getLogin(), usuario.getPerfil());
    }
}
