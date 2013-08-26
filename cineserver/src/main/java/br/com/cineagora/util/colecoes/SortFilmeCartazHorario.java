package br.com.cineagora.util.colecoes;

import java.util.Comparator;

import br.com.cineagora.model.Filme;
import br.com.cineagora.model.apresentacao.FilmeCartaz;

/**
 * Classifica pela ordem: dia, horario, ano.
 * @author Raul
 *
 */
public class SortFilmeCartazHorario implements Comparator<Filme> {
	
	@Override
	public int compare(Filme f1, Filme f2) {
		if (!(f1 instanceof FilmeCartaz && f2 instanceof FilmeCartaz))
			throw new RuntimeException("Não deveria existit outra  implementacao para Filme");
			
		FilmeCartaz filme1 = ((FilmeCartaz)f1);
		FilmeCartaz filme2 = ((FilmeCartaz)f2);
		
		//Horarios, ainda eh so uma String
		if (filme1.getHorarios().iterator().hasNext() 
				&& filme2.getHorarios().iterator().hasNext()) {
			
			String horario1 = filme1.getHorarios().iterator().next();
			String horario2 = filme2.getHorarios().iterator().next();
			
			int i = filme1.getdia().getDia() - filme2.getdia().getDia();
			if(i != 0) return i;

			i = horario1.compareTo(horario2);
			if(i != 0) return i;
			
			return filme1.getNome().compareTo(filme2.getNome());
			
		} else {
			throw new RuntimeException("Não poderia haver filme sem horario");
		}
	}

}
