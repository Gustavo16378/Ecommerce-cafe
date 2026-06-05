package br.unitins.topicos1.cafe.mapper;

import br.unitins.topicos1.cafe.dto.*;
import br.unitins.topicos1.cafe.model.EnderecoFornecedor;
import br.unitins.topicos1.cafe.model.Fornecedor;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FornecedorMapper {

    public EnderecoFornecedor toEnderecoModel(EnderecoFornecedorRequestDTO dto) {
        EnderecoFornecedor e = new EnderecoFornecedor();
        e.setRua(dto.getRua());
        e.setCidade(dto.getCidade());
        e.setUf(dto.getUf());
        e.setCep(dto.getCep());
        return e;
    }

    public EnderecoFornecedorResponseDTO toEnderecoResponseDTO(EnderecoFornecedor e) {
        if (e == null) return null;
        return new EnderecoFornecedorResponseDTO(e.getRua(), e.getCidade(), e.getUf(), e.getCep());
    }

    public Fornecedor toModel(FornecedorRequestDTO dto) {
        Fornecedor f = new Fornecedor();
        f.setNome(dto.getNome());
        f.setCnpj(dto.getCnpj());
        f.setContato(dto.getContato());
        f.setEndereco(toEnderecoModel(dto.getEndereco()));
        return f;
    }

    public FornecedorResponseDTO toResponseDTO(Fornecedor f) {
        return new FornecedorResponseDTO(
                f.getId(),
                f.getNome(),
                f.getCnpj(),
                f.getContato(),
                toEnderecoResponseDTO(f.getEndereco())
        );
    }
}
