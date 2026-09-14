package pedroherique.financas.services;

import java.math.BigDecimal;


public class ResultadoAnalise {
    private BigDecimal rendaTotal;
    private BigDecimal despesaTotal;
    private BigDecimal saldoDesponivel;
    private BigDecimal comprometimentoAtual;
    private BigDecimal comprometimentoProjeto;
    private final boolean viavel;
    private final String mensagem;

    public ResultadoAnalise(BigDecimal rendaTotal,BigDecimal despesaTotal, BigDecimal saldoDesponivel, BigDecimal comprometimentoAtual, BigDecimal comprometimentoProjeto, boolean viavel, String mensagem) {
        this.rendaTotal = rendaTotal;
        this.despesaTotal = despesaTotal;
        this.saldoDesponivel = saldoDesponivel;
        this.comprometimentoAtual = comprometimentoAtual;
        this.comprometimentoProjeto = comprometimentoProjeto;
        this.viavel = viavel;
        this.mensagem = mensagem;
    }

    public BigDecimal getRendaTotal() {
        return rendaTotal;
    }

    public void setRendaTotal(BigDecimal rendaTotal) {
        this.rendaTotal = rendaTotal;
    }

    public BigDecimal getDespesaTotal() {
        return despesaTotal;
    }

    public void setDespesaTotal(BigDecimal despesaTotal) {
        this.despesaTotal = despesaTotal;
    }

    public BigDecimal getSaldoDesponivel() {
        return saldoDesponivel;
    }

    public void setSaldoDesponivel(BigDecimal saldoDesponivel) {
        this.saldoDesponivel = saldoDesponivel;
    }

    public boolean isViavel() {
        return viavel;
    }

    public BigDecimal getComprometimentoProjeto() {
        return comprometimentoProjeto;
    }

    public void setComprometimentoProjeto(BigDecimal comprometimentoProjeto) {
        this.comprometimentoProjeto = comprometimentoProjeto;
    }

    public BigDecimal getComprometimentoAtual() {
        return comprometimentoAtual;
    }

    public void setComprometimentoAtual(BigDecimal comprometimentoAtual) {
        this.comprometimentoAtual = comprometimentoAtual;
    }

    public String getMensagem() {
        return mensagem;
    }
}
