package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping("/usuarios/{usuarioId}/grupos") 
public class UsuarioGrupoController {
	
	@Autowired
	private CadastroUsuarioService usuarioService;
	
	@Autowired
	private GrupoModelAssembler	grupoModelAssembler;
	
	@GetMapping()
	public List<GrupoModel> listar(@PathVariable Long usuarioId){
		
		Set<Grupo> todosUsuarios = usuarioService.buscarOuFalhar(usuarioId).getGrupos();
		
		return grupoModelAssembler.toCollectModel(todosUsuarios);
	}
	
	@PutMapping(value = "/{grupoId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void associar(@PathVariable Long usuarioId,@PathVariable Long grupoId) {
			usuarioService.associar(usuarioId, grupoId);
	}
	
	@DeleteMapping(value = "/{grupoId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void disassociar(@PathVariable Long usuarioId,@PathVariable Long grupoId) {
			usuarioService.disassociar(usuarioId, grupoId);
	}
}