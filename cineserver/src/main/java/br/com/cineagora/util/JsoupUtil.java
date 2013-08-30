package br.com.cineagora.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.cineagora.model.Cinema;
import br.com.cineagora.model.apresentacao.EnderecoResumo;
import br.com.cineagora.model.apresentacao.FilmeCartaz;
import br.com.cineagora.model.element.CinemaElement;

@Component
public class JsoupUtil {
	private static int alternaHeader;
	private static Calendar gc = new GregorianCalendar();
	private static Log log;

	public JsoupUtil() {
		log = LogUtil.openLog(this.getClass());
	}

	@Transactional
	public static List<Cinema> obtemCinemasDoSite(String url) {
		List<Cinema> listaDeCinemas = new ArrayList<>();

		Cinema cinema = null;
		EnderecoResumo endereco = null;
		FilmeCartaz filme = null;

		Elements elemsBlocoCinema = null;
		Elements elemsBlocoFilmes = null;
		Document doc = null;

		int qtdeCinema = 0;
		// Default = 0. Para testes, definir ultima pagina do site.
		int pagina = 0;
		boolean ultimaPagina = false;

		while (!ultimaPagina) {

			try {
				doc = configuraEFazRequest(url, pagina);
			} catch (IOException e) {
				e.printStackTrace();
				//DEBUG
				throw new RuntimeException(e);
			}

			// bloco com nome dos cinemas
			elemsBlocoCinema = doc.getElementsByClass(("j_entity_container"));
			// bloco com nome dos filmes
			elemsBlocoFilmes = doc.getElementsByClass("w-showtimetabs");
			// Eh obrigatorio ser a mesma quantidade de bloco de filmes e
			// cinemas
			if (elemsBlocoCinema.size() != elemsBlocoFilmes.size())
				throw new RuntimeException("Nao bate a quantidade");

			// Recupera cinema
			int contFilme = 0;
			for (Element elemCine : elemsBlocoCinema) {
				cinema = new CinemaElement();
				endereco = new EnderecoResumo();

				Element cine = elemCine.getElementsByAttribute("data-entities").first();
				Element enderecoCinema = elemCine.getElementsByClass("lighten").get(1);
				imprime(true, cine, enderecoCinema);

				cinema.setNome(formataHtml(cine));
				endereco.setDadosRecebidos(formataHtml(enderecoCinema));

				cinema.setEndereco(endereco);

				// Recupera todos filmes de cada cinema iterado
				Element elementoComFilmes = elemsBlocoFilmes.get(contFilme);

				// Recupera cada filme (de um cinema), para 8 dias
				for (int i = 0; i <= 8; i++) {
					String diaDaSemana = diaDaSemana(i);

					Element elemFilmesDia = elementoComFilmes.select("div.tabs_box_pan.item-" + i).first();
					if (elemFilmesDia == null) {
						log.info("Parece que nao ha sessao na " + diaDaSemana + url + "?pagina=" + pagina,
								new Exception());
						log.info("Passando para proximo dia, se houver...");
						continue;
					}

					Elements elemsFilmes = elemFilmesDia.getElementsByClass("w-shareyourshowtime");

					// Totos os filmes (do cinema) do dia que estao sendo
					// iterado
					for (Element elemFilme : elemsFilmes) {
						filme = new FilmeCartaz(i);

						String attrFilme = elemFilme.attr("data-movie");
						Element elemHora = elemFilme.nextElementSibling();
						Elements elemsHora = elemHora.getElementsByAttribute("data-times");

						for (Element hora : elemsHora) {
							filme.addHorario(formataHtml(hora));
						}
						// JSON - Nao utilizado ainda
						imprime(attrFilme);

						filme.setNome(formataHtml(elemFilme));
						cinema.addFilme(filme);
					}
				}
				listaDeCinemas.add(cinema);
				contFilme++;
			}
			++qtdeCinema;

			// Ultimo request feito apos a ultima pagina valida
			if (elemsBlocoCinema.size() == 0) {
				elemsBlocoCinema = doc
						.getElementsMatchingOwnText("Nenhum cinema tem hor.rios que atendam seus crit.rios");
				if (elemsBlocoCinema.size() > 0)
					ultimaPagina = true;
				else
					throw new RuntimeException("Termino Inesperado");
			}
			System.out.println("Fim da Pagina " + pagina++ + "\n");
		}
		System.err.println(qtdeCinema + " qtdeCinema");
		System.err.println(listaDeCinemas.size() + " listaDeCinemas.size()");
		return listaDeCinemas;

	}
	//Utilitario para transformar Set em List ordenado
	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
		List<T> list = new ArrayList<>(c);
		Collections.sort(list);
		return list;
	}

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

	private static Document configuraEFazRequest(String url, int pagina) throws IOException {
		Document doc = null;

		switch (alternaHeader) {
		case 0:
			doc = Jsoup
					.connect(url + "/?page=" + pagina)
					.header("User-Agent",
							"User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/12.0")
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
					.header("Content-Type", "text/html; charset=UTF-8").header("Keep-Alive", "115").get();
			alternaHeader++;
			break;
		case 1:
			doc = Jsoup
					.connect(url + "/?page=" + pagina)
					.header("User-Agent",
							"User-Agent:  Mozilla/5.0 (Macintosh; U; Intel Mac OS X; en) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3")
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
					.header("Content-Type", "text/html; charset=UTF-8").header("Keep-Alive", "115").get();
			alternaHeader++;
			break;
		case 2:
			doc = Jsoup.connect(url + "/?page=" + pagina)

			.header("User-Agent", "User-Agent:  Opera/9.27 (Windows NT 5.1; U; en)")
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
					.header("Content-Type", "text/html; charset=UTF-8").header("Keep-Alive", "115").get();
			alternaHeader = 0;
			break;
		}
		return doc;
	}

	static void imprime(boolean text, Element... element) {
		for (Element elem : element) {
			if (text == true)
				System.out.println(StringEscapeUtils.unescapeHtml4(elem.text()));
			else
				System.out.println(StringEscapeUtils.unescapeHtml4(elem.toString()));
		}
		System.out.println("---------------------");
	}

	static void imprime(boolean text, Elements elements) {
		for (Element elem : elements) {
			if (text == true)
				System.out.println(StringEscapeUtils.unescapeHtml4(elem.text()));
			else
				System.out.println(StringEscapeUtils.unescapeHtml4(elem.toString()));
		}
		System.out.println("---------------------");
	}

	static void imprime(String... dado) {
		for (String d : dado) {
			System.out.println(StringEscapeUtils.unescapeHtml4(d));
			System.out.println("---------------------");
		}
	}

	public static String formataHtml(Element element) {
		return StringEscapeUtils.unescapeHtml4(element.text());
	}

}
