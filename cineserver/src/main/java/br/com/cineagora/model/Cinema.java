package br.com.cineagora.model;

import java.io.Serializable;
import java.util.HashSet;
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
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, region="cinemaRegion")
@NamedQueries({
	 @NamedQuery(name = "CinemaElement.findAll",
		query="SELECT DISTINCT(c) FROM CinemaElement c LEFT JOIN FETCH c.filmes",
		hints={@QueryHint(name="org.hibernate.cacheable", value="true"),
//			 @QueryHint(name="javax.persistence.query.timeout", value="72000000"),
//			 @QueryHint(name="org.hibernate.timeout", value="72000000"),
//			 @QueryHint(name="org.hibernate.readOnly", value="true")
 		})
})
@Repository
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Cinema implements Serializable {
	private static final long serialVersionUID = 1574758147832526945L;

	@Column(length = 100, nullable=false)
	private String nome;
	//nullable=false
	@OneToOne(cascade = CascadeType.PERSIST)
	private Endereco endereco;
	
	@Column(nullable=false)
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "join_cinema_filme")
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<Filme> filmes;

	public Cinema() {
		filmes = new HashSet<Filme>();
	}

	public void addFilme(Filme filme) {
		if (!this.filmes.add(filme))
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



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cinema other = (Cinema) obj;
		if (id != other.id)
			return false;
		return true;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cinema_seq")
	@SequenceGenerator(name = "cinema_seq", sequenceName = "cinema_seq")
	private int id;
}
