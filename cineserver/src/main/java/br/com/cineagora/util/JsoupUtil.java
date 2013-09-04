package br.com.cineagora.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
/**
 * Referencia Sintaxe do Seletor Jsoup
 * http://jsoup.org/cookbook/extracting-data/selector-syntax

 * @author Raul
 *
 */
@Component
public class JsoupUtil {
	private static Calendar gc = new GregorianCalendar();
	private static final Logger LOG = LogManager.getLogger(JsoupUtil.class);

	public static String diaDaSemana(int qtdeAcrescimo) {
		String labelDiaDaSemana = null;
		gc.setTime(new Date());
		gc.add(Calendar.DAY_OF_MONTH, qtdeAcrescimo);
		int diaDaSemana = gc.get(GregorianCalendar.DAY_OF_WEEK);

		switch (diaDaSemana) {
		case 1:
			labelDiaDaSemana = "Domingo";
			break;
		case 2:
			labelDiaDaSemana = "Segunda";
			break;
		case 3:
			labelDiaDaSemana = "Terca";
			break;
		case 4:
			labelDiaDaSemana = "Quarta";
			break;
		case 5:
			labelDiaDaSemana = "Quinta";
			break;
		case 6:
			labelDiaDaSemana = "Sexta";
			break;
		case 7:
			labelDiaDaSemana = "Sabado";
			break;
		}
		return labelDiaDaSemana;
	}

	/**
	 * @param url
	 *            - obrigatorio
	 * @param urlQuery
	 *            - opcional
	 * @param paramsUrlQuery
	 *            - opcional
	 * 
	 *            <p>
	 *            <b>Exemplos</b> <br>
	 *            ("http://www.google.com/", "?page=", "3") <br>
	 *            ou <br>
	 *            ("http://www.google.com/", null, null) <br>
	 *            ("http://www.google.com/", "", "")
	 */
	public static Document configuraEFazRequest(String url, String urlQuery, String paramsUrlQuery) {
		// null vira ""
		urlQuery = urlQuery == null ? "" : urlQuery;
		paramsUrlQuery = paramsUrlQuery == null ? "" : paramsUrlQuery;

		String urlFinal = new StringBuffer().append(url).append(urlQuery).append(paramsUrlQuery).toString();

		Document doc = null;

		// Random de 1 a 3
		int alternaHeader = new Random().nextInt(3) + 1;

		try {
			switch (alternaHeader) {
			case 1:

				doc = Jsoup
						.connect(urlFinal)
						.header("User-Agent",
								"User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/12.0")
						.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
						.header("Content-Type", "text/html; charset=UTF-8")
						.header("Keep-Alive", "115")
						.get();
				break;
			case 2:
				doc = Jsoup
						.connect(urlFinal)
						.header("User-Agent",
								"User-Agent:  Mozilla/5.0 (Macintosh; U; Intel Mac OS X; en) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3")
						.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
						.header("Content-Type", "text/html; charset=UTF-8")
						.header("Keep-Alive", "115")
						.get();
				break;
			case 3:
				doc = Jsoup.connect(urlFinal)

						.header("User-Agent", "User-Agent:  Opera/9.27 (Windows NT 5.1; U; en)")
						.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
						.header("Content-Type", "text/html; charset=UTF-8")
						.header("Keep-Alive", "115")
						.get();
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
			// DEBUG
			throw new RuntimeException(e);
		}
		return doc;
	}
}
