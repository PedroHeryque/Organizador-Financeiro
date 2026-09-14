package pedroherique.financas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_objetivo_financeiro")
public class ObjetivoFinanceiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotNull
    @Positive(message = "O valor estimado deve ser maior que zero")
    @Column(precision = 12, scale = 2)
    private BigDecimal valorEstimado;

    @NotNull
    @Positive(message = "O valor da parcela estimada deve ser maior que zero")
    @Column(precision = 12, scale = 2)
    private BigDecimal valorParcelaEstimada;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusObjetivo status;

    @Column(nullable = false, updatable = false)
    private LocalDate dataCriacao;

    public ObjetivoFinanceiro() {
    }

    public ObjetivoFinanceiro(Pessoa pessoa, String descricao, BigDecimal valorEstimado, BigDecimal valorParcelaEstimada) {
        this.pessoa = pessoa;
        this.descricao = descricao;
        this.valorEstimado = valorEstimado;
        this.valorParcelaEstimada = valorParcelaEstimada;
        this.status = StatusObjetivo.EM_ANALISE; // todo objetivo novo começa em análise
    }

    @PrePersist
    protected void aoSalvar() {
        this.dataCriacao = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorEstimado() {
        return valorEstimado;
    }

    public void setValorEstimado(BigDecimal valorEstimado) {
        this.valorEstimado = valorEstimado;
    }

    public BigDecimal getValorParcelaEstimada() {
        return valorParcelaEstimada;
    }

    public void setValorParcelaEstimada(BigDecimal valorParcelaEstimada) {
        this.valorParcelaEstimada = valorParcelaEstimada;
    }

    public StatusObjetivo getStatus() {
        return status;
    }

    public void setStatus(StatusObjetivo status) {
        this.status = status;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }
}