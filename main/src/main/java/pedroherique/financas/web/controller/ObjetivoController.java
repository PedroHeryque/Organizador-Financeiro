package pedroherique.financas.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pedroherique.financas.exception.DadosInvalidosException;
import pedroherique.financas.model.ObjetivoFinanceiro;
import pedroherique.financas.model.Pessoa;
import pedroherique.financas.repository.ObjetivoRepository;
import pedroherique.financas.repository.PessoaRepository;
import pedroherique.financas.web.dto.ObjetivoDTO;

import java.util.List;

@RestController
@RequestMapping("/pessoas/{pessoaId}/objetivos")
public class ObjetivoController {

    private final ObjetivoRepository objetivoRepository;
    private final PessoaRepository pessoaRepository;

    public ObjetivoController(ObjetivoRepository objetivoRepository, PessoaRepository pessoaRepository) {
        this.objetivoRepository = objetivoRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @PostMapping
    public ResponseEntity<ObjetivoDTO> criar(@PathVariable Long pessoaId, @Valid @RequestBody ObjetivoDTO dto) {
        Pessoa pessoa = buscarPessoa(pessoaId);
        ObjetivoFinanceiro objetivo = new ObjetivoFinanceiro(pessoa, dto.getDescricao(),
                dto.getValorEstimado(), dto.getValorParcelaEstimada());
        objetivoRepository.save(objetivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(paraDTO(objetivo));
    }

    @GetMapping
    public List<ObjetivoDTO> listar(@PathVariable Long pessoaId) {
        return objetivoRepository.findByPessoaId(pessoaId).stream()
                .map(this::paraDTO)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long pessoaId, @PathVariable Long id) {
        if (!objetivoRepository.existsById(id)) {
            throw new DadosInvalidosException("Objetivo não encontrado com o ID " + id);
        }
        objetivoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Pessoa buscarPessoa(Long pessoaId) {
        return pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new DadosInvalidosException("Pessoa não encontrada com o ID " + pessoaId));
    }

    private ObjetivoDTO paraDTO(ObjetivoFinanceiro objetivo) {
        return new ObjetivoDTO(objetivo.getId(), objetivo.getDescricao(), objetivo.getValorEstimado(),
                objetivo.getValorParcelaEstimada(), objetivo.getStatus());
    }
}