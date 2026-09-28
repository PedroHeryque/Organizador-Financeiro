package pedroherique.financas.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pedroherique.financas.exception.RendaInsuficienteException;
import pedroherique.financas.model.Despesa;
import pedroherique.financas.model.Divida;
import pedroherique.financas.model.ObjetivoFinanceiro;
import pedroherique.financas.model.Pessoa;
import pedroherique.financas.model.Renda;
import pedroherique.financas.model.StatusObjetivo;
import pedroherique.financas.repository.DespesaRepository;
import pedroherique.financas.repository.DividaRepository;
import pedroherique.financas.repository.RendaRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class AnaliseFinanceiraService {

    private static final int ESCALA = 2; // 2 casas decimais em todo cálculo monetário/percentual

    private final RendaRepository rendaRepository;
    private final DespesaRepository despesaRepository;
    private final DividaRepository dividaRepository;

    // Vem do application.properties (ex.: app.limite-comprometimento=30.0)
    // Assim dá para ajustar sem recompilar o projeto.
    @Value("${app.limite-comprometimento:30.0}")
    private BigDecimal limiteComprometimentoRecomendado;

    public AnaliseFinanceiraService(RendaRepository rendaRepository,
                                    DespesaRepository despesaRepository,
                                    DividaRepository dividaRepository) {
        this.rendaRepository = rendaRepository;
        this.despesaRepository = despesaRepository;
        this.dividaRepository = dividaRepository;
    }

    public BigDecimal calcularRendaTotal(Pessoa pessoa) {
        List<Renda> rendas = rendaRepository.findByPessoaId(pessoa.getId());
        return rendas.stream()
                .map(Renda::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularDespesaTotal(Pessoa pessoa) {
        // Soma as despesas cadastradas que ainda NAO foram pagas...
        List<Despesa> despesas = despesaRepository.findByPessoaId(pessoa.getId());
        BigDecimal totalDespesas = despesas.stream()
                // Uma despesa paga deixa de comprometer a renda, entao nao pode entrar no calculo.
                .filter(d -> !d.isPaga())
                .map(Despesa::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ...mais as parcelas das dívidas que ainda não foram totalmente pagas.
        List<Divida> dividas = dividaRepository.findByPessoaId(pessoa.getId());
        BigDecimal totalParcelasDividas = dividas.stream()
                .filter(d -> d.getParcelasRestantes() > 0)
                .map(Divida::getValorParcela)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalDespesas.add(totalParcelasDividas);
    }

    public BigDecimal calcularSaldoDisponivel(Pessoa pessoa) {
        return calcularRendaTotal(pessoa).subtract(calcularDespesaTotal(pessoa));
    }

    /**
     * Método principal: avalia se a pessoa pode assumir a parcela estimada
     * de um novo objetivo, sem estourar o limite de comprometimento de renda.
     */
    public ResultadoAnalise avaliarObjetivo(Pessoa pessoa, ObjetivoFinanceiro objetivo) {
        BigDecimal rendaTotal = calcularRendaTotal(pessoa);

        // Sem essa validação, dividir por rendaTotal = 0 quebraria o cálculo
        // (ou pior: passaria despercebido, gerando um percentual sem sentido).
        if (rendaTotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RendaInsuficienteException(
                    "Não é possível calcular a viabilidade: a pessoa não possui renda cadastrada.");
        }

        BigDecimal despesaTotal = calcularDespesaTotal(pessoa);
        BigDecimal saldoDisponivel = rendaTotal.subtract(despesaTotal);

        BigDecimal comprometimentoAtual = despesaTotal
                .divide(rendaTotal, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(ESCALA, RoundingMode.HALF_UP);

        BigDecimal despesaComNovaParcela = despesaTotal.add(objetivo.getValorParcelaEstimada());
        BigDecimal comprometimentoProjetado = despesaComNovaParcela
                .divide(rendaTotal, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(ESCALA, RoundingMode.HALF_UP);

        boolean viavel = comprometimentoProjetado.compareTo(limiteComprometimentoRecomendado) <= 0
                && saldoDisponivel.compareTo(objetivo.getValorParcelaEstimada()) >= 0;

        String mensagem = viavel
                ? "Compra viável: o comprometimento da renda ficaria em " + comprometimentoProjetado + "%."
                : "Compra não recomendada: o comprometimento da renda passaria para "
                + comprometimentoProjetado + "%, acima do limite de "
                + limiteComprometimentoRecomendado + "%.";

        // Atualiza o status do objetivo automaticamente com base no resultado
        objetivo.setStatus(viavel ? StatusObjetivo.APROVADO : StatusObjetivo.REPROVADO);

        return new ResultadoAnalise(rendaTotal, despesaTotal, saldoDisponivel,
                comprometimentoAtual, comprometimentoProjetado, viavel, mensagem);
    }
}