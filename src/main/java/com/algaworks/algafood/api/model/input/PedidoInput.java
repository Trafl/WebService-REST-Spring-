package com.algaworks.algafood.api.model.input;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoInput {
	
	@NotNull
	@Valid
	private RestauranteIdInput restaurante;
	
	@NotNull
	@Valid
	private EnderecoInput enderecoEntrega;

	@NotNull
	@Valid
	private FormaPagamentoIdInput formaPagamento;
	
	@NotNull
	@Valid
	@Size(min = 1)
	private List<ItemPedidoInput> itens;
	
	
	
}
