package com.algaworks.algafood.api.exceptionhandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

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
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if(body == null) {
			body = Problema.builder()
				.dataHora(LocalDateTime.now())
				.mensagem(status.getReasonPhrase())
				.build();
		}else if(body instanceof String) {
			body = Problema.builder()
					.dataHora(LocalDateTime.now())
					.mensagem((String) body)
					.build();
		}
		
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
}
