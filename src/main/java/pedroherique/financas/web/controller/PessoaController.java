package pedroherique.financas.web.controller;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
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
import pedroherique.financas.exception.RecursoNaoEncontradoException;
import pedroherique.financas.model.Pessoa;
import pedroherique.financas.repository.PessoaRepository;
import pedroherique.financas.web.dto.PessoaDTO;

import java.util.List;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    private final PessoaRepository pessoaRepository;

    public PessoaController(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @PostMapping
    public ResponseEntity<PessoaDTO> criar(@Valid @RequestBody PessoaDTO dto) {
        Pessoa pessoa = new Pessoa(dto.getNome(), dto.getProfissao(), dto.getTelefone(),
                dto.getEmail(), dto.getDataNascimento());
        pessoaRepository.save(pessoa);

        return ResponseEntity.status(HttpStatus.CREATED).body(paraDTO(pessoa));
    }

    @GetMapping
    public List<PessoaDTO> listar() {
        return pessoaRepository.findAll().stream()
                .map(this::paraDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public PessoaDTO buscarPorId(@PathVariable Long id) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa não encontrada com o ID " + id));
        return paraDTO(pessoa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!pessoaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Pessoa não encontrada com o ID " + id);
        }
        try {
            pessoaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            // pessoa_id e NOT NULL em tb_analise/tb_despesa/tb_renda/tb_divida/tb_objetivo_financeiro,
            // entao o banco recusaria a exclusao com erro de FK (HTTP 500). Aqui vira 400 com mensagem clara.
            throw new DadosInvalidosException("Pessoa " + id
                    + " possui lançamentos vinculados e não pode ser excluída.");
        }
        return ResponseEntity.noContent().build();
    }

    private PessoaDTO paraDTO(Pessoa pessoa) {
        return new PessoaDTO(pessoa.getId(), pessoa.getNome(), pessoa.getProfissao(),
                pessoa.getTelefone(), pessoa.getEmail(), pessoa.getDataNascimento());
    }
}
