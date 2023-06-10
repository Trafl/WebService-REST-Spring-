package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

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

import com.algaworks.algafood.api.assembler.UsuarioInputDisassembler;
import com.algaworks.algafood.api.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.api.model.input.SenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.model.input.UsuarioInput;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping("/usuarios") 
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CadastroUsuarioService usuarioService;
	
	@Autowired
	private UsuarioModelAssembler usuarioModelAssembler;
	
	@Autowired
	private UsuarioInputDisassembler usuarioInputDisassembler; 
	
	@GetMapping
	public List<UsuarioModel> listar(){
		
		List<Usuario> todosUsuarios = usuarioRepository.findAll();
		
		return usuarioModelAssembler.toCollectModel(todosUsuarios);
	}
	
	@GetMapping(value = "/{usuarioId}")
	public UsuarioModel buscar(@PathVariable Long usuarioId){
		
		Usuario usuario = usuarioService.buscarOuFalha(usuarioId); 
		
		return usuarioModelAssembler.toModel(usuario);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioModel adicionar(@Valid @RequestBody UsuarioComSenhaInput usuarioInput) {
		
		Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioInput);
		usuario = usuarioService.salvar(usuario);
		
		return usuarioModelAssembler.toModel(usuario);
	}
	
	@PutMapping(value = "/{usuarioId}")
	public UsuarioModel atualizar(@PathVariable Long usuarioId, @Valid @RequestBody UsuarioInput usuarioInput){
		
		Usuario usuarioAtual = usuarioService.buscarOuFalha(usuarioId);
		
		usuarioInputDisassembler.copyToDomainObject(usuarioInput, usuarioAtual);
		
		usuarioAtual = usuarioService.salvar(usuarioAtual);		
		
			return usuarioModelAssembler.toModel(usuarioAtual);
	}
	
	@PutMapping(value = "/{usuarioId}/senha")
	public void atualizarSenha(@PathVariable Long usuarioId, @Valid @RequestBody SenhaInput usuarioInput){				
		
		usuarioService.alterarSenha(usuarioId, usuarioInput.getSenhaAtual(), usuarioInput.getNovaSenha());
	}	
}