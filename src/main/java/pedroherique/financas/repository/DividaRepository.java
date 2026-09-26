package pedroherique.financas.repository;
import pedroherique.financas.model.Divida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DividaRepository extends JpaRepository<Divida, Long> {

    List<Divida> findByPessoaId(Long id);
}

