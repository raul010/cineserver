package br.com.cineagora.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="CinemaRegion", include="all")
//@NamedQueries({
//	@NamedQuery(name = "CinemaElement.findAll", 
//		query="SELECT DISTINCT(c) FROM CinemaElement c LEFT JOIN FETCH c.filmes",
//		cacheable=true,
//		cacheRegion="CinemaRegion"
//		//hints={@QueryHint(name="org.hibernate.cacheable", value="true")}
//		)
//})
@NamedQueries({
	@NamedQuery(name = "CinemaElement.findAll", 
			query="SELECT DISTINCT(c) FROM CinemaElement c LEFT JOIN FETCH c.filmes",
			hints={@QueryHint(name="org.hibernate.cacheable", value="true")}
			)
})
@Repository
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class Cinema  {
	@Column(length=100)
	private String nome;
    
	@OneToOne(cascade=CascadeType.PERSIST)
	private Endereco endereco;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="join_cinema_filme")
	@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="CinemaRegion")
	private Set<Filme> filmes;
	
	public Cinema() {
		filmes = new HashSet<Filme>();
	}
	
	public void addFilme(Filme filme) {
		if(!this.filmes.add(filme))
			throw new RuntimeException("Set retornou NULL");
				
	}
	
	public Set<Filme> getFilmes() {
		return filmes;
	}
	
	public void setFilmes(Set<Filme> filmes) {
		this.filmes = filmes;
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
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="cinema_seq")
	@SequenceGenerator(name="cinema_seq", sequenceName="cinema_seq")
	private int id;
}
