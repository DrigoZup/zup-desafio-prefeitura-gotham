package br.com.zup.prefeitura.repository;


//import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.zup.prefeitura.entity.Secretaria;
import br.com.zup.prefeitura.enums.AreaAtuante;

@Repository
public interface SecretariaRepository extends CrudRepository<Secretaria, Long> {

	boolean existsByArea(AreaAtuante area);

}
