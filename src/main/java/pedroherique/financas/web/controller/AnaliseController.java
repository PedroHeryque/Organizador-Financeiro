package pedroherique.financas.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pedroherique.financas.exception.RecursoNaoEncontradoException;
import pedroherique.financas.model.HistoricoAnalise;
import pedroherique.financas.model.ObjetivoFinanceiro;
import pedroherique.financas.model.Pessoa;
import pedroherique.financas.repository.HistoricoAnaliseRepository;
import pedroherique.financas.repository.ObjetivoRepository;
import pedroherique.financas.repository.PessoaRepository;
import pedroherique.financas.service.AnaliseFinanceiraService;
import pedroherique.financas.service.ResultadoAnalise;
import pedroherique.financas.web.dto.HistoricoAnaliseDTO;
import pedroherique.financas.web.dto.ResultadoAnaliseDTO;

import java.util.List;

@RestController
@RequestMapping("/pessoas/{pessoaId}")
public class AnaliseController {

    private final PessoaRepository pessoaRepository;
    private final ObjetivoRepository objetivoRepository;
    private final HistoricoAnaliseRepository historicoAnaliseRepository;
    private final AnaliseFinanceiraService analiseFinanceiraService;

    public AnaliseController(PessoaRepository pessoaRepository,
                             ObjetivoRepository objetivoRepository,
                             HistoricoAnaliseRepository historicoAnaliseRepository,
                             AnaliseFinanceiraService analiseFinanceiraService) {
        this.pessoaRepository = pessoaRepository;
        this.objetivoRepository = objetivoRepository;
        this.historicoAnaliseRepository = historicoAnaliseRepository;
        this.analiseFinanceiraService = analiseFinanceiraService;
    }

    @PostMapping("/objetivos/{objetivoId}/analise")
    public ResultadoAnaliseDTO avaliar(@PathVariable Long pessoaId, @PathVariable Long objetivoId) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa não encontrada com o ID " + pessoaId));

        ObjetivoFinanceiro objetivo = objetivoRepository.findByIdAndPessoaId(objetivoId, pessoaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Objetivo não encontrado para essa pessoa."));

        ResultadoAnalise resultado = analiseFinanceiraService.avaliarObjetivo(pessoa, objetivo);
        objetivoRepository.save(objetivo); // persiste o status atualizado (APROVADO/REPROVADO)

        HistoricoAnalise historico = new HistoricoAnalise(pessoa, objetivo,
                resultado.getRendaTotal(), resultado.getDespesaTotal(), resultado.getSaldoDesponivel(),
                resultado.getComprometimentoAtual(), resultado.getComprometimentoProjeto(),
                resultado.isViavel(), resultado.getMensagem());
        historicoAnaliseRepository.save(historico);

        return paraDTO(resultado);
    }

    @GetMapping("/historico")
    public List<HistoricoAnaliseDTO> historico(@PathVariable Long pessoaId) {
        return historicoAnaliseRepository.findByPessoaIdOrderByDataAnaliseDesc(pessoaId).stream()
                .map(this::paraDTO)
                .toList();
    }

    private ResultadoAnaliseDTO paraDTO(ResultadoAnalise resultado) {
        return new ResultadoAnaliseDTO(resultado.getRendaTotal(), resultado.getDespesaTotal(),
                resultado.getSaldoDesponivel(), resultado.getComprometimentoAtual(),
                resultado.getComprometimentoProjeto(), resultado.isViavel(), resultado.getMensagem());
    }

    private HistoricoAnaliseDTO paraDTO(HistoricoAnalise historico) {
        String objetivoDescricao = historico.getObjetivo() != null
                ? historico.getObjetivo().getDescricao()
                : "(objetivo removido)";
        return new HistoricoAnaliseDTO(historico.getId(), objetivoDescricao,
                historico.getRendaTotal(), historico.getDespesaTotal(), historico.getSaldoDisponivel(),
                historico.getComprometimentoAtual(), historico.getComprometimentoProjetado(),
                historico.isViavel(), historico.getMensagem(), historico.getDataAnalise());
    }
}