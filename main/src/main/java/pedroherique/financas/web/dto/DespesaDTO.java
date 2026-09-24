package pedroherique.financas.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import pedroherique.financas.model.CategoriaDespesa;
import pedroherique.financas.model.TipoValor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DespesaDTO {

    private Long id;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotNull
    @Positive(message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    private CategoriaDespesa categoria;

    @NotNull
    private TipoValor tipo;

    private LocalDateTime dataVencimento;

    private boolean paga;

    public DespesaDTO() {
    }

    public DespesaDTO(Long id, String descricao, BigDecimal valor, CategoriaDespesa categoria,
                      TipoValor tipo, LocalDateTime dataVencimento, boolean paga) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
        this.tipo = tipo;
        this.dataVencimento = dataVencimento;
        this.paga = paga;
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

    public CategoriaDespesa getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDespesa categoria) {
        this.categoria = categoria;
    }

    public TipoValor getTipo() {
        return tipo;
    }

    public void setTipo(TipoValor tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDateTime dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public boolean isPaga() {
        return paga;
    }

    public void setPaga(boolean paga) {
        this.paga = paga;
    }
}