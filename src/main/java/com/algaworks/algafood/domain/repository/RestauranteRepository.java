package com.algaworks.algafood.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;

@Repository
public interface RestauranteRepository extends CustomJpaRepository<Restaurante, Long>, RestauranteRepositoryQueries, 
	JpaSpecificationExecutor<Restaurante>   {

	
	@Query("from Restaurante r join fetch r.cozinha join fetch r.formasPagamento")
	List<Restaurante> findAll();
	
	 
}
