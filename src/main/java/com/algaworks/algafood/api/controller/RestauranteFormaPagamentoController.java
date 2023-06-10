package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/formas-pagamento")
public class RestauranteFormaPagamentoController {
	
	@Autowired
	private CadastroRestauranteService restauranteService;
	
	@Autowired
	private FormaPagamentoModelAssembler formaPagamentoAssembler;
	
	@GetMapping
	@ResponseStatus(value = HttpStatus.OK)
	public List<FormaPagamentoModel> listar(@PathVariable Long restauranteId){
		
		Restaurante restaurante = restauranteService.buscaOuFalha(restauranteId);
		return formaPagamentoAssembler.toCollectModel(restaurante.getFormasPagamento());
	}
	
	@DeleteMapping(value = "/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void dessasociar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		restauranteService.desassociarFormaPagamento(restauranteId, formaPagamentoId);
	}

	@PutMapping(value = "/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void associar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		
		restauranteService.associarFormaPagamento(restauranteId, formaPagamentoId);
	}
		
}
