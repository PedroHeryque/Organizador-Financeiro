package pedroherique.financas.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class DividaDTO {

    private Long id;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotNull
    @Positive(message = "O valor total deve ser maior que zero")
    private BigDecimal valorTotal;

    @NotNull
    @Positive(message = "O valor da parcela deve ser maior que zero")
    private BigDecimal valorParcela;

    @NotNull
    @Positive(message = "A quantidade de parcelas deve ser maior que zero")
    private Integer quantidadeParcelas;

    @NotNull
    @PositiveOrZero
    private Integer parcelasPagas;

    @NotNull
    @PositiveOrZero
    private BigDecimal taxaJurosMensal;

    public DividaDTO() {
    }

    public DividaDTO(Long id, String descricao, BigDecimal valorTotal, BigDecimal valorParcela,
                     Integer quantidadeParcelas, Integer parcelasPagas, BigDecimal taxaJurosMensal) {
        this.id = id;
        this.descricao = descricao;
        this.valorTotal = valorTotal;
        this.valorParcela = valorParcela;
        this.quantidadeParcelas = quantidadeParcelas;
        this.parcelasPagas = parcelasPagas;
        this.taxaJurosMensal = taxaJurosMensal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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