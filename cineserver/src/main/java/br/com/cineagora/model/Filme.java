package br.com.cineagora.model;

import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import br.com.cineagora.util.enums.Genero;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "CinemaRegion", include = "all")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Filme {
	@Column
	private String nome;

	@Column(length = 30)
	@Enumerated(EnumType.STRING)
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@CollectionTable(name = "genero_filme")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "CinemaRegion", include = "all")
	private Set<Genero> genero;

	@ManyToMany(mappedBy = "filmes", targetEntity = Cinema.class)
	private Set<Cinema> cinemas;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Set<Genero> getGenero() {
		return genero;
	}

	public void setGenero(Set<Genero> genero) {
		this.genero = genero;
	}

	public Set<Cinema> getCinemas() {
		return cinemas;
	}

	public void setCinemas(Set<Cinema> cinemas) {
		this.cinemas = cinemas;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "filme_seq")
	@SequenceGenerator(name = "filme_seq", sequenceName = "filme_seq")
	private int id;
}
