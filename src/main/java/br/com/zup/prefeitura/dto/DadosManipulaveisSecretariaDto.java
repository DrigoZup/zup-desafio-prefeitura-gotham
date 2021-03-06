package br.com.zup.prefeitura.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import br.com.zup.prefeitura.enums.AreaAtuante;

public class DadosManipulaveisSecretariaDto {
	
	@Enumerated(EnumType.STRING)
	private AreaAtuante area;
	private Double orcamentoProjetos;
	private Double orcamentoFolha;
	private String telefone;
	private String endereco;
	private String site;
	private String email;
	
	public DadosManipulaveisSecretariaDto() {
		
	}
	
	public AreaAtuante getArea() {
		return area;
	}
	public void setArea(AreaAtuante area) {
		this.area = area;
	}
	public Double getOrcamentoProjetos() {
		return orcamentoProjetos;
	}
	public void setOrcamentoProjetos(Double orcamentoProjetos) {
		this.orcamentoProjetos = orcamentoProjetos;
	}
	public Double getOrcamentoFolha() {
		return orcamentoFolha;
	}
	public void setOrcamentoFolha(Double orcamentoFolha) {
		this.orcamentoFolha = orcamentoFolha;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
