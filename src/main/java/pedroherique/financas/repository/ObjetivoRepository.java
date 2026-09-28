package pedroherique.financas.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pedroherique.financas.model.ObjetivoFinanceiro;
@Repository
public  interface ObjetivoRepository extends JpaRepository<ObjetivoFinanceiro, Long> {

    List<ObjetivoFinanceiro> findByPessoaId(Long pessoaId);

    // Usado pelo AnaliseController para buscar o objetivo já filtrando por pessoa no proprio SQL,
    // em vez de carregar todos os objetivos da pessoa e filtrar em memoria.
    Optional<ObjetivoFinanceiro> findByIdAndPessoaId(Long id, Long pessoaId);

}
