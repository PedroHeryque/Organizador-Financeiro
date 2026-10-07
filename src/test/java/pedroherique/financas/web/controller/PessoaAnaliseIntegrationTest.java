package pedroherique.financas.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pedroherique.financas.web.dto.DividaDTO;
import pedroherique.financas.web.dto.ObjetivoDTO;
import pedroherique.financas.web.dto.PessoaDTO;
import pedroherique.financas.web.dto.RendaDTO;
import pedroherique.financas.web.dto.ResultadoAnaliseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Teste de integração ponta a ponta: sobe um Postgres real (via Testcontainers,
 * usando Docker) e chama a API de verdade, pelo mesmo caminho HTTP que o
 * Postman usa. Confirma que Controller, Service, Repository e banco funcionam
 * juntos corretamente — diferente do teste unitário do service, que usa mocks.
 *
 * Requer Docker instalado e rodando na máquina para executar este teste.
 */
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class PessoaAnaliseIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headersComAutenticacao() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("gestor", "123456");
        return headers;
    }

    @Test
    void deveCadastrarPessoaRendaObjetivoERodarAnaliseCompleta() {
        // 1. Cadastra a pessoa
        PessoaDTO pessoaRequisicao = new PessoaDTO(null, "Pessoa Teste Integração", "Testador",
                "11999999999", "teste@integracao.com", LocalDate.of(2000, 1, 1));

        ResponseEntity<PessoaDTO> respostaPessoa = restTemplate.exchange(
                "/pessoas", HttpMethod.POST,
                new HttpEntity<>(pessoaRequisicao, headersComAutenticacao()), PessoaDTO.class);

        assertEquals(HttpStatus.CREATED, respostaPessoa.getStatusCode());
        Long pessoaId = respostaPessoa.getBody().getId();
        assertNotNull(pessoaId);

        // 2. Cadastra a renda (3000, igual ao exemplo da moto no guia)
        RendaDTO rendaRequisicao = new RendaDTO(null, "Salário", new BigDecimal("3000"),
                pedroherique.financas.model.TipoValor.FIXO, java.time.LocalDate.now());

        ResponseEntity<RendaDTO> respostaRenda = restTemplate.exchange(
                "/pessoas/" + pessoaId + "/rendas", HttpMethod.POST,
                new HttpEntity<>(rendaRequisicao, headersComAutenticacao()), RendaDTO.class);

        assertEquals(HttpStatus.CREATED, respostaRenda.getStatusCode());

        // 3. Cadastra o objetivo (parcela 400, deve dar não viável com despesa alta simulada via só a parcela)
        ObjetivoDTO objetivoRequisicao = new ObjetivoDTO(null, "Moto Teste",
                new BigDecimal("12000"), new BigDecimal("400"), null);

        ResponseEntity<ObjetivoDTO> respostaObjetivo = restTemplate.exchange(
                "/pessoas/" + pessoaId + "/objetivos", HttpMethod.POST,
                new HttpEntity<>(objetivoRequisicao, headersComAutenticacao()), ObjetivoDTO.class);

        assertEquals(HttpStatus.CREATED, respostaObjetivo.getStatusCode());
        Long objetivoId = respostaObjetivo.getBody().getId();

        // 4. Roda a análise de viabilidade
        ResponseEntity<ResultadoAnaliseDTO> respostaAnalise = restTemplate.exchange(
                "/pessoas/" + pessoaId + "/objetivos/" + objetivoId + "/analise", HttpMethod.POST,
                new HttpEntity<>(null, headersComAutenticacao()), ResultadoAnaliseDTO.class);

        assertEquals(HttpStatus.OK, respostaAnalise.getStatusCode());
        ResultadoAnaliseDTO resultado = respostaAnalise.getBody();
        assertNotNull(resultado);

        // Sem despesas cadastradas, só a parcela de 400 sobre renda de 3000 = 13,33% -> viável
        assertEquals(new BigDecimal("13.33"), resultado.getComprometimentoProjetado());
        assertFalse(!resultado.isViavel()); // deve ser viável nesse cenário simples
    }

    @Test
    void deveRetornar400AoCadastrarDividaComParcelasPagasMaioresQueOTotal() {
        PessoaDTO pessoa = new PessoaDTO(null, "Pessoa Dívida", null, null, null, null);
        ResponseEntity<PessoaDTO> respostaPessoa = restTemplate.exchange(
                "/pessoas", HttpMethod.POST,
                new HttpEntity<>(pessoa, headersComAutenticacao()), PessoaDTO.class);
        assertEquals(HttpStatus.CREATED, respostaPessoa.getStatusCode());
        Long pessoaId = respostaPessoa.getBody().getId();

        DividaDTO divida = new DividaDTO(null, "Financiamento", new BigDecimal("1000"),
                new BigDecimal("100"), 2, 3, BigDecimal.ZERO);
        ResponseEntity<String> resposta = restTemplate.exchange(
                "/pessoas/" + pessoaId + "/dividas", HttpMethod.POST,
                new HttpEntity<>(divida, headersComAutenticacao()), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertTrue(resposta.getBody().contains("Parcelas pagas"));
    }

    @Test
    void deveRetornar400AoAnalisarObjetivoSemRenda() {
        PessoaDTO pessoa = new PessoaDTO(null, "Pessoa Sem Renda", null, null, null, null);
        ResponseEntity<PessoaDTO> respostaPessoa = restTemplate.exchange(
                "/pessoas", HttpMethod.POST,
                new HttpEntity<>(pessoa, headersComAutenticacao()), PessoaDTO.class);
        assertEquals(HttpStatus.CREATED, respostaPessoa.getStatusCode());
        Long pessoaId = respostaPessoa.getBody().getId();

        ObjetivoDTO objetivo = new ObjetivoDTO(null, "Compra", new BigDecimal("1000"),
                new BigDecimal("100"), null);
        ResponseEntity<ObjetivoDTO> respostaObjetivo = restTemplate.exchange(
                "/pessoas/" + pessoaId + "/objetivos", HttpMethod.POST,
                new HttpEntity<>(objetivo, headersComAutenticacao()), ObjetivoDTO.class);
        assertEquals(HttpStatus.CREATED, respostaObjetivo.getStatusCode());
        Long objetivoId = respostaObjetivo.getBody().getId();

        ResponseEntity<String> resposta = restTemplate.exchange(
                "/pessoas/" + pessoaId + "/objetivos/" + objetivoId + "/analise", HttpMethod.POST,
                new HttpEntity<>(null, headersComAutenticacao()), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertTrue(resposta.getBody().contains("não possui renda cadastrada"));
    }

    @Test
    void deveRetornar404AoBuscarPessoaInexistente() {
        ResponseEntity<String> resposta = restTemplate.exchange(
                "/pessoas/999999", HttpMethod.GET,
                new HttpEntity<>(headersComAutenticacao()), String.class);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }
}