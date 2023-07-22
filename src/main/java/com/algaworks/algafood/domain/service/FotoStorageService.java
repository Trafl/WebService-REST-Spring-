package com.algaworks.algafood.domain.service;

import java.io.InputStream;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

public interface FotoStorageService {

	FotoRecuperada recuperar(String nomeArquivo);
	
	void armazenar(NovaFoto novaFoto);
	
	void remover(String nomeArquivoAntigo);
	
	default String gerarNomeArquivo(String nomeOriginal) {
		return UUID.randomUUID() + "_" + nomeOriginal;
	}
	
	default void substituir(String nomeArquivoAntigo, NovaFoto novaFoto) {
		armazenar(novaFoto);
		if(nomeArquivoAntigo != null) {
			this.remover(nomeArquivoAntigo);
		}
	}
	
	@Getter
	@Builder
	class NovaFoto{
		private String nomeArquivo;
		private String contentType;
		private InputStream inputStream;
	}

	@Builder
	@Getter
	class FotoRecuperada {
		private InputStream inputStream;
		private String url;
		
		public boolean temUrl() {
			return url != null;
		}
		
		public boolean temInputStream() {
			return inputStream != null;
		}
	}
}