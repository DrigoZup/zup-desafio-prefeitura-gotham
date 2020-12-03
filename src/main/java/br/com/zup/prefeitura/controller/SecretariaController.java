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

import br.com.zup.prefeitura.dto.DadosManipulaveisSecretariaDto;
import br.com.zup.prefeitura.dto.MensagemDTO;
import br.com.zup.prefeitura.entity.Secretaria;
import br.com.zup.prefeitura.service.InterfaceSecretariaService;

@RestController
@RequestMapping("/secretaria")
//FIXME: É uma boa prática mapear os recursos no plural. Visto que através 
// daquela url estamos acessando ou trabalhando com secretarias e não uma só.
public class SecretariaController {
	
	@Autowired
	InterfaceSecretariaService secretariaService;
	
	
	@PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public MensagemDTO cadastraSecretaria(@RequestBody DadosManipulaveisSecretariaDto secretaria) {
		return this.secretariaService.cadastraSecretaria(secretaria);
	}
	
	@GetMapping(path = "/{idSecretaria}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Secretaria consultaSecretaria(@PathVariable Long idSecretaria) {
		return this.secretariaService.consultaSecretaria(idSecretaria);
	}
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<Secretaria> listaSecretarias() {
		return this.secretariaService.listaSecretarias();
	}

	@DeleteMapping(path = "/{idSecretaria}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public MensagemDTO deletaScretaria(@PathVariable Long idSecretaria) {
		return this.secretariaService.deletaSecretaria(idSecretaria);
		}
	
	@PutMapping(path = "/{idSecretaria}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public MensagemDTO editaSecretaria(@PathVariable Long idSecretaria, @RequestBody DadosManipulaveisSecretariaDto secretariaDto) {
		return this.secretariaService.editaSecretaria(idSecretaria, secretariaDto);
	}
}
