package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;

@Service
public class CadastroEstadoService {

	private static final String MSG_ESTADO_EM_USO = "Estado de código %d não pode ser removido, pois esta em uso";
	private static final String MSG_NAO_EXISTE_CADASTRO_ESTADO = "Não existe um cadastro de estado com código %d";
	@Autowired
	private EstadoRepository estadoRepository;
	
	public Estado salvar(Estado estado) {
		return estadoRepository.save(estado);
	}
	
	public void remover(Long estadoId) {
		try {
			estadoRepository.deleteById(estadoId);
			
		}catch(EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format(MSG_NAO_EXISTE_CADASTRO_ESTADO, estadoId));
			
		}catch(DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_ESTADO_EM_USO, estadoId));
		}
	}
	
	public Estado buscaOuFalha(Long estadoId) {
		return estadoRepository.findById(estadoId).orElseThrow(
				() -> new EntidadeNaoEncontradaException(MSG_NAO_EXISTE_CADASTRO_ESTADO));
	}
	
	
}
