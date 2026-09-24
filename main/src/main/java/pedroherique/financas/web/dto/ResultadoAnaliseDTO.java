package pedroherique.financas.web.dto;

import java.math.BigDecimal;


public class ResultadoAnaliseDTO {

    private final BigDecimal rendaTotal;
    private final BigDecimal despesaTotal;
    private final BigDecimal saldoDisponivel;
    private final BigDecimal comprometimentoAtual;
    private final BigDecimal comprometimentoProjetado;
    private final boolean viavel;
    private final String mensagem;

    public ResultadoAnaliseDTO(BigDecimal rendaTotal, BigDecimal despesaTotal, BigDecimal saldoDisponivel,
                               BigDecimal comprometimentoAtual, BigDecimal comprometimentoProjetado,
                               boolean viavel, String mensagem) {
        this.rendaTotal = rendaTotal;
        this.despesaTotal = despesaTotal;
        this.saldoDisponivel = saldoDisponivel;
        this.comprometimentoAtual = comprometimentoAtual;
        this.comprometimentoProjetado = comprometimentoProjetado;
        this.viavel = viavel;
        this.mensagem = mensagem;
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
}