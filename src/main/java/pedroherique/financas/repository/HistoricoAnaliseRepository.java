package pedroherique.financas.repository;

import pedroherique.financas.model.HistoricoAnalise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoAnaliseRepository extends JpaRepository<HistoricoAnalise, Long> {

    List<HistoricoAnalise> findByPessoaIdOrderByDataAnaliseDesc(Long pessoaId);
}