package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.PedidoInputDisassembler;
import com.algaworks.algafood.api.assembler.PedidoModelAssembler;
import com.algaworks.algafood.api.assembler.PedidoResumoModelAssembler;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.core.data.PageableTranslator;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.service.EmissaoPedidoService;
import com.algaworks.algafood.infrastructure.repository.spec.PedidoSpecs;
import com.google.common.collect.ImmutableMap;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {

	@Autowired
	private EmissaoPedidoService emissaoPedido;
	
	@Autowired 
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PedidoModelAssembler pedidoAssembler;

	@Autowired
	private PedidoResumoModelAssembler pedidoResumoAssembler;
	
	@Autowired
	private PedidoInputDisassembler pedidoDisassembler;
	
	@GetMapping
	public Page<PedidoResumoModel> pesquisar(PedidoFilter filtro,@PageableDefault(size = 10) Pageable peageble){
		
		peageble = traduzirPageable(peageble);
		
		Page<Pedido> pedidosPage = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro), peageble);
		
		List<PedidoResumoModel> pedidoModelPage = pedidoResumoAssembler.toCollectModel(pedidosPage.getContent());
		
		Page<PedidoResumoModel> listPedidoModelPage = new PageImpl<>(pedidoModelPage, peageble, pedidosPage.getTotalElements());
		
		return listPedidoModelPage;
	}
	
	@GetMapping(value = "/{codigoPedido}")
	@ResponseStatus(HttpStatus.OK)
	public PedidoModel buscar(@PathVariable String codigoPedido){
		Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
		return pedidoAssembler.toModel(pedido);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
	    try {
	        Pedido novoPedido = pedidoDisassembler.toDomainObject(pedidoInput);

	        // TODO pegar usuário autenticado
	        novoPedido.setCliente(new Usuario());
	        novoPedido.getCliente().setId(1L);

	        novoPedido = emissaoPedido.emitir(novoPedido);

	        return pedidoAssembler.toModel(novoPedido);
	    } catch (EntidadeNaoEncontradaException e) {
	        throw new NegocioException(e.getMessage(), e);
	    }
	}
	
	private Pageable traduzirPageable(Pageable apiPegeable){
		
		var mapeamento = ImmutableMap.of(
				"codigo", "codigo",
				"restaurante.nome", "restaurante.nome",
				"nomeCliente", "cliente.nome",
				"valorTotal", "valorTotal"
				);
		
		return PageableTranslator.translate(apiPegeable, mapeamento);
	}
}
