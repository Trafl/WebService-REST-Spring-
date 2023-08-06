package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Api(tags = "Cidades")
@RestController
@RequestMapping(value = "/cidades")
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroCidadeService cidadeService;
	
	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;
	
	@Autowired
	private CidadeInputDisassembler cidadeInputDisassembler; 
	
	@ApiOperation("Lista as cidades")
	@GetMapping
	public List<CidadeModel> listar(){
		List<Cidade> todasCidades = cidadeRepository.findAll();
		return cidadeModelAssembler.toCollectModel(todasCidades);
	}
	
	@ApiOperation("Busca as cidades por ID")
	@GetMapping(value = "/{cidadeId}")
	@ApiResponses({
		@ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
		@ApiResponse(responseCode = "400", description = "ID da cidade e invalido", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
	})
	public CidadeModel buscar(
			@ApiParam("ID de uma cidade") 
			@PathVariable Long cidadeId){
		
		Cidade cidade = cidadeService.buscarOuFalhar(cidadeId);
		return cidadeModelAssembler.toModel(cidade);
	}
	
	@ApiOperation("Cadastra uma cidade por ID")
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	@ApiResponses(
		@ApiResponse(responseCode = "201", description = "Cidade cadastrada")
	)
	public CidadeModel adicionar(
			@ApiParam(name = "corpo", value = "Representação de uma nova cidade") 
			@Valid @RequestBody CidadeInput cidadeInput) {
		
		try {
			Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);
			cidade = cidadeService.salvar(cidade);
			
			return cidadeModelAssembler.toModel(cidade);
		
		}catch(EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}	
	}
	
	@ApiOperation("Atualiza uma cidade por ID")
	@PutMapping(value = "/{cidadeId}")
	@ApiResponses({
		@ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class))),
		@ApiResponse(responseCode = "200", description = "Cidade atualizada")
	})
	public CidadeModel atualizar(
			@ApiParam(value = "ID de uma cidade") @PathVariable Long cidadeId, 
			@ApiParam(name = "corpo", value = "Representação de uma cidade com os novos dados") 
			@Valid @RequestBody CidadeInput cidadeInput){
		
		try {
			Cidade cidadeAtual = cidadeService.buscarOuFalhar(cidadeId);
			
			cidadeInputDisassembler.toDomainObject(cidadeInput);
			
			cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
			
			cidadeAtual = cidadeService.salvar(cidadeAtual);
			
			return cidadeModelAssembler.toModel(cidadeAtual);
		
		}catch(EstadoNaoEncontradoException e){
			throw new NegocioException(e.getMessage(), e);
		}	
	}
	
	@ApiOperation("Exclui uma cidade por ID")
	@DeleteMapping(value = "/{cidadeId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Cidade excluida"),
		@ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Problem.class)))
	})
	public void remover(
			@ApiParam(value = "ID de uma cidade")
			@PathVariable Long cidadeId){
		
		cidadeService.remover(cidadeId);
	}
}
