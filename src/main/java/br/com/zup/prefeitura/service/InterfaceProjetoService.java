package br.com.zup.prefeitura.service;

import java.time.LocalDate;
import java.util.List;

import br.com.zup.prefeitura.dto.DadosManipulaveisProjetoDto;
import br.com.zup.prefeitura.dto.MensagemDTO;
import br.com.zup.prefeitura.entity.Projeto;

public interface InterfaceProjetoService {
	
	public MensagemDTO cadastraProjeto(DadosManipulaveisProjetoDto projetoDto);
	
	public Projeto buscaProjeto(Long idProjeto);
	
	public List<Projeto> listaProjetos();
	
	public MensagemDTO atualizaProjeto(Long idProjeto, String descricao);
	
	public MensagemDTO finalizaProjeto(Long idProjeto, LocalDate dataEntrega);

}
