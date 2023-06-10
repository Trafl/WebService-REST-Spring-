package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;

@Service
public class CadastroUsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		
		return usuarioRepository.save(usuario);
	} 
	
	@Transactional
	public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
		
		Usuario usuarioAtual = buscarOuFalha(usuarioId);
		
		if(usuarioAtual.senhaNaoCoincide(senhaAtual)){
			throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
		}
		usuarioAtual.setSenha(novaSenha);
	}
	
	
	public Usuario buscarOuFalha(Long usuarioId) {
		
		return usuarioRepository.findById(usuarioId).orElseThrow(
				() -> new UsuarioNaoEncontradoException(usuarioId));
	}
}
