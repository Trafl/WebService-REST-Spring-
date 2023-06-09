package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {


	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroCozinhaService cozinhaService;
	
	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaid = restaurante.getCozinha().getId();
		
		Cozinha cozinha = cozinhaService.buscarOuFalha(cozinhaid);
		
		restaurante.setCozinha(cozinha);		
		return restauranteRepository.save(restaurante);
	}

	public Restaurante buscaOuFalha(Long restauranteId) {
		return restauranteRepository.findById(restauranteId).orElseThrow(
				()-> new RestauranteNaoEncontradoException(restauranteId));
	}
	
	@Transactional
	public void ativar(Long restauranteId) {
		Restaurante restauranteAtual = buscaOuFalha(restauranteId);
		
		restauranteAtual.ativar();
		
	}
	
	@Transactional
	public void inativar (Long restauranteId) {
		Restaurante restauranteAtual = buscaOuFalha(restauranteId);
		
		restauranteAtual.inativar();
		
	}
}
