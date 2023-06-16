package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
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
	
	@Autowired
	private RestauranteModelAssembler restauranteAssembler;
	
	@Autowired
	private RestauranteInputDisassembler restauranteInputDisassembler; 
	
	@GetMapping
	@ResponseStatus(value = HttpStatus.OK)
	public List<RestauranteModel> listar(){
		return restauranteAssembler.toCollectModel(restauranteRepository.findAll());
	}
	
	@GetMapping(value = "/{restauranteId}")
	public RestauranteModel buscar(@PathVariable Long restauranteId){	
		return restauranteAssembler.toModel(restauranteService.buscarOuFalhar(restauranteId));
	}
	
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public RestauranteModel adicionar(@Valid @RequestBody RestauranteInput restauranteInput){
		try {
			Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);
			
			return restauranteAssembler.toModel(restauranteService.salvar(restaurante));
		}catch(CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	} 
	
	@PutMapping(value = "/{restauranteId}")
	public RestauranteModel atualizar(@PathVariable Long restauranteId,
			@Valid @RequestBody  RestauranteInput restauranteInput){
		try {
		Restaurante restauranteAtual = restauranteService.buscarOuFalhar(restauranteId);
		
		restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);
			
		restauranteAtual = restauranteService.salvar(restauranteAtual);
			return restauranteAssembler.toModel(restauranteAtual);
			
		}catch(CozinhaNaoEncontradaException  | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}	
	}
	
	@PutMapping(value = "/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		restauranteService.ativar(restauranteId);
		
	}
	
	@DeleteMapping(value = "/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)	
	public void inativar(@PathVariable Long restauranteId) {
		restauranteService.inativar(restauranteId);
		
	}
	@PutMapping(value = "/ativaçoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarMultiplos(@RequestBody List<Long> restauranteIds){
		try {
			restauranteService.ativar(restauranteIds);
		}catch(RestauranteNaoEncontradoException e){
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@DeleteMapping(value = "/ativaçoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativarMultiplos(@RequestBody List<Long> restauranteIds){
		try {
			restauranteService.inativar(restauranteIds);
		}catch(RestauranteNaoEncontradoException e){
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	
	
	@PutMapping(value = "/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abertura(@PathVariable Long restauranteId) {
		restauranteService.abrir(restauranteId);
		
	}
	
	@PutMapping(value = "/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechamento(@PathVariable Long restauranteId) {
		restauranteService.fechar(restauranteId);

	}
		
}
