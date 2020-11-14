package br.com.zup.prefeitura.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.prefeitura.dto.DadosManipulaveisProjetoDto;
import br.com.zup.prefeitura.dto.MensagemDTO;
import br.com.zup.prefeitura.entity.Projeto;
import br.com.zup.prefeitura.service.ProjetoService;

@RestController
@RequestMapping("/projeto")
public class ProjetoController {

	@Autowired
	ProjetoService projetoService;
	
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public MensagemDTO cadastraProjeto(@RequestBody DadosManipulaveisProjetoDto projeto) {
		return this.projetoService.cadastraProjeto(projeto);
	}
	
	@GetMapping(path = "/{idProjeto}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Projeto buscaProjeto(@PathVariable Long idProjeto) {
		return this.projetoService.buscaProjeto(idProjeto);
	}
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<Projeto> listaProjetos() {
		return this.projetoService.listaProjetos();
	}
	
	@PatchMapping(path = "/{idProjeto}", produces = {MediaType.APPLICATION_JSON_VALUE}) 
	public MensagemDTO atualizaProjeto(@PathVariable Long idProjeto, @RequestBody String descricao) {
		return this.projetoService.atualizaProjeto(idProjeto, descricao);
	}
	
	@PutMapping(path = "/{idProjeto}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public MensagemDTO finalizaProjeto(@PathVariable Long idProjeto, @RequestBody LocalDate dataEntrega) {
		return this.projetoService.finalizaProjeto(idProjeto, dataEntrega);
	}
	
}
