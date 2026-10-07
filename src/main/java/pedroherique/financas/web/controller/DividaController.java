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
import pedroherique.financas.model.Divida;
import pedroherique.financas.model.Pessoa;
import pedroherique.financas.repository.DividaRepository;
import pedroherique.financas.repository.PessoaRepository;
import pedroherique.financas.web.dto.DividaDTO;

import java.util.List;

@RestController
@RequestMapping("/pessoas/{pessoaId}/dividas")
public class DividaController {

    private final DividaRepository dividaRepository;
    private final PessoaRepository pessoaRepository;

    public DividaController(DividaRepository dividaRepository, PessoaRepository pessoaRepository) {
        this.dividaRepository = dividaRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @PostMapping
    public ResponseEntity<DividaDTO> criar(@PathVariable Long pessoaId, @Valid @RequestBody DividaDTO dto) {
        Pessoa pessoa = buscarPessoa(pessoaId);
        Divida divida = new Divida(pessoa, dto.getDescricao(), dto.getValorTotal(), dto.getValorParcela(),
                dto.getQuantidadeParcelas(), dto.getParcelasPagas(), dto.getTaxaJurosMensal());
        dividaRepository.save(divida);
        return ResponseEntity.status(HttpStatus.CREATED).body(paraDTO(divida));
    }

    @GetMapping
    public List<DividaDTO> listar(@PathVariable Long pessoaId) {
        return dividaRepository.findByPessoaId(pessoaId).stream()
                .map(this::paraDTO)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long pessoaId, @PathVariable Long id) {
        Divida divida = dividaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Dívida não encontrada com o ID " + id));
        // getId() no proxy lazy do Hibernate nao dispara carga, entao nao ha LazyInitializationException aqui.
        if (!pessoaId.equals(divida.getPessoa().getId())) {
            throw new RecursoNaoEncontradoException("Dívida " + id + " não pertence à pessoa " + pessoaId);
        }
        dividaRepository.delete(divida);
        return ResponseEntity.noContent().build();
    }

    private Pessoa buscarPessoa(Long pessoaId) {
        return pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa não encontrada com o ID " + pessoaId));
    }

    private DividaDTO paraDTO(Divida divida) {
        return new DividaDTO(divida.getId(), divida.getDescricao(), divida.getValorTotal(),
                divida.getValorParcela(), divida.getQuantidadeParcelas(), divida.getParcelasPagas(),
                divida.getTaxaJurosMensal());
    }
}