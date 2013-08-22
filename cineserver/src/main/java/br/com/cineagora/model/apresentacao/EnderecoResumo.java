package br.com.cineagora.model.apresentacao;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.cineagora.model.Endereco;

@Entity(name="endereco_resumo")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="CinemaRegion", include="all")
public class EnderecoResumo extends Endereco {
	
	@Column(name="dados_recebidos")
	private String dadosRecebidos;

	public String getDadosRecebidos() {
		return dadosRecebidos;
	}

	public void setDadosRecebidos(String dadosRecebidos) {
		this.dadosRecebidos = dadosRecebidos;
	}
	
}
