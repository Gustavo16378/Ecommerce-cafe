package br.unitins.topicos1.cafe.dto;

public class FornecedorResponseDTO {
    private Long id;
    private String nome;
    private String cnpj;
    private String contato;
    private EnderecoFornecedorResponseDTO endereco;

    public FornecedorResponseDTO() {}

    public FornecedorResponseDTO(Long id, String nome, String cnpj, String contato, EnderecoFornecedorResponseDTO endereco) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.contato = contato;
        this.endereco = endereco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public EnderecoFornecedorResponseDTO getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoFornecedorResponseDTO endereco) {
        this.endereco = endereco;
    }
}
