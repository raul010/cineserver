package br.com.cineagora.resources;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.cineagora.dao.interfaces.CinemaDao;
import br.com.cineagora.model.Cinema;
import br.com.cineagora.model.element.CinemaElement;
import br.com.cineagora.negocio.CinemaService;
import br.com.cineagora.util.Util;

import com.googlecode.ehcache.annotations.Cacheable;

@Controller
@RequestMapping("cinemas")
public class FilmeCartazResource {
	private static final Logger LOG = LogManager.getLogger(FilmeCartazResource.class.getName());
	public static final String CIDADE = "cidade";
	public static final String ESTADO = "estado";

	@Resource(type = CinemaDao.class)
	CinemaDao cinemaDao;

	@Autowired
	CinemaService cinemaComponent;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@Transactional
	public @ResponseBody
	List<? extends Cinema> getCinemas() {
		List<? extends Cinema> cinemas = cinemaDao.findAll(CinemaElement.class);
		return cinemas;
	}
	
	@Cacheable(cacheName="cinema.json")
	@RequestMapping(value = "/cidade-estado",
			params = { CIDADE, ESTADO },
			produces = "application/json;charset=UTF-8")
	@Transactional
	public @ResponseBody
	List<? extends Cinema> getCinemaPorCidade(
			@RequestParam(value = CIDADE) String cidade,
			@RequestParam(value = ESTADO) String estado,
			HttpServletResponse response,
			HttpServletRequest request) {

		fazAnalisesParaLog(response, request);

		// TODO Analisar se ja existe cache antes de bater o site
		List<? extends Cinema> cinemas = cinemaDao.findAll(CinemaElement.class);
		
		if (cinemas == null || (cinemas != null && cinemas.size() <= 0))
			cinemas = cinemaComponent.obtemListaDeCinemasPorCidadeDoSite(cidade, estado);

		return cinemas;
	}

	private void fazAnalisesParaLog(HttpServletResponse response, HttpServletRequest request) {
		String charset = Charset.defaultCharset().name();
		if (!Util.UTF_8.equals(charset))
			LOG.warn("Charset da plataforma nao eh UTF-8: " + charset);

		if (request != null) {
			String requestEncoding = request.getCharacterEncoding();
			if (!Util.UTF_8.equals(requestEncoding)) {
				try {
					request.setCharacterEncoding(Util.UTF_8);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				LOG.warn("Encoding do REQUEST nao veio UTF-8, observar que agora eh --> "
						+ requestEncoding);
			}
		}
		if (response != null) {
			String responseEncoding = response.getCharacterEncoding();
			if (!Util.UTF_8.equals(responseEncoding)) {
				response.setCharacterEncoding(Util.UTF_8);
				LOG.warn("Encoding do RESPONSE nao veio UTF-8, observar que agora eh --> "
						+ responseEncoding);
			}
		}
	}

}
