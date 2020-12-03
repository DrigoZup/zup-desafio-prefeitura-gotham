package br.com.zup.prefeitura.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.zup.prefeitura.dto.DadosManipulaveisProjetoDto;
import br.com.zup.prefeitura.dto.MensagemDTO;
import br.com.zup.prefeitura.entity.Projeto;
import br.com.zup.prefeitura.entity.Secretaria;
import br.com.zup.prefeitura.repository.ProjetoRepository;
import br.com.zup.prefeitura.repository.SecretariaRepository;

@Service
public class ProjetoService implements InterfaceProjetoService {

	private static final String PROJETO_EXISTENTE = "Erro de duplicidade! Já existe um Projeto com esse nome!";
	private static final String PROJETO_CADASTRADO_COM_SUCESSO = "O Projeto foi cadastrado com sucesso!";
	private static final String ORCAMENTO_INSUFICIENTE = "Projeto fora de Orçamento";
	private static final String PROJETO_ALTERADO_COM_SUCESSO = "O Projeto foi alterado com sucesso!";
	private static final String PROJETO_INEXISTENTE = "O projeto não foi encontrado em nosso sistema!";
	private static final String DATA_DE_ENTREGA_INVALIDA = "A data de entrega não é permitida por ser anterior a data de início do projeto!";
	private static final String PROJETO_CONCLUIDO_COM_SUCESSO = "O Projeto foi concluido com sucesso!";
	private static final String SECRETARIA_INEXISTENTE = "Não existe uma secretária que represente esse projeto";
	
	@Autowired
	ProjetoRepository projetoRepository;
	
	@Autowired
	SecretariaRepository secretariaRepository;

	public MensagemDTO cadastraProjeto(DadosManipulaveisProjetoDto projetoASerCadastrado) {
		
	    // TODO: Como será que podemos encurtar esse método?
		String nomeProjeto = projetoASerCadastrado.getNome();
		boolean projetoExiste = this.projetoRepository.existsByNome(nomeProjeto);
		
		if (projetoExiste) {
			return new MensagemDTO(PROJETO_EXISTENTE);
		}
		
		Long idSecretaria = projetoASerCadastrado.getIdSecretaria();
		Optional<Secretaria> secretariaRetornada = this.secretariaRepository.findById(idSecretaria);
		
		if (secretariaRetornada.isEmpty()) {
			return new MensagemDTO(SECRETARIA_INEXISTENTE);
		}
		
		Secretaria secretaria = secretariaRetornada.get();
		boolean orcamentoInvalido =  projetoASerCadastrado.getCusto() > secretaria.getOrcamentoProjetos();
		if (orcamentoInvalido) {
			return new MensagemDTO(ORCAMENTO_INSUFICIENTE);
		}
		
		Projeto projetoCadastrado = new Projeto();
		BeanUtils.copyProperties(projetoASerCadastrado, projetoCadastrado);
		
		Double novoOrcamento = secretaria.getOrcamentoProjetos() - projetoCadastrado.getCusto();
		secretaria.setOrcamentoProjetos(novoOrcamento);
		
		this.projetoRepository.save(projetoCadastrado);
		this.secretariaRepository.save(secretaria);
		
		return new MensagemDTO(PROJETO_CADASTRADO_COM_SUCESSO);
	}

	public Projeto buscaProjeto(Long idProjeto) {
		return this.projetoRepository.findById(idProjeto).orElse(null);
	}

	public List<Projeto> listaProjetos() {
		return (List<Projeto>) this.projetoRepository.findAll();
	}

	public MensagemDTO atualizaProjeto(Long idProjeto, String descricao) {
		
		Optional<Projeto> projeto = this.projetoRepository.findById(idProjeto);
		if (projeto.isPresent()) {
			
			Projeto projetoAtualizado = projeto.get();
			projetoAtualizado.setDescricao(descricao);
			this.projetoRepository.save(projetoAtualizado);
			return new MensagemDTO(PROJETO_ALTERADO_COM_SUCESSO);
		}
		//TODO: Tente inverter a ordem pra usar o fail first.
		return new MensagemDTO(PROJETO_INEXISTENTE);
	}

	public MensagemDTO finalizaProjeto(Long idProjeto, LocalDate dataEntrega) {
		
		Optional<Projeto> projeto = this.projetoRepository.findById(idProjeto);
		
		if (projeto.isPresent()) {
			
			Projeto projetoParaFinalizar = projeto.get();
			
			boolean inicioDepoisDaEntrega = projetoParaFinalizar.getDataInicio().isAfter(dataEntrega);
			if (inicioDepoisDaEntrega) {
				return new MensagemDTO(DATA_DE_ENTREGA_INVALIDA);
			}
			projetoParaFinalizar.setDataEntrega(dataEntrega);
			projetoParaFinalizar.setConcluido(true);
			this.projetoRepository.save(projetoParaFinalizar);
			return new MensagemDTO(PROJETO_CONCLUIDO_COM_SUCESSO);
		}
		//TODO: Tente inverter a ordem pra usar o fail first.
		return new MensagemDTO(PROJETO_INEXISTENTE);
	}
}
