package pedroherique.financas.view;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import pedroherique.financas.exception.DadosInvalidosException;
import pedroherique.financas.exception.RendaInsuficienteException;
import pedroherique.financas.model.*;
import pedroherique.financas.repository.*;
import pedroherique.financas.service.AnaliseFinanceiraService;
import pedroherique.financas.service.ResultadoAnalise;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;


@Component
public class MenuConsole implements CommandLineRunner {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Scanner scanner = new Scanner(System.in);

    private final PessoaRepository pessoaRepository;
    private final RendaRepository rendaRepository;
    private final DespesaRepository despesaRepository;
    private final DividasRepository dividasRepository;
    private final ObjetivoRepository objetivoRepository;
    private final AnaliseFinanceiraService analiseFinanceiraService;
    private final ConfigurableApplicationContext applicationContext;
    private final HistoricoAnaliseRepository historicoAnaliseRepository;

    public MenuConsole(PessoaRepository pessoaRepository,
                       RendaRepository rendaRepository,
                       DespesaRepository despesaRepository,
                       DividasRepository dividasRepository,
                       ObjetivoRepository objetivoRepository,
                       AnaliseFinanceiraService analiseFinanceiraService,
                       ConfigurableApplicationContext applicationContext,
                       HistoricoAnaliseRepository historicoAnaliseRepository) {
        this.pessoaRepository = pessoaRepository;
        this.rendaRepository = rendaRepository;
        this.despesaRepository = despesaRepository;
        this.dividasRepository = dividasRepository;
        this.objetivoRepository = objetivoRepository;
        this.analiseFinanceiraService = analiseFinanceiraService;
        this.applicationContext = applicationContext;
        this.historicoAnaliseRepository = historicoAnaliseRepository;
    }

    @Override
    public void run(String... args) {
        boolean continuar = true;
        while (continuar) {
            exibirMenu();
            try {
                int opcao = lerNumeroInteiro("Escolha uma opção: ");
                switch (opcao) {
                    case 1 -> cadastrarPessoa();
                    case 2 -> cadastrarRenda();
                    case 3 -> cadastrarDespesa();
                    case 4 -> cadastrarDivida();
                    case 5 -> cadastrarObjetivo();
                    case 6 -> rodarAnalise();
                    case 7 -> listarPessoas();
                    case 8 -> verHistoricoAnalise();
                    case 0 -> continuar = false;
                    default -> System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NoSuchElementException e) {
                System.out.println("\nEntrada de dados encerrada. Até a próxima!");
                continuar = false;
            } catch (RendaInsuficienteException | DadosInvalidosException e) {

                System.out.println("\n[Aviso] " + e.getMessage() + "\n");
            } catch (Exception e) {
                System.out.println("\n[Erro inesperado] " + e.getMessage() + "\n");
            }
        }
        System.out.println("Encerrando. Até a próxima!");
        applicationContext.close();
    }

    private void exibirMenu() {
        System.out.println("=================================================");
        System.out.println("  GERENCIADOR DE FINANÇAS PESSOAIS");
        System.out.println("=================================================");
        System.out.println("1 - Cadastrar pessoa");
        System.out.println("2 - Cadastrar renda");
        System.out.println("3 - Cadastrar despesa");
        System.out.println("4 - Cadastrar dívida");
        System.out.println("5 - Cadastrar objetivo financeiro");
        System.out.println("6 - Rodar análise de viabilidade");
        System.out.println("7 - Listar pessoas cadastradas");
        System.out.println("8 - Ver histórico de análise de uma pessoa");
        System.out.println("0 - Sair");
        System.out.println("=================================================");
    }

    private void cadastrarPessoa() {
        System.out.println("\n--- Cadastro de Pessoa ---");
        String nome = lerTexto("Nome completo: ");
        String profissao = lerTexto("Profissão: ");
        String telefone = lerTexto("Telefone: ");
        String email = lerTexto("E-mail: ");
        LocalDate dataNascimento = lerData("Data de nascimento (dd/mm/aaaa): ");

        Pessoa pessoa = new Pessoa(null, nome, profissao, telefone, email, dataNascimento);
        pessoaRepository.save(pessoa);

        System.out.println("Pessoa cadastrada com sucesso! ID: " + pessoa.getId());
    }

    private void cadastrarRenda() {
        System.out.println("\n--- Cadastro de Renda ---");
        Pessoa pessoa = selecionarPessoa();
        String descricao = lerTexto("Descrição (ex.: Salário, Freelance): ");
        BigDecimal valor = lerValorMonetario("Valor: R$ ");
        TipoValor tipo = lerTipoValor();
        LocalDate dataRecebimento = lerData("Data de recebimento (dd/mm/aaaa): ");

        Renda renda = new Renda(pessoa, descricao, valor, tipo, dataRecebimento);
        rendaRepository.save(renda);

        System.out.println("Renda cadastrada com sucesso!");
    }

    private void cadastrarDespesa() {
        System.out.println("\n--- Cadastro de Despesa ---");
        Pessoa pessoa = selecionarPessoa();
        String descricao = lerTexto("Descrição: ");
        BigDecimal valor = lerValorMonetario("Valor: R$ ");
        CategoriaDespesa categoria = lerCategoriaDespesa();
        TipoValor tipo = lerTipoValor();
        LocalDateTime dataVencimento = lerDataHora("Data de vencimento (dd/mm/aaaa): ");

        Despesa despesa = new Despesa(null, descricao, pessoa, valor, categoria, tipo, dataVencimento, false);
        despesaRepository.save(despesa);

        System.out.println("Despesa cadastrada com sucesso!");
    }

    private void cadastrarDivida() {
        System.out.println("\n--- Cadastro de Dívida ---");
        Pessoa pessoa = selecionarPessoa();
        String descricao = lerTexto("Descrição (ex.: Financiamento do carro): ");
        BigDecimal valorTotal = lerValorMonetario("Valor total: R$ ");
        BigDecimal valorParcela = lerValorMonetario("Valor da parcela: R$ ");
        int quantidadeParcelas = lerNumeroInteiro("Quantidade de parcelas: ");
        int parcelasPagas = lerNumeroInteiro("Parcelas já pagas: ");
        BigDecimal taxaJuros = lerValorMonetario("Taxa de juros mensal (%): ");

        Divida divida = new Divida(pessoa, descricao, valorTotal, valorParcela,
                quantidadeParcelas, parcelasPagas, taxaJuros);
        dividasRepository.save(divida);

        System.out.println("Dívida cadastrada com sucesso!");
    }

    private void cadastrarObjetivo() {
        System.out.println("\n--- Cadastro de Objetivo Financeiro ---");
        Pessoa pessoa = selecionarPessoa();
        String descricao = lerTexto("Descrição (ex.: Moto Honda CG 160): ");
        BigDecimal valorEstimado = lerValorMonetario("Valor estimado: R$ ");
        BigDecimal valorParcelaEstimada = lerValorMonetario("Valor da parcela estimada: R$ ");

        ObjetivoFinanceiro objetivo = new ObjetivoFinanceiro(pessoa, descricao, valorEstimado, valorParcelaEstimada);
        objetivoRepository.save(objetivo);

        System.out.println("Objetivo cadastrado com sucesso! Status inicial: " + objetivo.getStatus());
    }

    private void rodarAnalise() {
        System.out.println("\n--- Análise de Viabilidade ---");
        Pessoa pessoa = selecionarPessoa();

        List<ObjetivoFinanceiro> objetivos = objetivoRepository.findByPessoaId(pessoa.getId());
        if (objetivos.isEmpty()) {
            System.out.println("Essa pessoa ainda não tem nenhum objetivo cadastrado.");
            return;
        }

        System.out.println("Objetivos cadastrados:");
        for (ObjetivoFinanceiro o : objetivos) {
            System.out.println("  ID " + o.getId() + " - " + o.getDescricao() + " (status: " + o.getStatus() + ")");
        }

        Long objetivoId = (long) lerNumeroInteiro("Digite o ID do objetivo a analisar: ");
        ObjetivoFinanceiro objetivo = objetivos.stream()
                .filter(o -> o.getId().equals(objetivoId))
                .findFirst()
                .orElseThrow(() -> new DadosInvalidosException("Objetivo não encontrado para essa pessoa."));

        ResultadoAnalise resultado = analiseFinanceiraService.avaliarObjetivo(pessoa, objetivo);
        objetivoRepository.save(objetivo);

        System.out.println("\n----- RESULTADO DA ANÁLISE -----");
        System.out.println("Renda total:              R$ " + resultado.getRendaTotal());
        System.out.println("Despesa total:             R$ " + resultado.getDespesaTotal());
        System.out.println("Saldo disponível:          R$ " + resultado.getSaldoDesponivel());
        System.out.println("Comprometimento atual:     " + resultado.getComprometimentoAtual() + "%");
        System.out.println("Comprometimento projetado: " + resultado.getComprometimentoProjeto() + "%");
        System.out.println(resultado.getMensagem());
        System.out.println("---------------------------------\n");
    }

    private void listarPessoas() {
        System.out.println("\n--- Pessoas cadastradas ---");
        List<Pessoa> pessoas = pessoaRepository.findAll();
        if (pessoas.isEmpty()) {
            System.out.println("Nenhuma pessoa cadastrada ainda.");
            return;
        }
        for (Pessoa p : pessoas) {
            System.out.println("ID " + p.getId() + " - " + p.getNome() + " (" + p.getProfissao() + ")");
        }
    }
    private void verHistoricoAnalise() {
        System.out.println("\n--- Histórico de Análises ---");
        Pessoa pessoa = selecionarPessoa();

        List<HistoricoAnalise> historico = historicoAnaliseRepository.findByPessoaIdOrderByDataAnaliseDesc(pessoa.getId());
        if (historico.isEmpty()) {
            System.out.println("Essa pessoa ainda não tem nenhuma análise registrada.");
            return;
        }

        for (HistoricoAnalise h : historico) {
            String descricaoObjetivo = h.getObjetivo() != null ? h.getObjetivo().getDescricao() : "(objetivo removido)";
            System.out.println("\nData: " + h.getDataAnalise());
            System.out.println("Objetivo: " + descricaoObjetivo);
            System.out.println("Renda total: R$ " + h.getRendaTotal() + " | Despesa total: R$ " + h.getDespesaTotal());
            System.out.println("Comprometimento projetado: " + h.getComprometimentoProjetado() + "%");
            System.out.println("Resultado: " + (h.isViavel() ? "VIÁVEL" : "NÃO VIÁVEL"));
        }
    }


    private Pessoa selecionarPessoa() {
        listarPessoas();
        List<Pessoa> pessoas = pessoaRepository.findAll();
        if (pessoas.isEmpty()) {
            throw new DadosInvalidosException("Cadastre uma pessoa antes de continuar.");
        }
        Long id = (long) lerNumeroInteiro("Digite o ID da pessoa: ");
        Optional<Pessoa> pessoa = pessoaRepository.findById(id);
        return pessoa.orElseThrow(() -> new DadosInvalidosException("Nenhuma pessoa encontrada com o ID " + id));
    }

    private String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private int lerNumeroInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    private BigDecimal lerValorMonetario(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim().replace(",", ".");
            try {
                BigDecimal valor = new BigDecimal(entrada);
                if (valor.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("O valor não pode ser negativo.");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Digite um valor numérico válido (ex.: 1500.50).");
            }
        }
    }

    private LocalDate lerData(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            try {
                return LocalDate.parse(entrada, FORMATO_DATA);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Use o formato dd/mm/aaaa.");
            }
        }
    }

    private LocalDateTime lerDataHora(String mensagem) {
        return lerData(mensagem).atStartOfDay();
    }

    private TipoValor lerTipoValor() {
        System.out.println("Tipo de valor: 1 - Fixo | 2 - Variável");
        int opcao = lerNumeroInteiro("Escolha: ");
        return opcao == 1 ? TipoValor.FIXO : TipoValor.VARIVAEL;
    }

    private CategoriaDespesa lerCategoriaDespesa() {
        CategoriaDespesa[] categorias = CategoriaDespesa.values();
        System.out.println("Categorias:");
        for (int i = 0; i < categorias.length; i++) {
            System.out.println("  " + (i + 1) + " - " + categorias[i]);
        }
        int opcao = lerNumeroInteiro("Escolha: ");
        if (opcao < 1 || opcao > categorias.length) {
            System.out.println("Opção inválida, usando OUTROS.");
            return CategoriaDespesa.OUTROS;
        }
        return categorias[opcao - 1];
    }
}