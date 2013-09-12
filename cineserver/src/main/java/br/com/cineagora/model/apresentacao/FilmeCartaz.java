package br.com.cineagora.model.apresentacao;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.cineagora.model.Filme;
import br.com.cineagora.model.base.NomeFilme;
import br.com.cineagora.util.JsoupUtil;
import br.com.cineagora.util.enums.DataApos;

/**
 * Contem a informacao dos horarios, data e ordenacao para apresentacao ao usuario
 * @author Raul
 */
@Entity(name="filme_cartaz")
public class FilmeCartaz extends Filme {
	private static final long serialVersionUID = -5445640160801563619L;
	
	@Enumerated(EnumType.ORDINAL)
	private DataApos dia;
	
	@ManyToOne(cascade=CascadeType.ALL)
	//@Column(name="nome_filme")
	private NomeFilme nomeFilme;

	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="horario_filme", joinColumns=@JoinColumn(name="filme_cartaz_id"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<String> horarios;
	
	@Transient
	@Column(name="dia_semana",length=10)
	private String diaSemana;

	public FilmeCartaz(int diaIteracao) {
		switch (diaIteracao) {
		case 0:
			dia = DataApos.HOJE;
			break;
		case 1:
			dia = DataApos.UM_DIA;
			break;
		case 2:
			dia = DataApos.DOIS_DIAS;
			break;
		case 3:
			dia = DataApos.TRES_DIAS;
			break;
		case 4:
			dia = DataApos.QUATRO_DIAS;
			break;
		case 5:
			dia = DataApos.CINCO_DIAS;
			break;
		case 6:
			dia = DataApos.SEIS_DIAS;
			break;
		case 7:
			dia = DataApos.SETE_DIAS;
			break;
		case 8:
			dia = DataApos.OITO_DIAS;
			break;

		default:
			throw new RuntimeException();
		}
		diaSemana = JsoupUtil.diaDaSemana(dia.getDia());

		horarios = new HashSet<String>();
	}
	public FilmeCartaz() {
	}

	public DataApos getdia() {
		return dia;
	}

	public String getDiaSemana() {
		return diaSemana;
	}

	public Set<String> getHorarios() {
		return horarios;
	}

	public void addHorario(String horarios) {
		if (!this.horarios.add(horarios))
			throw new RuntimeException("Set retornou null");
	}
	
	public NomeFilme getNomeFilme() {
		return nomeFilme;
	}
	public void setNomeFilme(NomeFilme nomeFilme) {
		this.nomeFilme = nomeFilme;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dia == null) ? 0 : dia.hashCode());
		result = prime * result + ((nomeFilme == null) ? 0 : nomeFilme.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof FilmeCartaz))
			return false;
		FilmeCartaz other = (FilmeCartaz) obj;
		if (dia != other.dia)
			return false;
		if (nomeFilme == null) {
			if (other.nomeFilme != null)
				return false;
		} else if (!nomeFilme.equals(other.nomeFilme))
			return false;
		return true;
	}
	
}
