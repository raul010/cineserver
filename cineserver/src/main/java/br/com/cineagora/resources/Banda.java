package br.com.cineagora.resources;

import java.util.List;

enum Enums {
	A, E, I;
}

public class Banda {  
	private String nome; 
	private int anoDeFormacao; 
	private int id;
	private List<String> lista;
	List<Enums> enums;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getAnoDeFormacao() {
		return anoDeFormacao;
	}
	public void setAnoDeFormacao(int anoDeFormacao) {
		this.anoDeFormacao = anoDeFormacao;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<String> getLista() {
		return lista;
	}
	public void setLista(List<String> lista) {
		this.lista = lista;
	}
	public List<Enums> getEnums() {
		return enums;
	}
	public void setEnums(List<Enums> enums) {
		this.enums = enums;
	}
}
