package com.algaworks.algafood.core.openapi;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.fasterxml.classmate.TypeResolver;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RepresentationBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig  {

	@Bean 
	public Docket apiDocket() {
		
		var  typeResolver = new TypeResolver();
		
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.algaworks.algafood.api"))
				.paths(PathSelectors.any())
				.build()
				.useDefaultResponseMessages(false)
				.globalResponses(HttpMethod.GET, globalGetResponseMesssages())
				.globalResponses(HttpMethod.POST, globalPostResponseMesssages())
				.globalResponses(HttpMethod.PUT, globalPutResponseMesssages())
				.globalResponses(HttpMethod.DELETE, globalDeleteResponseMesssages())
				.additionalModels(typeResolver.resolve(Problem.class))
				.apiInfo(apiInfo())
				.tags(new Tag("Cidades", "Gerencia as cidades"));
	}

	private List<Response> globalPutResponseMesssages() {
		return Arrays.asList(
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
		          .description("Erro interno do Servidor")
		          .representation(MediaType.APPLICATION_JSON )
		          .apply(getProblemaModelReference())
		          .build(),
		          
		      new ResponseBuilder()
		          .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
		          .description("Recurso não possui representação que pode ser aceita pelo consumidor")
		          .build(),
		          
	          new ResponseBuilder()
				.code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
		          .description("Requisição recusada porque o corpo está em um formato não suportado")
		          .representation(MediaType.APPLICATION_JSON )
		          .apply(getProblemaModelReference())
		          .build(),
		          
		      new ResponseBuilder()
		          .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
		          .description("Requisição inválida (erro do cliente)")
		          .representation(MediaType.APPLICATION_JSON )
		          .apply(getProblemaModelReference())
		          .build());
	}

	private List<Response> globalDeleteResponseMesssages() {
		return Arrays.asList(
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
		          .description("Erro interno do Servidor")
		          .representation(MediaType.APPLICATION_JSON )
		          .apply(getProblemaModelReference())
		          .build(),
		          
		      new ResponseBuilder()
		          .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
		          .description("Requisição inválida (erro do cliente)")
		          .representation(MediaType.APPLICATION_JSON )
		          .apply(getProblemaModelReference())
		          .build());
	}

	private List<Response> globalPostResponseMesssages() {
		return Arrays.asList(
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
		          .description("Erro interno do Servidor")
		          .representation(MediaType.APPLICATION_JSON )
		          .apply(getProblemaModelReference())
		          .build(),
		          
		      new ResponseBuilder()
		          .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
		          .description("Recurso não possui representação que pode ser aceita pelo consumidor")
		          .build(),
		          
          	  new ResponseBuilder()
		          .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
		          .description("Requisição inválida (erro do cliente)")
		          .representation(MediaType.APPLICATION_JSON )
		          .apply(getProblemaModelReference())
		          .build(),
		          
	          new ResponseBuilder()
		          .code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
		          .description("Requisição recusada porque o corpo está em um formato não suportado")
		          .representation(MediaType.APPLICATION_JSON )
		          .apply(getProblemaModelReference())
		          .build());
				
	}

	private List<Response> globalGetResponseMesssages(){
		return Arrays.asList(
				new ResponseBuilder()
				.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
		          .description("Erro interno do Servidor")
		          .representation(MediaType.APPLICATION_JSON )
		          .apply(getProblemaModelReference())
		          .build(),
		          
		      new ResponseBuilder()
		          .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
		          .description("Recurso não possui representação que pode ser aceita pelo consumidor")
		          .build());
	}
	
	private Consumer<RepresentationBuilder> getProblemaModelReference() {
	    return r -> r.model(m -> m.name("Problema")
	            .referenceModel(ref -> ref.key(k -> k.qualifiedModelName(
	                    q -> q.name("Problema").namespace("com.algaworks.algafood.api.exceptionhandler")))));
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("AlgaFood API")
				.description("API aberta para clientes e restaurantes")
				.version("1")
				.contact(new Contact("AlgaWors", "https://www,algaworks", "contato@algaworks.com"))
				.build();
	}
}
