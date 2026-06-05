package br.unitins.topicos1.cafe.service;

import br.unitins.topicos1.cafe.dto.*;
import br.unitins.topicos1.cafe.exception.ValidationException;
import br.unitins.topicos1.cafe.mapper.UsuarioMapper;
import br.unitins.topicos1.cafe.model.EnderecoCliente;
import br.unitins.topicos1.cafe.model.Perfil;
import br.unitins.topicos1.cafe.model.Usuario;
import br.unitins.topicos1.cafe.repository.EnderecoClienteRepository;
import br.unitins.topicos1.cafe.repository.UsuarioRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UsuarioServiceImpl {

    @Inject
    UsuarioRepository repository;

    @Inject
    UsuarioMapper mapper;

    @Inject
    EnderecoClienteRepository enderecoRepository;

    public List<UsuarioResponseDTO> listar() {
        return repository.listAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioResponseDTO salvar(UsuarioRequestDTO dto) {
        Usuario usuario = mapper.toModel(dto);
        usuario.setSenha(BcryptUtil.bcryptHash(dto.senha()));
        repository.persist(usuario);
        return mapper.toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        Usuario existente = repository.findById(id);
        if (existente == null) throw new NotFoundException("Usuário não encontrado");
        existente.setLogin(dto.login());
        existente.setSenha(BcryptUtil.bcryptHash(dto.senha()));
        existente.setPerfil(dto.perfil());
        return mapper.toResponseDTO(existente);
    }

    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = repository.findById(id);
        if (usuario == null) throw new NotFoundException("Usuário não encontrado");
        return mapper.toResponseDTO(usuario);
    }

    @Transactional
    public void alterarSenha(String login, AlterarSenhaRequestDTO dto) {
        Usuario usuario = repository.findByLogin(login);
        if (usuario == null) throw new NotFoundException("Usuário não encontrado");
        if (!BcryptUtil.matches(dto.senhaAtual(), usuario.getSenha())) {
            throw new ValidationException("Senha atual incorreta", "senhaAtual");
        }
        usuario.setSenha(BcryptUtil.bcryptHash(dto.novaSenha()));
    }

    @Transactional
    public void esqueceuSenha(EsqueceuSenhaRequestDTO dto) {
        Usuario usuario = repository.findByLogin(dto.login());
        if (usuario == null) throw new NotFoundException("Usuário não encontrado");
        usuario.setSenha(BcryptUtil.bcryptHash(dto.novaSenha()));
    }

    @Transactional
    public UsuarioResponseDTO cadastroSimples(CadastroClienteSimplesRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setLogin(dto.login());
        usuario.setSenha(BcryptUtil.bcryptHash(dto.senha()));
        usuario.setPerfil(Perfil.USER);
        repository.persist(usuario);
        return mapper.toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO cadastroCompleto(CadastroClienteCompletoRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setLogin(dto.login());
        usuario.setSenha(BcryptUtil.bcryptHash(dto.senha()));
        usuario.setPerfil(Perfil.USER);
        usuario.setNome(dto.nome());
        usuario.setCpf(dto.cpf());
        usuario.setEmail(dto.email());
        usuario.setTelefone(dto.telefone());
        repository.persist(usuario);

        if (dto.enderecos() != null) {
            List<EnderecoCliente> enderecos = dto.enderecos().stream().map(e -> {
                EnderecoCliente end = new EnderecoCliente();
                end.setRua(e.rua());
                end.setCidade(e.cidade());
                end.setUf(e.uf());
                end.setCep(e.cep());
                end.setComplemento(e.complemento());
                end.setUsuario(usuario);
                return end;
            }).collect(Collectors.toList());
            usuario.setEnderecos(enderecos);
        }

        return mapper.toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO editarPerfil(String login, EditarPerfilRequestDTO dto) {
        Usuario usuario = repository.findByLogin(login);
        if (usuario == null) throw new NotFoundException("Usuário não encontrado");
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setTelefone(dto.telefone());
        return mapper.toResponseDTO(usuario);
    }

    public List<EnderecoClienteResponseDTO> buscarEnderecosPorCliente(String login) {
        Usuario usuario = repository.findByLogin(login);
        if (usuario == null) throw new NotFoundException("Usuário não encontrado");
        if (usuario.getEnderecos() == null) return List.of();
        return usuario.getEnderecos().stream().map(this::toEnderecoDTO).collect(Collectors.toList());
    }

    @Transactional
    public EnderecoClienteResponseDTO adicionarEndereco(String login, EnderecoClienteRequestDTO dto) {
        Usuario usuario = repository.findByLogin(login);
        if (usuario == null) throw new NotFoundException("Usuário não encontrado");
        EnderecoCliente endereco = new EnderecoCliente();
        endereco.setRua(dto.rua());
        endereco.setCidade(dto.cidade());
        endereco.setUf(dto.uf());
        endereco.setCep(dto.cep());
        endereco.setComplemento(dto.complemento());
        endereco.setUsuario(usuario);
        enderecoRepository.persist(endereco);
        if (usuario.getEnderecos() == null) usuario.setEnderecos(new ArrayList<>());
        usuario.getEnderecos().add(endereco);
        return toEnderecoDTO(endereco);
    }

    @Transactional
    public EnderecoClienteResponseDTO atualizarEndereco(String login, Long enderecoId, EnderecoClienteRequestDTO dto) {
        Usuario usuario = repository.findByLogin(login);
        if (usuario == null) throw new NotFoundException("Usuário não encontrado");
        EnderecoCliente endereco = usuario.getEnderecos().stream()
                .filter(e -> e.getId().equals(enderecoId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Endereço não encontrado"));
        endereco.setRua(dto.rua());
        endereco.setCidade(dto.cidade());
        endereco.setUf(dto.uf());
        endereco.setCep(dto.cep());
        endereco.setComplemento(dto.complemento());
        return toEnderecoDTO(endereco);
    }

    @Transactional
    public void removerEndereco(String login, Long enderecoId) {
        Usuario usuario = repository.findByLogin(login);
        if (usuario == null) throw new NotFoundException("Usuário não encontrado");
        boolean removido = usuario.getEnderecos().removeIf(e -> e.getId().equals(enderecoId));
        if (!removido) throw new NotFoundException("Endereço não encontrado");
    }

    private EnderecoClienteResponseDTO toEnderecoDTO(EnderecoCliente e) {
        EnderecoClienteResponseDTO dto = new EnderecoClienteResponseDTO();
        dto.setId(e.getId());
        dto.setRua(e.getRua());
        dto.setCidade(e.getCidade());
        dto.setUf(e.getUf());
        dto.setCep(e.getCep());
        dto.setComplemento(e.getComplemento());
        return dto;
    }
}
