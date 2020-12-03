package br.com.zup.prefeitura.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.zup.prefeitura.dto.DadosManipulaveisSecretariaDto;
import br.com.zup.prefeitura.dto.MensagemDTO;
import br.com.zup.prefeitura.entity.Secretaria;
import br.com.zup.prefeitura.enums.AreaAtuante;
import br.com.zup.prefeitura.repository.SecretariaRepository;

@Service
public class SecretariaService implements InterfaceSecretariaService {
	
	private static final String SECRETARIA_EXISTENTE = "Erro de duplicidade! Já existe uma Secretaria para esta área!";
	private static final String SECRETARIA_CADASTRADA_COM_SUCESSO = "A Secretaria foi cadastrada com sucesso no Sistema!";
	private static final String SECRETARIA_REMOVIDA_COM_SUCESSO = "A Secretaria foi removida com sucesso do Sistema!";
	private static final String SECRETARIA_INEXISTENTE = "Secretaria não encontrada!";
	private static final String SECRETARIA_ATUALIZADA_COM_SUCESSO = "A Secretaria foi atualizada com sucesso";
	private static final String IMPOSSIVEL_DELETAR_SECRETARIA = "Não é possível excluir uma secretaria que possui funcionários!";
	
	@Autowired
	SecretariaRepository secretariaRepository;
	

	public MensagemDTO cadastraSecretaria(DadosManipulaveisSecretariaDto secretariaASerCadastrada) {

		AreaAtuante areaSecretaria = secretariaASerCadastrada.getArea();
		
		boolean secretariaExiste = this.secretariaRepository.existsByArea(areaSecretaria);
		if (secretariaExiste) {
			return new MensagemDTO(SECRETARIA_EXISTENTE);
		}
		Secretaria secretaria = new Secretaria();
		BeanUtils.copyProperties(secretariaASerCadastrada, secretaria);
		this.secretariaRepository.save(secretaria);
	
		return new MensagemDTO(SECRETARIA_CADASTRADA_COM_SUCESSO);
	}


	public Secretaria consultaSecretaria(Long idSecretaria) {
		return this.secretariaRepository.findById(idSecretaria).orElse(null);
	}


	public List<Secretaria> listaSecretarias() {
		return (List<Secretaria>) this.secretariaRepository.findAll();
	}


	public MensagemDTO deletaSecretaria(Long idSecretaria) {
		
		Optional<Secretaria> secretaria = this.secretariaRepository.findById(idSecretaria);
		
		if (secretaria.isPresent()) {
			
			if (secretaria.get().getFuncionarios().isEmpty()) {
				this.secretariaRepository.deleteById(idSecretaria); 
				return new MensagemDTO(SECRETARIA_REMOVIDA_COM_SUCESSO);
			}
			return new MensagemDTO(IMPOSSIVEL_DELETAR_SECRETARIA);
		}
		// TODO: Tente exercitar refatorar esse método pra 
		// usar fail first em tudo.
		return new MensagemDTO(SECRETARIA_INEXISTENTE);
	}


	public MensagemDTO editaSecretaria(Long idSecretaria, DadosManipulaveisSecretariaDto secretariaASerAlterada) {
		
		Optional<Secretaria> secretaria = this.secretariaRepository.findById(idSecretaria);
		
		if (secretaria.isPresent()) {
			
			Secretaria secretariaAtualizada = secretaria.get();
			BeanUtils.copyProperties(secretariaASerAlterada, secretariaAtualizada);
			this.secretariaRepository.save(secretariaAtualizada);
			
			return new MensagemDTO(SECRETARIA_ATUALIZADA_COM_SUCESSO);
		}
		
		return new MensagemDTO(SECRETARIA_INEXISTENTE);
	}

	
}
