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
import pedroherique.financas.model.Divida;
import pedroherique.financas.model.Pessoa;
import pedroherique.financas.repository.DividasRepository;
import pedroherique.financas.repository.PessoaRepository;
import pedroherique.financas.web.dto.DividaDTO;

import java.util.List;

@RestController
@RequestMapping("/pessoas/{pessoaId}/dividas")
public class DividasController {

    private final DividasRepository dividasRepository;
    private final PessoaRepository pessoaRepository;

    public DividasController(DividasRepository dividasRepository, PessoaRepository pessoaRepository) {
        this.dividasRepository = dividasRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @PostMapping
    public ResponseEntity<DividaDTO> criar(@PathVariable Long pessoaId, @Valid @RequestBody DividaDTO dto) {
        Pessoa pessoa = buscarPessoa(pessoaId);
        Divida divida = new Divida(pessoa, dto.getDescricao(), dto.getValorTotal(), dto.getValorParcela(),
                dto.getQuantidadeParcelas(), dto.getParcelasPagas(), dto.getTaxaJurosMensal());
        dividasRepository.save(divida);
        return ResponseEntity.status(HttpStatus.CREATED).body(paraDTO(divida));
    }

    @GetMapping
    public List<DividaDTO> listar(@PathVariable Long pessoaId) {
        return dividasRepository.findByPessoaId(pessoaId).stream()
                .map(this::paraDTO)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long pessoaId, @PathVariable Long id) {
        if (!dividasRepository.existsById(id)) {
            throw new DadosInvalidosException("Dívida não encontrada com o ID " + id);
        }
        dividasRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Pessoa buscarPessoa(Long pessoaId) {
        return pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new DadosInvalidosException("Pessoa não encontrada com o ID " + pessoaId));
    }

    private DividaDTO paraDTO(Divida divida) {
        return new DividaDTO(divida.getId(), divida.getDescricao(), divida.getValorTotal(),
                divida.getValorParcela(), divida.getQuantidadeParcelas(), divida.getParcelasPagas(),
                divida.getTaxaJurosMensal());
    }
}