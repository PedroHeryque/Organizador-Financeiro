package pedroherique.financas.web.controller;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pedroherique.financas.repository.PessoaRepository;
import pedroherique.financas.exception.DadosInvalidosException;
import pedroherique.financas.model.Pessoa;
import pedroherique.financas.web.dto.PessoaDTO;

import java.util.List;
@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    private PessoaRepository pessoaRepository;

    public PessoaController(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }
    @PostMapping
    public ResponseEntity<PessoaDTO>criar(@Valid @RequestBody PessoaDTO  dto) {
        Pessoa pessoa = new Pessoa(dto.getNome(), dto.getProfissao(),dto.getTelefone(),dto.getEmail(),dto.getDataNascimento());
        pessoaRepository.save(pessoa);
        return ResponseEntity.status(HttpStatus.CREATED).body(paraDTO(pessoa));
    }
    @GetMapping
    public List<PessoaDTO> listar() {
        return pessoaRepository.findAll().stream().map(this::paraDTO).toList();
    }

    @GetMapping("/{id}")
    public PessoaDTO buscarPorId(@PathVariable Long id) {
        Pessoa pessoa = pessoaRepository.findById(id).orElseThrow(() -> new DadosInvalidosException("Pessoa não encontrada com o ID " + id));
        return paraDTO(pessoa);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!pessoaRepository.existsById(id)) {
            throw new DadosInvalidosException("Pessoa não encontrada com o ID " + id);
        }
        pessoaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    private PessoaDTO paraDTO(Pessoa pessoa) {
        return new PessoaDTO(pessoa.getId(), pessoa.getNome(), pessoa.getProfissao(),
                pessoa.getTelefone(), pessoa.getEmail(), pessoa.getDataNascimento());
    }
}
