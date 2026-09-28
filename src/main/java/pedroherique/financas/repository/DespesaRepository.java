package pedroherique.financas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pedroherique.financas.model.Despesa;
@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    List<Despesa> findByPessoaId(Long id);
}
