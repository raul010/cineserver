package br.com.cineagora.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Cronometro {
	public static final Logger log = LogManager.getLogger(Cronometro.class.getName());
	private long inicioAuditoria;
	private double totalEmSegundos;
	/**
	 * Criar new Util() evidentemente, medotos de auditoria nao sao
	 * estaticos
	 */
	public void start() {
		inicioAuditoria = System.nanoTime();
	}

	public void stop() {
		long fim = System.nanoTime() - inicioAuditoria;
		totalEmSegundos = (double) fim / 1000000000.0;
	}

	public void print(String titulo) {
		log.trace("Duracao (s) " + "[" + titulo + "]" + "--> " + totalEmSegundos);

	}
}
