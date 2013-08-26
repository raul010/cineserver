package br.com.cineagora.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.QueryHint;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.stereotype.Repository;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "cinemaRegion")
@NamedQueries({ @NamedQuery(name = "CinemaElement.findAll", query = "SELECT DISTINCT(c) FROM CinemaElement c LEFT JOIN FETCH c.filmes", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true"), }) })
@Repository
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Cinema implements Serializable {
	private static final long serialVersionUID = 1574758147832526945L;

	@Column(length = 100, nullable = false)
	private String nome;
	// nullable=false
	@OneToOne(cascade = CascadeType.PERSIST)
	private Endereco endereco;

	// Os filmes que vêm dentro do cinema, devem vir na ordem de inclusão, deve
	// ser List
	@Column(nullable = false)
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "join_cinema_filme")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Filme> filmes;

	public Cinema() {
		filmes = new ArrayList<>();
	}

	/*
	 * TODO Metodo criado para ser Set, perde o intuito para uso com List, remover depois
	 */
	public boolean addFilme(Filme filme) {
		return this.filmes.add(filme);
	}

	public List<Filme> getFilmes() {
		return filmes;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (!(obj instanceof Cinema))
//			return false;
//		Cinema other = (Cinema) obj;
//		if (nome == null) {
//			if (other.nome != null)
//				return false;
//		} else if (!nome.equals(other.nome))
//			return false;
//		return true;
//	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cinema_seq")
	@SequenceGenerator(name = "cinema_seq", sequenceName = "cinema_seq")
	private int id;

}
