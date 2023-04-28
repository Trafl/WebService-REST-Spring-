package com.algaworks.algafood.api.exceptionhandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;

@ControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> tratarEntidadeNaoEncontradaExeption(EntidadeNaoEncontradaException e){
		Problema problema = Problema.builder().dataHora(LocalDateTime.now())
				.mensagem(e.getMessage()).build();
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problema);
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> tratarNegocioExeption(NegocioException e){
		Problema problema = Problema.builder().dataHora(LocalDateTime.now())
				.mensagem(e.getMessage()).build();
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problema);
	}
	
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> entidadeEmUsoException(EntidadeEmUsoException e){
		Problema problema = Problema.builder().dataHora(LocalDateTime.now()).mensagem(e.getMessage()).build();
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(problema);
		
	}
}
