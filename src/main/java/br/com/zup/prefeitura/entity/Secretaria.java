package br.com.zup.prefeitura.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.zup.prefeitura.enums.AreaAtuante;

@Entity
@Table(name = "secretaria")
public class Secretaria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_secretaria")
	private Long idSecretaria;
	@Column(name = "area_atuante", unique = true, nullable = false)
	@Enumerated(EnumType.STRING)
	private AreaAtuante area;
	@Column(name = "orcamento_projetos", nullable = false)
	private Double orcamentoProjetos;
	@Column(name = "orcamento_folha", nullable = false)
	private Double orcamentoFolha;
	@Column(nullable = false)
	private String telefone;
	@Column(nullable = false)
	private String endereco;
	@Column(nullable = false)
	private String site;
	@Column(nullable = false)
	private String email;
	@OneToMany
	@JoinColumn(name = "id_secretaria", foreignKey = @ForeignKey(name = "FUNCIONARIO_FK"))
	private List<Funcionario> funcionarios;
	@OneToMany
	@JoinColumn(name = "id_secretaria", foreignKey = @ForeignKey(name = "PROJETO_FK"))
	private List<Projeto> projetos;

	public Secretaria() {
		
	}
	
	public Long getIdSecretaria() {
		return idSecretaria;
	}

	public void setIdSecretaria(Long idSecretaria) {
		this.idSecretaria = idSecretaria;
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

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}
	public List<Projeto> getProjetos() {
		return projetos;
	}
	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}
	
	@Override
	public String toString() {
		return "Secretaria [area=" + area + ", orcamentoProjetos=" + orcamentoProjetos + ", orcamentoFolha="
				+ orcamentoFolha + ", telefone=" + telefone + ", endereco=" + endereco + ", site=" + site + ", email - "
				+ email + "]";
	}
}
