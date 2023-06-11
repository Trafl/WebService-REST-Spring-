package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.ProdutoRepository;

@Service
public class CadastroProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CadastroRestauranteService restauranteService;
	
	@Transactional
	public Produto salvar(Produto produto, Long restaurantaId) {

		Restaurante restaurante = restauranteService.buscaOuFalha(restaurantaId);
		produto.associarProduto(restaurante, produto);
		
		return produtoRepository.save(produto);
	}
	
	public Produto buscarProduto(Long restauranteId, Long produtoId) {
		
		Produto produto = buscaOuFalha(produtoId);
		Restaurante restaurante = restauranteService.buscaOuFalha(restauranteId);
	
		if(!restaurante.getProdutos().contains(produto)) {
			throw new ProdutoNaoEncontradoException(
					String.format("Não existe um cadastro de produto com código %d para o restaurante de código %d.", produtoId,restauranteId ));
		}
		return produto;
	}
	
	public Produto buscaOuFalha(Long produtoId) {
		return produtoRepository.findById(produtoId).orElseThrow(
				() -> new ProdutoNaoEncontradoException(produtoId));
	}
	
	
}
