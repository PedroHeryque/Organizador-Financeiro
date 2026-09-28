package pedroherique.financas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pedroherique.financas.model.HistoricoAnalise;

@Repository
public interface HistoricoAnaliseRepository extends JpaRepository<HistoricoAnalise, Long> {

    // Busca persona e objetivo junto: como sao LAZY, sem isso a serializacao do JSON
    // lancaria LazyInitializationException (a sessao ja foi fechada ao sair do repositorio).
    @EntityGraph(attributePaths = {"pessoa", "objetivo"})
    List<HistoricoAnalise> findByPessoaIdOrderByDataAnaliseDesc(Long pessoaId);
}
