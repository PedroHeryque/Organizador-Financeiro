package pedroherique.financas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Guarda o resultado de cada análise de viabilidade já realizada,
 * para permitir consultar a evolução financeira de uma pessoa ao longo do tempo.
 */
@Entity
@Table(name = "tb_analise")
public class HistoricoAnalise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    // SET_NULL: ao excluir o objetivo, o historico e preservado com objetivo_id = null.
    // O AnaliseController ja trata esse caso exibindo "(objetivo removido)".
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "objetivo_id")
    private ObjetivoFinanceiro objetivo; // pode ser null se um dia a análise não vier de um objetivo específico

    @Column(precision = 12, scale = 2)
    private BigDecimal rendaTotal;

    @Column(precision = 12, scale = 2)
    private BigDecimal despesaTotal;

    @Column(precision = 12, scale = 2)
    private BigDecimal saldoDisponivel;

    @Column(precision = 7, scale = 2)
    private BigDecimal comprometimentoAtual;

    @Column(precision = 7, scale = 2)
    private BigDecimal comprometimentoProjetado;

    private boolean viavel;

    @Column(length = 500)
    private String mensagem;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataAnalise;

    public HistoricoAnalise() {
    }

    public HistoricoAnalise(Pessoa pessoa, ObjetivoFinanceiro objetivo, BigDecimal rendaTotal,
                            BigDecimal despesaTotal, BigDecimal saldoDisponivel,
                            BigDecimal comprometimentoAtual, BigDecimal comprometimentoProjetado,
                            boolean viavel, String mensagem) {
        this.pessoa = pessoa;
        this.objetivo = objetivo;
        this.rendaTotal = rendaTotal;
        this.despesaTotal = despesaTotal;
        this.saldoDisponivel = saldoDisponivel;
        this.comprometimentoAtual = comprometimentoAtual;
        this.comprometimentoProjetado = comprometimentoProjetado;
        this.viavel = viavel;
        this.mensagem = mensagem;
    }

    @PrePersist
    protected void aoSalvar() {
        this.dataAnalise = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public ObjetivoFinanceiro getObjetivo() {
        return objetivo;
    }

    public BigDecimal getRendaTotal() {
        return rendaTotal;
    }

    public BigDecimal getDespesaTotal() {
        return despesaTotal;
    }

    public BigDecimal getSaldoDisponivel() {
        return saldoDisponivel;
    }

    public BigDecimal getComprometimentoAtual() {
        return comprometimentoAtual;
    }

    public BigDecimal getComprometimentoProjetado() {
        return comprometimentoProjetado;
    }

    public boolean isViavel() {
        return viavel;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDataAnalise() {
        return dataAnalise;
    }
}