package com.algaworks.algafood.domain.exception;

import com.algaworks.algafood.domain.model.FormaPagamento;

public class FormaPagamentoInvalidaException extends NegocioException {

	private static final long serialVersionUID = 1L;
	
	public FormaPagamentoInvalidaException(String mensagem) {
		super(mensagem);
	}
	public FormaPagamentoInvalidaException(FormaPagamento formaPagamento) {
		this(String.format("Forma de pagamento '%s' não e aceita por esse restaurante.", formaPagamento));
	}
}
