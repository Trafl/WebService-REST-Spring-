package com.algaworks.algafood;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@SpringBootTest
class CadastroCozinhaIT {

	@Autowired
	CadastroCozinhaService cadastroCozinha;
	
	@Test
	public void testarCadastroCozinhaComSucesso() {
		// cenário
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");
		cadastroCozinha.salvar(novaCozinha); 
		
		// ação
		novaCozinha = cadastroCozinha.salvar(novaCozinha);
		
		//validação
		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
		
	}

	@Test()
	public void testarCadastroCozinhaSemNome() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome(null);
		
		
	
	}
	
	@Test()
	public void deveFalhar_QuandoExcluirCozinhaEmUso() {
		
		EntidadeEmUsoException erroEsperado =
			      Assertions.assertThrows(EntidadeEmUsoException.class, () -> {
			         cadastroCozinha.excluir(1L);
			      });
		assertThat(erroEsperado).isNotNull();
	}
	
	@Test()
	public void deveFalhar_QuandoExcluirCozinhaInexistente() {
		CozinhaNaoEncontradaException erroEsperado =
			      Assertions.assertThrows(CozinhaNaoEncontradaException.class, () -> {
			         cadastroCozinha.excluir(100L);
			      });
		assertThat(erroEsperado).isNotNull();
	}
	
}
