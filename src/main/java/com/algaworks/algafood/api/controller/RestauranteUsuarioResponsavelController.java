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

import com.algaworks.algafood.api.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/responsaveis")
public class RestauranteUsuarioResponsavelController {
	
	@Autowired
	private CadastroRestauranteService restauranteService;
	
	@Autowired
	private UsuarioModelAssembler usuarioAssembler;
	
	@GetMapping
	@ResponseStatus(value = HttpStatus.OK)
	public List<UsuarioModel> listar(@PathVariable Long restauranteId){
		Set<Usuario> usuarios = restauranteService.buscaOuFalhar(restauranteId).getUsuarios();
	
		return usuarioAssembler.toCollectModel(usuarios);
	}
	
	@PutMapping(value = "/{usuarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void adicionarUsuario(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		restauranteService.associarUsuario(restauranteId, usuarioId);
	}
	
	@DeleteMapping(value = "/{usuarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removerUsuario(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		restauranteService.desassociarUsuario(restauranteId, usuarioId);
	}
		
}
