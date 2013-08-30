package br.com.cineagora.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.cineagora.dao.interfaces.CinemaDao;
import br.com.cineagora.model.Cinema;
import br.com.cineagora.model.element.CinemaElement;
import br.com.cineagora.util.JsoupUtil;
import br.com.cineagora.util.Util;

@Controller
@RequestMapping("cinemas")
public class FilmeCartazResource {

	public static final String CIDADE = "cidade";
	public static final String ESTADO = "estado";

	@Resource(type = CinemaDao.class)
	CinemaDao cinemaDao;

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public @ResponseBody
	List<? extends Cinema> getTudo() {
		List<? extends Cinema> cinemas = cinemaDao.findAll(CinemaElement.class);
		return cinemas;
	}

	@RequestMapping(value = "/cidade-estado",
			params = { CIDADE, ESTADO },
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public @ResponseBody
	List<? extends Cinema> getCinemaPorCidade(
			@RequestParam(value = CIDADE) String cidade,
			@RequestParam(value = ESTADO) String estado)  {
		
		try {
			cidade = URLDecoder.decode(cidade, "UTF-8");
			estado = URLDecoder.decode(estado, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		// TODO Analisar se já existe cache antes de bater o site
		List<? extends Cinema> cinemas = null;// cinemaDao.findAll(CinemaElement.class);


		String siteFile = 
				Util.getPathAbsolutoApp("src", "main", "resources", "META-INF", "recursos.properties");

		String url = Util.obtemPrimeiraLinhaDoArquivo(siteFile);
		
		cinemas = JsoupUtil.obtemCinemasDoSite(url);
		
		return cinemas;
	}

}
