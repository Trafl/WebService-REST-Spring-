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

import com.algaworks.algafood.api.assembler.EstadoInputDisassembler;
import com.algaworks.algafood.api.assembler.EstadoModelAssembler;
import com.algaworks.algafood.api.model.EstadoModel;
import com.algaworks.algafood.api.model.input.EstadoInput;
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
	
	@Autowired
	private EstadoModelAssembler estadoModelAssembler;
	
	@Autowired
	private EstadoInputDisassembler estadoInputDisassembler; 
	
	@GetMapping
	public List<EstadoModel> listar(){
		return estadoModelAssembler.toCollectModel(estadoRepository.findAll());
	}
	
	@GetMapping(value = "/{estadoId}")
	public EstadoModel buscar(@PathVariable Long estadoId){
		return estadoModelAssembler.toModel(estadoService.buscaOuFalha(estadoId));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EstadoModel adicionar(@Valid @RequestBody EstadoInput estadoInput) {
		Estado estado = estadoInputDisassembler.toDomainObject(estadoInput);
			estadoService.salvar(estado);
		return estadoModelAssembler.toModel(estado);
	}
	
	@PutMapping(value = "/{estadoId}")
	public EstadoModel atualizar(@PathVariable Long estadoId, @Valid @RequestBody EstadoInput estadoInput){
		Estado estadoAtual = estadoService.buscaOuFalha(estadoId);
		
		estadoInputDisassembler.toDomainObject(estadoInput);
		estadoInputDisassembler.copyToDomainObject(estadoInput, estadoAtual);
				
		return estadoModelAssembler.toModel(estadoService.salvar(estadoAtual));
	}
	
	@DeleteMapping(value = "/{estadoId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long estadoId){
		estadoService.remover(estadoId);	
	}
}