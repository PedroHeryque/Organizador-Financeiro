package pedroherique.financas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_despesa")
public class Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;


    @NotBlank(message = "Descrição obrigatoria")
    private String descricao;


    @NotNull(message = "O valor é obrigatorio ")
    @Positive(message = "O valor tem que ser maior que 0")
    @Column(precision = 12 ,scale = 2)
    private BigDecimal valor;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CategoriaDespesa categoria;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoValor tipo;

    private LocalDateTime dataVencimento;

    private Boolean paga;

    public Despesa() {
    }

    public Despesa(Long id, String descricao, Pessoa pessoa, BigDecimal valor, CategoriaDespesa categoria, TipoValor tipo, LocalDateTime dataVencimento, Boolean paga) {
        this.id = id;
        this.descricao = descricao;
        this.pessoa = pessoa;
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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDateTime dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public TipoValor getTipo() {
        return tipo;
    }

    public void setTipo(TipoValor tipo) {
        this.tipo = tipo;
    }

    public CategoriaDespesa getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDespesa categoria) {
        this.categoria = categoria;
    }

    public Boolean getPaga() {
        return paga;
    }

    public void setPaga(Boolean paga) {
        this.paga = paga;
    }

    public boolean isPaga() {
        return paga;
    }
}
