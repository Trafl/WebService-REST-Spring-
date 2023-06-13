package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.GrupoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.GrupoRepository;

@Service
public class CadastroGrupoService {

	private static final String MSG_GRUPO_EM_USO = "Grupo de código %d não pode ser removido, pois esta em uso";
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private CadastroPermissaoService permissaoService;
	
	@Transactional
	public Grupo salvar(Grupo grupo) {
		return grupoRepository.save(grupo);
	}
	
	@Transactional
	public void remover(Long grupoId) {
		try {
			grupoRepository.deleteById(grupoId);
			grupoRepository.flush();
			
		}catch(EmptyResultDataAccessException e) {
			throw new GrupoNaoEncontradoException(grupoId);
			
		}catch(DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_GRUPO_EM_USO, grupoId));
		}
	}
	
	public Grupo buscaOuFalha(Long grupoId) {
		return grupoRepository.findById(grupoId).orElseThrow(
				() -> new GrupoNaoEncontradoException(grupoId));
	}
	
	public boolean associar (Long grupoId, Long permissaoId) {
		Grupo grupo = buscaOuFalha(grupoId);
		Permissao permissao = permissaoService.buscaOuFalha(permissaoId);
		return grupo.assosicar(permissao);	
	}
	
	public boolean disassociar(Long grupoId, Long permissaoId) {
		Grupo grupo = buscaOuFalha(grupoId);
		Permissao permissao = permissaoService.buscaOuFalha(permissaoId);
		return grupo.disassosicar(permissao);	
	}
}
