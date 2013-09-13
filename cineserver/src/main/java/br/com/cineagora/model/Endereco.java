package br.com.cineagora.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.cineagora.util.enums.Cidade;
import br.com.cineagora.util.enums.Estado;

@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class Endereco implements Serializable {
	private static final long serialVersionUID = 6348400782533816916L;

	//@Enumerated(EnumType.STRING)
	@Column(length=100)
	private String cidade;
	
	//@Enumerated(EnumType.STRING)
	@Column(length=40)
	private String estado;
	
	public String getCidade() {
		return cidade;
	}
	@JsonIgnore
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	@JsonIgnore
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="endereco_seq")
	@SequenceGenerator(name="endereco_seq", sequenceName="endereco_seq")
	private int id;
}
