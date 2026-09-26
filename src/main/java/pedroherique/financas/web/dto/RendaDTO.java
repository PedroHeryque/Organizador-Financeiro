package pedroherique.financas.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import pedroherique.financas.model.TipoValor;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RendaDTO {

    private Long id;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotNull
    @Positive(message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull
    private TipoValor tipo;

    private LocalDate dataRecebimento;

    public RendaDTO() {
    }

    public RendaDTO(Long id, String descricao, BigDecimal valor, TipoValor tipo, LocalDate dataRecebimento) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.dataRecebimento = dataRecebimento;
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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public TipoValor getTipo() {
        return tipo;
    }

    public void setTipo(TipoValor tipo) {
        this.tipo = tipo;
    }

    public LocalDate getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(LocalDate dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }
}