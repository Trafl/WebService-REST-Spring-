package com.algaworks.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.algafood.domain.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
