package pedroherique.financas.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pedroherique.financas.model.Divida;
@Repository
public interface DividaRepository extends JpaRepository<Divida, Long> {

    List<Divida> findByPessoaId(Long id);
}

