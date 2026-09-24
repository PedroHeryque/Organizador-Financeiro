package pedroherique.financas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_divida")
public class Divida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao; // ex.: "Financiamento do carro"

    @NotNull
    @Positive(message = "O valor total deve ser maior que zero")
    @Column(precision = 12, scale = 2)
    private BigDecimal valorTotal;

    @NotNull
    @Positive(message = "O valor da parcela deve ser maior que zero")
    @Column(precision = 12, scale = 2)
    private BigDecimal valorParcela;

    @NotNull
    @Positive(message = "A quantidade de parcelas deve ser maior que zero")
    private Integer quantidadeParcelas;

    @NotNull
    @PositiveOrZero(message = "Parcelas pagas não pode ser negativo")
    private Integer parcelasPagas;

    @NotNull
    @PositiveOrZero(message = "A taxa de juros não pode ser negativa")
    @Column(precision = 6, scale = 4)
    private BigDecimal taxaJurosMensal;

    public Divida() {
    }

    public Divida(Pessoa pessoa, String descricao, BigDecimal valorTotal, BigDecimal valorParcela,
                  Integer quantidadeParcelas, Integer parcelasPagas, BigDecimal taxaJurosMensal) {
        this.pessoa = pessoa;
        this.descricao = descricao;
        this.valorTotal = valorTotal;
        this.valorParcela = valorParcela;
        this.quantidadeParcelas = quantidadeParcelas;
        this.parcelasPagas = parcelasPagas;
        this.taxaJurosMensal = taxaJurosMensal;
    }


    public int getParcelasRestantes() {
        return quantidadeParcelas - parcelasPagas;
    }


    public BigDecimal getSaldoDevedor() {
        return valorParcela.multiply(BigDecimal.valueOf(getParcelasRestantes()));
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

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(BigDecimal valorParcela) {
        this.valorParcela = valorParcela;
    }

    public Integer getQuantidadeParcelas() {
        return quantidadeParcelas;
    }

    public void setQuantidadeParcelas(Integer quantidadeParcelas) {
        this.quantidadeParcelas = quantidadeParcelas;
    }

    public Integer getParcelasPagas() {
        return parcelasPagas;
    }

    public void setParcelasPagas(Integer parcelasPagas) {
        this.parcelasPagas = parcelasPagas;
    }

    public BigDecimal getTaxaJurosMensal() {
        return taxaJurosMensal;
    }

    public void setTaxaJurosMensal(BigDecimal taxaJurosMensal) {
        this.taxaJurosMensal = taxaJurosMensal;
    }
}