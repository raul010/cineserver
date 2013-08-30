package br.com.cineagora.model;

import java.util.HashSet;
import java.util.Set;

public class Cinema  {
	private String nome;

	private Endereco endereco;

	private Set<Filme> filmes;

	public Cinema() {
		filmes = new HashSet<>();
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
	private int id;
}
