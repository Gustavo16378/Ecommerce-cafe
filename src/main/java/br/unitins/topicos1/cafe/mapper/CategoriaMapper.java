package br.unitins.topicos1.cafe.mapper;

import br.unitins.topicos1.cafe.dto.CategoriaRequestDTO;
import br.unitins.topicos1.cafe.dto.CategoriaResponseDTO;
import br.unitins.topicos1.cafe.model.Categoria;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoriaMapper {
	public Categoria toModel(CategoriaRequestDTO dto) {
		Categoria categoria = new Categoria();
		categoria.setNome(dto.getNome());
		return categoria;
	}

	public CategoriaResponseDTO toResponseDTO(Categoria categoria) {
		if (categoria == null) return null;
		return new CategoriaResponseDTO(categoria.getId(), categoria.getNome());
	}
}
