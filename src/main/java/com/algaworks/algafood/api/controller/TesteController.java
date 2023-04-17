package com.algaworks.algafood.api.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@RestController
@RequestMapping(value = "/teste")
public class TesteController  {

	
	private RestauranteRepository restauranteRepo;
	
	
	private CozinhaRepository repo;
	
	@GetMapping(value = "/cozinhas/por-nome")
	public List<Cozinha> buscar(String nome){
		return repo.findTodasByNomeContaining(nome);
	}
	
	@GetMapping(value = "/restaurantes/por-taxa-frete")
	public List<Restaurante> restaurantesPorTaxa(BigDecimal taxaInicial, BigDecimal taxaFinal){
		return restauranteRepo.findByTaxaFreteBetween(taxaInicial, taxaFinal);
	}
	
	@GetMapping(value = "/restaurantes/por-nome")
	public List<Restaurante> restaurantePorNome(String nome, Long cozinhaId){
		return restauranteRepo.findByNomeContainingAndCozinhaId(nome, cozinhaId);
	}
	
	/*@GetMapping("/restaurante/top2-por-nome")
	public List<Restaurante> restauranteTop2(String nome){
		return restauranteRepo.findTop2ByNomeContaining(nome);
	}*/
	
	
	
	
}