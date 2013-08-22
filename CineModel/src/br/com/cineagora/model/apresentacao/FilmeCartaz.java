package br.com.cineagora.model.apresentacao;

import java.util.List;

import br.com.cineagora.model.Filme;
import br.com.cineagora.util.enums.DataApos;

public class FilmeCartaz extends Filme {
	private DataApos dia;
	
	private List<String> horarios;
	
	private String diaSemana;
	
	public FilmeCartaz(int diaIteracao) {
		
	}
	public FilmeCartaz() {
	}

	public DataApos getdia() {
		return dia;
	}

	public String getDiaSemana() {
		return diaSemana;
	}
	
	public List<String> getHorarios() {
		return horarios;
	}

	public void addHorario(String horarios) {
		this.horarios.add(horarios);
	}

}
