package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {

	private static final String MSG_NAO_EXISTE_RESTAURANTE = "Não existe cadastro de restaurante com código %d";

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroCozinhaService cozinhaService;
	
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaid = restaurante.getCozinha().getId();
		Cozinha cozinha = cozinhaService.buscarOuFalha(cozinhaid);
		restaurante.setCozinha(cozinha);		
		return restauranteRepository.save(restaurante);
	}
	
	public Restaurante buscaOuFalha(Long restauranteId) {
		return restauranteRepository.findById(restauranteId).orElseThrow(
				()-> new EntidadeNaoEncontradaException(String.format(MSG_NAO_EXISTE_RESTAURANTE, restauranteId)));
	}
	
	
}
