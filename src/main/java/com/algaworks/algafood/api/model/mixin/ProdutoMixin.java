package com.algaworks.algafood.api.model.mixin;

import com.algaworks.algafood.domain.model.Restaurante;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProdutoMixin {

	@JsonIgnore
	private Restaurante restaurante;
}
