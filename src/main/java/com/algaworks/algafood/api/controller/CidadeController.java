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
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping(value = "/cidades")
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroCidadeService cidadeService;
	
	@GetMapping
	public ResponseEntity<List<Cidade>> listar(){
		List<Cidade> list = cidadeRepository.findAll();
		return ResponseEntity.ok(list);
	}
	
	@GetMapping(value = "/{cidadeId}")
	public ResponseEntity<Cidade> buscar(@PathVariable Long cidadeId){
		
		Optional<Cidade> cidade = cidadeRepository.findById(cidadeId);
		if(cidade.isPresent()){
			return ResponseEntity.ok(cidade.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {
		try {
			 cidade = cidadeService.salvar(cidade);
			 return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
		}
		catch(EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping(value = "/{cidadeId}")
	public ResponseEntity<?> atualizar(@PathVariable Long cidadeId,@RequestBody Cidade cidade){
		
		try {
			Cidade cidadeAtual = cidadeRepository.findById(cidadeId).orElse(null);
			
			if(cidadeAtual != null) {
				BeanUtils.copyProperties(cidade, cidadeAtual, "id");
				
				cidadeAtual = cidadeService.salvar(cidadeAtual);
				return ResponseEntity.ok(cidadeAtual);
			}
			return ResponseEntity.notFound().build();
		
		}catch(EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping(value = "/{cidadeId}")
	public ResponseEntity<?> remover(@PathVariable Long cidadeId){
			try {
				cidadeService.remover(cidadeId);
				return ResponseEntity.noContent().build();
				
			}catch(EntidadeNaoEncontradaException e) {
				return ResponseEntity.notFound().build();
			
			}catch(EntidadeEmUsoException e) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
			}
	}
	
	
}
