package br.com.cineagora.util.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DataApos {
	HOJE(0), UM_DIA(1), DOIS_DIAS(2), TRES_DIAS(3), QUATRO_DIAS(4)
	, CINCO_DIAS(5), SEIS_DIAS(6), SETE_DIAS(7), OITO_DIAS(8);
	
	private int dia;
	
	private DataApos(int i) {
		this.dia = i;
	}
	@JsonValue
	public int getDia() {
		return dia;
	}
}
