package br.com.cineagora.model.apresentacao;

import javax.persistence.Column;
import javax.persistence.Entity;

import br.com.cineagora.model.Endereco;

@Entity(name="endereco_resumo")
public class EnderecoResumo extends Endereco {
	private static final long serialVersionUID = -4754655350654168217L;

	@Column(name="dados_recebidos")
	private String dadosRecebidos;

	public String getDadosRecebidos() {
		return dadosRecebidos;
	}

	public void setDadosRecebidos(String dadosRecebidos) {
		this.dadosRecebidos = dadosRecebidos;
	}
	
}
