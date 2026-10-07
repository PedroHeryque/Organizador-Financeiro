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
import pedroherique.financas.exception.RecursoNaoEncontradoException;
import pedroherique.financas.model.Despesa;
import pedroherique.financas.model.Pessoa;
import pedroherique.financas.repository.DespesaRepository;
import pedroherique.financas.repository.PessoaRepository;
import pedroherique.financas.web.dto.DespesaDTO;

import java.util.List;

@RestController
@RequestMapping("/pessoas/{pessoaId}/despesas")
public class DespesaController {

    private final DespesaRepository despesaRepository;
    private final PessoaRepository pessoaRepository;

    public DespesaController(DespesaRepository despesaRepository, PessoaRepository pessoaRepository) {
        this.despesaRepository = despesaRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @PostMapping
    public ResponseEntity<DespesaDTO> criar(@PathVariable Long pessoaId, @Valid @RequestBody DespesaDTO dto) {
        Pessoa pessoa = buscarPessoa(pessoaId);
        // Ajuste a ordem/tipos abaixo se o construtor da sua Despesa for diferente
        // (lembre-se: no MenuConsole ficou Despesa(id, descricao, pessoa, valor, categoria, tipo, dataVencimento, paga))
        Despesa despesa = new Despesa(null, dto.getDescricao(), pessoa, dto.getValor(),
                dto.getCategoria(), dto.getTipo(), dto.getDataVencimento(), dto.isPaga());
        despesaRepository.save(despesa);
        return ResponseEntity.status(HttpStatus.CREATED).body(paraDTO(despesa));
    }

    @GetMapping
    public List<DespesaDTO> listar(@PathVariable Long pessoaId) {
        return despesaRepository.findByPessoaId(pessoaId).stream()
                .map(this::paraDTO)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long pessoaId, @PathVariable Long id) {
        Despesa despesa = despesaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Despesa não encontrada com o ID " + id));
        // getId() no proxy lazy do Hibernate nao dispara carga, entao nao ha LazyInitializationException aqui.
        if (!pessoaId.equals(despesa.getPessoa().getId())) {
            throw new RecursoNaoEncontradoException("Despesa " + id + " não pertence à pessoa " + pessoaId);
        }
        despesaRepository.delete(despesa);
        return ResponseEntity.noContent().build();
    }

    private Pessoa buscarPessoa(Long pessoaId) {
        return pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa não encontrada com o ID " + pessoaId));
    }

    private DespesaDTO paraDTO(Despesa despesa) {
        return new DespesaDTO(despesa.getId(), despesa.getDescricao(), despesa.getValor(),
                despesa.getCategoria(), despesa.getTipo(), despesa.getDataVencimento(), despesa.isPaga());
    }
}