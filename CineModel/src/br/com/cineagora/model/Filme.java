package br.com.cineagora.model;

import java.util.Set;

import br.com.cineagora.util.enums.Genero;

public class Filme  {
	private String nome;

	private Set<Genero> genero;
	
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
}
