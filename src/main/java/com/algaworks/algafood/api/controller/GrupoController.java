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

import com.algaworks.algafood.api.assembler.GrupoInputDisassembler;
import com.algaworks.algafood.api.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;
import com.algaworks.algafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping("/grupos") 
public class GrupoController {

	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private CadastroGrupoService grupoService;
	
	@Autowired
	private GrupoModelAssembler grupoModelAssembler;
	
	@Autowired
	private GrupoInputDisassembler grupoInputDisassembler; 
	
	@GetMapping
	public List<GrupoModel> listar(){
		List<Grupo> todosGrupos = grupoRepository.findAll();
		
		return grupoModelAssembler.toCollectModel(todosGrupos);
	}
	
	@GetMapping(value = "/{grupoId}")
	public GrupoModel buscar(@PathVariable Long grupoId){
		Grupo grupo = grupoService.buscaOuFalha(grupoId); 
		
		return grupoModelAssembler.toModel(grupo);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GrupoModel adicionar(@Valid @RequestBody GrupoInput grupoInput) {
		Grupo grupo = grupoInputDisassembler.toDomainObject(grupoInput);
		grupo = grupoService.salvar(grupo);
		return grupoModelAssembler.toModel(grupo);
	}
	
	@PutMapping(value = "/{grupoId}")
	public GrupoModel atualizar(@PathVariable Long grupoId, @Valid @RequestBody GrupoInput grupoInput){
		Grupo grupoAtual = grupoService.buscaOuFalha(grupoId);
		
		grupoInputDisassembler.toDomainObject(grupoInput);
		
		grupoInputDisassembler.copyToDomainObject(grupoInput, grupoAtual);
		
		grupoAtual = grupoService.salvar(grupoAtual);		
		
			return grupoModelAssembler.toModel(grupoAtual);
	}
	
	@DeleteMapping(value = "/{grupoId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long grupoId){
		grupoService.remover(grupoId);	
	}
	
}