package com.algaworks.algafood.api.controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
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
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.algaworks.algafood.api.assembler.FormaPagamentoInputDissaembler;
import com.algaworks.algafood.api.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.service.CadastroFormaPagamentoService;

@RestController
@RequestMapping(value = "/formas-pagamento")
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
	public ResponseEntity<List<FormaPagamentoModel>> listar(ServletWebRequest request) {
		ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
		
		String eTag= "0";
		
		OffsetDateTime dataUltimaAtualizacao = repository.getDataUltimaAtualizacao();
		
		if(dataUltimaAtualizacao != null) {
			eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
		}
		
		 if(request.checkNotModified(eTag)) {
			 return null;
		 }
		
		List<FormaPagamento> todasFormasPagamentos = repository.findAll() ;
		
		List<FormaPagamentoModel> formaPagamentoModels = assembler.toCollectModel(todasFormasPagamentos);
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
				.eTag(eTag)
				.body(formaPagamentoModels);
	}
	
	@GetMapping(value = "/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<FormaPagamentoModel> buscar (@PathVariable Long formaPagamentoId) {
		
		FormaPagamento formaPagamento = service.buscarOuFalhar(formaPagamentoId);
		
		FormaPagamentoModel formaPagamentoModel = assembler.toModel(formaPagamento);
		
		return ResponseEntity.ok().cacheControl(CacheControl.maxAge(10,TimeUnit.SECONDS)).body(formaPagamentoModel);
	}
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FormaPagamentoModel salvar(@Valid @RequestBody FormaPagamentoInput input) {
		
		FormaPagamento formaPagamento  = dissaembler.toDomainObject(input);
		service.salvar(formaPagamento );
		return assembler.toModel(formaPagamento );
	}
	
	@PutMapping(value = "/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.OK)
	public FormaPagamentoModel atualizar(@PathVariable Long formaPagamentoId,
			@Valid @RequestBody FormaPagamentoInput input) {
		
		FormaPagamento formaPagamentoAtual = service.buscarOuFalhar(formaPagamentoId);
		
		dissaembler.toCopyToDomainModel(input, formaPagamentoAtual);
		
		formaPagamentoAtual = service.salvar(formaPagamentoAtual);
	
		return assembler.toModel(formaPagamentoAtual);
	}
	
	
	@DeleteMapping(value = "/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long formaPagamentoId) {
		service.remove(formaPagamentoId);
	}
}
