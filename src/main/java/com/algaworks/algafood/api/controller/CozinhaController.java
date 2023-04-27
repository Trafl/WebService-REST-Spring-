package com.algaworks.algafood.api.controller;

import java.util.List;

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

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@RestController
@RequestMapping(value ="/cozinhas")
public class CozinhaController {

	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CadastroCozinhaService cozinhaCadastro;
	
	@GetMapping
	public ResponseEntity<List<Cozinha>> listar(){
		List<Cozinha> list = cozinhaRepository.findAll();
		if(list != null) {
			return ResponseEntity.ok(list);
		}else 
			return ResponseEntity.notFound().build();
	}
	
	@GetMapping(value = "/{cozinhaId}") 
	public Cozinha buscar(@PathVariable Long cozinhaId) {
		 return cozinhaCadastro.buscarOuFalha(cozinhaId);
	}
	
	@PostMapping
	public ResponseEntity<Cozinha> adicionar(@RequestBody Cozinha cozinha) {
		cozinha = cozinhaCadastro.salvar(cozinha);
		return ResponseEntity.status(HttpStatus.CREATED).body(cozinha);
	}
	
	@PutMapping(value = "/{cozinhaId}")
	public Cozinha atualizar(@PathVariable Long cozinhaId,@RequestBody Cozinha cozinha){
		
		Cozinha cozinhaAtual = cozinhaCadastro.buscarOuFalha(cozinhaId);
		BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");			
			return cozinhaCadastro.salvar(cozinhaAtual);			
	}
	
	@DeleteMapping(value = "/{cozinhaId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId){
			 cozinhaCadastro.excluir(cozinhaId);	
	}
}
