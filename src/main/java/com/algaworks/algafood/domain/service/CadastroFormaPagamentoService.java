package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.FormaPagamentoNaoEncontradoException;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;

@Service
public class CadastroFormaPagamentoService {

	private static final String MSG_Pagamento_EM_USO = "Forma de pagamento de código %d não pode ser removida pois esta em uso";
	
	@Autowired
	private FormaPagamentoRepository repository;
	
	@Transactional
	public FormaPagamento salvar(FormaPagamento pagamento) {
		return repository.save(pagamento);
	}
	
	@Transactional
	public void remove(Long PagamentoId) {
		try {
			repository.deleteById(PagamentoId);
			repository.flush();
		
		}catch(EmptyResultDataAccessException e) {
				throw new FormaPagamentoNaoEncontradoException(PagamentoId);
			
		}catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_Pagamento_EM_USO, PagamentoId));
		
		}
	}
	
	public FormaPagamento buscaOuFalha(Long PagamentoId) {
		return repository.findById(PagamentoId).orElseThrow(()
				-> new FormaPagamentoNaoEncontradoException(PagamentoId));
	}
}
