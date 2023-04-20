package com.algaworks.algafood.api.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@GetMapping(value = "/restaurantes/por-nome-e-frete")
	public List<Restaurante> buscar(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal){
		return restauranteRepository.find(nome, taxaFreteInicial, taxaFreteFinal);
	}
	
	/*@GetMapping(value = "/restaurantes/por-taxa-frete")
	public List<Restaurante> restaurantesPorTaxa(BigDecimal taxaInicial, BigDecimal taxaFinal){
		return restauranteRepo.findByTaxaFreteBetween(taxaInicial, taxaFinal);
	}*/
	
	@GetMapping(value = "/cozinha/primeira")
	public Optional<Cozinha> cozinhaPrimeiro(){
		return cozinhaRepository.buscarPrimeiro();
	}
	
	@GetMapping("/restaurante/primeiro")
	public Optional<Restaurante> restaurantePrimeiro(){
		return restauranteRepository.buscarPrimeiro();
	}
	
	@GetMapping("/restaurante/com-frete-gratis")
	public List<Restaurante> restauranteComFreteGratis(String nome){
		
		
		return restauranteRepository.findComFreteGratis(nome);
	}
	
	
}