package pedroherique.financas.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HistoricoAnaliseDTO {

    private final Long id;
    private final String objetivoDescricao;
    private final BigDecimal rendaTotal;
    private final BigDecimal despesaTotal;
    private final BigDecimal saldoDisponivel;
    private final BigDecimal comprometimentoAtual;
    private final BigDecimal comprometimentoProjetado;
    private final boolean viavel;
    private final String mensagem;
    private final LocalDateTime dataAnalise;

    public HistoricoAnaliseDTO(Long id, String objetivoDescricao, BigDecimal rendaTotal, BigDecimal despesaTotal,
                               BigDecimal saldoDisponivel, BigDecimal comprometimentoAtual,
                               BigDecimal comprometimentoProjetado, boolean viavel, String mensagem,
                               LocalDateTime dataAnalise) {
        this.id = id;
        this.objetivoDescricao = objetivoDescricao;
        this.rendaTotal = rendaTotal;
        this.despesaTotal = despesaTotal;
        this.saldoDisponivel = saldoDisponivel;
        this.comprometimentoAtual = comprometimentoAtual;
        this.comprometimentoProjetado = comprometimentoProjetado;
        this.viavel = viavel;
        this.mensagem = mensagem;
        this.dataAnalise = dataAnalise;
    }

    public Long getId() {
        return id;
    }

    public String getObjetivoDescricao() {
        return objetivoDescricao;
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

    public LocalDateTime getDataAnalise() {
        return dataAnalise;
    }
}
