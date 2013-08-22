package br.com.cineagora.model;

import br.com.cineagora.util.enums.Cidade;
import br.com.cineagora.util.enums.Estado;

public class Endereco {
	private Cidade cidade;
	
	private Estado estado;
	
	public Cidade getCidade() {
		return cidade;
	}
	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
}
