package br.com.cineagora.model.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.stereotype.Repository;

import br.com.cineagora.model.Filme;
import br.com.cineagora.model.apresentacao.FilmeCartaz;
import br.com.cineagora.util.JsoupUtil;
import br.com.cineagora.util.enums.DataApos;

/**
 * 
 * @author Raul
 *
 */
@Entity(name="nome_filme")
public class NomeFilme implements Serializable {
	
	private static final long serialVersionUID = -8422182943359724062L;
	
	@Column(name="nome_do_filme")
	private String nomeDoFilme;
	
	public String getNomeDoFilme() {
		return nomeDoFilme;
	}

	public void setNomeDoFilme(String nomeDoFilme) {
		this.nomeDoFilme = nomeDoFilme;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nomeDoFilme == null) ? 0 : nomeDoFilme.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof NomeFilme))
			return false;
		NomeFilme other = (NomeFilme) obj;
		if (nomeDoFilme == null) {
			if (other.nomeDoFilme != null)
				return false;
		} else if (!nomeDoFilme.equals(other.nomeDoFilme))
			return false;
		return true;
	}





	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nome_filme2_seq")
	@SequenceGenerator(name = "nome_filme2_seq", sequenceName = "nome_filme2_seq")
	@Column(unique=false)
	private int id;

}
