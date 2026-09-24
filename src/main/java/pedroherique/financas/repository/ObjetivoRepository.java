package pedroherique.financas.repository;
import pedroherique.financas.model.ObjetivoFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public  interface ObjetivoRepository extends JpaRepository<ObjetivoFinanceiro, Long> {

    List<ObjetivoFinanceiro> findByPessoaId(Long pessoaId);

}
