package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
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
	public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {
		Optional<Cozinha> cozinha = cozinhaRepository.findById(cozinhaId);
		
		if(cozinha.isPresent()) {
			return ResponseEntity.ok(cozinha.get());
		}else 
			return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<Cozinha> adicionar(@RequestBody Cozinha cozinha) {
		cozinha = cozinhaCadastro.salvar(cozinha);
		return ResponseEntity.status(HttpStatus.CREATED).body(cozinha);
	}
	
	@PutMapping(value = "/{cozinhaId}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId,@RequestBody Cozinha cozinha){
		
		Optional<Cozinha> cozinhaAtual = cozinhaRepository.findById(cozinhaId);
		if(cozinhaAtual.isPresent()) {
			BeanUtils.copyProperties(cozinha, cozinhaAtual.get(), "id");
			
			Cozinha cozinhaSalva = cozinhaCadastro.salvar(cozinhaAtual.get());
			return ResponseEntity.ok(cozinhaSalva);
		}
		return ResponseEntity.notFound().build();
		
	}
	
	@DeleteMapping(value = "/{cozinhaId}")
	public ResponseEntity<?> remover(@PathVariable Long cozinhaId){
		try {
			 cozinhaCadastro.excluir(cozinhaId);
			 return ResponseEntity.noContent().build();
			}
		
		catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
			}
		
		catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
			}
	}
}
