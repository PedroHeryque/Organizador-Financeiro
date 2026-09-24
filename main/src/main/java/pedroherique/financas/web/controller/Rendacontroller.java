package pedroherique.financas.web.controller;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pedroherique.financas.exception.DadosInvalidosException;
import pedroherique.financas.model.Pessoa;
import pedroherique.financas.model.Renda;
import pedroherique.financas.repository.PessoaRepository;
import pedroherique.financas.repository.RendaRepository;
import pedroherique.financas.web.dto.RendaDTO;

import java.util.List;


@RestController
@RequestMapping("/pessoas/{pessoaId}/rendas")
public class Rendacontroller {

    private final PessoaRepository pessoaRepository;
    private final RendaRepository rendaRepository;
    private Long pessoaId;

    public Rendacontroller(PessoaRepository pessoaRepository, RendaRepository rendaRepository) {
        this.pessoaRepository = pessoaRepository;
        this.rendaRepository = rendaRepository;
    }
    @PostMapping
    public ResponseEntity<RendaDTO> criar(@PathVariable Long pessoaId, @Valid @RequestBody RendaDTO rendaDTO) {
        Pessoa pessoa = buscarPessoa(pessoaId);
        Renda renda = new Renda(pessoa, rendaDTO.getDescricao(), rendaDTO.getValor(), rendaDTO.getTipo(), rendaDTO.getDataRecebimento());
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
        if (!rendaRepository.existsById(id)) {
            throw new DadosInvalidosException("Renda não encontrada com o ID " + id);
        }
        rendaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Pessoa buscarPessoa(Long pessoaId) {
        return pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new DadosInvalidosException("Pessoa não encontrada com o ID " + pessoaId));
    }

    private RendaDTO paraDTO(Renda renda) {
        return new RendaDTO(renda.getId(), renda.getDescricao(), renda.getValor(),
                renda.getTipo(), renda.getDataRecebimento());
    }
    }

