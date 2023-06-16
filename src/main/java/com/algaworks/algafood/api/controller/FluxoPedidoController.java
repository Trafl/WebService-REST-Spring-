package com.algaworks.algafood.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.service.FluxoPedidoService;

@RestController
@RequestMapping(value = "/pedidos/{pedidoId}")
public class FluxoPedidoController {

	@Autowired
	private FluxoPedidoService fluxoPedido;
	
	@PutMapping(value = "/confirmacao")
	public void confirmar(@PathVariable Long pedidoId) {
		fluxoPedido.confirmar(pedidoId);
	}

	@PutMapping(value = "/entrega")
	public void entregar(@PathVariable Long pedidoId) {
		fluxoPedido.entregue(pedidoId);
	}
	
	@PutMapping(value = "/cancelamento")
	public void cancelar(@PathVariable Long pedidoId) {
		fluxoPedido.cancelado(pedidoId);
	}

}

