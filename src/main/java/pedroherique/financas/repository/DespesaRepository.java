package pedroherique.financas.repository;

import pedroherique.financas.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DespessaRepository extends JpaRepository<Despesa, Long> {

    List<Despesa> findAllById(long pessoaId);
}
