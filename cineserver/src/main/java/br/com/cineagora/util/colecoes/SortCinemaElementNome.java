package br.com.cineagora.util.colecoes;

import java.util.Comparator;

import br.com.cineagora.model.element.CinemaElement;

public class SortCinemaElementNome implements Comparator<CinemaElement> {

	@Override
	public int compare(CinemaElement c1, CinemaElement c2) {
		return c1.getNome().compareTo(c2.getNome());
	}

}
