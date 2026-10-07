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
import pedroherique.financas.model.Pessoa;
import pedroherique.financas.model.Renda;
import pedroherique.financas.repository.PessoaRepository;
import pedroherique.financas.repository.RendaRepository;
import pedroherique.financas.web.dto.RendaDTO;

import java.util.List;

@RestController
@RequestMapping("/pessoas/{pessoaId}/rendas")
public class RendaController {

    private final RendaRepository rendaRepository;
    private final PessoaRepository pessoaRepository;

    public RendaController(RendaRepository rendaRepository, PessoaRepository pessoaRepository) {
        this.rendaRepository = rendaRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @PostMapping
    public ResponseEntity<RendaDTO> criar(@PathVariable Long pessoaId, @Valid @RequestBody RendaDTO dto) {
        Pessoa pessoa = buscarPessoa(pessoaId);
        Renda renda = new Renda(pessoa, dto.getDescricao(), dto.getValor(), dto.getTipo(), dto.getDataRecebimento());
        rendaRepository.save(renda);
        return ResponseEntity.status(HttpStatus.CREATED).body(paraDTO(renda));
    }

    @GetMapping
    public List<RendaDTO> listar(@PathVariable Long pessoaId) {
        return rendaRepository.findByPessoaId(pessoaId).stream()
                .map(this::paraDTO)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long pessoaId, @PathVariable Long id) {
        Renda renda = rendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Renda não encontrada com o ID " + id));
        // getId() no proxy lazy do Hibernate nao dispara carga, entao nao ha LazyInitializationException aqui.
        if (!pessoaId.equals(renda.getPessoa().getId())) {
            throw new RecursoNaoEncontradoException("Renda " + id + " não pertence à pessoa " + pessoaId);
        }
        rendaRepository.delete(renda);
        return ResponseEntity.noContent().build();
    }

    private Pessoa buscarPessoa(Long pessoaId) {
        return pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa não encontrada com o ID " + pessoaId));
    }

    private RendaDTO paraDTO(Renda renda) {
        return new RendaDTO(renda.getId(), renda.getDescricao(), renda.getValor(),
                renda.getTipo(), renda.getDataRecebimento());
    }
}