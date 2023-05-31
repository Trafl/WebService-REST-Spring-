package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {
	
	@Autowired
	private CadastroRestauranteService restauranteService;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@GetMapping
	@ResponseStatus(value = HttpStatus.OK)
	public List<RestauranteModel> listar(){
		return toCollectModel(restauranteRepository.findAll());
	}
	
	@GetMapping(value = "/{restauranteId}")
	public RestauranteModel buscar(@PathVariable Long restauranteId){	
		return toModel(restauranteService.buscaOuFalha(restauranteId));
	}
	
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public RestauranteModel adicionar(@Valid @RequestBody RestauranteInput restauranteInput){
		try {
			Restaurante restaurante = toDomainObject(restauranteInput);
			
			return toModel(restauranteService.salvar(restaurante));
		}catch(CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	} 
	
	@PutMapping(value = "/{restauranteId}")
	public RestauranteModel atualizar(@PathVariable Long restauranteId,
			@Valid @RequestBody  RestauranteInput restauranteInput){
		try {
			
		Restaurante restaurante = toDomainObject(restauranteInput);
		
		Restaurante restauranteAtual = restauranteService.buscaOuFalha(restauranteId);
		
		BeanUtils.copyProperties(restaurante,
				restauranteAtual, "id", "formasPagamento", "endereco", "dataCadastro", "produtos");
		
		
			return toModel(restauranteService.salvar(restauranteAtual));
			
		}catch(CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
		
	}
	
	private RestauranteModel toModel(Restaurante restaurante) {
		CozinhaModel cozinhaModel = new CozinhaModel();
		cozinhaModel.setId(restaurante.getCozinha().getId());
		cozinhaModel.setNome(restaurante.getCozinha().getNome());
		
		RestauranteModel restauranteModel = new RestauranteModel();
		restauranteModel.setId(restaurante.getId());
		restauranteModel.setNome(restaurante.getNome());
		restauranteModel.setTaxaFrete(restaurante.getTaxaFrete());
		restauranteModel.setCozinha(cozinhaModel);
		return restauranteModel;
	}
	
	private List<RestauranteModel> toCollectModel(List<Restaurante> restaurantes){
		
		return restaurantes.stream().map(restaurante -> toModel(restaurante)).collect(Collectors.toList());
	}
	
	private Restaurante toDomainObject(RestauranteInput restauranteInput) {
		Restaurante restaurante = new Restaurante();
		restaurante.setNome(restauranteInput.getNome());
		restaurante.setTaxaFrete(restauranteInput.getTaxaFrete());
		
		Cozinha cozinha = new Cozinha();
		cozinha.setId(restauranteInput.getCozinha().getId());
		restaurante.setCozinha(cozinha);
		 
		return restaurante;
	}
}
