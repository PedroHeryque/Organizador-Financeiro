package pedroherique.financas.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "tb_analise")
public class HistoricoAnalise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objetivo_id")
    private ObjetivoFinanceiro objetivo;

    @Column(precision = 12 , scale = 2)
    private BigDecimal rendaTotal;

    @Column(precision = 12 , scale = 2)
    private BigDecimal despesaTotal;

    @Column(precision = 12,scale = 2)
    private BigDecimal saldoDisponivel;

    @Column(precision = 7,scale = 2)
    private BigDecimal comprometimentoAtual;

    @Column(precision = 7,scale = 2)
    private BigDecimal comprometimentoProjetado;

    private boolean viavel;

    @Column(length = 500)
    private String mensagem;

    @Column(nullable = false,updatable = false)
    private LocalDate dataAnalise;

    public HistoricoAnalise() {
    }

    public HistoricoAnalise(Pessoa pessoa, ObjetivoFinanceiro objetivo,BigDecimal rendaTotal, BigDecimal despesaTotal,BigDecimal saldoDisponivel,BigDecimal comprometimentoAtual,BigDecimal comprometimentoProjetado,Boolean viavel,String mensagem){
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
    protected  void aoSalvar(){
        this.dataAnalise = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public ObjetivoFinanceiro getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(ObjetivoFinanceiro objetivo) {
        this.objetivo = objetivo;
    }

    public BigDecimal getDespesaTotal() {
        return despesaTotal;
    }

    public void setDespesaTotal(BigDecimal despesaTotal) {
        this.despesaTotal = despesaTotal;
    }

    public BigDecimal getRendaTotal() {
        return rendaTotal;
    }

    public void setRendaTotal(BigDecimal rendaTotal) {
        this.rendaTotal = rendaTotal;
    }

    public BigDecimal getSaldoDisponivel() {
        return saldoDisponivel;
    }

    public void setSaldoDisponivel(BigDecimal saldoDisponivel) {
        this.saldoDisponivel = saldoDisponivel;
    }

    public BigDecimal getComprometimentoAtual() {
        return comprometimentoAtual;
    }

    public void setComprometimentoAtual(BigDecimal comprometimentoAtual) {
        this.comprometimentoAtual = comprometimentoAtual;
    }

    public boolean isViavel() {
        return viavel;
    }

    public void setViavel(boolean viavel) {
        this.viavel = viavel;
    }

    public BigDecimal getComprometimentoProjetado() {
        return comprometimentoProjetado;
    }

    public void setComprometimentoProjetado(BigDecimal comprometimentoProjetado) {
        this.comprometimentoProjetado = comprometimentoProjetado;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDate getDataAnalise() {
        return dataAnalise;
    }

    public void setDataAnalise(LocalDate dataAnalise) {
        this.dataAnalise = dataAnalise;
    }
}
