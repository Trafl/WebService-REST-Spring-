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

import com.algaworks.algafood.api.assembler.FormaPagamentoInputDissaembler;
import com.algaworks.algafood.api.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.service.CadastroFormaPagamentoService;

@RestController
@RequestMapping(value = "/pagamentos")
public class FormaPagamentoController {

	@Autowired
	private CadastroFormaPagamentoService service;
	
	@Autowired
	private FormaPagamentoModelAssembler assembler;
	
	@Autowired
	private FormaPagamentoInputDissaembler dissaembler;
	
	@Autowired
	FormaPagamentoRepository repository;
	
	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public List<FormaPagamentoModel> listar() {
		List<FormaPagamento> pagamentos = repository.findAll() ;
		
		return assembler.toCollectModel(pagamentos);
	}
	
	@GetMapping(value = "/{pagamentoId}")
	@ResponseStatus(HttpStatus.OK)
	public FormaPagamentoModel buscar (@PathVariable Long pagamentoId) {
		
		FormaPagamento pagamento = service.buscaOuFalha(pagamentoId);
		
		return assembler.toModel(pagamento);
	}
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FormaPagamentoModel salvar(@Valid @RequestBody FormaPagamentoInput input) {
		
		FormaPagamento pagamento = dissaembler.toDomainObject(input);
		service.salvar(pagamento);
		return assembler.toModel(pagamento);
	}
	
	@PutMapping(value = "/{pagamentoId}")
	@ResponseStatus(HttpStatus.OK)
	public FormaPagamentoModel atualizar(@PathVariable Long pagamentoId,
			@Valid @RequestBody FormaPagamentoInput input) {
		
		FormaPagamento pagamentoAtual = service.buscaOuFalha(pagamentoId);
		
		dissaembler.toCopyToDomainModel(input, pagamentoAtual);
		
		pagamentoAtual = service.salvar(pagamentoAtual);
	
		return assembler.toModel(pagamentoAtual);
	}
	
	
	@DeleteMapping(value = "/{pagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long pagamentoId) {
		service.remove(pagamentoId);
	}
}
