package br.com.zup.prefeitura.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;

import br.com.zup.prefeitura.dto.AtualizaFuncionarioDto;
import br.com.zup.prefeitura.dto.MensagemDTO;
import br.com.zup.prefeitura.entity.Funcionario;
import br.com.zup.prefeitura.entity.Secretaria;
import br.com.zup.prefeitura.enums.AreaAtuante;
import br.com.zup.prefeitura.repository.FuncionarioRepository;
import br.com.zup.prefeitura.repository.SecretariaRepository;

@RunWith(MockitoJUnitRunner.class)
public class FuncionarioServiceTest {

	private static final String FUNCIONARIO_EXISTENTE = "Erro de duplicidade! Já existe um Funcionário com esse CPF!";
	private static final String FUNCIONARIO_CADASTRADO_COM_SUCESSO = "O Funcionário foi cadastrado com sucesso!";
	private static final String FUNCIONARIO_REMOVIDO_COM_SUCESSO = "O Funcionário foi removido com sucesso!";
	private static final String FUNCIONARIO_INEXISTENTE = "Não foi possível encontrar nenhum funcionário com esse identificador!";
	private static final String DADOS_DO_FUNCIONARIO_ATUALIZADOS_COM_SUCESSO = "O cadasrtro do funcionário foi atualizado com sucesso!!";
	private static final String SECRETARIA_INEXISTENTE = "Não existe uma secretária que represente esse funcionário";
	
	private static Funcionario criaFuncionario() {
		Funcionario funcionario = new Funcionario();
		funcionario.setIdFuncionario(1l);
		funcionario.setCpf("123");
		funcionario.setIdSecretaria(1l);
		funcionario.setConcursado(true);
		funcionario.setNome("Rodrigo");
		funcionario.setSalario(10000D);
		funcionario.setDataAdmissao(LocalDate.now());
		funcionario.setFuncao("Fazer Raiva");
		return funcionario;
	}
	private static Secretaria criaSecretaria() {
		List<Funcionario> funcionarios = new ArrayList<>();
		Secretaria secretaria = new Secretaria();
		secretaria.setIdSecretaria(1L);
		secretaria.setArea(AreaAtuante.OBRAS);
		secretaria.setOrcamentoProjetos(10000000D);
		secretaria.setOrcamentoFolha(900000D);
		secretaria.setTelefone("1276453223");
		secretaria.setEndereco("Rua dos Bobos");
		secretaria.setSite("www.potpcpm.br");
		secretaria.setEmail("secreUrb@gmail.com");
		secretaria.setFuncionarios(funcionarios);
		return secretaria;
	}
	private static AtualizaFuncionarioDto criaFuncionarioDto() {
		AtualizaFuncionarioDto funcionarioDto = new AtualizaFuncionarioDto();
		funcionarioDto.setNome("João");
		funcionarioDto.setCpf("1298765");
		funcionarioDto.setFuncao("encher o saco");
		funcionarioDto.setIdSecretaria(1l);
		funcionarioDto.setSalario(10000D);
		funcionarioDto.setConcursado(true);
		return funcionarioDto;
	}
	
	
	@Mock
	FuncionarioRepository funcionarioRepository;
	
	@InjectMocks
	FuncionarioService funcionarioService;
	
	@Mock
	SecretariaRepository secretariaRepository;
	
	@Test
	public void cadastraFuncionarioComSucesso() {
		
		Funcionario funcionario = criaFuncionario();
		String cpfFuncionario = funcionario.getCpf();
		Optional<Secretaria> secretaria = Optional.of(criaSecretaria());
		
		Mockito.when(funcionarioRepository.existsByCpf(cpfFuncionario)).thenReturn(false);
		Mockito.when(secretariaRepository.findById(funcionario.getIdSecretaria())).thenReturn(secretaria);
		
		MensagemDTO mensagemRetornada = funcionarioService.cadastraFuncionario(funcionario);
	    MensagemDTO mensagemEsperada = new MensagemDTO(FUNCIONARIO_CADASTRADO_COM_SUCESSO);
	        
	     Assert.assertEquals("Deve cadastraro funcionario com sucesso", mensagemEsperada, mensagemRetornada);	
	}
	
	@Test
	public void naoCadastrarFuncionarioQueJaExiste() {
		
		Funcionario funcionario = criaFuncionario();
		String cpfFuncionario = funcionario.getCpf();
		
		Mockito.when(funcionarioRepository.existsByCpf(cpfFuncionario)).thenReturn(true);
		
		MensagemDTO mensagemRetornada = funcionarioService.cadastraFuncionario(funcionario);
	    MensagemDTO mensagemEsperada = new MensagemDTO(FUNCIONARIO_EXISTENTE);
	        
	     Assert.assertEquals("Não deve cadastrar um funcionario com CPF existente", mensagemEsperada, mensagemRetornada);	
	}
	
	@Test
	public void naoCadastraFuncionarioQueNaoTemSecretariaRelacionada() {
		
		Funcionario funcionario = criaFuncionario();
		String cpfFuncionario = funcionario.getCpf();
		Optional<Secretaria> secretaria = Optional.empty();
		
		Mockito.when(funcionarioRepository.existsByCpf(cpfFuncionario)).thenReturn(false);
		Mockito.when(secretariaRepository.findById(funcionario.getIdSecretaria())).thenReturn(secretaria);
		
		MensagemDTO mensagemRetornada = funcionarioService.cadastraFuncionario(funcionario);
	    MensagemDTO mensagemEsperada = new MensagemDTO(SECRETARIA_INEXISTENTE);
	        
	    Assert.assertEquals("Não deve cadastrar um funcionario que não pertença a nenhuma secretaria", mensagemEsperada, mensagemRetornada);
	}
	
	@Test
	public void removeFuncinarioComSucesso() {
		
		Optional<Funcionario> funcionario = Optional.of(criaFuncionario());
		Long idFuncionario = funcionario.get().getIdFuncionario();
		Optional<Secretaria> secretaria = Optional.of(criaSecretaria());
		Long idSecretaria = secretaria.get().getIdSecretaria();
		
		Mockito.when(funcionarioRepository.findById(idFuncionario)).thenReturn(funcionario);
		Mockito.when(secretariaRepository.findById(idSecretaria)).thenReturn(secretaria);
		
		MensagemDTO mensagemRetornada = funcionarioService.deletaFuncionario(idFuncionario);
	    MensagemDTO mensagemEsperada = new MensagemDTO(FUNCIONARIO_REMOVIDO_COM_SUCESSO);
	        
	    Assert.assertEquals("Deve remover o funcionario com sucesso", mensagemEsperada, mensagemRetornada);
	}
	
	@Test
	public void naoRemoveFuncionarioSeNaoExistir() {
		
		Optional<Funcionario> funcionario = Optional.empty();
		
		Mockito.when(funcionarioRepository.findById(1l)).thenReturn(funcionario);
		
		MensagemDTO mensagemRetornada = funcionarioService.deletaFuncionario(1l);
	    MensagemDTO mensagemEsperada = new MensagemDTO(FUNCIONARIO_INEXISTENTE);
	        
	    Assert.assertEquals("Não existe funcionario com o id fornecido", mensagemEsperada, mensagemRetornada);
	}
	
	@Test
	public void atualizaFuncionarioComSucesso() {
		AtualizaFuncionarioDto funcionarioDto = criaFuncionarioDto();
		Funcionario funcionarioAtualizado = new Funcionario();
		BeanUtils.copyProperties(funcionarioDto, funcionarioAtualizado);
		
		Optional<Funcionario> funcionario = Optional.of(funcionarioAtualizado);
		Long idFuncionario = funcionarioAtualizado.getIdFuncionario();
		
		Mockito.when(funcionarioRepository.findById(idFuncionario)).thenReturn(funcionario);
		
		MensagemDTO mensagemRetornada = funcionarioService.atualizaDadosFuncionario(idFuncionario, funcionarioDto);
	    MensagemDTO mensagemEsperada = new MensagemDTO(DADOS_DO_FUNCIONARIO_ATUALIZADOS_COM_SUCESSO);
	        
	    Assert.assertEquals("Deve atualizar os dados do funcionario com sucesso", mensagemEsperada, mensagemRetornada);
		}
	
	@Test
	public void naoAtualizaFuncionarioSeEleNaoExistir() {
		AtualizaFuncionarioDto funcionarioDto = criaFuncionarioDto();
		Optional<Funcionario> funcionario = Optional.empty();
		
		Mockito.when(funcionarioRepository.findById(3l)).thenReturn(funcionario);
		
		MensagemDTO mensagemRetornada = funcionarioService.atualizaDadosFuncionario(3l, funcionarioDto);
	    MensagemDTO mensagemEsperada = new MensagemDTO(FUNCIONARIO_INEXISTENTE);
	        
	    Assert.assertEquals("Não deve atualizar um funcionario que não existe", mensagemEsperada, mensagemRetornada);
		}
}
