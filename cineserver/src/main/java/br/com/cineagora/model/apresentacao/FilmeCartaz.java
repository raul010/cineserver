package br.com.cineagora.model.apresentacao;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.cineagora.model.Cinema;
import br.com.cineagora.model.Filme;
import br.com.cineagora.util.JsoupUtil;
import br.com.cineagora.util.enums.DataApos;

@Entity(name="filme_cartaz")
public class FilmeCartaz extends Filme implements Comparable<FilmeCartaz>{
	private static final long serialVersionUID = -5445640160801563619L;

	@Enumerated(EnumType.STRING)
	private DataApos dia;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="horario_filme", joinColumns=@JoinColumn(name="filme_cartaz_id"))
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<String> horarios;
	
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
	@Override
	public int compareTo(FilmeCartaz f) {
		return f.getHorarios().iterator().next().compareTo(f.getHorarios().iterator().next());
	}

}
