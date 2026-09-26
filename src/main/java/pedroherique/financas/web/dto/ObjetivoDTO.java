package pedroherique.financas.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import pedroherique.financas.model.StatusObjetivo;

import java.math.BigDecimal;

public class ObjetivoDTO {

    private Long id;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotNull
    @Positive(message = "O valor estimado deve ser maior que zero")
    private BigDecimal valorEstimado;

    @NotNull
    @Positive(message = "O valor da parcela estimada deve ser maior que zero")
    private BigDecimal valorParcelaEstimada;

    private StatusObjetivo status;

    public ObjetivoDTO() {
    }

    public ObjetivoDTO(Long id, String descricao, BigDecimal valorEstimado,
                       BigDecimal valorParcelaEstimada, StatusObjetivo status) {
        this.id = id;
        this.descricao = descricao;
        this.valorEstimado = valorEstimado;
        this.valorParcelaEstimada = valorParcelaEstimada;
        this.status = status;
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
}