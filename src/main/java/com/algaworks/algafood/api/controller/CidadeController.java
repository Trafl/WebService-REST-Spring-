package com.algaworks.algafood.api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.exceptionhandler.Problema;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
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
	@ResponseStatus(value = HttpStatus.CREATED)
	public Cidade buscar(@PathVariable Long cidadeId){
		return cidadeService.buscaOuFalha(cidadeId);
	}
	
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public Cidade adicionar(@RequestBody Cidade cidade) {
		try {
			return cidadeService.salvar(cidade);
		}catch(EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}	
	}
	
	@PutMapping(value = "/{cidadeId}")
	public Cidade atualizar(@PathVariable Long cidadeId,@RequestBody Cidade cidade){
		try {
			Cidade cidadeAtual = cidadeService.buscaOuFalha(cidadeId);
			BeanUtils.copyProperties(cidade, cidadeAtual, "id");
			return  cidadeService.salvar(cidadeAtual);
		}catch(EstadoNaoEncontradoException e){
			throw new NegocioException(e.getMessage(), e);
		}	
	}
	
	@DeleteMapping(value = "/{cidadeId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cidadeId){
		cidadeService.remover(cidadeId);
	}
}
