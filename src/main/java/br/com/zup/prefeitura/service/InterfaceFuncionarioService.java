package br.com.zup.prefeitura.service;

import java.util.List;

import br.com.zup.prefeitura.dto.AtualizaFuncionarioDto;
import br.com.zup.prefeitura.dto.MensagemDTO;
import br.com.zup.prefeitura.entity.Funcionario;

public interface InterfaceFuncionarioService {
	
	public MensagemDTO cadastraFuncionario(Funcionario funcionario);
	
	public Funcionario consultaFuncionario(Long idFuncionario);
	
	public List<Funcionario> listaFuncionarios();
	
	public MensagemDTO deletaFuncionario(Long idFuncionario);
	
	public MensagemDTO atualizaDadosFuncionario(Long idFuncionario, AtualizaFuncionarioDto funcionarioAtualizado);

}
