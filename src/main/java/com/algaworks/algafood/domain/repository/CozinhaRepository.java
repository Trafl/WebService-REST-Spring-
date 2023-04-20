package com.algaworks.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import com.algaworks.algafood.domain.model.Cozinha;

public interface CozinhaRepository extends CustomJpaRepository<Cozinha, Long> {

	List<Cozinha>findTodasByNomeContaining(String nome);
	
	Optional<Cozinha> findByNome(String nome);
}
