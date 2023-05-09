package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;

@RestController
@RequestMapping("/estados") 
public class EstadoController {

	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CadastroEstadoService estadoService;
	
	@GetMapping
	public ResponseEntity<List<Estado>> listar(){
		List<Estado> estados = estadoRepository.findAll();
		return ResponseEntity.ok(estados);
	}
	
	
	@GetMapping(value = "/{estadoId}")
	public Estado buscar(@PathVariable Long estadoId){
		return 	estadoService.buscaOuFalha(estadoId);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Estado adicionar(@Valid @RequestBody Estado estado) {
		return estadoService.salvar(estado);
	}
	
	@PutMapping(value = "/{estadoId}")
	public Estado atualizar(@PathVariable Long estadoId, @Valid @RequestBody Estado estado){
		Estado estadoAtual = estadoService.buscaOuFalha(estadoId);
		BeanUtils.copyProperties(estado, estadoAtual, "id");
			return estadoService.salvar(estadoAtual);
	}
	
	@DeleteMapping(value = "/{estadoId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long estadoId){
		estadoService.remover(estadoId);	
	}
}