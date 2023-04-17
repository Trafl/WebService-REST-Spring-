package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {
	
	@Autowired
	private CadastroRestauranteService restauranteService;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@GetMapping
	public ResponseEntity<List<Restaurante>> listar(){
		List<Restaurante> restaurantes = restauranteRepository.findAll();
		
		return ResponseEntity.ok(restaurantes);
	}
	
	@GetMapping(value = "/{restauranteId}")
	public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId){
		
		Optional<Restaurante> restaurante = restauranteRepository.findById(restauranteId);
		if(restaurante.isPresent()) {
			return ResponseEntity.ok(restaurante.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante){
		try {
			restaurante = restauranteService.salvar(restaurante);
			return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
		
		}catch(EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}		
	} 
	
	@PutMapping(value = "/{restauranteId}")
	public ResponseEntity<?> atualizar(@PathVariable Long restauranteId, 
			                           @RequestBody Restaurante restaurante){
		try { 
			
			Restaurante restauranteAtual = restauranteRepository
			        .findById(restauranteId).orElse(null);
			
			if(restauranteAtual != null) {
				BeanUtils.copyProperties(restaurante, restauranteAtual, "id");
				restauranteAtual = restauranteService.salvar(restauranteAtual);
				return ResponseEntity.ok(restauranteAtual);
			}
			return ResponseEntity.notFound().build();
		
		}catch(EntidadeNaoEncontradaException e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PatchMapping(value = "/{restauranteId}")
	public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId,@RequestBody Map<String, Object> campos){
		
		Restaurante restauranteAtual = restauranteRepository
		        .findById(restauranteId).orElse(null);
		if(restauranteAtual == null) {
			return ResponseEntity.notFound().build();
		}
		merge(campos, restauranteAtual);
		
		return atualizar(restauranteId, restauranteAtual);	
	}

	private void merge(Map<String, Object> dadosOrigem,Restaurante  restauranteDestino) {
		ObjectMapper objectMapper = new ObjectMapper();
		Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
		
		dadosOrigem.forEach((nomePropriedade , valorPropriedade)-> {
		Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
		field.setAccessible(true);
		
		Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
  
			  ReflectionUtils.setField(field, restauranteDestino, novoValor);
		});
	}
	
	
}
