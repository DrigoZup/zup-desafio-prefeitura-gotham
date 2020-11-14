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

import br.com.zup.prefeitura.dto.DadosManipulaveisProjetoDto;
import br.com.zup.prefeitura.dto.MensagemDTO;
import br.com.zup.prefeitura.entity.Projeto;
import br.com.zup.prefeitura.entity.Secretaria;
import br.com.zup.prefeitura.enums.AreaAtuante;
import br.com.zup.prefeitura.repository.ProjetoRepository;
import br.com.zup.prefeitura.repository.SecretariaRepository;

@RunWith(MockitoJUnitRunner.class)
public class ProjetoTest {
	
	private static final String PROJETO_EXISTENTE = "Erro de duplicidade! Já existe um Projeto com esse nome!";
	private static final String PROJETO_CADASTRADO_COM_SUCESSO = "O Projeto foi cadastrado com sucesso!";
	private static final String PROJETO_ALTERADO_COM_SUCESSO = "O Projeto foi alterado com sucesso!";
	private static final String PROJETO_INEXISTENTE = "O projeto não foi encontrado em nosso sistema!";
	private static final String PROJETO_CONCLUIDO_COM_SUCESSO = "O Projeto foi concluido com sucesso!";
	private static final String SECRETARIA_INEXISTENTE = "Não existe uma secretária que represente esse projeto";
	
	private static DadosManipulaveisProjetoDto criaProjetoDto() {
		DadosManipulaveisProjetoDto projetoDto = new DadosManipulaveisProjetoDto();
		projetoDto.setNome("Pracinha");
		projetoDto.setDescricao("Rolê aleatório");
		projetoDto.setCusto(500000D);
		projetoDto.setIdSecretaria(1l);
		return projetoDto;
		}
	private static Projeto criaProjeto() {
		Projeto projeto = new Projeto();
		projeto.setNome("Pracinha");
		projeto.setCusto(500000D);
		projeto.setConcluido(false);
		projeto.setDataInicio(LocalDate.now());
		projeto.setDescricao("Role aleatorios");
		projeto.setIdProjeto(1l);
		projeto.setIdSecretaria(1l);
		return projeto;
	}
	private static Secretaria criaSecretaria() {
		List<Projeto> projetos = new ArrayList<>();
		Secretaria secretaria = new Secretaria();
		secretaria.setIdSecretaria(1L);
		secretaria.setArea(AreaAtuante.OBRAS);
		secretaria.setOrcamentoProjetos(10000000D);
		secretaria.setOrcamentoFolha(900000D);
		secretaria.setTelefone("1276453223");
		secretaria.setEndereco("Rua dos Bobos");
		secretaria.setSite("www.potpcpm.br");
		secretaria.setEmail("secreUrb@gmail.com");
		secretaria.setProjetos(projetos);
		return secretaria;
	}
	
	
	@Mock
	ProjetoRepository projetoRepository;
	@Mock
	SecretariaRepository secretariaRepository;
	@InjectMocks
	ProjetoService projetoService;

	
	@Test
	public void cadastraProjetoComSucesso() {
		DadosManipulaveisProjetoDto projeto = criaProjetoDto();
		String nomeProjeto = projeto.getNome();
		Optional<Secretaria> secretaria = Optional.of(criaSecretaria());
		
		Mockito.when(projetoRepository.existsByNome(nomeProjeto)).thenReturn(false);
		Mockito.when(secretariaRepository.findById(projeto.getIdSecretaria())).thenReturn(secretaria);
		
		MensagemDTO mensagemRetornada = projetoService.cadastraProjeto(projeto);
	    MensagemDTO mensagemEsperada = new MensagemDTO(PROJETO_CADASTRADO_COM_SUCESSO);
	        
	     Assert.assertEquals("Deve cadastraro o projeto com sucesso", mensagemEsperada, mensagemRetornada);
	}
	
	@Test
	public void naoCadastrarProjetoQueJaExiste() {
		
		DadosManipulaveisProjetoDto projeto = criaProjetoDto();
		String nomeProjeto = projeto.getNome();
		
		Mockito.when(projetoRepository.existsByNome(nomeProjeto)).thenReturn(true);
		
		MensagemDTO mensagemRetornada = projetoService.cadastraProjeto(projeto);
	    MensagemDTO mensagemEsperada = new MensagemDTO(PROJETO_EXISTENTE);
	        
	     Assert.assertEquals("Não deve cadastrar um projeto com nome existente", mensagemEsperada, mensagemRetornada);	
	}
	
	@Test
	public void naoDeveCadastrarQuandoNaoTiverSecretariaRelacionada() {
		DadosManipulaveisProjetoDto projeto = criaProjetoDto();
		String nomeProjeto = projeto.getNome();
		Optional<Secretaria> secretaria = Optional.empty();
		
		Mockito.when(projetoRepository.existsByNome(nomeProjeto)).thenReturn(false);
		Mockito.when(secretariaRepository.findById(projeto.getIdSecretaria())).thenReturn(secretaria);
		
		MensagemDTO mensagemRetornada = projetoService.cadastraProjeto(projeto);
	    MensagemDTO mensagemEsperada = new MensagemDTO(SECRETARIA_INEXISTENTE);
	        
	     Assert.assertEquals("Não deve cadastrar se não houver secretaria relacionada ao projeto", mensagemEsperada, mensagemRetornada);
	}
	
	@Test
	public void atualizaDescricaoDoProjetoComSucesso() {
		Optional<Projeto> projeto = Optional.of(criaProjeto());
		Long idProjeto = projeto.get().getIdProjeto();
		
		String novaDescricao = "nova aleatoria";
		
		Mockito.when(projetoRepository.findById(idProjeto)).thenReturn(projeto);
		
		MensagemDTO mensagemRetornada = projetoService.atualizaProjeto(idProjeto, novaDescricao);
	    MensagemDTO mensagemEsperada = new MensagemDTO(PROJETO_ALTERADO_COM_SUCESSO);
        
	     Assert.assertEquals("Não deve cadastrar se não houver secretaria relacionada ao projeto", mensagemEsperada, mensagemRetornada);
	}
	
	@Test
	public void naoAtualizaDescricaoProjetoQuandoNaoExistir() {
		Optional<Projeto> projeto = Optional.empty();
		
		String novaDescricao = "nova aleatoria";
		
		Mockito.when(projetoRepository.findById(1l)).thenReturn(projeto);
		
		MensagemDTO mensagemRetornada = projetoService.atualizaProjeto(1l, novaDescricao);
	    MensagemDTO mensagemEsperada = new MensagemDTO(PROJETO_INEXISTENTE);
        
	     Assert.assertEquals("Não deve cadastrar se não houver projeto relacionado ao Id fornecido", mensagemEsperada, mensagemRetornada);
	}
	
	@Test
	public void finalizaProjetoComSucesso() {
		Optional<Projeto> projeto = Optional.of(criaProjeto());
		Long idProjeto = projeto.get().getIdProjeto();
		LocalDate dataEntrega = LocalDate.now();
		
		Mockito.when(projetoRepository.findById(idProjeto)).thenReturn(projeto);
		
		MensagemDTO mensagemRetornada = projetoService.finalizaProjeto(idProjeto, dataEntrega);
	    MensagemDTO mensagemEsperada = new MensagemDTO(PROJETO_CONCLUIDO_COM_SUCESSO);
        
	    Assert.assertEquals("O projeto foi concluido com sucesso", mensagemEsperada, mensagemRetornada);
		
	}
	
	@Test
	public void naoFinalizaProjetoSeEleNaoExistir() {
		Optional<Projeto> projeto = Optional.empty();
		LocalDate dataEntrega = LocalDate.now();
		
		Mockito.when(projetoRepository.findById(1l)).thenReturn(projeto);
		
		MensagemDTO mensagemRetornada = projetoService.finalizaProjeto(1l, dataEntrega);
	    MensagemDTO mensagemEsperada = new MensagemDTO(PROJETO_INEXISTENTE);
        
	    Assert.assertEquals("O projeto foi concluido com sucesso", mensagemEsperada, mensagemRetornada);
		
	}
}
