package br.com.cineagora.negocio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cineagora.dao.interfaces.CinemaDao;
import br.com.cineagora.model.Cinema;
import br.com.cineagora.model.Filme;
import br.com.cineagora.model.apresentacao.EnderecoResumo;
import br.com.cineagora.model.apresentacao.FilmeCartaz;
import br.com.cineagora.model.base.NomeFilme;
import br.com.cineagora.model.element.CinemaElement;
import br.com.cineagora.util.JsoupUtil;
import br.com.cineagora.util.Util;

@Service
public class CinemaService {

	@Resource(type = CinemaDao.class)
	private CinemaDao cinemaDao;

	public static final Logger LOG = LogManager.getLogger(CinemaService.class);

	@Transactional(readOnly = false)
	public List<? extends Cinema> obtemListaDeCinemasPorCidadeDoSite(String cidade, String estado) {
		String url = Util.obtemValorDaPropriedade("S1");
		String pagina = Util.obtemValorDaPropriedade("P1");

		String urlCompleta = new StringBuffer()
				.append(url)
				.append(pagina)
				.toString();

		// Primeiro obtem o link do estado corrente
		String linkCidade = obtemLinkDoEstadoOuCidadeDoSite(urlCompleta, estado);
		// ...depois da cidade
		String linkEstado = obtemLinkDoEstadoOuCidadeDoSite(linkCidade, cidade);
		// ...e por fim retorna os cinemas
		List<? extends Cinema> cinemas = obtemCinemasPorCidadeDoSite(linkEstado, cidade, estado);

		if (cinemas == null || cinemas.size() <= 0)
			throw new RuntimeException("Nao retornaram cinemas");

		cinemaDao.createAll(cinemas);

		return cinemas;
	}

	private String obtemLinkDoEstadoOuCidadeDoSite(String url, String cidadeOuEstado) {

		if (StringUtils.isBlank(cidadeOuEstado) || StringUtils.isBlank(cidadeOuEstado))
			throw new RuntimeException("Cidade ou Estado URL null");

		Document doc = JsoupUtil.configuraEFazRequest(url, null, null);
		// procura por elemento que contem o texto da cidadeEscolhida
		Elements elemsBloco = doc.select("a:matches(" + cidadeOuEstado + ")");

		String link;
		// Se houver CIDADE igual o estado (e.g. São Paulo, Rio de Janeiro),
		// seu ESTADO estara no segundo indice da lista
		if (elemsBloco.size() == 0)
			return null;
		else if (elemsBloco.size() == 1)
			link = elemsBloco.get(0).attr("abs:href");
		else if (elemsBloco.size() == 2)
			link = elemsBloco.get(1).attr("abs:href");
		else
			throw new RuntimeException("Analisar possibilidade de 3 ou mais links com mesmo texto");

		return Util.unescapeHtml(link);
	}

	@Transactional
	private List<? extends Cinema> obtemCinemasPorCidadeDoSite(String url, String cidade, String estado) {
		List<Cinema> listaDeCinemas = new ArrayList<>();

		Cinema cinema = null;
		EnderecoResumo endereco = null;

		Elements elemsBlocoCinema = null;
		Elements elemsBlocoFilmes = null;
		Document doc = null;

		int paginasPesquisadas = 0;
		// pagina DEFAULT = 1. Para testes, definir ultima pagina do site.
		int pagina = 1;
		boolean ultimaPagina = false;

		while (!ultimaPagina) {
			doc = JsoupUtil.configuraEFazRequest(url, "?page=", Integer.toString(pagina));

			// bloco com nome dos cinemas
			elemsBlocoCinema = doc.getElementsByClass("j_entity_container");
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
				// imprime(true, cine, enderecoCinema);

				cinema.setNome(Util.unescapeHtml(cine));
				endereco.setDadosRecebidos(Util.unescapeHtml(enderecoCinema));

				cinema.setEndereco(endereco);

				// Recupera todos filmes de cada cinema iterado
				Element elementoComFilmes = elemsBlocoFilmes.get(contFilme);

				// Set<NomeFilme> nomeFilmeSet = new HashSet<>();
				Set<NomeFilme> nomeFilmeSet = new HashSet<>();
				// Recupera cada filme (de um cinema), para ate 8 dias
				for (int i = 0; i <= 8; i++) {
					String diaDaSemana = JsoupUtil.diaDaSemana(i);

					Element elemFilmesDia = elementoComFilmes.select("div.tabs_box_pan.item-" + i)
							.first();
					if (elemFilmesDia == null) {
						// LOG.info("Parece que nao ha sessao de " + diaDaSemana
						// + " " + url + "?pagina=" + pagina);
						// LOG.info("Passando para proximo dia, se houver...");
						continue;
					}

					Elements elemsFilmes = elemFilmesDia.getElementsByClass("w-shareyourshowtime");

					// Totos os filmes (do cinema) do dia que estao sendo
					// iterado
					for (Element elemFilme : elemsFilmes) {
						FilmeCartaz filmeCartaz = new FilmeCartaz(i);
						NomeFilme nomeFilme = new NomeFilme();

						// String attrFilme = elemFilme.attr("data-movie");
						Element elemHora = elemFilme.nextElementSibling();
						Elements elemsHora = elemHora.getElementsByAttribute("data-times");

						for (Element hora : elemsHora) {
							filmeCartaz.addHorario(Util.unescapeHtml(hora));
						}

						String nomeDoFilme = Util.unescapeHtml(elemFilme);

						nomeFilme.setNomeDoFilme(nomeDoFilme);

						// adiciona no HashSet apenas se ainda nao existe outro
						// filme com
						// mesmo nome (equals implementado)
						if (nomeFilmeSet.add(nomeFilme)) {
							filmeCartaz.setNomeFilme(nomeFilme);
							
							// se ja houver NomeFilme na lista, adiciona este
							// existente no FilmeCartaz
						} else {
							for (NomeFilme nf : nomeFilmeSet) {
								String nome = nf.getNomeDoFilme();

								if (nome.equals(nomeDoFilme)) {
									filmeCartaz.setNomeFilme(nf);
									break;
								}
							}
						}
						cinema.setCidade(cidade);
						cinema.setEstado(estado);
						
						if (!cinema.addFilme(filmeCartaz))
							throw new RuntimeException("Set nao poderia ter voltado FALSE");
					}
				}
				listaDeCinemas.add(cinema);
				contFilme++;
			}
			++paginasPesquisadas;

			// Se por algum motivo nao finalizou, finaliza agora, como medida de
			// seguranca
			if (elemsBlocoCinema.size() == 0) {
				elemsBlocoCinema = doc
						.getElementsMatchingOwnText(Util.obtemValorDaPropriedade("L2"));
				if (elemsBlocoCinema.size() > 0)
					ultimaPagina = true;
				else
					throw new RuntimeException("Termino Inesperado");
			}
			// mostra pagina e itera-a neste ponto
			LOG.info("Fim da Pagina " + pagina++ + "\n");

			String regex = Util.obtemValorDaPropriedade("L1");

			// Verifica se existe proxima pagina, se nao, muda flag de
			// ultimaPAgina para verdadeiro
			Elements elemProximaPagina = doc
					.select("a:matches(" + pagina + ")")
					.attr("href", regex);
			if (elemProximaPagina == null
					|| elemProximaPagina.last() == null
					|| StringUtils.isBlank(elemProximaPagina.last().text()))
				ultimaPagina = true;

		}
		LOG.info(paginasPesquisadas + " Quantidade de paginas pesquisadas (Requisicoes feitas)");
		LOG.info(listaDeCinemas.size() + " Quantidade de cinemas encontrados");
		return listaDeCinemas;

	}
}
