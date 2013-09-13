import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.com.cineagora.dao.interfaces.CinemaDao;
import br.com.cineagora.model.Cinema;
import br.com.cineagora.model.element.CinemaElement;
import br.com.cineagora.negocio.CinemaService;

@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class TestePersiste {

	@Autowired
	private CinemaService cinemaService;

	// Vai direto buscar no site e salvar na base
	@Test
	@Transactional(readOnly = false)
	public void salvaListaNaBase() {
		// bate no site e insere na base
		List<? extends Cinema> cinemas = cinemaService.obtemListaDeCinemasPorCidadeDoSite(
				"São Paulo", "São Paulo");
		for (Cinema cinema : cinemas) {
			System.out.println(cinema.getNome());
		}
	}
}
