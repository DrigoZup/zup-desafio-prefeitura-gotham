package br.com.zup.prefeitura.service;

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

import br.com.zup.prefeitura.dto.DadosManipulaveisSecretariaDto;
import br.com.zup.prefeitura.dto.MensagemDTO;
import br.com.zup.prefeitura.entity.Funcionario;
import br.com.zup.prefeitura.entity.Secretaria;
import br.com.zup.prefeitura.enums.AreaAtuante;
import br.com.zup.prefeitura.repository.SecretariaRepository;

@RunWith(MockitoJUnitRunner.class)
public class SecretariaServiceTest {
	
	private static final String SECRETARIA_EXISTENTE = "Erro de duplicidade! Já existe uma Secretaria para esta área!";
	private static final String SECRETARIA_CADASTRADA_COM_SUCESSO = "A Secretaria foi cadastrada com sucesso no Sistema!";
	private static final String SECRETARIA_REMOVIDA_COM_SUCESSO = "A Secretaria foi removida com sucesso do Sistema!";
	private static final String SECRETARIA_INEXISTENTE = "Secretaria não encontrada!";
	private static final String SECRETARIA_ATUALIZADA_COM_SUCESSO = "A Secretaria foi atualizada com sucesso";

	private static DadosManipulaveisSecretariaDto criaSecretariaDto() {
		DadosManipulaveisSecretariaDto secretaria = new DadosManipulaveisSecretariaDto();
		secretaria.setArea(AreaAtuante.SAUDE);
		secretaria.setOrcamentoProjetos(10000000D);
		secretaria.setOrcamentoFolha(900000D);
		secretaria.setTelefone("1276453223");
		secretaria.setEndereco("Rua dos Bobos");
		secretaria.setSite("www.potpcpm.br");
		secretaria.setEmail("secreUrb@gmail.com");
		return secretaria;
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

	
	@Mock
	SecretariaRepository secretariaRepository;
	
	@InjectMocks
	SecretariaService secretariaService;
	
	@Test
	public void cadastraSecretariaComSucesso() {

		DadosManipulaveisSecretariaDto secretaria = criaSecretariaDto();
		AreaAtuante areaSecretaria = secretaria.getArea();
		
		Mockito.when(secretariaRepository.existsByArea(areaSecretaria)).thenReturn(false);
		
		 MensagemDTO mensagemRetornada = secretariaService.cadastraSecretaria(secretaria);
	     MensagemDTO mensagemEsperada = new MensagemDTO(SECRETARIA_CADASTRADA_COM_SUCESSO);
	        
	     Assert.assertEquals("Deve cadastrar a secretaria com sucesso", mensagemEsperada, mensagemRetornada);
	}
	
	@Test
	public void naoCadastraSecretariaSeJaHouverUmaSecretariaNaquelaArea() {

		DadosManipulaveisSecretariaDto secretaria = criaSecretariaDto();
		AreaAtuante areaSecretaria = secretaria.getArea();
		
		Mockito.when(secretariaRepository.existsByArea(areaSecretaria)).thenReturn(true);
		
		 MensagemDTO mensagemRetornada = secretariaService.cadastraSecretaria(secretaria);
	     MensagemDTO mensagemEsperada = new MensagemDTO(SECRETARIA_EXISTENTE);
	        
	     Assert.assertEquals("Não deve cadastrar a secretaria", mensagemEsperada, mensagemRetornada);
	}
	
	@Test
	public void removeSecretariaComSucesso() {
		
		Optional<Secretaria> secretaria = Optional.of(criaSecretaria());
		Long idSecretaria = secretaria.get().getIdSecretaria();
		
		Mockito.when(secretariaRepository.findById(idSecretaria)).thenReturn(secretaria);
		
		 MensagemDTO mensagemRetornada = secretariaService.deletaSecretaria(idSecretaria);
	     MensagemDTO mensagemEsperada = new MensagemDTO(SECRETARIA_REMOVIDA_COM_SUCESSO);
	     
	     Assert.assertEquals("Deve deletar a secretaria com sucesso", mensagemEsperada, mensagemRetornada);
	}
	
	@Test
	public void naoEncontraSecretariaParaRemover() {
		
		Optional<Secretaria> secretaria = Optional.empty();
		
		Mockito.when(secretariaRepository.findById(1L)).thenReturn(secretaria);
		
		 MensagemDTO mensagemRetornada = secretariaService.deletaSecretaria(1L);
	     MensagemDTO mensagemEsperada = new MensagemDTO(SECRETARIA_INEXISTENTE);
	     
	     Assert.assertEquals("Não encontra nenhuma secretaria com o ID informado", mensagemEsperada, mensagemRetornada);
	}
	
	@Test
	public void atualizaSecretariaComSucesso() {
		
		DadosManipulaveisSecretariaDto secretariaDto = criaSecretariaDto();
		Secretaria secretaria = new Secretaria();
		BeanUtils.copyProperties(secretariaDto, secretaria);
		
		Optional<Secretaria> secretariaOptional = Optional.of(secretaria);
		
		Mockito.when(secretariaRepository.findById(1l)).thenReturn(secretariaOptional);
		
		 MensagemDTO mensagemRetornada = secretariaService.editaSecretaria(1l, secretariaDto);
	     MensagemDTO mensagemEsperada = new MensagemDTO(SECRETARIA_ATUALIZADA_COM_SUCESSO);
	     
	     Assert.assertEquals("Não encontra nenhuma secretaria com o ID informado", mensagemEsperada, mensagemRetornada);
	}
	
	@Test
	public void naoEncontraSecretariaParaAtualizar() {
		
		DadosManipulaveisSecretariaDto secretariaDto = criaSecretariaDto();
		
		
		Optional<Secretaria> secretariaOptional = Optional.empty();
		
		Mockito.when(secretariaRepository.findById(1l)).thenReturn(secretariaOptional);
		
		 MensagemDTO mensagemRetornada = secretariaService.editaSecretaria(1l, secretariaDto);
	     MensagemDTO mensagemEsperada = new MensagemDTO(SECRETARIA_INEXISTENTE);
	     
	     Assert.assertEquals("Não encontra nenhuma secretaria com o ID informado", mensagemEsperada, mensagemRetornada);
	}
	
}
