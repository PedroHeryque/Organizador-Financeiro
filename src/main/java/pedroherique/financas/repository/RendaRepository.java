package pedroherique.financas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pedroherique.financas.model.Renda;

@Repository
public interface RendaRepository extends JpaRepository<Renda, Long> {

    List<Renda> findByPessoaId(Long id);
}
