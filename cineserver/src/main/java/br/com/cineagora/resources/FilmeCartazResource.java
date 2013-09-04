package br.com.cineagora.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.UniversalEncodingDetector;
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

	@RequestMapping(value = "/cidade-estado",
			params = { CIDADE, ESTADO },
			produces = "application/json;charset=UTF-8")
	@Transactional
	public @ResponseBody
	List<? extends Cinema> getCinemaPorCidade(
			@RequestParam(value = CIDADE) String cidade,
			@RequestParam(value = ESTADO) String estado) {

		try {
			cidade = URLDecoder.decode(cidade, Util.UTF_8);
			estado = URLDecoder.decode(estado, Util.UTF_8);

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		// TODO Analisar se ja existe cache antes de bater o site
		List<? extends Cinema> cinemas = cinemaDao.findAll(CinemaElement.class);

		cinemas = cinemaComponent.obtemListaDeCinemasPorCidadeDoSite(cidade, estado);

		return cinemas;
	}

}
