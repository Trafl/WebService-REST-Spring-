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

import com.algaworks.algafood.api.assembler.ProdutoInputDisassembler;
import com.algaworks.algafood.api.assembler.ProdutoModelAssembler;
import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.api.model.input.ProdutoInput;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/produtos")
public class RestauranteProdutoController {
	
	@Autowired
	private CadastroRestauranteService restauranteService;
	
	@Autowired
	private CadastroProdutoService produtoService;
	
	@Autowired
	private ProdutoModelAssembler produtoAssembler;
	
	@Autowired
	private ProdutoInputDisassembler produtoDisassembler;
	
	@GetMapping
	@ResponseStatus(value = HttpStatus.OK)
	public List<ProdutoModel> listar(@PathVariable Long restauranteId){
		
		Restaurante restaurante = restauranteService.buscaOuFalha(restauranteId);
		return produtoAssembler.toCollectModel(restaurante.getProdutos());
	}
	
	@GetMapping(value = "/{produtoId}")
	@ResponseStatus(value = HttpStatus.OK)
	public ProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId){
		
		Produto produto = produtoService.buscarProduto(restauranteId, produtoId);
		
		 return produtoAssembler.toModel(produto);
	}
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutoModel adicionar(@RequestBody @Valid ProdutoInput produtoInput, @PathVariable Long restauranteId ) {
		
		Produto produto = produtoDisassembler.toDomainObject(produtoInput);
		
		produtoService.salvar(produto, restauranteId);
		
		return produtoAssembler.toModel(produto);
	}
	

	@PutMapping(value = "/{produtoId}")
	@ResponseStatus(HttpStatus.OK)
	public ProdutoModel atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId, @RequestBody @Valid ProdutoInput produtoInput) {
		
		Produto produtoAtual = produtoService.buscarProduto(restauranteId, produtoId);
		
		produtoDisassembler.copyToDomainObject(produtoInput, produtoAtual);
		produtoAtual = produtoService.salvar(produtoAtual, restauranteId);
		return produtoAssembler.toModel(produtoAtual);
	}
		
}
