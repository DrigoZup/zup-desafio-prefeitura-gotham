package br.com.zup.prefeitura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.prefeitura.dto.AtualizaFuncionarioDto;
import br.com.zup.prefeitura.dto.MensagemDTO;
import br.com.zup.prefeitura.entity.Funcionario;
import br.com.zup.prefeitura.service.FuncionarioService;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

	@Autowired
	FuncionarioService funcionarioService;
	
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public MensagemDTO cadastraFuncionario(@RequestBody Funcionario funcionario) {
		return this.funcionarioService.cadastraFuncionario(funcionario);
	}
	
	@GetMapping(path = "/{idFuncionario}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Funcionario consultaFuncionario(@PathVariable Long idFuncionario) {
		return this.funcionarioService.consultaFuncionario(idFuncionario);
	}
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<Funcionario> listaFuncionarios() {
		return this.funcionarioService.listaFuncionarios();
	}
	
	@DeleteMapping(path = "/{idFuncionario}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public MensagemDTO deletaFuncionario(@PathVariable Long idFuncionario) {
		return this.funcionarioService.deletaFuncionario(idFuncionario);
	}
	
	@PutMapping(path = "/{idFuncionario}", produces = {MediaType.APPLICATION_JSON_VALUE}) 
	public MensagemDTO atualizaDadosFuncionario(@PathVariable Long idFuncionario, @RequestBody AtualizaFuncionarioDto funcionario){
		return this.funcionarioService.atualizaDadosFuncionario(idFuncionario, funcionario);
	}
	
}
