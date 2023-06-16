package com.algaworks.algafood.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.service.FluxoPedidoService;

@RestController
@RequestMapping(value = "/pedidos/{codigoPedido}")
public class FluxoPedidoController {

	@Autowired
	private FluxoPedidoService fluxoPedido;
	
	@PutMapping(value = "/confirmacao")
	public void confirmar(@PathVariable String codigoPedido) {
		fluxoPedido.confirmar(codigoPedido);
	}

	@PutMapping(value = "/entrega")
	public void entregar(@PathVariable String codigoPedido) {
		fluxoPedido.entregue(codigoPedido);
	}
	
	@PutMapping(value = "/cancelamento")
	public void cancelar(@PathVariable String codigoPedido) {
		fluxoPedido.cancelado(codigoPedido);
	}

}

