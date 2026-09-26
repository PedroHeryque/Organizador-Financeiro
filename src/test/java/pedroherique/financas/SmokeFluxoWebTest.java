package pedroherique.financas;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SmokeFluxoWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void fluxoCompletoPessoaObjetivoAnaliseEHistorico() throws Exception {
        String pessoaJson = mockMvc.perform(post("/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Maria\",\"email\":\"maria@test.com\",\"dataNascimento\":\"1990-05-10\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long pessoaId = Long.parseLong(pessoaJson.replaceAll(".*\"id\":(\\d+).*", "$1"));

        String objetivoJson = mockMvc.perform(post("/pessoas/" + pessoaId + "/objetivos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricao\":\"Moto\",\"valorEstimado\":15000,\"valorParcelaEstimada\":500}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long objetivoId = Long.parseLong(objetivoJson.replaceAll(".*\"id\":(\\d+).*", "$1"));

        mockMvc.perform(post("/pessoas/" + pessoaId + "/rendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricao\":\"Salario\",\"valor\":4000,\"tipo\":\"FIXO\",\"dataRecebimento\":\"2026-09-01\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/pessoas/" + pessoaId + "/objetivos/" + objetivoId + "/analise"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.viavel").value(true));

        mockMvc.perform(get("/pessoas/" + pessoaId + "/historico"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].objetivoDescricao").value("Moto"))
                .andExpect(jsonPath("$[0].rendaTotal").value(4000))
                .andExpect(jsonPath("$[0].dataAnalise").exists());
    }
}
