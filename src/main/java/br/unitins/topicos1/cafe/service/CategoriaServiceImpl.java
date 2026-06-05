package br.unitins.topicos1.cafe.service;

import br.unitins.topicos1.cafe.dto.CategoriaRequestDTO;
import br.unitins.topicos1.cafe.dto.CategoriaResponseDTO;
import br.unitins.topicos1.cafe.mapper.CategoriaMapper;
import br.unitins.topicos1.cafe.model.Categoria;
import br.unitins.topicos1.cafe.repository.CategoriaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CategoriaServiceImpl {

	@Inject
	CategoriaRepository repository;

	@Inject
	CategoriaMapper mapper;

	public List<CategoriaResponseDTO> listar() {
		return repository.listAll().stream()
				.map(mapper::toResponseDTO)
				.collect(Collectors.toList());
	}

	@Transactional
	public CategoriaResponseDTO salvar(CategoriaRequestDTO dto) {
		Categoria categoria = mapper.toModel(dto);
		repository.persist(categoria);
		return mapper.toResponseDTO(categoria);
	}

	@Transactional
	public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dto) {
		Categoria existente = repository.findById(id);
		if (existente == null) {
			throw new NotFoundException("Categoria não encontrada");
		}
		existente.setNome(dto.getNome());
		return mapper.toResponseDTO(existente);
	}

	@Transactional
	public void excluir(Long id) {
		repository.deleteById(id);
	}

	public CategoriaResponseDTO buscarPorId(Long id) {
		Categoria categoria = repository.findById(id);
		if (categoria == null) {
			throw new NotFoundException("Categoria não encontrada");
		}
		return mapper.toResponseDTO(categoria);
	}
}
