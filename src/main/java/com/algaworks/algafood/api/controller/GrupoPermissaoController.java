package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.PermissaoModelAssembler;
import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping("/grupos/{grupoId}/permissoes") 
public class GrupoPermissaoController {

	
	@Autowired
	private CadastroGrupoService grupoService;
	
	@Autowired
	private PermissaoModelAssembler permissaoModelAssembler;
	
	@GetMapping
	public List<PermissaoModel> listar(@PathVariable Long grupoId){
		Grupo grupo = grupoService.buscaOuFalha(grupoId);
		Set<Permissao> todasPermissoes = grupo.getPermissoes();
		
		return permissaoModelAssembler.toCollectModel(todasPermissoes);
	}
	
	@Transactional
	@PutMapping(value = "/{permissaoId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void associar(@PathVariable Long grupoId, @PathVariable Long permissaoId){
		
		grupoService.associar(grupoId, permissaoId);
	}
	
	@Transactional
	@DeleteMapping(value = "/{permissaoId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void disassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId){
		
		grupoService.disassociar(grupoId, permissaoId);
	}
	
}