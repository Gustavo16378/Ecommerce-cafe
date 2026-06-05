package br.unitins.topicos1.cafe.service;

import br.unitins.topicos1.cafe.model.*;
import br.unitins.topicos1.cafe.repository.*;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class DataInitializerService {

    @Inject UsuarioRepository usuarioRepository;
    @Inject CategoriaRepository categoriaRepository;
    @Inject TorraRepository torraRepository;
    @Inject MaterialEmbalagemRepository materialEmbalagemRepository;
    @Inject TamanhoEmbalagemRepository tamanhoEmbalagemRepository;
    @Inject FornecedorRepository fornecedorRepository;
    @Inject ProdutoRepository produtoRepository;
    @Inject LoteEstoqueRepository loteEstoqueRepository;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        if (usuarioRepository.count() > 0) return; // já inicializado

        criarUsuarios();
        var categorias = criarCategorias();
        var torras = criarTorras();
        var materiais = criarMateriais();
        var tamanhos = criarTamanhos();
        var fornecedores = criarFornecedores();
        criarProdutos(categorias, torras, materiais, tamanhos, fornecedores);
    }

    private void criarUsuarios() {
        // Admin
        Usuario admin = new Usuario();
        admin.setLogin("admin");
        admin.setSenha(BcryptUtil.bcryptHash("admin123"));
        admin.setPerfil(Perfil.ADMIN);
        admin.setNome("Administrador");
        admin.setEmail("admin@ecommercecafe.com");
        admin.setTelefone("63991110000");
        usuarioRepository.persist(admin);

        // Cliente com endereço
        EnderecoCliente endereco = new EnderecoCliente();
        endereco.setRua("Rua das Flores, 123");
        endereco.setCidade("Palmas");
        endereco.setUf("TO");
        endereco.setCep("77000000");
        endereco.setComplemento("Apt 201");

        Usuario cliente = new Usuario();
        cliente.setLogin("cliente");
        cliente.setSenha(BcryptUtil.bcryptHash("cliente123"));
        cliente.setPerfil(Perfil.USER);
        cliente.setNome("João da Silva");
        cliente.setCpf("12345678901");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("63992223333");
        endereco.setUsuario(cliente);
        cliente.setEnderecos(List.of(endereco));
        usuarioRepository.persist(cliente);
    }

    private List<Categoria> criarCategorias() {
        Categoria especial = categoria("Café Especial");
        Categoria gourmet = categoria("Café Gourmet");
        Categoria tradicional = categoria("Café Tradicional");
        Categoria organico = categoria("Café Orgânico");
        categoriaRepository.persist(especial, gourmet, tradicional, organico);
        return List.of(especial, gourmet, tradicional, organico);
    }

    private List<Torra> criarTorras() {
        Torra clara = torra(TipoTorra.CLARA);
        Torra media = torra(TipoTorra.MEDIA);
        Torra escura = torra(TipoTorra.ESCURA);
        torraRepository.persist(clara, media, escura);
        return List.of(clara, media, escura);
    }

    private List<MaterialEmbalagem> criarMateriais() {
        MaterialEmbalagem papel = material("Papel Kraft");
        MaterialEmbalagem plastico = material("Plástico Biodegradável");
        MaterialEmbalagem aluminio = material("Alumínio");
        materialEmbalagemRepository.persist(papel, plastico, aluminio);
        return List.of(papel, plastico, aluminio);
    }

    private List<TamanhoEmbalagem> criarTamanhos() {
        TamanhoEmbalagem p250 = tamanho(250);
        TamanhoEmbalagem p500 = tamanho(500);
        TamanhoEmbalagem p1000 = tamanho(1000);
        tamanhoEmbalagemRepository.persist(p250, p500, p1000);
        return List.of(p250, p500, p1000);
    }

    private List<Fornecedor> criarFornecedores() {
        Fornecedor fazenda = new Fornecedor();
        fazenda.setNome("Fazenda Serra Verde");
        fazenda.setCnpj("12345678000195");
        fazenda.setContato("63991234567");
        EnderecoFornecedor ef1 = new EnderecoFornecedor();
        ef1.setRua("Estrada Rural KM 12");
        ef1.setCidade("Palmas");
        ef1.setUf("TO");
        ef1.setCep("77001000");
        fazenda.setEndereco(ef1);

        Fornecedor coop = new Fornecedor();
        coop.setNome("Cooperativa Café Brasil");
        coop.setCnpj("98765432000111");
        coop.setContato("11999887766");
        EnderecoFornecedor ef2 = new EnderecoFornecedor();
        ef2.setRua("Av. Paulista, 1000");
        ef2.setCidade("São Paulo");
        ef2.setUf("SP");
        ef2.setCep("01310100");
        coop.setEndereco(ef2);

        fornecedorRepository.persist(fazenda, coop);
        return List.of(fazenda, coop);
    }

    private void criarProdutos(List<Categoria> cats, List<Torra> torras,
                                List<MaterialEmbalagem> mats, List<TamanhoEmbalagem> tams,
                                List<Fornecedor> forn) {

        // Produto 1 — Café Arábica Especial (grão, torra média, 250g, papel kraft)
        ProdutoCafe arabica = new ProdutoCafe();
        arabica.setNome("Café Arábica Especial");
        arabica.setDescricao("Café arábica de altitude com notas de caramelo e frutas vermelhas. Colhido à mão nas montanhas do Tocantins.");
        arabica.setPreco(49.90);
        arabica.setAtivo(true);
        arabica.setTipoCafe(TipoCafe.GRAO);
        arabica.setTipoMoagem(null);
        arabica.setTorra(torras.get(1)); // MEDIA
        arabica.setTamanhoEmbalagem(tams.get(0)); // 250g
        arabica.setMaterialEmbalagem(mats.get(0)); // Papel Kraft
        arabica.setCategorias(List.of(cats.get(0), cats.get(1))); // Especial + Gourmet
        arabica.setFornecedor(forn.get(0)); // Fazenda Serra Verde
        produtoRepository.persist(arabica);
        lote(arabica, 150, LocalDate.of(2027, 6, 1));

        // Produto 2 — Café Robusta Tradicional (moído, torra escura, 500g, plástico)
        ProdutoCafe robusta = new ProdutoCafe();
        robusta.setNome("Café Robusta Tradicional");
        robusta.setDescricao("Blend encorpado com sabor intenso e amargor equilibrado. Ideal para espresso.");
        robusta.setPreco(32.50);
        robusta.setAtivo(true);
        robusta.setTipoCafe(TipoCafe.MOIDO);
        robusta.setTipoMoagem(TipoMoagem.MEDIA);
        robusta.setTorra(torras.get(2)); // ESCURA
        robusta.setTamanhoEmbalagem(tams.get(1)); // 500g
        robusta.setMaterialEmbalagem(mats.get(1)); // Plástico Biodegradável
        robusta.setCategorias(List.of(cats.get(2))); // Tradicional
        robusta.setFornecedor(forn.get(1)); // Cooperativa
        produtoRepository.persist(robusta);
        lote(robusta, 200, LocalDate.of(2027, 3, 15));

        // Produto 3 — Café Blend Gourmet (cápsula, torra clara, 1kg, alumínio)
        ProdutoCafe blend = new ProdutoCafe();
        blend.setNome("Café Blend Gourmet Premium");
        blend.setDescricao("Mistura exclusiva de grãos selecionados com acidez suave e aroma floral. Certificado orgânico.");
        blend.setPreco(89.90);
        blend.setAtivo(true);
        blend.setTipoCafe(TipoCafe.CAPSULA);
        blend.setTipoMoagem(TipoMoagem.FINA);
        blend.setTorra(torras.get(0)); // CLARA
        blend.setTamanhoEmbalagem(tams.get(2)); // 1kg
        blend.setMaterialEmbalagem(mats.get(2)); // Alumínio
        blend.setCategorias(List.of(cats.get(1), cats.get(3))); // Gourmet + Orgânico
        blend.setFornecedor(forn.get(0)); // Fazenda Serra Verde
        produtoRepository.persist(blend);
        lote(blend, 80, LocalDate.of(2027, 12, 31));

        // Produto 4 — Café Descafeinado (moído fino, torra média, 250g, papel kraft)
        ProdutoCafe descaf = new ProdutoCafe();
        descaf.setNome("Café Descafeinado Suave");
        descaf.setDescricao("Todo o sabor do café sem a cafeína. Processo de extração natural com CO2.");
        descaf.setPreco(44.90);
        descaf.setAtivo(true);
        descaf.setTipoCafe(TipoCafe.MOIDO);
        descaf.setTipoMoagem(TipoMoagem.FINA);
        descaf.setTorra(torras.get(1)); // MEDIA
        descaf.setTamanhoEmbalagem(tams.get(0)); // 250g
        descaf.setMaterialEmbalagem(mats.get(0)); // Papel Kraft
        descaf.setCategorias(List.of(cats.get(3))); // Orgânico
        descaf.setFornecedor(forn.get(1)); // Cooperativa
        produtoRepository.persist(descaf);
        lote(descaf, 60, LocalDate.of(2027, 8, 20));
    }

    // ---------- helpers ----------

    private void lote(Produto produto, int quantidade, LocalDate validade) {
        LoteEstoque lote = new LoteEstoque();
        lote.setProduto(produto);
        lote.setQuantidade(quantidade);
        lote.setDataValidade(validade);
        loteEstoqueRepository.persist(lote);
    }

    private Categoria categoria(String nome) {
        Categoria c = new Categoria();
        c.setNome(nome);
        return c;
    }

    private Torra torra(TipoTorra tipo) {
        Torra t = new Torra();
        t.setTipo(tipo);
        return t;
    }

    private MaterialEmbalagem material(String nome) {
        MaterialEmbalagem m = new MaterialEmbalagem();
        m.setNome(nome);
        return m;
    }

    private TamanhoEmbalagem tamanho(int gramas) {
        TamanhoEmbalagem t = new TamanhoEmbalagem();
        t.setGramas(gramas);
        return t;
    }
}
