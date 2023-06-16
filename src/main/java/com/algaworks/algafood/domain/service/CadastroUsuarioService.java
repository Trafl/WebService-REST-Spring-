package com.algaworks.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;

@Service
public class CadastroUsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CadastroGrupoService grupoService;
	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		
		usuarioRepository.detach(usuario);
		
		Optional<Usuario> usuarioExistente =  usuarioRepository.findByEmail(usuario.getEmail());
		
		if(usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
			throw new NegocioException(String.format(
					"Já existe um usuário cadastrado com esse email %s", usuarioExistente.get().getEmail()));
		}
		
		return usuarioRepository.save(usuario);
	} 
	
	@Transactional
	public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
		
		Usuario usuarioAtual = buscarOuFalhar(usuarioId);
		
		if(usuarioAtual.senhaNaoCoincide(senhaAtual)){
			throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
		}
		usuarioAtual.setSenha(novaSenha);
	}
	
	public Usuario buscarOuFalhar(Long usuarioId) {
		
		return usuarioRepository.findById(usuarioId).orElseThrow(
				() -> new UsuarioNaoEncontradoException(usuarioId));
	}
	
	@Transactional
	public void associar (Long usuarioId, Long grupoId) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		Grupo grupo = grupoService.buscaOuFalha(grupoId);
		
		usuario.AssociarAoGrupo(grupo);
	}
	
	@Transactional
	public void disassociar (Long usuarioId, Long grupoId) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		Grupo grupo = grupoService.buscaOuFalha(grupoId);
		
		usuario.disassociarAoGrupo(grupo);
	}
	
}
