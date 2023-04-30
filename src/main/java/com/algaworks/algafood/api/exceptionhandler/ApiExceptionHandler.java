package com.algaworks.algafood.api.exceptionhandler;

import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
			
		ProblemType problemType = ProblemType.MENSAGEM_IMCOMPREENSIVEL;
		
		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		
		if(rootCause instanceof InvalidFormatException ) {
			return handleInvalidFormatException((InvalidFormatException) rootCause, headers , status, request);
		}
		else if(rootCause instanceof IgnoredPropertyException ) {
			
			return handleIgnoredPropertyException((IgnoredPropertyException) rootCause, headers , status, request);
		}
			
		else if(rootCause instanceof UnrecognizedPropertyException) {
			
			return  handleUnrecognizedPropertyException((UnrecognizedPropertyException) rootCause, headers, status, request);
		}
		
		String detail = "O corpo da requisição esta inválido. Verifique erro de sintaxe";
		Problem problem = createProblemBuilder(status, problemType, detail).build();
		
		
		return handleExceptionInternal(ex, problem, headers, status, request);
		
	}
	/////////////////////////////
	private ResponseEntity<Object> handleIgnoredPropertyException(
			IgnoredPropertyException ex, HttpHeaders headers ,  HttpStatus status, WebRequest request) {
		ProblemType problemType = ProblemType.PROPIEDADE_IGNORADA;
			
		Problem problem = createProblemBuilder(status, problemType, "Propeidade Ignorada").build();
		
		return handleExceptionInternal(ex, problem,  headers, status,request);
	}
	
	private ResponseEntity<Object> handleUnrecognizedPropertyException(
			UnrecognizedPropertyException ex, HttpHeaders headers ,  HttpStatus status, WebRequest request) {
		ProblemType problemType = ProblemType.PROPIEDADE_NAO_RECONHECIDA;
		
		Problem problem = createProblemBuilder(status, problemType, "Propiedade esta incorreta").build();
		
		return handleExceptionInternal(ex, problem,  headers, status,request);	
	}
	///////////////////////
	private ResponseEntity<Object> handleInvalidFormatException(
			InvalidFormatException ex, HttpHeaders headers ,  HttpStatus status, WebRequest request) {
			
		ProblemType problemType = ProblemType.MENSAGEM_IMCOMPREENSIVEL;
		
		String path = ex.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
		
		String detail = String.format(
				"A propriedade '%s', recebeu o valor de '%s ;"
				+ "que é um tipo invalido. Corrija e informe um valor compativel com o tipo %s",
				path, ex.getValue(), ex.getTargetType().getSimpleName());
		
		Problem problem = createProblemBuilder(status, problemType, detail).build();
			
		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaExeption(EntidadeNaoEncontradaException ex, WebRequest request){
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.ENTIDADE_NAO_ENCONTRADA;
		String detail = ex.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail).build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioExeption(NegocioException ex, WebRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String detail = ex.getMessage();
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		
		Problem problem = createProblemBuilder(status, problemType, detail).build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request){
		
		HttpStatus status = HttpStatus.CONFLICT;
		String detail = ex.getMessage();
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
		
		Problem problem = createProblemBuilder(status, problemType, detail).build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
		
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if(body == null) {
			body = Problem.builder()
					.title(status.getReasonPhrase())
					.status(status.value())
					.build();
			
		}else if(body instanceof String) {
			body = Problem.builder()
					.title((String) body)
					.status(status.value())
					.build();
		}
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {
		
		return Problem.builder()
				.status(status.value())
				.type(problemType.getUri())
				.title(problemType.getTitle())
				.detail(detail);		
	}
}
