package br.unitins.topicos1.cafe.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class FornecedorRequestDTO {
    @NotBlank
    private String nome;

    @NotBlank
    @Pattern(regexp = "\\d{14}")
    private String cnpj;

    @NotBlank
    private String contato;

    @NotNull
    @Valid
    private EnderecoFornecedorRequestDTO endereco;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public EnderecoFornecedorRequestDTO getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoFornecedorRequestDTO endereco) {
        this.endereco = endereco;
    }
}
