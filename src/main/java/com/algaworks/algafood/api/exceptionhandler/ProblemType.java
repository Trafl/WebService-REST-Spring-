package com.algaworks.algafood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {
	
	
	PROPIEDADE_NAO_RECONHECIDA("/propiedade-nap-reconhcida", "O parametro passado na requisiçao nao foi reconhcido"),
	PROPIEDADE_IGNORADA("/propiedade-ignorda", "O parametro passado na requisição esta invalido"),
	MENSAGEM_IMCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensivel"),
	ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontada", "Entidade não encontrada"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio");
	
	private String title;
	private String uri;
	
	private ProblemType(String path, String title) {
		this.uri = "https://algafood.com.br" + path;
		this.title = title;
		
	}
	
	
}
