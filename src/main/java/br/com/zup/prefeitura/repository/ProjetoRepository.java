package br.com.zup.prefeitura.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.zup.prefeitura.entity.Projeto;

@Repository
public interface ProjetoRepository extends CrudRepository<Projeto, Long> {

	boolean existsByNome (String nome);
}
