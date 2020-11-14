package br.com.zup.prefeitura.service;

import java.util.List;

import br.com.zup.prefeitura.dto.DadosManipulaveisSecretariaDto;
import br.com.zup.prefeitura.dto.MensagemDTO;
import br.com.zup.prefeitura.entity.Secretaria;

public interface InterfaceSecretariaService {

	public MensagemDTO cadastraSecretaria(DadosManipulaveisSecretariaDto secretariaDto);
	
	 public Secretaria consultaSecretaria(Long idSecretaria);

	 public List<Secretaria> listaSecretarias();

	 public MensagemDTO deletaSecretaria(Long idSecretaria);

	 public MensagemDTO editaSecretaria(Long idSecretaria, DadosManipulaveisSecretariaDto secretariaDto);
}
