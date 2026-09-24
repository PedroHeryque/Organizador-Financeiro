package pedroherique.financas.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pedroherique.financas.exception.RendaInsuficienteException;
import pedroherique.financas.model.Despesa;
import pedroherique.financas.model.ObjetivoFinanceiro;
import pedroherique.financas.model.Pessoa;
import pedroherique.financas.model.Renda;
import pedroherique.financas.model.StatusObjetivo;
import pedroherique.financas.model.TipoValor;
import pedroherique.financas.repository.DespesaRepository;
import pedroherique.financas.repository.DividasRepository;
import pedroherique.financas.repository.RendaRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnaliseFinanceiraServiceTest {

    @Mock
    private RendaRepository rendaRepository;

    @Mock
    private DespesaRepository despesaRepository;

    @Mock
    private DividasRepository dividaRepository;

    @InjectMocks
    private AnaliseFinanceiraService service;

    private Pessoa pessoa;

    @BeforeEach
    void setUp() {
        pessoa = new Pessoa(null, "Pessoa Teste", "Profissão Teste", "0000-0000", "teste@email.com", null);

        // @Value não é processado fora do contexto do Spring em um teste unitário puro,
        // então setamos manualmente o limite que normalmente viria do application.properties.
        ReflectionTestUtils.setField(service, "limiteComprometimentoRecomendado", new BigDecimal("30.0"));

        // Por padrão, ninguém tem dívida nenhuma nesses testes — evita NullPointerException.
        // "lenient" porque nem todo teste chega a consultar esse mock (ex.: o de renda zero
        // lança a exceção antes disso, e o Mockito reclamaria de "stub não usado" sem o lenient).
        lenient().when(dividaRepository.findByPessoaId(any())).thenReturn(Collections.emptyList());
    }

    private Renda renda(BigDecimal valor) {
        return new Renda(pessoa, "Salário", valor, TipoValor.FIXO, null);
    }

    private Despesa despesa(BigDecimal valor) {
        return new Despesa(null, "Despesa fixa", pessoa, valor, null, TipoValor.FIXO, null, false);
    }

    @Test
    void deveConsiderarViavelQuandoComprometimentoFicaAbaixoDoLimite() {
        // renda 5000, despesa 1000 (20%), nova parcela 200 -> projetado 24%
        when(rendaRepository.findByPessoaId(any())).thenReturn(List.of(renda(new BigDecimal("5000"))));
        when(despesaRepository.findByPessoaId(any())).thenReturn(List.of(despesa(new BigDecimal("1000"))));

        ObjetivoFinanceiro objetivo = new ObjetivoFinanceiro(pessoa, "Objetivo teste",
                new BigDecimal("2000"), new BigDecimal("200"));

        ResultadoAnalise resultado = service.avaliarObjetivo(pessoa, objetivo);

        assertTrue(resultado.isViavel());
        assertEquals(new BigDecimal("24.00"), resultado.getComprometimentoProjeto());
        assertEquals(StatusObjetivo.APROVADO, objetivo.getStatus());
    }

    @Test
    void deveConsiderarNaoViavelQuandoComprometimentoPassaDoLimite() {
        // Exemplo da moto do guia: renda 3000, despesa 1800, parcela 400 -> projetado 73,33%
        when(rendaRepository.findByPessoaId(any())).thenReturn(List.of(renda(new BigDecimal("3000"))));
        when(despesaRepository.findByPessoaId(any())).thenReturn(List.of(despesa(new BigDecimal("1800"))));

        ObjetivoFinanceiro objetivo = new ObjetivoFinanceiro(pessoa, "Moto Honda CG 160",
                new BigDecimal("12000"), new BigDecimal("400"));

        ResultadoAnalise resultado = service.avaliarObjetivo(pessoa, objetivo);

        assertFalse(resultado.isViavel());
        assertEquals(new BigDecimal("73.33"), resultado.getComprometimentoProjeto());
        assertEquals(StatusObjetivo.REPROVADO, objetivo.getStatus());
    }

    @Test
    void deveLancarExcecaoQuandoPessoaNaoTemRenda() {
        when(rendaRepository.findByPessoaId(any())).thenReturn(Collections.emptyList());

        ObjetivoFinanceiro objetivo = new ObjetivoFinanceiro(pessoa, "Objetivo teste",
                new BigDecimal("1000"), new BigDecimal("100"));

        assertThrows(RendaInsuficienteException.class,
                () -> service.avaliarObjetivo(pessoa, objetivo));
    }

    @Test
    void deveConsiderarViavelQuandoComprometimentoEstaExatamenteNoLimite() {
        // renda 1000, despesa 200 (20%), parcela 100 -> projetado exatamente 30.00%
        when(rendaRepository.findByPessoaId(any())).thenReturn(List.of(renda(new BigDecimal("1000"))));
        when(despesaRepository.findByPessoaId(any())).thenReturn(List.of(despesa(new BigDecimal("200"))));

        ObjetivoFinanceiro objetivo = new ObjetivoFinanceiro(pessoa, "Objetivo no limite",
                new BigDecimal("500"), new BigDecimal("100"));

        ResultadoAnalise resultado = service.avaliarObjetivo(pessoa, objetivo);

        assertEquals(new BigDecimal("30.00"), resultado.getComprometimentoProjeto());
        assertTrue(resultado.isViavel(), "No limite exato (30%) ainda deve ser considerado viável");
    }
}