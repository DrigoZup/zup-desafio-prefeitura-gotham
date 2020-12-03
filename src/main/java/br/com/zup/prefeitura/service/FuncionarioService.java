package br.com.zup.prefeitura.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.zup.prefeitura.dto.AtualizaFuncionarioDto;
import br.com.zup.prefeitura.dto.MensagemDTO;
import br.com.zup.prefeitura.entity.Funcionario;
import br.com.zup.prefeitura.entity.Secretaria;
import br.com.zup.prefeitura.repository.FuncionarioRepository;
import br.com.zup.prefeitura.repository.SecretariaRepository;

@Service
public class FuncionarioService implements InterfaceFuncionarioService {

	private static final String FUNCIONARIO_EXISTENTE = "Erro de duplicidade! Já existe um Funcionário com esse CPF!";
	private static final String FUNCIONARIO_CADASTRADO_COM_SUCESSO = "O Funcionário foi cadastrado com sucesso!";
	private static final String FUNCIONARIO_REMOVIDO_COM_SUCESSO = "O Funcionário foi removido com sucesso!";
	private static final String FUNCIONARIO_INEXISTENTE = "Não foi possível encontrar nenhum funcionário com esse identificador!";
	private static final String DADOS_DO_FUNCIONARIO_ATUALIZADOS_COM_SUCESSO = "O cadasrtro do funcionário foi atualizado com sucesso!!";
	private static final String ORCAMENTO_INSUFICIENTE = "Orçamento insuficiente para contratação de funcionário";
	private static final String SECRETARIA_INEXISTENTE = "Não existe uma secretária que represente esse funcionário";
	
	@Autowired
	FuncionarioRepository funcionarioRepository;
	
	@Autowired
	SecretariaRepository secretariaRepository;
	
	public MensagemDTO cadastraFuncionario(Funcionario funcionario) {
		
		String cpfFuncionario = funcionario.getCpf();
		boolean funcionarioExiste = this.funcionarioRepository.existsByCpf(cpfFuncionario);
		
		if (funcionarioExiste) {
			return new MensagemDTO(FUNCIONARIO_EXISTENTE);
		}
		
		Optional<Secretaria> secretariaRetornada = secretariaRepository.findById(funcionario.getIdSecretaria());
		if (secretariaRetornada.isEmpty()) {
			return new MensagemDTO(SECRETARIA_INEXISTENTE);
		}
		
		Secretaria secretaria = secretariaRetornada.get();
		boolean orcamentoInvalido = secretaria.getOrcamentoFolha() < funcionario.getSalario();
		if (orcamentoInvalido) {
			return new MensagemDTO(ORCAMENTO_INSUFICIENTE);
		}
		
		this.funcionarioRepository.save(funcionario);
		
		Double novoOrcamento = secretaria.getOrcamentoFolha() - funcionario.getSalario();
		secretaria.setOrcamentoFolha(novoOrcamento);
		secretariaRepository.save(secretaria);

		return new MensagemDTO(FUNCIONARIO_CADASTRADO_COM_SUCESSO);
	}

	public Funcionario consultaFuncionario(Long idFuncionario) {
		return this.funcionarioRepository.findById(idFuncionario).orElse(null);
	}

	public List<Funcionario> listaFuncionarios() {
		return (List<Funcionario>) this.funcionarioRepository.findAll();
	}

	public MensagemDTO deletaFuncionario(Long idFuncionario) {
		
		Optional<Funcionario> funcionario = this.funcionarioRepository.findById(idFuncionario);
		if (funcionario.isPresent()) {
			
			Long idSecretaria = funcionario.get().getIdSecretaria();
			Optional<Secretaria> secretariaRetornada = this.secretariaRepository.findById(idSecretaria);
			Secretaria secretaria = secretariaRetornada.get();
			
			Funcionario funcionarioASerDeletado = funcionario.get();
			
			Double novoOrcamento = secretaria.getOrcamentoFolha() + funcionarioASerDeletado.getSalario();
			secretaria.setOrcamentoFolha(novoOrcamento);
			
			this.funcionarioRepository.deleteById(idFuncionario);
			
			this.secretariaRepository.save(secretaria);
			
			return new MensagemDTO(FUNCIONARIO_REMOVIDO_COM_SUCESSO);
		}
		return new MensagemDTO(FUNCIONARIO_INEXISTENTE);
	}

	public MensagemDTO atualizaDadosFuncionario(Long idFuncionario, AtualizaFuncionarioDto funcionarioASerAtualizado) {
		
		Optional<Funcionario> funcionario = this.funcionarioRepository.findById(idFuncionario);

		//FIXME: Jovem, você se esqueceu de considerar um fluxo grande aqui, que seria o de troca de secretaria:
		// "Ao alterar um funcionário, caso haja uma alteração de secretaria deverá ser realizada a mesma verificação 
		// do cadastro inicial (verificação do orçamento), além disso deverá haver uma alteração nos orçamentos das 
		// duas secretarias para refletir a alocação de verba em uma e desalocação de verba em outra."
		if (funcionario.isPresent() && funcionarioASerAtualizado.getSalario()>=funcionario.get().getSalario()) {
			Funcionario funcionarioAtualizado = funcionario.get();
			boolean verificaStatusConcursado = 	funcionarioAtualizado.getConcursado();
			
			BeanUtils.copyProperties(funcionarioASerAtualizado, funcionarioAtualizado);
			
			//TODO: E esse if ternário aqui jovem?
			verificaStatusConcursado = verificaStatusConcursado == true ? true : funcionarioASerAtualizado.isConcursado();
			funcionarioAtualizado.setConcursado(verificaStatusConcursado);

			this.funcionarioRepository.save(funcionarioAtualizado);
			
			return new MensagemDTO(DADOS_DO_FUNCIONARIO_ATUALIZADOS_COM_SUCESSO);
			
		}
		return new MensagemDTO(FUNCIONARIO_INEXISTENTE);
	}

}
