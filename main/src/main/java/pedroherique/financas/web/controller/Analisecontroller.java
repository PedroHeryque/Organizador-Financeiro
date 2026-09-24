package pedroherique.financas.web.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pedroherique.financas.model.HistoricoAnalise;
import pedroherique.financas.model.ObjetivoFinanceiro;
import pedroherique.financas.model.Pessoa;
import pedroherique.financas.exception.DadosInvalidosException;
import pedroherique.financas.repository.HistoricoAnaliseRepository;
import pedroherique.financas.repository.ObjetivoRepository;
import pedroherique.financas.repository.PessoaRepository;
import pedroherique.financas.service.AnaliseFinanceiraService;
import pedroherique.financas.service.ResultadoAnalise;
import pedroherique.financas.web.dto.ResultadoAnaliseDTO;

import java.util.List;

@RestController
@RequestMapping("\"/pessoas/{pessoaId}/objetivos/{objetivoId}/analise\"")
public class Analisecontroller {

    private final PessoaRepository pessoaRepository;
    private final ObjetivoRepository objetivoRepository;
    private final HistoricoAnaliseRepository historicoAnaliseRepository;
    private final AnaliseFinanceiraService analiseFinanceiraService;

    public Analisecontroller(PessoaRepository pessoaRepository, ObjetivoRepository objetivoRepository, HistoricoAnaliseRepository historicoAnaliseRepository, AnaliseFinanceiraService analiseFinanceiraService) {
        this.pessoaRepository = pessoaRepository;
        this.objetivoRepository = objetivoRepository;
        this.historicoAnaliseRepository = historicoAnaliseRepository;
        this.analiseFinanceiraService = analiseFinanceiraService;
    }
    @PostMapping
    public  ResultadoAnaliseDTO avalia(@PathVariable long pessoaId ,@PathVariable long objetivoId) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId).orElseThrow(() -> new DadosInvalidosException("Pessoa não encontrada com o ID " + pessoaId));
        ObjetivoFinanceiro objetivo = objetivoRepository.findByPessoaId(pessoaId).stream().filter(o -> o.getId().equals(objetivoId)).findFirst().orElseThrow(() -> new DadosInvalidosException("Objetivo não encontrado para essa pessoa."));
        ResultadoAnalise resultado = analiseFinanceiraService.avaliarObjetivo(pessoa, objetivo);
        objetivoRepository.save(objetivo);
        HistoricoAnalise historico = new HistoricoAnalise(pessoa, objetivo,
                resultado.getRendaTotal(), resultado.getDespesaTotal(), resultado.getSaldoDesponivel(),
                resultado.getComprometimentoAtual(), resultado.getComprometimentoProjeto(),
                resultado.isViavel(), resultado.getMensagem());
        historicoAnaliseRepository.save(historico);

        return paraDTO(resultado);

    }
    @GetMapping("/historico")
    public List<HistoricoAnalise> historico(@PathVariable Long pessoaId, @PathVariable Long objetivoId) {
        return historicoAnaliseRepository.findByPessoaIdOrderByDataAnaliseDesc(pessoaId);
    }

    private ResultadoAnaliseDTO paraDTO(ResultadoAnalise resultado) {
        return new ResultadoAnaliseDTO(resultado.getRendaTotal(), resultado.getDespesaTotal(),
                resultado.getSaldoDesponivel(), resultado.getComprometimentoAtual(),
                resultado.getComprometimentoProjeto(), resultado.isViavel(), resultado.getMensagem());
    }
}


